name := "MachineLearningPipeline"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion = "2.4.6"
val cassandraVersion = "2.4.0"
val sqlServerVersion = "8.2.2.jre8"
lazy val excludeJpountz = ExclusionRule(organization = "net.jpountz.lz4", name = "lz4")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.12" % Provided,
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-sql" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-streaming" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-catalyst" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-mllib" % sparkVersion % Provided,
  "org.apache.hadoop" % "hadoop-common" % "3.2.1" % Provided,
  "org.apache.hadoop" % "hadoop-client" % "3.2.1" % Provided,
  "org.apache.hadoop" % "hadoop-aws" % "3.2.1" % Provided,
  "com.databricks" %% "dbutils-api" % "0.0.4" % Provided,
  "com.datastax.spark" %% "spark-cassandra-connector" % cassandraVersion % Provided,
  "com.microsoft.sqlserver" % "mssql-jdbc" % sqlServerVersion % Provided,
  "mysql" % "mysql-connector-java" % "8.0.20" % Provided,
  "cc.blynk.clickhouse" % "clickhouse4j" % "1.4.4" % Provided,
  "ru.yandex.clickhouse" % "clickhouse-jdbc" % "0.2.4" % Provided,
  "cc.blynk.clickhouse" % "clickhouse4j" % "1.4.4" % Provided,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.1"
)
