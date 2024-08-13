package org.itc.com

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, split, when, min,max,avg}
import org.apache.spark.sql.types.{DoubleType, FloatType, StringType, StructField, StructType}


object ScalaSparkHW  extends App {


  Logger.getLogger("org").setLevel(Level.ERROR)

  val sparkconf = new SparkConf()
  sparkconf.set("spark.app.name","DataframeDemo")
  sparkconf.set("spark.master","local[*]")

  val spark = SparkSession.builder().config(sparkconf).getOrCreate();

  val productschema = StructType(Array(
    StructField("product_number", StringType, nullable=false),
    StructField("product_name", StringType, nullable=false),
    StructField("product_category", StringType, nullable=false),
    StructField("product_scale", StringType, nullable=false),
    StructField("product_manufacturer", StringType, nullable=false),
    StructField("product_description", StringType, nullable=false),
    StructField("length", DoubleType, nullable=false),
    StructField("width", DoubleType, nullable=false),
    StructField("height", FloatType, nullable=false)
  ))

  val productdf = spark.read.option("header",true).schema(productschema).csv("C:\\Users\\ramin\\OneDrive\\Documents\\BigData\\sample_data\\warehouse\\raw\\products.csv\\");

  productdf.show()
  println(productdf.count())
  //change datatype from double to float
  var casteddf = productdf.withColumn("length",col("length").cast("Float")).withColumn("width",col("width").cast("Float"))

  //remove duplicates
  casteddf = casteddf.dropDuplicates("product_number")

  //missing value
  //  var cleaneddf = casteddf.na.fill("unknown", Seq("product_name")).na.fill(0.0,Seq("length","width","height"))

  // trim spaces in begin and end
  //  cleaneddf = cleaneddf.withColumn("product_name", trim(col("product_name")))

  //  cleaneddf.show();
  //  println(cleaneddf.count())

  //1. remove outlier for length and width

  var validdf = casteddf.filter(col("width")<200).filter(col("length")<7000)

  //2. Add 2 new columns based on product_number, split by _ and first part as store_name and second as product_number

  var addcolumnsdf = validdf.withColumn("store_name", split(col("product_number"), "_")(0))
    .withColumn("new_product_number", split(col("product_number"), "_")(1))

  //3. Add new column product_classification considering length and width.
  // if length >4000 and width >20 then "small and wide"
  // if length >6000 and width >40 then "small and narrow"
  // if length >8000 and width >60 then "large and wide"
  //else "large and narrow.

  addcolumnsdf = addcolumnsdf.withColumn("product_classification",
    when(col("length")>4000 and col("width")>20,"Small and wide")
      .when(col("length")>6000 and col("width")>40,"small and narrow")
      .when(col("length")>8000 and col("width")>60,"large and wide")
      .otherwise("large and narrow")

  )

  addcolumnsdf.show()
  println(addcolumnsdf.count())


  //4. Find min , max , avg of length for each category
  val categoryStats = addcolumnsdf.groupBy("product_category")
    .agg(
      min("length").as("min_length"),
      max("length").as("max_length"),
      avg("length").as("avg_length")
    )

  println("Category Stats (Min, Max, Avg Length):")
  categoryStats.show()

}
