
use testjdbc;

/*
 * Create table
 */
CREATE TABLE `users` (
  `id`     bigint not null auto_increment primary key,
  `name`   varchar(100) not null,
  `email`  varchar(100) not null,
  `contry` varchar(100) not null
);

CREATE TABLE `Permision`(
    `id` int(11) not null auto_increment primary key,
    `name` varchar(50) not null
);

CREATE TABLE `User_Permision`(
    `permision_id` int(11),
    `user_id` bigint(11),
    KEY `users`(`user_id`),
    UNIQUE `usersP_unique`(`user_id`, `permision_id`)
);


/*
 * Insert data
 */
INSERT INTO `users`(`name`, `email`, `contry`) VALUES
    ('users 1', 'abc@abc.com', 'USA'),
    ('coder', 'cpde@a.com', 'VN');



INSERT INTO `Permision`(`name`) VALUES
    ('add'),
    ('edit'),
    ('delete'),
    ('view');




/*
 * Create Stored Procedure get user by ID
 */
DELIMITER $$
CREATE PROCEDURE get_user_by_id (IN user_id INT)
BEGIN
    SELECT users.name, users.email, users.country
    FROM users
    where users.id = user_id;
    END$$
DELIMITER ;


/*
 * Create Stored Procedure insert user
 */
DELIMITER $$
CREATE PROCEDURE insert_user (
    IN user_name varchar(50),
    IN user_email varchar(50),
    IN user_country varchar(50)
)
BEGIN
    INSERT INTO users(name, email, country) VALUES(user_name, user_email, user_country);
    END$$
DELIMITER ;

