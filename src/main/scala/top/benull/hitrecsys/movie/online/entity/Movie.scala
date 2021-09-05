package top.benull.hitrecsys.movie.online.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import top.benull.hitrecsys.movie.online.model.Embedding

import scala.beans.BeanProperty

/**
 * Movie Class, contains attributes loaded from movielens movies.csv
 * and other advanced data like averageRating, emb, etc.
 */
case class Movie(){
  @BeanProperty var movieId : Int = _
  @BeanProperty var title : String = _
  @BeanProperty var releaseYear: Int = _
  @BeanProperty var imdbId : String = _
  @BeanProperty var tmdbId : String = _
  @BeanProperty var ratingNumber: Int = 0
  @BeanProperty var averageRating: Double = 0
  @BeanProperty var genres:List[String] = List()
  @BeanProperty @JsonIgnore var ratings: Array[Rating] = Array()
  @BeanProperty @JsonIgnore var emb: Embedding = _
  @BeanProperty var topRatings: List[Rating] = List()
  @JsonIgnore var movieFeatures: Map[String,String] = Map()

  private val TOP_RATING_SIZE = 10

  def addTopRating(rating: Rating): Unit = {
    topRatings = topRatings :+ rating
    topRatings = topRatings.sortBy(_.getScore)(Ordering.Float.reverse).take(TOP_RATING_SIZE)
  }

  def addRating(rating:Rating) : Unit= {
    averageRating = (averageRating * ratingNumber + rating.getScore) / (ratingNumber+1)
    ratingNumber += 1
    ratings = ratings :+ rating
    addTopRating(rating)
  }

  def addGenre(genre:String) : Unit ={
    genres = genres:+genre
  }

}

object Movie{
  def main(args: Array[String]): Unit = {
    var m = Movie()
    println(m.getTopRatings.length)
    m.addRating(Rating())
    println(m.getTopRatings.length)




  }
}
