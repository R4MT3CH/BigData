import mysql.connector
from mysql.connector import errorcode
from datetime import datetime

# Function to connect to the MySQL database
def connect_to_database():
    try:
        connection = mysql.connector.connect( 
            host="localhost", 
            user="root", 
            passwd="password", 
            database="BDChallange"
        )
        return connection
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)

# Function to display sales table data
def display_sales_data(cursor):
    query = "SELECT * FROM sales"
    cursor.execute(query)
    rows = cursor.fetchall()
    for row in rows:
        print(row)

# Function to insert a row into the sales table
def insert_sales_data(cursor, connection):
    try:
        customer_id = input("Enter Customer ID: ")
        order_date_str = input("Enter Order Date (YYYY-MM-DD): ")
        
        # Parse the input string to a date object
        order_date = datetime.strptime(order_date_str, '%Y-%m-%d').date()
        
        product_id = input("Enter Product ID: ")

        query = "INSERT INTO sales (customer_id, order_date, product_id) VALUES (%s, %s, %s)"
        data = (customer_id, order_date, product_id)
        cursor.execute(query, data)
        connection.commit()
        print("Row inserted successfully.")
    except mysql.connector.Error as err:
        print(f"Error: {err}")
    except ValueError:
        print("Invalid input. Please enter the correct data types.")

# Main function
def main():
    connection = connect_to_database()
    if connection:
        cursor = connection.cursor()
        menu = input("Select a option: (1- Display sales table, 2-Insert sales data) : ")
         
        if menu=='1':
            print("Displaying sales table data:")
            display_sales_data(cursor)
        elif menu=='2':
            insert_sales_data(cursor, connection)
        
        cursor.close()
        connection.close()
    else:
        print("Failed to connect to the database.")

if __name__ == "__main__":
    main()


