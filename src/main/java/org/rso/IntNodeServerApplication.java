package org.rso;

import org.rso.service.NodesCfgService;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.rso.utils.NodeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IntNodeServerApplication {

	public static void main(String[] args) {

		/* TODO:
			 1. Refactor this bs!
			 2. Get rid of xml context configuration
		*/
		final ClassPathXmlApplicationContext configurationCtx = new ClassPathXmlApplicationContext("nodesContext.xml");
		final NodesCfgService nodesCfgService = configurationCtx.getBean("nodesCfgService", NodesCfgService.class);

		AppProperty property = AppProperty.getInstance();

//		property.setCoordinatorNode(new NodeInfo(5,"127.0.0.1", NodeType.INTERNAL_COORDINATOR));
		property.setCoordinatorNode(nodesCfgService.getCoordinatorNode());

//		ustawienia dla siebie samego
//		property.setSelfNode(new NodeInfo(5,"127.0.0.1",NodeType.INTERNAL));
		property.setSelfNode(nodesCfgService.getSelfNode());
//
//		dodanie wezla na ktory wysylany jest protokul bicia serca

		nodesCfgService.getInternalNodes().stream()
				.forEach(property::addAvaiableNode);


//		property.addAvaiableNode(new NodeInfo(10,"127.0.0.1",NodeType.INTERNAL));
//		property.addAvaiableNode(new NodeInfo(4,"127.0.0.1",NodeType.INTERNAL));
		SpringApplication.run(IntNodeServerApplication.class, args);
	}

	@Configuration
	static class IntNodeServerContext {

	}
}
