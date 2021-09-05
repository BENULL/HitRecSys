package top.benull.hitrecsys.movie.online.recprocess

import top.benull.hitrecsys.movie.online.datamanager.DataManager
import top.benull.hitrecsys.movie.online.entity.Movie

import scala.collection.mutable

/**
 * descriptions: Recommendation process of similar movies
 * author: benull
 * date: 2021 - 09 - 05 11:52
 */

object SimilarMovieProcess {

  /**
   * generate candidates for similar movies recommendation
   * @param movie input movie object
   * @return  movie candidates
   */
  def candidateGenerator(movie: Movie) = {
    movie.getGenres.flatMap(genre=>{
      DataManager.getInstance().getMoviesByGenre(genre, 100, "rating").get.map(candidateMovie =>{
        (candidateMovie.getMovieId,candidateMovie)
      })
    }).filter(_._1!=movie.getMovieId).map(_._2)
  }

  /**
   * function to calculate similarity score based on embedding
   * @param movie     input movie
   * @param candidate candidate movie
   * @return  similarity score
   */
  def calculateEmbSimilarScore(movie: Movie, candidate: Movie): Double = {
    if (null == movie || null == candidate) -1.0 else movie.getEmb.calculateSimilarity(candidate.getEmb)
  }

  /**
   * function to calculate similarity score
   * @param movie     input movie
   * @param candidate candidate movie
   * @return  similarity score
   */
  def calculateSimilarScore(movie: Movie, candidate: Movie):Double = {
    val sameGenreCount = movie.getGenres.count(genre => candidate.getGenres.contains(genre))

    val genreSimilarity = sameGenreCount * 1.0 / (movie.getGenres.length + candidate.getGenres.length) / 2
    val ratingScore = candidate.getAverageRating / 5

    val similarityWeight = 0.7
    val ratingScoreWeight = 0.3
    genreSimilarity * similarityWeight + ratingScore * ratingScoreWeight
  }

  /**
   * rank candidates
   *
   * @param movie    input movie
   * @param candidates    movie candidates
   * @param model     model name used for ranking
   * @return  ranked movie list
   */
  def ranker(movie: Movie, candidates:List[Movie], model: String):List[Movie] = {
    candidates.map(candidate => {
      model match {
        case "emb" => (candidate, calculateEmbSimilarScore(movie, candidate))
        case _ => (candidate, calculateSimilarScore(movie, candidate))
      }
    }).sortBy(_._2)(Ordering.Double.reverse).map(_._1)
  }

  /**
   * get recommendation movie list
   *
   * @param movieId input movie id
   * @param size  size of similar items
   * @param model model used for calculating similarity
   * @return  list of similar movies
   */
  def getRecList(movieId:Int, size: Int, model:String):List[Movie] = {
    DataManager.getInstance().getMovieById(movieId) match {
      case Some(movie) => {
        val candidates = candidateGenerator(movie)
        ranker(movie, candidates, model).take(size)
      }
      case None => List()
    }
  }
}