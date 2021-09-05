package top.benull.hitrecsys.movie.online.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RestController}
import top.benull.hitrecsys.movie.online.common.ServerResponse
import top.benull.hitrecsys.movie.online.entity.User
import top.benull.hitrecsys.movie.online.service.UserService

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 08 - 27 14:37
 */
@RestController
class UserController @Autowired() (private val userService: UserService){

  @GetMapping(value = Array("/user/{id}"))
  def getUserById(@PathVariable(value = "id") id: Int) = userService.getUserById(id) match {
    case Some(user) => ServerResponse.createBySuccess(user)
    case None => ServerResponse.createByErrorMessage("user does not exist")
  }

}
