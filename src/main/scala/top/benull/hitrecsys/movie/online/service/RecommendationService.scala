package top.benull.hitrecsys.movie.online.service

import org.springframework.stereotype.Service
import top.benull.hitrecsys.movie.online.datamanager.DataManager
import top.benull.hitrecsys.movie.online.recprocess.{RecForYouProcess, SimilarMovieProcess}

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 05 10:52
 */
@Service
class RecommendationService {
  def recForYou(userId: Int, size: Int, model: String) = {
    RecForYouProcess.getRecList(userId, size, model)
  }

  def getSimilarMovieRec(movieId: Int, size: Int, model: String) = {
    SimilarMovieProcess.getRecList(movieId.toInt, size, model)
  }

  def getMoviesByGenre(genre: String, size: Int, sortBy: String) = {
    DataManager.getInstance().getMoviesByGenre(genre, size, sortBy)
  }

}
