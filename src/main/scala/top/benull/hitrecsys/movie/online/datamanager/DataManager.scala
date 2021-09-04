package top.benull.hitrecsys.movie.online.datamanager

import java.io.File
import java.net.URI

import breeze.linalg.csvread
import org.springframework.context.annotation.Bean
import top.benull.hitrecsys.movie.online.entity.{Movie, Rating, User}
import top.benull.hitrecsys.movie.util.{Config, Utility}

import scala.collection.mutable
import scala.collection.mutable.{HashMap, ListBuffer}
import scala.io.Source



/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 02 15:22
 */

@Bean
object DataManager {
  private val userMap:mutable.HashMap[Int, User] = mutable.HashMap()
  private val movieMap:mutable.HashMap[Int, Movie] = mutable.HashMap()
  private val genreReverseIndexMap:mutable.HashMap[String, List[Movie]] = mutable.HashMap()

  def getInstance() = this

  def loadMovieEmb(movieEmbPath: String, embKey: String) = {
    if (Config.EMB_DATA_SOURCE.equals(Config.DATA_SOURCE_FILE)) {
      println(s"Loading movie embedding from $movieEmbPath ...")
      var validEmbCount = 0
      val bufferedSource = Source.fromFile(movieEmbPath)
      bufferedSource.getLines().foreach(line => {
        val rowData = line.split(":").map(_.trim)
        if (rowData.length == 2) {
          getMovieById(rowData(0).toInt) match{
            case Some(movie) => {
              movie.setEmb(Utility.parseEmbStr(rowData(1)))
              validEmbCount += 1
            }
            case None =>
          }
        }
      })
      println(s"Loading movie embedding completed. $validEmbCount movie embeddings in total.")
    }else{
      println("Loading movie embedding from Redis ...")
//      println(s"Loading movie embedding completed. $validEmbCount movie embeddings in total.")
    }
  }

  def loadData(movieDataPath:String, linkDataPath:String, ratingDataPath:String, movieEmbPath:String,
               userEmbPath:String, movieRedisKey:String, userRedisKey:String) = {
    loadMovieData(movieDataPath)
    loadLinkData(linkDataPath)
    loadRatingData(ratingDataPath)
    loadMovieEmb(movieEmbPath, movieRedisKey)
//    if (Config.IS_LOAD_ITEM_FEATURE_FROM_REDIS){
//      loadMovieFeatures("mf:")
//    }

//    loadUserEmb(userEmbPath, userRedisKey);
  }

  def loadLinkData(linkDataPath: String) = {
    println(s"Loading link data from $linkDataPath ...")
    var count = 0
    val bufferedSource = Source.fromFile(linkDataPath)
    bufferedSource.getLines().drop(1).foreach(line => {
      val rowData = line.split(",").map(_.trim)
      if (rowData.length == 3) {
        movieMap.get(rowData(0).toInt) match {
          case Some(movie) => {
            count += 1
            movie.setImdbId(rowData(1).trim)
            movie.setTmdbId(rowData(1).trim)
          }
          case None =>
        }
      }
    })
    bufferedSource.close()
    println(s"Loading link data completed. $count links in total.")
  }

  def loadMovieData(movieDataPath: String) = {
    println(s"Loading movie data from $movieDataPath ...")
    val bufferedSource = Source.fromFile(movieDataPath)
    bufferedSource.getLines().drop(1).foreach(line => {
      val rowData = line.split(",").map(_.trim)
      if (rowData.length == 3) {
        val movie = Movie()
        movie.setMovieId(rowData(0).toInt)
        parseReleaseYear(rowData(1).trim) match {
          case -1 => movie.setTitle(rowData(1).trim)
          case releaseYear => {
            movie.setReleaseYear(releaseYear)
            movie.setTitle(rowData(1).trim.substring(0, rowData(1).trim.length - 6).trim)
          }
        }
        val genres = rowData(2).trim
        if (!genres.isEmpty){
          genres.split("\\|").foreach(genre => {
            movie.addGenre(genre)
            addMovie2GenreIndex(genre, movie)
          })
        }
        movieMap += (movie.getMovieId -> movie)
      }
    })
    bufferedSource.close()
    println("Loading movie data completed. " + movieMap.size + " movies in total.")
  }
  def addMovie2GenreIndex(genre:String, movie:Movie):Unit = {
    genreReverseIndexMap.update(genre, movie::genreReverseIndexMap.getOrElse(genre, List[Movie]()))
  }

  def parseReleaseYear(rawTitle:String):Int = rawTitle match {
    case null => -1
    case _ if rawTitle.trim.length<6 => -1
    case _ => {
      val releaseYear = rawTitle.trim.substring(rawTitle.length-5, rawTitle.length-1)
      try{
        releaseYear.toInt
      }catch {
        case ex:NumberFormatException => -1
      }
    }
  }

  def loadRatingData(ratingDataPath: String) = {
    println("Loading rating data from " + ratingDataPath + " ...")
    var count = 0
    val bufferedSource = Source.fromFile(ratingDataPath)
    bufferedSource.getLines().drop(1).foreach(line => {
      val rowData = line.split(",").map(_.trim)
      if (rowData.length == 4){
        count += 1
        val rating  = Rating()
        rating.setUserId(rowData(0).toInt)
        rating.setMovieId(rowData(1).toInt)
        rating.setScore(rowData(2).toFloat)
        rating.setTimestamp(rowData(3).toLong)
        getMovieById(rating.getMovieId) match {
          case Some(movie) => movie.addRating(rating)
          case None =>
        }
        if (!userMap.contains(rating.getUserId)){
          val user = User()
          user.setUserId(rating.getUserId)
          userMap += (user.getUserId -> user)
        }
        userMap.get(rating.getUserId).get.addRating(rating)
      }
    })
    bufferedSource.close
    println(s"Loading rating data completed. $count ratings in total.")
    // use breeze csvread(new File(ratingDataPath), separator=',', skipLines=1 )
  }

  def getUserById(userId:Int) = userMap.get(userId)

  def getMovieById(movieId:Int) = movieMap.get(movieId)

  def main(args: Array[String]): Unit = {
    val ratingResourcesPath = this.getClass.getResource("/webroot/sampledata/ratings.csv")
    val movieResourcesPath = this.getClass.getResource("/webroot/sampledata/movies.csv")
    val linkResourcesPath = this.getClass.getResource("/webroot/sampledata/links.csv")
    val movieEmbPath = this.getClass.getResource("/webroot/modeldata/item2vecEmb.csv")
//    System.out.printf("Web Root URI: %s%n", webRootUri.getPath)

    //load all the data to DataManager
//    DataManager.getInstance().loadData(webRootUri.getPath() + "sampledata/movies.csv",
//      webRootUri.getPath() + "sampledata/links.csv",webRootUri.getPath() + "sampledata/ratings.csv",
//      webRootUri.getPath() + "modeldata/item2vecEmb.csv",
//      webRootUri.getPath() + "modeldata/userEmb.csv",
//      "i2vEmb", "uEmb");
//    this.getClass.getResource("/webroot/sampledata/ratings.csv")
//    val ratingsResourcesPath = this.getClass.getResource("/webroot/sampledata/ratings.csv")
    DataManager.getInstance().loadRatingData(ratingResourcesPath.getPath)
    DataManager.getInstance().loadMovieData(movieResourcesPath.getPath)
    DataManager.getInstance().loadLinkData(linkResourcesPath.getPath)
    DataManager.getInstance().loadMovieEmb(movieEmbPath.getPath, "i2vEmb")
    println(s"Loading rating data completed  ratings in total.")
  }
}