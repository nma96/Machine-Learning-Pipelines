package DBtoCHPipeline

import java.util.Properties
import com.datastax.driver.core.utils.UUIDs
import org.apache.spark.sql.{SaveMode, SparkSession, functions}

object DBtoCH {

  def runPipeline() {

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("DBtoCH")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val departmentDF = spark.read
      .format("jdbc")
      .option("driver","com.mysql.cj.jdbc.Driver") //.option("driver","com.mysql.cj.jdbc.Driver")
      .option("url", "jdbc:mysql://localhost/demo2?serverTimezone=EST")
      .option("dbtable", "(select * from Department) as t")
      .option("fetchSize", "100")
      .option("user", "root")
      .option("password", "")
      .load();

    val employeeDF = spark.read
      .format("jdbc")
      .option("driver","com.mysql.cj.jdbc.Driver") //.option("driver","com.mysql.cj.jdbc.Driver")
      .option("url", "jdbc:mysql://localhost/demo2?serverTimezone=EST")
      .option("dbtable", "(select * from Employee) as t")
      .option("fetchSize", "100")
      .option("user", "root")
      .option("password", "")
      .load();

    val combinedDF = employeeDF.join(departmentDF, employeeDF.col("DepartmentID").equalTo(departmentDF.col("DepartmentID")))
      // Since both Employee and Department Tables have DepartmentID, select only one among those, along with the rest of the columns.
      .select(employeeDF.col("EmployeeID"),
        employeeDF.col("EmployeeName"),
        employeeDF.col("DepartmentID"),
        employeeDF.col("JoinDate"),
        departmentDF.col("DepartmentName"));

    spark.udf.register("createuuid", () => UUIDs.timeBased.toString)

    val addedDF = combinedDF.withColumn("Age",org.apache.spark.sql.functions
      .floor(org.apache.spark.sql.functions.datediff(org.apache.spark.sql.functions.current_date(),
        combinedDF.col("JoinDate")).divide(365)))
      .withColumn("EmployeeFactUUID",functions.callUDF("createuuid").alias("employeeuuid"))
      .withColumn("DepartmentNameUpper", org.apache.spark.sql.functions.upper(combinedDF.col("DepartmentName")));

    val finalDS = addedDF
      // Select all the columns required for the final dataframe that will be written to clickhouse
      .select(addedDF.col("EmployeeFactUUID").as("employeefactuuid"),
        addedDF.col("EmployeeID").as("employeeid"),
        addedDF.col("EmployeeName").as("employeename"),
        addedDF.col("JoinDate").as("joindate"),
        addedDF.col("DepartmentName").as("departmentname"),
        addedDF.col("DepartmentID").as("departmentID"),
        addedDF.col("DepartmentNameUpper").as("departmentnameupper"),
        addedDF.col("Age").as("age"));

    val ckProperties = new Properties()
    ckProperties.setProperty("user","default")
    ckProperties.setProperty("password","qwertyuiop")

    finalDS
      .write
      .mode(SaveMode.Append)
      .options(
        Map(
          "driver" -> "ru.yandex.clickhouse.ClickHouseDriver"
        ))
      .jdbc(url = "jdbc:clickhouse://192.168.254.22:8123/demo2",
        table = "EmpDept",
        connectionProperties = ckProperties
      )

    spark.stop()
  }
  def main(args: Array[String]) {
    println("Run Main Program Starting... ***\n")
    DBtoCH.runPipeline()
  }
}
