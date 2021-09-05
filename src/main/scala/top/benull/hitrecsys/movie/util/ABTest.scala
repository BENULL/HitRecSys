package top.benull.hitrecsys.movie.util

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 05 11:42
 */
object ABTest {
  val trafficSplitNumber:Int = 5
  val bucketAModel:String = "emb"
  val bucketBModel:String = "nerualcf"
  val defaultModel:String = "emb"

  def getConfigByUserId(userId:String) =  userId match {
    case null => defaultModel
    case _ if userId.isEmpty => defaultModel
    case _ if userId.hashCode() % trafficSplitNumber == 0 => {
      println(s"$userId is in bucketA.")
      bucketAModel
    }
    case _ if userId.hashCode() % trafficSplitNumber == 1 => {
      println(s"$userId is in bucketB.")
      bucketBModel
    }
    case _ => {
      println(s"$userId isn't in ABTest.")
      defaultModel
    }
  }
}