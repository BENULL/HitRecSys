package top.benull.hitrecsys.movie.online.service

import org.springframework.stereotype.Service
import top.benull.hitrecsys.movie.online.entity.User

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 08 - 27 14:46
 */

@Service
class UserService {

  def getUserById(id: Long) = {
    Option(User())
  }

}
