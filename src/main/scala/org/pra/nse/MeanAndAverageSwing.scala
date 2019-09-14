package org.pra.nse

//import org.apache.spark.sql.{SQLContext, SparkSession}
//import org.apache.spark.sql.types.StructType
//import org.apache.spark.sql.types._
// For implicit conversions like converting RDDs to DataFrames
//import spark.implicits._

class MeanAndAverageSwing {
  def compute(): Unit = {
    //        CREATE TEMPORARY TABLE temp_diamonds
    //        USING csv
    //        OPTIONS (path "/databricks-datasets/Rdatasets/data-001/csv/ggplot2/diamonds.csv", header "true", mode "FAILFAST")
//    val schema = new StructType()
//      .add("_c0", IntegerType, true)
//      .add("carat", DoubleType, true)
//      .add("cut", StringType, true)
//      .add("color", StringType, true)
//      .add("clarity", StringType, true)
//      .add("depth", DoubleType, true)
//      .add("table", DoubleType, true)
//      .add("price", IntegerType, true)
//      .add("x", DoubleType, true)
//      .add("y", DoubleType, true)
//      .add("z", DoubleType, true)
//
//    val sqlContext = new SQLContext()
//    val diamonds_with_schema = sqlContext.read.format("csv").option("header", "true").schema(schema).load("/databricks-datasets/Rdatasets/data-001/csv/ggplot2/diamonds.csv")
  }

  def compute2(): Unit = {
//    val spark = SparkSession
//      .builder()
//      .appName("Pradeep NSE Compute Using Scala")
//      //.config("spark.some.config.option", "some-value")
//      .getOrCreate()
////    val usersDF = spark.read.load("examples/src/main/resources/users.parquet")
////    usersDF.select("name", "favorite_color").write.save("namesAndFavColors.parquet")
//    val usersDF = spark.read
//    .format("csv")
//    .option("sep", ",")
//    .option("inferSchema", "true")
//    .option("header", "true")
//    .load("C:\\Users\\prajinda\\pra-nse-fo\\fo-2019-09-05.csv")
//    usersDF.show(10)
  }
}
