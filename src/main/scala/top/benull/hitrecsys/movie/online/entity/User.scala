package top.benull.hitrecsys.movie.online.entity


import net.minidev.json.annotate.JsonIgnore
import top.benull.hitrecsys.movie.online.entity.Rating
import top.benull.hitrecsys.movie.online.model.Embedding

import scala.beans.BeanProperty

/**
 * User class, contains attributes loaded from movielens ratings.csv
 */
case class User(){
  @BeanProperty var userId:Int = _
  @BeanProperty var averageRating:Double = 0
  @BeanProperty var highestRating:Double = 0
  @BeanProperty var lowestRating:Double = 5.0
  @BeanProperty var ratingCount:Int = 0
  @BeanProperty var ratings:List[Rating] = List()
  var userFeatures : Map[String,String] = Map()
  @BeanProperty @JsonIgnore var emb:Embedding = _

  def addRating(rating: Rating) = {
    ratings = ratings:+rating
    averageRating = (averageRating * ratingCount + rating.getScore) / (ratingCount + 1)
    highestRating = math.max(highestRating,rating.getScore)
    lowestRating = math.min(lowestRating, rating.getScore)
    ratingCount += 1
  }
}
