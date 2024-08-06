import mysql.connector

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

# Example usage
db_config = {
    'user': 'root',
    'password': 'password',
    'host': 'localhost',
    'database': 'BDChallange'
}
full_load_customer_to_backup(db_config)