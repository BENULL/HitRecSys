package top.benull.hitrecsys.movie.online.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RestController}
import top.benull.hitrecsys.movie.online.common.ServerResponse
import top.benull.hitrecsys.movie.online.service.MovieService

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 04 17:31
 */
@RestController
class MovieController @Autowired() (private val movieService: MovieService){
  @GetMapping(value = Array("/movie/{id}"))
  def getMovieById(@PathVariable(value = "id") id: Int) = movieService.getMovieById(id) match {
    case Some(movie) => ServerResponse.createBySuccess(movie)
    case None => ServerResponse.createByErrorMessage("userid not exist")
  }
}
