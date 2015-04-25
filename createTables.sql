CREATE DATABASE  IF NOT EXISTS `chatvs`;
USE `chatvs`;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `idmessages` int(11) NOT NULL AUTO_INCREMENT,
  `owner` varchar(64) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime,
  PRIMARY KEY (`idmessages`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `idusers` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  PRIMARY KEY (`idusers`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `servers`
--

DROP TABLE IF EXISTS `servers`;

CREATE TABLE `servers` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`host` varchar(255) NOT NULL,
	`port` int(6),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Create user for the chat application.
--
CREATE USER 'chat-app'@'localhost' IDENTIFIED BY 'chat-app-password';
GRANT SELECT,INSERT ON chatvs.messages TO 'chat-app'@'localhost';
GRANT SELECT,INSERT,DELETE ON chatvs.users TO 'chat-app'@'localhost';
GRANT SELECT ON chatvs.servers TO 'chat-app'@'localhost';