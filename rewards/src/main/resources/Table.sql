-- Drop tables if they already exist (safe start)
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
    cust_id INT AUTO_INCREMENT PRIMARY KEY,
    cust_name VARCHAR(100) NOT NULL,
    phone_no CHAR(10) NOT NULL
);

CREATE TABLE transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    amount INT NOT NULL,
    cust_id INT,
    CONSTRAINT fk_customer FOREIGN KEY (cust_id) REFERENCES customer(cust_id) ON DELETE CASCADE
);


INSERT INTO customer (cust_name, phone_no) VALUES ('John Doe', 9876543210);
INSERT INTO customer (cust_name, phone_no) VALUES ('Alice Smith', 9123456780);

INSERT INTO transaction (amount, date, cust_id) VALUES (120, '2025-08-01', 1);
INSERT INTO transaction (amount, date, cust_id) VALUES (75, '2025-08-05', 2);
INSERT INTO transaction (amount, date, cust_id) VALUES (200, '2025-08-10', 1);
