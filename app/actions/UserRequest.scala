package actions

import play.api.mvc.Request

case class UserRequest[A](user: String, request: Request[A])
  extends RequestApi[A](request)