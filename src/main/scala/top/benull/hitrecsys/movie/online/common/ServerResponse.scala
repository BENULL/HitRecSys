package top.benull.hitrecsys.movie.online.common

import com.fasterxml.jackson.annotation.{JsonInclude}

import scala.beans.BeanProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
case class ServerResponse[A](@BeanProperty val status: Int = 0,
                             @BeanProperty val msg: String = null,
                             @BeanProperty val data:A = null) extends Serializable{
}

object ServerResponse{

  def createBySuccess(): ServerResponse[Null] = ServerResponse(ResponseCode.SUCCESS.id)

  def createBySuccessMessage(msg:String): ServerResponse[Null] = ServerResponse(ResponseCode.SUCCESS.id,msg)

  def createBySuccess[A](msg:String,data:A): ServerResponse[A] = ServerResponse(ResponseCode.SUCCESS.id,msg,data)

  def createBySuccess[A](data:A): ServerResponse[A] = ServerResponse(ResponseCode.SUCCESS.id,data = data)

  def createByError(): ServerResponse[Null] = ServerResponse(ResponseCode.ERROR.id,ResponseCode.ERROR.toString)

  def createByErrorMessage(msg:String): ServerResponse[Null] = ServerResponse(ResponseCode.ERROR.id,msg)

  def createByErrorMessage(errorCode:Int,errorMsg:String): ServerResponse[Null] = ServerResponse(errorCode,errorMsg)

}
