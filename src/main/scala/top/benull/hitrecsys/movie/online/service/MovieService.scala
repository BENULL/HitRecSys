package top.benull.hitrecsys.movie.online.service

import org.springframework.stereotype.Service
import top.benull.hitrecsys.movie.online.datamanager.DataManager


/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 04 17:32
 */
@Service
class MovieService {
  def getMovieById(id: Int) = {
    DataManager.getInstance().getMovieById(id)
  }


}
