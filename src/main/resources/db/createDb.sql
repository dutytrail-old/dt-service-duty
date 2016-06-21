DROP TABLE dt_user_subscribe_duty;
DROP TABLE dt_duty;
DROP TABLE dt_user;
CREATE TABLE `dt_user_subscribe_duty` (`user_id` mediumint(9) NOT NULL, `duty_id` mediumint(9) NOT NULL, PRIMARY KEY (`user_id`,`duty_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `dt_duty` (`id` mediumint(9) NOT NULL AUTO_INCREMENT, `name` varchar(100) NOT NULL DEFAULT '', PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
CREATE TABLE `dt_user` (`id` mediumint(9) NOT NULL AUTO_INCREMENT, `name` varchar(100) NOT NULL DEFAULT '', PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
INSERT INTO dt_user VALUES (1, 'Joan');
COMMIT;