1--create table role

CREATE TABLE `h8_bus`.`tb_role` 
(`id` INT NOT NULL AUTO_INCREMENT , `role` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , PRIMARY KEY (`id`))
 ENGINE = InnoDB;

2--create table user

CREATE TABLE `h8_bus`.`tb_user` 
(`id` INT NOT NULL AUTO_INCREMENT , 
`email` VARCHAR(50) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`password` VARCHAR(100) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`first_name` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci , 
`last_name` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci , 
`mobile_number` VARCHAR(15) CHARACTER SET armscii8 COLLATE armscii8_general_ci , 
`roles` INT, 
PRIMARY KEY (`id`),
FOREIGN KEY (roles) REFERENCES tb_role(id))
ENGINE = InnoDB;

3--create table agency

CREATE TABLE `h8_bus`.`tb_agency` 
(`id` INT NOT NULL AUTO_INCREMENT , 
`code` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`name` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`details` VARCHAR(50) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`owner` INT, 
PRIMARY KEY (`id`),
FOREIGN KEY (owner) REFERENCES tb_user(id),
INDEX (code, name))
ENGINE = InnoDB;

4--create table bus

CREATE TABLE `h8_bus`.`tb_bus` 
(`id` INT NOT NULL AUTO_INCREMENT , 
`code` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`capacity` INT(20),
`make` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL ,
`agency` INT,
PRIMARY KEY (`id`),
FOREIGN KEY (agency) REFERENCES tb_agency(id),
INDEX (code))
ENGINE = InnoDB;

5--create table stop

CREATE TABLE `h8_bus`.`tb_stop` 
(`id` INT NOT NULL AUTO_INCREMENT ,
`code` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`name` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL , 
`detail` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL ,
PRIMARY KEY (`id`), 
INDEX (code))
ENGINE = InnoDB;

6--create table trip

CREATE TABLE `h8_bus`.`tb_trip` 
(`id` INT NOT NULL AUTO_INCREMENT , 
`fare` INT,
`journey_time` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL ,
`source_stop` INT,
`dest_stop` INT,
`bus` INT,
`agency` INT,
PRIMARY KEY (`id`),
FOREIGN KEY (source_stop) REFERENCES tb_stop(id),
FOREIGN KEY (dest_stop) REFERENCES tb_stop(id),
FOREIGN KEY (bus) REFERENCES tb_bus(id),
FOREIGN KEY (agency) REFERENCES tb_agency(id))
ENGINE = InnoDB;

7--create table trip schedule

CREATE TABLE `h8_bus`.`tb_tripschedule` 
(`id` INT NOT NULL AUTO_INCREMENT ,
`trip_date` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL, 
`available_seats` INT,
`trip_detail` INT,
`tickets_sold` JSON,
PRIMARY KEY (`id`),
FOREIGN KEY (trip_detail) REFERENCES tb_trip(id))
ENGINE = InnoDB;



8--create table ticket
CREATE TABLE `h8_bus`.`tb_ticket` 
(`id` INT NOT NULL AUTO_INCREMENT ,
`seat_number` INT,
`cancellable` BOOLEAN,
`journey_date` VARCHAR(20) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL, 
`passanger` INT,
`trip_schedule` INT,
PRIMARY KEY (`id`),
FOREIGN KEY (passanger) REFERENCES tb_user(id),
FOREIGN KEY (trip_schedule) REFERENCES tb_tripschedule(id))
ENGINE = InnoDB;




