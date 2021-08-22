package top.benull.hitrecsys.movie.offline.spark.evaluator

import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}

case class Evaluator(){
  def evaluate(predictions: DataFrame): Unit = {

    import predictions.sparkSession.implicits._
    val scoreAndLabels = predictions.select(col("label"), col("probability")).rdd.map { row =>
      (row.getAs[DenseVector](1)(0), row.getDouble(0))
    }

    val metrics = new BinaryClassificationMetrics(scoreAndLabels)

    println("AUC under PR = " + metrics.areaUnderPR())
    println("AUC under ROC = " + metrics.areaUnderROC())

  }
}
object Evaluator{

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(this.getClass.getSimpleName)
      .master("local")
      .getOrCreate()

    val df = spark.createDataFrame(Seq(
      (0.0, new DenseVector(Array(0.8))),
      (1.0, new DenseVector(Array(0.8))),
      (1.0, new DenseVector(Array(0.8)))
    )).toDF("label", "probability")

    Evaluator().evaluate(df)
  }

}

