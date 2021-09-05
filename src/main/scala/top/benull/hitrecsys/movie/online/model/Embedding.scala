package top.benull.hitrecsys.movie.online.model

import scala.beans.BeanProperty

case class Embedding() {
  @BeanProperty var embVector: Array[Float] = Array()

  def this(embVector: Array[Float]) = {
    this()
    this.embVector = embVector
  }

  def addDim(element: Float): Unit = {
    this.embVector = this.embVector :+ element
  }

  //calculate cosine similarity between two embeddings
  def calculateSimilarity(otherEmb: Embedding): Double = {
    if (null == embVector || null == otherEmb || null == otherEmb.getEmbVector
      || embVector.length != otherEmb.getEmbVector.length)  -1d
    else{
      val dotProduct = this.embVector.zip(otherEmb.getEmbVector).map(d => d._1 * d._2).sum
      val denominator1 = math.sqrt(this.embVector.map(math.pow(_, 2)).sum)
      val denominator2 = math.sqrt(otherEmb.getEmbVector.map(math.pow(_, 2)).sum)
      dotProduct / (denominator1 * denominator2)
    }
  }
}


