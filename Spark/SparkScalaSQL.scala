//Spark-scala code to read from mysql database for all tables
//  1 to 5 : dataframe
//  6 to 10 : spark sql

package org.itc.com

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, count, countDistinct, dense_rank, desc, row_number, sum}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.itc.com.sparksqldemo.spark


object SparkScalaSQL extends App {

  //MySQL connection
  val jdbcUrl = "jdbc:mysql://localhost:3306/BDChallange"
  val jdbcUsername = "root"
  val jdbcPassword = "password"

  val connectionProperties = new java.util.Properties()
  connectionProperties.setProperty("user", jdbcUsername)
  connectionProperties.setProperty("password", jdbcPassword)


  Logger.getLogger("org").setLevel(Level.ERROR)

  val sparkconf = new SparkConf()
  sparkconf.set("spark.app.name","DataframeDemo")
  sparkconf.set("spark.master","local[*]")

  val spark = SparkSession.builder().config(sparkconf).getOrCreate();

  //get datas from database into df

  val salesdf = spark.read.jdbc(jdbcUrl, "sales", connectionProperties);
  val menudf = spark.read.jdbc(jdbcUrl, "menu", connectionProperties);
  val membersdf = spark.read.jdbc(jdbcUrl, "members", connectionProperties);


  salesdf.show()

  menudf.show()

  membersdf.show()


  //  1. What is the total amount each customer spent at the restaurant?

  // Calculate total amount spent by each customer
  val totalSpent = salesdf
    .join(menudf, salesdf("product_id") === menudf("product_id"))
    .groupBy("customer_id")
    .agg(sum("price").alias("amount"))
    .orderBy("customer_id")

  // Show the result
  totalSpent.show()

//  2. How many days has each customer visited the restaurant?
// Calculate the number of distinct order dates for each customer
val visitCount = salesdf
  .groupBy("customer_id")
  .agg(countDistinct("order_date").alias("days_visited"))
  .orderBy("customer_id")

  // Show the result
  visitCount.show()


//  3. What was the first item from the menu purchased by each customer?

  // Define the window specification
  val windowSpec3 = Window
    .partitionBy("customer_id")
    .orderBy("order_date")

  // Perform the join and add row number
  val joinedDF3 = salesdf
    .join(menudf, salesdf("product_id") === menudf("product_id"))
    .withColumn("rownum", row_number().over(windowSpec3))

  // Filter to get the first product for each customer
  val result = joinedDF3
    .filter(col("rownum") === 1)
    .select("customer_id", "product_name")

  // Show the result
  result.show()

//  4. What is the most purchased item on the menu and how many times was it purchased by all customers?
// Join sales and menu DataFrames
val joinedDF4 = salesdf
  .join(menudf, salesdf("product_id") === menudf("product_id"))

  // Calculate product counts
  val productCounts = joinedDF4
    .groupBy("product_name")
    .agg(count("product_name").alias("prod_count"))
    .orderBy(desc("prod_count"))
    .limit(1)

  // Show the result
  productCounts.show()

//  5. Which item was the most popular for each customer?

  // Define the window specification
  val windowSpec5 = Window
  .partitionBy("customer_id")
  .orderBy(desc("order_count"))

  // Perform the join, aggregation, and apply window function
  val itemCounts = salesdf
    .join(menudf, salesdf("product_id") === menudf("product_id"))
    .groupBy("customer_id", "product_name")
    .agg(count("*").alias("order_count"))
    .withColumn("rn", dense_rank().over(windowSpec5))

  // Filter to get the top product for each customer
  val topProducts = itemCounts
    .filter(col("rn") === 1)
    .select("customer_id", "product_name")

  // Show the result
  topProducts.show()

  // Register DataFrames as temporary views
  salesdf.createOrReplaceTempView("sales")
  menudf.createOrReplaceTempView("menu")
  membersdf.createOrReplaceTempView("members")

//  6. Which item was purchased first by the customer after they became a member?
  // Define the SQL query
  val sqlQuery6 = """
      WITH cte AS (
        SELECT s.customer_id, s.order_date, m.product_name, mb.join_date,
               DENSE_RANK() OVER (PARTITION BY s.customer_id ORDER BY s.order_date) as rn
        FROM sales s
        JOIN menu m ON s.product_id = m.product_id
        JOIN members mb ON s.customer_id = mb.customer_id
        WHERE s.order_date >= mb.join_date
      )
      SELECT customer_id, product_name, order_date
      FROM cte
      WHERE rn = 1
    """

  // Execute the SQL query
  val result6 = spark.sql(sqlQuery6)

  // Show the result
  result6.show()

  //  7. Which item was purchased just before the customer became a member?
  // Define the SQL query
  val sqlQuery7 = """
     WITH cte AS (SELECT s.customer_id, s.order_date, m.product_name, mb.join_date,
      DENSE_RANK() OVER (PARTITION BY s.customer_id ORDER BY s.order_date DESC) as rn
      FROM sales s
      JOIN menu m ON s.product_id = m.product_id
      JOIN members mb ON s.customer_id = mb.customer_id
      WHERE s.order_date<mb.join_date)
      SELECT customer_id, product_name FROM cte WHERE rn=1
    """

  // Execute the SQL query
  val result7 = spark.sql(sqlQuery7)

  // Show the result
  result7.show()

//  8. What is the total items and amount spent for each member before they became a member?

  // Define the SQL query
  val sqlQuery8 = """
     SELECT s.customer_id,
      COUNT(m.product_name) as total_items, SUM(m.price) as total_spent
      FROM sales s
      JOIN menu m ON s.product_id = m.product_id
      JOIN members mb ON s.customer_id = mb.customer_id
      WHERE s.order_date<mb.join_date
      GROUP BY s.customer_id ORDER BY s.customer_id
    """

  // Execute the SQL query
  val result8 = spark.sql(sqlQuery8)

  // Show the result
  result8.show()


//  9.  If each $1 spent equates to 10 points and sushi has a 2x points multiplier - how many points would each customer have?

  // Define the SQL query
  val sqlQuery9 = """
    WITH points AS (
  SELECT * , CASE WHEN product_id=1
  THEN price*20 ELSE price*10 END as Points
  FROM menu m)
  SELECT s.customer_id, SUM(p.points) as points
  FROM sales s JOIN points p
  WHERE s.product_id = p.product_id
  GROUP BY s.customer_id
    """

  // Execute the SQL query
  val result9 = spark.sql(sqlQuery9)

  // Show the result
  result9.show()

//  10. In the first week after a customer joins the program (including their join date)
  //  they earn 2x points on all items, not just sushi - how many points do customer
  //  A and B have at the end of January?

  // Define the SQL query
  val sqlQuery10 = """
      WITH points_table AS (
        SELECT s.customer_id, m.product_name, m.price, s.order_date, mb.join_date,
               CASE
                 WHEN s.order_date BETWEEN mb.join_date AND DATE_ADD(mb.join_date, 7) THEN m.price * 10 * 2
                 WHEN m.product_name = 'sushi' THEN m.price * 10 * 2
                 ELSE m.price * 10
               END AS points
        FROM sales s
        JOIN menu m ON s.product_id = m.product_id
        JOIN members mb ON s.customer_id = mb.customer_id
        WHERE s.order_date < '2021-02-01'
      )
      SELECT customer_id, SUM(points) AS total_points
      FROM points_table
      WHERE customer_id IN ('A', 'B')
      GROUP BY customer_id
    """

  // Execute the SQL query
  val result10 = spark.sql(sqlQuery10)

  // Show the result
  result10.show()



}
