DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) DEFAULT NULL,
  `created` datetime NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `users` (`id`, `access_token`, `created`, `email`, `name`, `password`) VALUES
(1,	NULL,	'2022-04-19 12:47:33',	'john.doe@gmail.com',	'John Doe',	'$2a$10$ASaIXVftgSY4bXziRoE1w.SGe7qvpuTyCSeI0NM2DUYLflOLlJGwy'),
(2,	NULL,	'2022-04-19 12:47:33',	'test.user@gmail.com',	'Test User',	'$2a$10$ASaIXVftgSY4bXziRoE1w.SGe7qvpuTyCSeI0NM2DUYLflOLlJGwy');


DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_price` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `product_groups`;
CREATE TABLE `product_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `group_name` varchar(255) NOT NULL,
  `price` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `product_groups` (`id`, `created`, `group_name`, `price`) VALUES
(1,	'2022-04-19 12:47:33',	'Vehicles',	''),
(13,	'2022-05-02 10:30:22',	'Clothes',	NULL);

DROP TABLE IF EXISTS `group_variants`;
CREATE TABLE group_variants (
  id int(11) NOT NULL AUTO_INCREMENT,
  variant_name varchar(255) NOT NULL,
  group_id int(11) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_group_id (group_id),
  CONSTRAINT fk_group_id FOREIGN KEY (group_id) REFERENCES product_groups (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `group_variants` (`id`, `variant_name`, `group_id`) VALUES
(1,	'Blue',	1),
(2,	'White', 1);

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` varchar(5) DEFAULT NULL,
  `description` text,
  `created` datetime NOT NULL,
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9an6554j5j7f3x8rifrgh4phr` (`group_id`),
  KEY `FKdb050tk37qryv15hd932626th` (`user_id`),
  CONSTRAINT `FK9an6554j5j7f3x8rifrgh4phr` FOREIGN KEY (`group_id`) REFERENCES `product_groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKdb050tk37qryv15hd932626th` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `products` (`id`, `name`, `price`, `description`, `created`, `group_id`, `user_id`) VALUES
(6,	'Shirt',	'24',	NULL,	'2022-05-02 15:58:11',	13,	1),
(7,	'Sweat Shirt',	'60',	NULL,	'2022-05-02 15:58:18',	13,	1),
(8,	'Flag',	'24',	NULL,	'2022-05-02 16:05:46',	1,	1),
(9,	'Golf V',	'20000',	NULL,	'2022-04-19 15:53:40',	1,	1);

DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` varchar(255) DEFAULT NULL,
  `product_variant_id` int(11) DEFAULT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrftu5hh0vupk70occwy3rx8on` (`product_variant_id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKrftu5hh0vupk70occwy3rx8on` FOREIGN KEY (`product_variant_id`) REFERENCES `group_variants` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int not NULL,
  `path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;