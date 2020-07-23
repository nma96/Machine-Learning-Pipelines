package PurchasePrediction

import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object PurchasePredict {

  def runRegression() {

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("PredictionPipeline")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val schema = new StructType()
      .add("User ID", DoubleType, true)
      .add("Gender", StringType, true)
      .add("Age", DoubleType, true)
      .add("EstimatedSalary", DoubleType, true)
      .add("label", DoubleType, true)

    val baseDF = spark.read
      .options(
        Map("header" -> "true",
          "inferSchema" -> "false",
          "delimiter"->",",
          "quotes"->"\"",
          "dateFormat"->"yyyy-MM-dd",
          "timestampFormat"->"yyyy-MM-dd hh:mm",
          "treatEmptyValuesAsNulls" -> "true",
          "mode"-> "DROPMALFORMED"
        ))
      .schema(schema)
      .csv("Datasets/Social_Network_Ads.csv")

    val genderindexer = new StringIndexer()
      .setInputCol("Gender")
      .setOutputCol("IndexedGender")
    val finalDF = genderindexer.fit(baseDF).transform(baseDF)

    finalDF.show()

    val cols = Array("IndexedGender", "Age", "EstimatedSalary")

    val assembler = new VectorAssembler()
      .setInputCols(cols)
      .setOutputCol("features")
    val featureDf = assembler.transform(finalDF)
    featureDf.printSchema()
    featureDf.show(10)


    val seed = 5043
    val Array(trainingData, testData) = featureDf.randomSplit(Array(0.7, 0.3), seed)

    //    // train logistic regression model with training data set
    //    val logisticRegression = new LogisticRegression()
    //      .setMaxIter(100)
    //      .setRegParam(0.0001)
    //      .setElasticNetParam(0.09)
    //    val logisticRegressionModel = logisticRegression.fit(trainingData)
    //
    //    val predictionDf = logisticRegressionModel.transform(testData)
    //    predictionDf.show(10)

    val randomForestClassifier = new RandomForestClassifier()
      .setImpurity("gini")
      .setMaxDepth(3)
      .setNumTrees(10)
      .setFeatureSubsetStrategy("auto")
      .setSeed(seed)
    val randomForestModel = randomForestClassifier.fit(trainingData)
    //    println(randomForestModel.toDebugString)
    val predictionDf = randomForestModel.transform(testData)
    predictionDf.show(10)

    // evaluate model with area under ROC
    val evaluator = new BinaryClassificationEvaluator()
      .setLabelCol("label")
      .setRawPredictionCol("prediction")
      .setMetricName("areaUnderROC")

    // measure the accuracy
    val accuracy = evaluator.evaluate(predictionDf)
    println("Accuracy: "+accuracy)

    spark.stop()
  }
  def main(args: Array[String]) {
    println("Run Main Program Starting... ***\n")
    PurchasePredict.runRegression()
  }
}
