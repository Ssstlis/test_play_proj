package controllers

import actions.AuthUtils
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.InjectedController

@Singleton
class MainController @Inject()(
  authUtils: AuthUtils
) extends InjectedController {

  def test = (
    authUtils.authAction andThen
    authUtils.theonlyliesRefiner andThen
    authUtils.theonlyliesFilter
  ) { request =>
    Ok(Json.obj(
      "no_abc" -> false,
      "user" -> request.user,
      "hello" -> "Привет",
      "hello2" -> "Ohaio"
    ))
  }

  def test2 = (
    authUtils.authAction andThen
    authUtils.theonlyliesRefiner
  ) { request =>
    Ok(Json.obj(
      "no_abc" -> true,
      "user" -> request.user,
      "hello" -> "Привет",
      "hello2" -> "Ohaio"
    ))
  }
}