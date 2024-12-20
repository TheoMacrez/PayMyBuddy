package com.openclassrooms.PayMyBuddy;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@ActiveProfiles("test") // Utilise un profil de test si nécessaire
public class PayMyBuddyApplicationTests {

	@Autowired
	private DataSource dataSource;


	@Test
	public void contextLoads() {
		// Vérifie que le contexte de l'application se charge correctement
		assertThat(dataSource).isNotNull();
	}

	@Test
	public void testDatabaseConnection() throws Exception {
		// Vérifie que la connexion à la base de données fonctionne
		try (var connection = dataSource.getConnection()) {
			assertThat(connection).isNotNull();
			assertThat(connection.isValid(1)).isTrue();
		}
	}
}
