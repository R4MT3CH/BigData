CREATE TABLE sales (
  customer_id VARCHAR(1),
  order_date DATE,
  product_id INTEGER
);

INSERT INTO sales
  (customer_id, order_date, product_id)
VALUES
  ('A', '2021-01-01', '1'),
  ('A', '2021-01-01', '2'),
  ('A', '2021-01-07', '2'),
  ('A', '2021-01-10', '3'),
  ('A', '2021-01-11', '3'),
  ('A', '2021-01-11', '3'),
  ('B', '2021-01-01', '2'),
  ('B', '2021-01-02', '2'),
  ('B', '2021-01-04', '1'),
  ('B', '2021-01-11', '1'),
  ('B', '2021-01-16', '3'),
  ('B', '2021-02-01', '3'),
  ('C', '2021-01-01', '3'),
  ('C', '2021-01-01', '3'),
  ('C', '2021-01-07', '3');


 

CREATE TABLE menu (
  product_id INTEGER,
  product_name VARCHAR(5),
  price INTEGER
);

INSERT INTO menu
  (product_id, product_name, price)
VALUES
  ('1', 'sushi', '10'),
  ('2', 'curry', '15'),
  ('3', 'ramen', '12');
  

CREATE TABLE members (
  customer_id VARCHAR(1),
  join_date DATE
);

INSERT INTO members
  (customer_id, join_date)
VALUES
  ('A', '2021-01-07'),
  ('B', '2021-01-09');
  
  
show tables;

-- 1. What is the total amount each customer spent at the restaurant?

SELECT s.customer_id, sum(m.price) amount FROM sales s 
JOIN menu m on s.product_id = m.product_id
GROUP BY customer_id ORDER BY customer_id;

-- 2. How many days has each customer visited the restaurant?

SELECT customer_id, COUNT(DISTINCT order_date) FROM sales GROUP BY customer_id;


-- 3. What was the first item from the menu purchased by each customer?

WITH cte AS (SELECT s.customer_id , m.product_name, 
ROW_NUMBER() OVER(PARTITION BY s.customer_id ORDER BY s.order_date) rownum
FROM sales s JOIN menu m WHERE s.product_id = m.product_id)
SELECT customer_id, product_name FROM cte WHERE rownum = 1;


-- 4. What is the most purchased item on the menu and how many times was it purchased by all customers?
SELECT m.product_name, COUNT(m.product_name) as prod_count
FROM sales s JOIN menu m ON s.product_id = m.product_id
GROUP BY m.product_name ORDER BY prod_count DESC LIMIT 1;

-- 5. Which item was the most popular for each customer?
WITH item_count AS (
SELECT s.customer_id, m.product_name, COUNT(*) as order_count,
DENSE_RANK() OVER (PARTITION BY s.customer_id ORDER BY COUNT(*) DESC) as rn
FROM sales s JOIN menu m  WHERE s.product_id = m.product_id
GROUP BY s.customer_id, m.product_name
)
SELECT customer_id, product_name FROM item_count WHERE rn=1;


-- 6. Which item was purchased first by the customer after they became a member?

WITH cte AS (
SELECT s.customer_id, s.order_date, m.product_name, mb.join_date,
DENSE_RANK() OVER (PARTITION BY s.customer_id ORDER BY s.order_date) as rn
FROM sales s 
JOIN menu m ON s.product_id = m.product_id 
JOIN members mb ON s.customer_id = mb.customer_id 
WHERE s.order_date >= mb.join_date
)
SELECT customer_id, product_name FROM cte WHERE rn=1;

-- 7. Which item was purchased just before the customer became a member?

WITH cte AS (SELECT s.customer_id, s.order_date, m.product_name, mb.join_date,
DENSE_RANK() OVER (PARTITION BY s.customer_id ORDER BY s.order_date DESC) as rn
FROM sales s 
JOIN menu m ON s.product_id = m.product_id
JOIN members mb ON s.customer_id = mb.customer_id
WHERE s.order_date<mb.join_date)
SELECT customer_id, product_name FROM cte WHERE rn=1;

-- 8. What is the total items and amount spent for each member before they became a member?

SELECT s.customer_id,
COUNT(m.product_name) as total_items, SUM(m.price) as total_spent
FROM sales s 
JOIN menu m ON s.product_id = m.product_id
JOIN members mb ON s.customer_id = mb.customer_id
WHERE s.order_date<mb.join_date
GROUP BY s.customer_id ORDER BY s.customer_id;

-- 9.  If each $1 spent equates to 10 points and sushi has a 2x points multiplier - how many points would each customer have?

WITH points AS (
  SELECT * , CASE WHEN product_id=1 
  THEN price*20 ELSE price*10 END as Points 
FROM menu m) 
SELECT s.customer_id, SUM(p.points) as points 
FROM sales s JOIN points p
WHERE s.product_id = p.product_id
GROUP BY s.customer_id; 

-- 10. In the first week after a customer joins the program (including their join date) they earn 2x points on all items, not just sushi - how many points do customer A and B have at the end of January?

WITH points_table AS (SELECT s.customer_id, m.product_name, m.price, order_date, join_date,
CASE 
  WHEN s.order_date BETWEEN mb.join_date AND DATE_ADD(mb.join_date, INTERVAL 7 DAY) THEN m.price*10*2
  WHEN m.product_name = 'sushi' THEN m.price*10*2
  ELSE m.price*10
  END as points
FROM menu m 
JOIN sales s
ON s.product_id = m.product_id
JOIN members mb
ON s.customer_id = mb.customer_id
WHERE order_date<'2021-02-01')
SELECT customer_id, SUM(points) as total_points FROM points_table GROUP BY customer_id;