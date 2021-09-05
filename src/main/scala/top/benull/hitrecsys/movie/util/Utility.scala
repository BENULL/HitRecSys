package top.benull.hitrecsys.movie.util

import top.benull.hitrecsys.movie.online.model.Embedding

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 04 16:06
 */
object Utility {
  def parseEmbStr(embStr:String) = {
    new Embedding(embStr.split("\\s").map(_.toFloat))
  }
}