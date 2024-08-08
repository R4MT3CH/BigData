import mysql.connector

# connection
db_config = {
    'user': 'root',
    'password': 'password',
    'host': 'localhost',
    'database': 'BDChallange'
}


def full_load_customer_to_backup(db_config):
    # Connect to the MySQL database
    conn = mysql.connector.connect(
        user=db_config['user'],
        password=db_config['password'],
        host=db_config['host'],
        database=db_config['database']
    )
    cursor = conn.cursor()
    
    # Create the customer_backup table if it doesn't exist
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS customer_backup LIKE customer;
    """)
    
    # Delete all existing records in customer_backup
    cursor.execute("TRUNCATE TABLE customer_backup")
    
    # Insert all records from customer to customer_backup
    cursor.execute("INSERT INTO customer_backup SELECT * FROM customer")
    
    # Commit and close the connection
    conn.commit()
    cursor.close()
    conn.close()


def incremental_load_customer_to_backup(df_config):
    # Connect to the MySQL database
    conn = mysql.connector.connect(
        user=db_config['user'],
        password=db_config['password'],
        host=db_config['host'],
        database=db_config['database']
    )

    cursor = conn.cursor()

    # Incremental load: Synchronize customer_backup with customer
    cursor.execute("""
        INSERT INTO customer_backup (cid, name, email, lastChange)
        SELECT cid, name, email, lastChange
        FROM customer
        ON DUPLICATE KEY UPDATE
            name = VALUES(name),
            email = VALUES(email),
            lastChange = VALUES(lastChange);

        DELETE FROM customer_backup
        WHERE cid NOT IN (SELECT cid FROM customer);
    """)

    # Commit and close connection
    conn.commit()
    cursor.close()
    conn.close()

def add_triggers(db_config):
    # Connect to the MySQL database
    conn = mysql.connector.connect(
        user=db_config['user'],
        password=db_config['password'],
        host=db_config['host'],
        database=db_config['database']
    )
    cursor = conn.cursor()

    # Define the triggers
    trigger_after_insert = """
    CREATE TRIGGER after_customer_insert
    AFTER INSERT ON customer
    FOR EACH ROW
    BEGIN
        INSERT INTO customer_backup (cid, name, email, lastChange)
        VALUES (NEW.cid, NEW.name, NEW.email, NEW.lastChange)
        ON DUPLICATE KEY UPDATE
            name = VALUES(name),
            email = VALUES(email),
            lastChange = VALUES(lastChange);
    END;
    """

    trigger_after_update = """
    CREATE TRIGGER after_customer_update
    AFTER UPDATE ON customer
    FOR EACH ROW
    BEGIN
        UPDATE customer_backup
        SET name = NEW.name,
            email = NEW.email,
            lastChange = NEW.lastChange
        WHERE cid = NEW.cid;
    END;
    """

    trigger_after_delete = """
    CREATE TRIGGER after_customer_delete
    AFTER DELETE ON customer
    FOR EACH ROW
    BEGIN
        DELETE FROM customer_backup
        WHERE cid = OLD.cid;
    END;
    """

    # Set the delimiter
    cursor.execute("DELIMITER $$")
    
    # Add the triggers to the database
    try:
        cursor.execute(trigger_after_insert)
        cursor.execute("$$")
        cursor.execute(trigger_after_update)
        cursor.execute("$$")
        cursor.execute(trigger_after_delete)
        cursor.execute("$$")
        print("Triggers added successfully!")
    except mysql.connector.Error as err:
        print(f"Error: {err}")
    
    # Reset the delimiter
    cursor.execute("DELIMITER ;")

    # Commit and close connection
    conn.commit()
    cursor.close()
    conn.close()

# full load
full_load_customer_to_backup(db_config)

# Run the incremental load function
incremental_load_customer_to_backup(db_config)


# Call the function to add triggers to the database
# this needs to be run only one time
add_triggers(db_config)