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

