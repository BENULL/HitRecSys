package top.benull.hitrecsys.movie

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import top.benull.hitrecsys.movie.online.datamanager.DataManager

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 08 - 16 14:48
 */
@SpringBootApplication
class HitRecSysMovieApplication

object HitRecSysMovie extends App {
  val ratingDataPath = this.getClass.getResource("/webroot/sampledata/ratings.csv").getPath
  val movieDataPath = this.getClass.getResource("/webroot/sampledata/movies.csv").getPath
  val linkDataPath = this.getClass.getResource("/webroot/sampledata/links.csv").getPath
  val movieEmbPath = this.getClass.getResource("/webroot/modeldata/item2vecEmb.csv").getPath
  val userEmbPath = this.getClass.getResource("/webroot/modeldata/userEmb.csv").getPath
  DataManager.getInstance().loadData(movieDataPath, linkDataPath, ratingDataPath, movieEmbPath, userEmbPath,"i2vEmb", "uEmb")
  SpringApplication.run(classOf[HitRecSysMovieApplication])
}