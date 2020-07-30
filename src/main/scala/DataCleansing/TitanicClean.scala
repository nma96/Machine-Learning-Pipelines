package DataCleansing

import org.apache.spark.ml.feature.{Bucketizer, StandardScaler, StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.IntegerType


object TitanicClean {
  def clean() {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("CleanPipeline")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val baseDF = spark.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("Datasets/Titanic/train.csv")

    baseDF.printSchema()

//    val fareDF = baseDF.select("Fare")
//    fareDF.describe().show()
//
//    val passenger_fareDF = baseDF.select("PassengerId", "Fare")

    val bucketizer = new Bucketizer()
      .setInputCol("Fare")
      .setOutputCol("Bucketized_Fare")
      .setSplits(Array(0.0, 100.0, 200.0, 300.0, 400.0, Double.PositiveInfinity))

    val bucketedData = bucketizer.transform(baseDF)
    bucketedData.show(30)

    bucketedData.groupBy("Bucketized_Fare").count().orderBy("Bucketized_Fare").show()

    val finalDF = bucketedData.withColumn("Int_Fare",bucketedData("Bucketized_Fare").cast("Integer")).drop("Bucketized_Fare")
    finalDF.show(30)
    finalDF.printSchema()

    val Quantiles = finalDF.stat.approxQuantile("Fare", Array(0.25, 0.75), 0.0)
    val Q1 = Quantiles(0)
    val Q3 = Quantiles(1)
    val IQR = Q3 - Q1
    val low= Q1 - 1.5*IQR
    val up= Q3 + 1.5*IQR

    val cleanDF = finalDF.filter(s"Fare > $low and Fare < $up").drop("Int_Fare")
    cleanDF.describe().show()

//    val cols = Array("Int_Fare")
//
//    val assembler = new VectorAssembler()
//      .setInputCols(cols)
//      .setOutputCol("features")
//    val featureDf = assembler.transform(finalDF)

  }



  def main(args: Array[String]) {
    println("Run Main Program Starting... ***\n")
    TitanicClean.clean()
  }
}
