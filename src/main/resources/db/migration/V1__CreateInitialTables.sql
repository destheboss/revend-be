CREATE TABLE users (
    id int NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    image_data BLOB,
    PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    role_name varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES users (id)
);