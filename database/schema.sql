DROP DATABASE IF EXISTS eshop;

CREATE DATABASE eshop;

USE eshop;

CREATE TABLE customers(
    name VARCHAR(32) NOT NULL,
    address VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,
    PRIMARY KEY(name)
);

CREATE TABLE `order`(
    id INT AUTO_INCREMENT NOT NULL,
    order_id CHAR(8) NOT NULL,
    name VARCHAR(32) NOT NULL,
    item VARCHAR(32),
    quantity INT,
    PRIMARY KEY(id),
    CONSTRAINT fk_name
        FOREIGN KEY(name) REFERENCES customers(name)
);

CREATE TABLE order_status(
    order_id CHAR(8) NOT NULL,
    delivery_id VARCHAR(128) NOT NULL,
    status ENUM('pending','dispatched'),
    status_update DATE,
    PRIMARY KEY(delivery_id)
    -- CONSTRAINT fk_order_id
    --     FOREIGN KEY(order_id) REFERENCES `order`(order_id)
);

SELECT "Inserting data into user table" AS "";

INSERT INTO customers(name, address, email) VALUES
("fred", "201 Cobblestone Lane", "fredflintstone@bedrock.com"),
("sherlock", "221B Baker Street, London", "sherlock@consultingdetective.org"),
("spongebob", "124 Conch Street, Bikini Bottom", "spongebob@yahoo.com"),
("jessica", "698 Candlewood Land, Cabot Cove", "fletcher@gmail.com"),
("dursley", "4 Privet Drive, Little Whinging, Surrey", "dursley@gmail.com");

SELECT "Completed!" AS "";