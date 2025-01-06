package com.openclassrooms.PayMyBuddy;

import org.slf4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PayMyBuddyApplication {

	private static final Logger logger = LoggerFactory.getLogger(PayMyBuddyApplication.class);
	public static void main(String[] args) {
		logger.debug("Démarrage de l'application PayMyBuddy...");
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

}
