package top.benull.hitrecsys.movie.online.recprocess

import top.benull.hitrecsys.movie.online.datamanager.DataManager
import top.benull.hitrecsys.movie.online.entity.{Movie, User}

/**
 * descriptions:
 *   Recommendation process of similar movies
 * author: benull
 * date: 2021 - 09 - 05 14:38
 */
object RecForYouProcess {

  def callNeuralCFTFServing(user: User, candidates: List[Movie]) = {
    List()
  }

  def ranker(user: User, candidates: List[Movie], model: String):List[Movie] =  {
    val candidatesMap = model match {
      case "emb" => candidates.map(candidate => (candidate,calculateEmbSimilarScore(user, candidate)))
      case "nerualcf" => callNeuralCFTFServing(user, candidates)
      case _ => candidates.zip((1 to candidates.length).reverse.map(_.toDouble))
    }
    candidatesMap.sortBy(_._2)(Ordering.Double.reverse).map(_._1)
  }

  def calculateEmbSimilarScore(user:User, candidate:Movie):Double = {
    if (null == user || null == candidate || null == user.getEmb) -1d else user.getEmb.calculateSimilarity(candidate.getEmb)
  }

  /**
   * get recommendation movie list
   *
   * @param userId input user id
   * @param size  size of similar items
   * @param model model used for calculating similarity
   * @return  list of similar movies
   */
  def getRecList(userId:Int, size:Int, model:String):List[Movie] = {
    DataManager.getInstance().getUserById(userId) match {
      case Some(user) => {
        val CANDIDATE_SIZE = 800
        val candidates = DataManager.getInstance().getMovies(CANDIDATE_SIZE, "rating")
        ranker(user, candidates, model).take(size)
      }
      case None => List()
    }
  }
}