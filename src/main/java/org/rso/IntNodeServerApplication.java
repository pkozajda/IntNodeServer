package org.rso;

import javaslang.control.Try;
import lombok.extern.java.Log;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.rso.service.InternalNodeUtilService;
import org.rso.service.NodeUtilService;
import org.rso.service.NodesCfgService;
import org.rso.utils.AppProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

@Log
@EnableScheduling
@SpringBootApplication
public class IntNodeServerApplication {

	/* TODO:
          1. Refactor this bs!
          2. Get rid of xml context configuration
          3. Command line arguments help
    */
	final static Option nodeIpOption = Option.builder("node")
			.longOpt("node-address")
			.desc("Active Node IP Address")
			.hasArg()
			.argName("IP")
			.build();

	public static void main(String[] args) {

		final AppProperty appProperty = AppProperty.getInstance();

		if(args.length > 0) {
			/* Command line arguments based configuration */
			final CommandLineParser commandLineParser = new DefaultParser();

			final Options options = new Options();

			options.addOption(nodeIpOption);

			Try.of(() -> commandLineParser.parse(options, args))
					.andThen((commandLine) -> {
						if(!commandLine.hasOption("node-address") || StringUtils.isEmpty(commandLine.getOptionValue("node-address"))) {
							throw new RuntimeException("No node address specified!");
						}
					}).onFailure((e) -> {
						log.info(String.format("Exception during command line arguments parsing: %s\nExiting...", e.getMessage()));
						System.exit(1);
					}).andThen((commandLine -> {
						final String nodeIpAddress = commandLine.getOptionValue("node-address");

						final AnnotationConfigApplicationContext configurationCtx = new AnnotationConfigApplicationContext(CommandLineContext.class);
						final NodeUtilService nodeUtilService = configurationCtx.getBean("internalNodeUtilService", NodeUtilService.class);

						nodeUtilService.connectToNetwork(nodeIpAddress);

						configurationCtx.close();

					})).onFailure((e) -> { throw new RuntimeException(String.format("Cannot connect to network: %s", e.getMessage())); });
		} else {
			/* Properties based configuration */

			final ClassPathXmlApplicationContext configurationCtx = new ClassPathXmlApplicationContext("nodesContext.xml");
			final NodesCfgService nodesCfgService = configurationCtx.getBean("nodesCfgService", NodesCfgService.class);

			appProperty.setCoordinatorNode(nodesCfgService.getCoordinatorNode());

			appProperty.setSelfNode(nodesCfgService.getSelfNode());

			nodesCfgService.getInternalNodes().stream()
					.forEach(appProperty::addAvaiableNode);

			/* TODO: cleanup context */
		}

		SpringApplication.run(IntNodeServerApplication.class, args);
	}

	@Configuration
	@PropertySource("classpath:application.properties")
	static class CommandLineContext {

		@Bean
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}

		@Bean(name = "internalNodeUtilService")
		public NodeUtilService nodeUtilService() {
			return new InternalNodeUtilService();
		}
	}
}
