package FlightDataRead

import java.util.Properties
import org.apache.spark.sql.types._
import org.apache.spark.sql.{SaveMode, SparkSession}

object FlightDataRead {

  def runReader() {

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("FlightDataRead")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val schema = new StructType()
      .add("column0", IntegerType, true)
      .add("flightdate", TimestampType, true)
      .add("airline", StringType, true)
      .add("tailnumber", StringType, true)
      .add("flightnumber", StringType, true)
      .add("origin", StringType, true)
      .add("origincityname", StringType, true)
      .add("originstate", StringType, true)
      .add("originstatefips", IntegerType, true)
      .add("dest", StringType, true)
      .add("destcityname", StringType, true)
      .add("deststate", StringType, true)
      .add("deststatefips", IntegerType, true)
      .add("deststatename", StringType, true)
      .add("depdelay", DoubleType, true)
      .add("arrdelay", DoubleType, true)
      .add("cancelled", DoubleType, true)
      .add("cancellationcode", StringType, true)
      .add("crselapsedtime", DoubleType, true)
      .add("actualelapsedtime", DoubleType, true)
      .add("airtime", DoubleType, true)
      .add("flights", DoubleType, true)
      .add("distance", DoubleType, true)
      .add("distancegroup", DoubleType, true)
      .add("carrierdelay", DoubleType, true)
      .add("weatherdelay", DoubleType, true)
      .add("nasdelay", DoubleType, true)
      .add("securitydelay", DoubleType, true)
      .add("lateaircraftdelay", DoubleType, true)
      .add("arrivaldate", TimestampType, true)
      .add("crsarrivaldate", TimestampType, true)
      .add("depdatetime", StringType, true)
      .add("arrdatetime", StringType, true)
      .add("crsdepdatetime", StringType, true)
      .add("crsarrdatetime", StringType, true)

    val baseDF = spark.read
      .options(
        Map("header" -> "true",
          "inferSchema" -> "false",
          "delimiter"->",",
          "quotes"->"\"",
          "dateFormat"->"yyyy-MM-dd",
          "timestampFormat"->"MM-dd-yyyy hh mm ss",
          "treatEmptyValuesAsNulls" -> "true",
          "mode"-> "DROPMALFORMED"
        ))
      .schema(schema)
      .csv("Datasets/FlightData.csv")

    val ckProperties = new Properties()
    ckProperties.setProperty("user","default")
    ckProperties.setProperty("password","qwertyuiop")

    baseDF
      .write
      .mode(SaveMode.Append)
      .options(
        Map(
          "driver" -> "ru.yandex.clickhouse.ClickHouseDriver"
        ))
      .jdbc(url = "jdbc:clickhouse://192.168.254.22:8123/FlightData",
        table = "flight_data_spark_csv",
        connectionProperties = ckProperties
      )

    spark.stop()
  }
  def main(args: Array[String]) {
    println("Run Main Program Starting... ***\n")
    FlightDataRead.runReader()
  }
}
