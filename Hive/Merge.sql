-- 1. Then, create the customer table with cid as the primary key:

CREATE TABLE customer (
    cid INT,
    name STRING,
    email STRING,
    lastChange TIMESTAMP
) CLUSTERED BY (cid) INTO 3 BUCKETS
STORED AS ORC
TBLPROPERTIES (
    'transactional'='true',
);

-- 2. Insert 3 records into the customer table.

INSERT INTO customer (cid, name, email, lastChange) VALUES 
(1, 'John Doe', 'john.doe@example.com', current_timestamp()),
(2, 'Jane Smith', 'jane.smith@example.com', current_timestamp()),
(3, 'Bob Johnson', 'bob.johnson@example.com', current_timestamp());

-- 3. Create customer_backup table and insert the same data.

CREATE TABLE customer_backup LIKE customer;

INSERT INTO customer_backup 
SELECT * FROM customer;

-- 4. Update the email of the customer with cid=1 in the customer table.

UPDATE customer 
SET email = 'new.email@example.com', lastChange = current_timestamp() 
WHERE cid = 1;


-- 5. Delete the customer with cid=2 from the customer table.

DELETE FROM customer WHERE cid = 2;

-- 6. Insert a new record with cid=4 in the customer table.

INSERT INTO customer (cid, name, email, lastChange) VALUES 
(4, 'Alice Brown', 'alice.brown@example.com', current_timestamp());

-- 7. Use the MERGE statement to update the customer_backup table using the customer table.

MERGE INTO customer_backup AS b
USING customer AS c
ON b.cid = c.cid
WHEN MATCHED THEN
    UPDATE SET b.name = c.name, b.email = c.email, b.lastChange = c.lastChange
WHEN NOT MATCHED THEN
    INSERT VALUES (c.cid, c.name, c.email, c.lastChange)
WHEN NOT MATCHED BY SOURCE THEN
    DELETE;
