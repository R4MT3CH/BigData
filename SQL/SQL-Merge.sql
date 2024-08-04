CREATE TABLE customer (
    cid INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    lastChange TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO customer (cid, name, email) VALUES (1, 'Alice', 'alice@example.com');
INSERT INTO customer (cid, name, email) VALUES (2, 'Bob', 'bob@example.com');
INSERT INTO customer (cid, name, email) VALUES (3, 'Charlie', 'charlie@example.com');

SELECT * FROM customer;

CREATE TABLE customer_backup (
    cid INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    lastChange TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

SELECT * FROM customer_backup;

-- Copy data from customer to customer_backup
INSERT INTO customer_backup (cid, name, email, lastChange)
SELECT cid, name, email, lastChange FROM customer;

UPDATE customer
SET email = 'newalice@example.com'
WHERE cid = 1;

DELETE FROM customer
WHERE cid = 2;

INSERT INTO customer (cid, name, email)
VALUES (4, 'David', 'david@example.com');

SELECT * FROM customer;

-- Merge was not available in MYSQL
-- MERGE INTO customer_backup AS target
-- USING customer AS source
-- ON target.cid = source.cid
-- WHEN MATCHED THEN
--     UPDATE SET
--         target.name = source.name,
--         target.email = source.email,
--         target.lastChange = source.lastChange
-- WHEN NOT MATCHED BY TARGET THEN
--     INSERT (cid, name, email, lastChange)
--     VALUES (source.cid, source.name, source.email, source.lastChange);



INSERT INTO customer_backup (cid, name, email, lastChange)
SELECT cid, name, email, lastChange
FROM customer
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    email = VALUES(email),
    lastChange = VALUES(lastChange);

DELETE FROM customer_backup
WHERE cid NOT IN (SELECT cid FROM customer);

SELECT * FROM customer_backup;