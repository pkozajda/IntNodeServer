package org.rso;

import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.rso.utils.NodeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IntNodeServerApplication {
	public static void main(String[] args) {
		AppProperty property = AppProperty.getInstance();
		property.setCoordinatorNode(new NodeInfo(5,"127.0.0.1", NodeType.INTERNAL_COORDINATOR));
		property.setSelfNode(new NodeInfo(5,"127.0.0.1",NodeType.INTERNAL_COORDINATOR));
		property.addAvaiableNode(new NodeInfo(4,"127.0.0.1",NodeType.INTERNAL));
		SpringApplication.run(IntNodeServerApplication.class, args);
	}
}
