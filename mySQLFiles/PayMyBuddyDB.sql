-- MySQL Script for PayMyBuddy

DROP SCHEMA IF EXISTS `PayMyBuddy`;
CREATE SCHEMA IF NOT EXISTS `PayMyBuddy` DEFAULT CHARACTER SET utf8;
USE `PayMyBuddy`;

-- Table: user
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `balance` DOUBLE NOT NULL DEFAULT 0,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- Table: transaction
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(225) NULL,
  `amount` DOUBLE NOT NULL,
  `sender_id` INT NOT NULL,
  `receiver_id` INT NOT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_transaction_sender`
    FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_transaction_receiver`
    FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Table: connection
CREATE TABLE IF NOT EXISTS `connection` (
  `user_id_1` INT NOT NULL,
  `user_id_2` INT NOT NULL,
  PRIMARY KEY (`user_id_1`, `user_id_2`),
  CONSTRAINT `fk_connection_user1`
    FOREIGN KEY (`user_id_1`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_connection_user2`
    FOREIGN KEY (`user_id_2`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Insérer des utilisateurs dans la table user
INSERT INTO `user` (`name`, `email`, `password`, `balance`, `create_time`) VALUES
('Alice Dupont', 'alice.dupont@example.com', '$2a$10$hSD798tDRdZxpVybrpwtA.IYm.l6DGLgwZtlXvJIxtU5YcKLUgpva', 100.50, NOW()),
('Bob Martin', 'bob.martin@example.com', '$2a$10$jgnhBStOjBCx0UhJYeeKgurtfpldBjj1pyTgbGs3e1xkpGEk9ktiG', 250.00, NOW()),
('Chloé Bernard', 'chloe.bernard@example.com', '$2a$10$mR/YD/2qRchr7SwlKSP4Y.uMi2.8HFC6I3FY.1jja.xaepNe5sPm2', 75.25, NOW()),
('David Lefevre', 'david.lefevre@example.com', '$2a$10$zj/goNAjv0TCY1A0lYg6HOznyTxfd6aQMPWvwHqpTRv197Kc3gS5q', 0.00, NOW()),
('Emma Dubois', 'emma.dubois@example.com', '$2a$10$PjQErtrh6k32wIlCTMQG1.Nd7KoRQopi6LE/wa8H3Lmztun4vbaR.', 500.00, NOW());

