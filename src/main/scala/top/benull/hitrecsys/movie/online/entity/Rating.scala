package top.benull.hitrecsys.movie.online.entity

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.{JsonSerializable, SerializerProvider}

import scala.beans.BeanProperty

/**
 * Rating Class, contains attributes loaded from movielens ratings.csv
 */
case class Rating() {
  @BeanProperty var movieId: Int = _
  @BeanProperty var userId: Int = _
  @BeanProperty var score: Float = _
  @BeanProperty var timestamp: Long = _
}
