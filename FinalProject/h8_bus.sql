-- Adminer 4.8.1 MySQL 5.5.5-10.4.24-MariaDB dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `agency`;
CREATE TABLE `agency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8y56hykf78k5u3wmutny52wcf` (`owner_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `agency` (`id`, `code`, `details`, `name`, `owner_user_id`) VALUES
(1,	'001',	'Pesan Ticket',	'akunku',	1),
(2,	'002',	'Pesan Ticket',	'akunmu',	2),
(3,	'003',	'Pesan Ticket',	'akunnya',	3),
(4,	'004',	'Pesan Ticket',	'akunlagi',	4),
(5,	'004',	'Agency pemesanan tiket online',	'Traveloka',	4),
(6,	NULL,	NULL,	NULL,	5),
(7,	NULL,	NULL,	NULL,	6);

DROP TABLE IF EXISTS `bus`;
CREATE TABLE `bus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `capacity` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `make` varchar(20) DEFAULT NULL,
  `agency_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK64nil2xxuhqde813s57dp1n9t` (`agency_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `bus` (`id`, `capacity`, `code`, `make`, `agency_id`) VALUES
(1,	20,	'BDL01',	'20',	1),
(2,	15,	'BDL02',	'15',	1),
(3,	25,	'001',	'20',	4);

DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES
(1,	'1',	'<< Flyway Baseline >>',	'BASELINE',	'<< Flyway Baseline >>',	NULL,	'root',	'2022-07-05 11:13:56',	0,	1),
(2,	'1.1',	'insert bus',	'SQL',	'V1.1__insert_bus.sql',	-323711555,	'root',	'2022-07-05 11:13:56',	9,	1),
(3,	'1.2',	'insert role',	'SQL',	'V1.2__insert_role.sql',	-555065487,	'root',	'2022-07-05 11:13:56',	5,	1),
(4,	'1.3',	'insert stop',	'SQL',	'V1.3__insert_stop.sql',	-297623919,	'root',	'2022-07-05 11:13:56',	3,	1),
(5,	'1.4',	'insert trip',	'SQL',	'V1.4__insert_trip.sql',	-1324405995,	'root',	'2022-07-05 11:13:56',	5,	1);

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `roles` (`id`, `name`) VALUES
(1,	'ROLE_USER'),
(2,	'ROLE_ADMIN');

DROP TABLE IF EXISTS `stop`;
CREATE TABLE `stop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `stop` (`id`, `code`, `detail`, `name`) VALUES
(1,	'1-9',	'Bulan',	'Bintang 1-9'),
(2,	'1-8',	'Cimeng',	'Sukaraja 1-8'),
(3,	'1-3',	'Raja BasaI',	'Kedaton 1-3'),
(4,	'1-2',	'Sukabumi',	'Sukarame 1-2'),
(5,	'1-10',	'Tanjung Senang',	'Jati Agung 1-10');

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cancellable` bit(1) DEFAULT NULL,
  `journey_date` varchar(255) DEFAULT NULL,
  `seat_number` int(11) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `trip_schedule_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdvt57mcco3ogsosi97odw563o` (`user_id`),
  KEY `FKfhudhsxbslgtmbrbkd3uak4ha` (`trip_schedule_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `ticket` (`id`, `cancellable`, `journey_date`, `seat_number`, `user_id`, `trip_schedule_id`) VALUES
(1,	CONV('0', 2, 10) + 0,	'2022-07-09',	0,	4,	2);

DROP TABLE IF EXISTS `trip`;
CREATE TABLE `trip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fare` int(11) NOT NULL,
  `journey_time` int(11) NOT NULL,
  `agency_id` bigint(20) DEFAULT NULL,
  `bus_id` bigint(20) DEFAULT NULL,
  `dest_stop_id` bigint(20) DEFAULT NULL,
  `source_stop_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKab03gksoo5t3lo12ga3ixnykk` (`agency_id`),
  KEY `FKptvi61dd1hao1yig3in0gvcjs` (`bus_id`),
  KEY `FK1evbxrekvflflkfscj2i0fwv0` (`dest_stop_id`),
  KEY `FK5ln8w8n974euslk976dh6x7k5` (`source_stop_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `trip` (`id`, `fare`, `journey_time`, `agency_id`, `bus_id`, `dest_stop_id`, `source_stop_id`) VALUES
(1,	15000,	15,	1,	1,	3,	1),
(2,	15000,	2,	1,	1,	5,	2),
(3,	15000,	3,	1,	1,	2,	3),
(4,	15000,	5,	1,	1,	4,	4),
(5,	15000,	4,	1,	1,	2,	5),
(6,	15000,	7,	1,	1,	6,	6),
(7,	2000,	0,	1,	1,	1,	1);

DROP TABLE IF EXISTS `trip_schedule`;
CREATE TABLE `trip_schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `available_seats` int(11) NOT NULL,
  `trip_date` varchar(255) DEFAULT NULL,
  `trip_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaqjflucdvoypakmjxtb7n2mmm` (`trip_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `trip_schedule` (`id`, `available_seats`, `trip_date`, `trip_id`) VALUES
(1,	0,	NULL,	1),
(2,	19,	'2022-07-09',	1),
(3,	20,	'2022-07-09',	7);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `first_name` varchar(120) DEFAULT NULL,
  `last_name` varchar(120) DEFAULT NULL,
  `mobile_number` varchar(120) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (`id`, `email`, `first_name`, `last_name`, `mobile_number`, `password`, `username`) VALUES
(1,	'akun1@gmail.com',	'akun',	'satu',	'081111111111',	'$2a$10$3IOL3bWeCu21y4w9B10GBOXLYfO5hmLhWb4WajoZol1Wa1P9i6QWK',	'akun1'),
(2,	'akun2@gmail.com',	'akun',	'duaa',	'082222222222',	'$2a$10$4LtqmyTks.GazZyZkEHLsO5URx3.PwN6iIDF/0iYa1/VqDYWl0xLC',	'akun2'),
(4,	'akun4@gmail.com',	'akun',	'empat',	'08444444444',	'$2a$10$Ii6AtOama79NRmfBvhyd3.o0eZEZTYtNsYpzEaZEy.S.XqmX88LtW',	'akun4'),
(5,	'akun5@gmail.com',	'akun',	'lima',	'085555555555',	'$2a$10$3Z6P6ZaenEm4IfNfhKkNd.vOF65fb7zOBgsDAqXZ3iVE3ZpHIM4wW',	'akun5'),
(6,	'akun6@gmail.com',	'akun',	'enam',	'08666666666',	'$2a$10$se/pAK5An86EMmU/slniV.Vx/W.tNc8TGNJm0edy.XaGHb4Qs8D4O',	'akun6');

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1,	1),
(2,	1),
(4,	2),
(5,	1),
(6,	1),
(6,	2);

-- 2022-07-05 15:14:07
