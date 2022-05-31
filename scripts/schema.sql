create database posts;


use posts;

create table users (
	id INT AUTO_INCREMENT PRIMARY KEY, 
    email VARCHAR(50) not null,
    password VARCHAR(120) not null
);


create table posts (
	id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    post_text TEXT,
    post_creation_time TIMESTAMP,
    user_id INT not null,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

create table comments(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_text TEXT,
    comment_creation_time TIMESTAMP,
    user_id INT not null,
    post_id BIGINT not null,
    FOREIGN KEY(user_id)  REFERENCES users(id),
    FOREIGN KEY(post_id)  REFERENCES posts(id)
);


create table roles (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
CREATE TABLE USER_ROLES(
	user_id INT,
    role_id INT,
    PRIMARY KEY(user_id, role_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(user_id) REFERENCES roles(id)
);


insert into roles(name) values ('ROLE_USER');
insert into roles(name) values ('ROLE_ADMIN');
