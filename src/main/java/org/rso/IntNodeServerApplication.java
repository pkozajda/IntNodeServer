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

//		ustawienia dla siebie samego
		property.setSelfNode(new NodeInfo(5,"127.0.0.1",NodeType.INTERNAL));
//
//		dodanie wezla na ktory wysylany jest protokul bicia serca

		property.addAvaiableNode(new NodeInfo(10,"127.0.0.1",NodeType.INTERNAL));
//		property.addAvaiableNode(new NodeInfo(4,"127.0.0.1",NodeType.INTERNAL));
		SpringApplication.run(IntNodeServerApplication.class, args);
	}
}
