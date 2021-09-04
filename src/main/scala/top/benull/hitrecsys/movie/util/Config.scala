package top.benull.hitrecsys.movie.util

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 02 18:39
 */
object Config {
  val DATA_SOURCE_REDIS = "redis"
  val DATA_SOURCE_FILE = "file"
  val EMB_DATA_SOURCE = Config.DATA_SOURCE_FILE
  val IS_LOAD_USER_FEATURE_FROM_REDIS = false
  val IS_LOAD_ITEM_FEATURE_FROM_REDIS = false
  val IS_ENABLE_AB_TEST = false
}