package com.openclassrooms.PayMyBuddy;

import com.openclassrooms.PayMyBuddy.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.*;

import java.io.*;

@SpringBootApplication
public class PayMyBuddyApplication {

	private static final Logger logger = LoggerFactory.getLogger(PayMyBuddyApplication.class);


	public static void main(String[] args) throws IOException {
		logger.debug("Démarrage de l'application PayMyBuddy...");
		ApplicationContext context = SpringApplication.run(PayMyBuddyApplication.class, args);

	}



}
