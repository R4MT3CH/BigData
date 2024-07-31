package org.itc.com

import org.apache.spark.SparkContext

object SensorData {

  def main(args: Array[String]): Unit ={

    val sc = new SparkContext(master="local[1]", appName = "AppName")

    val rdd1 = sc.textFile(path="C:\\Users\\ramin\\OneDrive\\Documents\\BigData\\input\\sensordata.txt")


    val temps = rdd1.flatMap { line =>
      val parts = line.split(",")
      if (parts.length == 3) {
        Some(parts(2).toDouble)
      } else {
        None
      }
    }

    // Filter temperatures greater than 50 and map to (sensor_id, 1)
    val counts = rdd1
      .map { line =>
        val parts = line.split(",")
        val sensorId = parts(0)
        val temp = parts(2).toDouble
        if (temp > 50) (sensorId, 1) else (sensorId, 0)
      }
      .reduceByKey((count1, count2) => count1 + count2)

    // Find the maximum temperature
    val maxTemp = temps.max()


    println(s"Highest temperature: $maxTemp")

    // Collect and print the results
    counts.collect().foreach { case (sensorId, count) =>
      println(s"$sensorId, $count")
    }
  }

}