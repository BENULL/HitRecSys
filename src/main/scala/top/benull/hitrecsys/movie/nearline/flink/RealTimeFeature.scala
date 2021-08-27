package top.benull.hitrecsys.movie.nearline.flink


import org.apache.flink.api.java.io.TextInputFormat
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.{PrintSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 08 - 26 10:33
 */
case class Rating(line:String){
  val Array(userId, movieId, rating, timestamp) = line.split(",")
  val latestMovieId = movieId
}

object RealTimeFeature {

  def test():Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val ratingResourcesPath = this.getClass.getResource("/webroot/sampledata/ratings.csv")

    // monitor directory, checking for new files
    val format = new TextInputFormat(new Path(ratingResourcesPath.getPath))

    val inputStream:DataStream[String] = env.readFile(
      format,
      ratingResourcesPath.getPath,
      FileProcessingMode.PROCESS_CONTINUOUSLY,
      100)

    val ratingStream = inputStream.map(Rating)
    ratingStream.keyBy(_.userId)
      .timeWindow(Time.seconds(1))
      .reduce({(r1, r2)=> if (r1.timestamp.compareTo(r2.timestamp) > 0) r1 else r2})
      .addSink(new SinkFunction[Rating] {
        override def invoke(value: Rating, context: SinkFunction.Context[_]): Unit = {
          println(s"userId: ${value.userId} \t lastMovieId: ${value.latestMovieId}")
        }
      })
    env.execute()
  }

  def main(args: Array[String]): Unit = {
    test()
  }

}