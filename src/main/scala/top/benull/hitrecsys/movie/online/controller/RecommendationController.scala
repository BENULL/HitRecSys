package top.benull.hitrecsys.movie.online.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}
import org.springframework.web.servlet.tags.Param
import top.benull.hitrecsys.movie.online.common.ServerResponse
import top.benull.hitrecsys.movie.online.service.RecommendationService

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 05 10:50
 */
@RestController
class RecommendationController @Autowired() (private val recommendationService: RecommendationService) {

  @GetMapping(value = Array("/recMovieByGenre"))
  def getRecMoviesByGenre(@Param genre:String, @Param size:String, @Param sortBy:String ) = recommendationService.getMoviesByGenre(genre, size.toInt, sortBy) match {
    case Some(movies) => ServerResponse.createBySuccess(movies)
    case None => ServerResponse.createByErrorMessage("movies do not exist")
  }

  @GetMapping(value = Array("/recForYou"))
  def getRecForYou(@Param userId:String, @Param size:String, @Param model:String ) = {
    ServerResponse.createBySuccess(recommendationService.recForYou(userId.toInt, size.toInt, model))
  }

  @GetMapping(value = Array("/similarMovieRec"))
  def getSimilarMovieRec(@Param movieId:String, @Param size:String, @Param model:String ) = {
    ServerResponse.createBySuccess(recommendationService.getSimilarMovieRec(movieId.toInt, size.toInt, model))
  }
}
