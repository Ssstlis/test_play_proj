package controllers

import actions.AuthUtils
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.InjectedController

@Singleton
class MainController @Inject()(authUtils: AuthUtils) extends InjectedController {

  def test = (
    authUtils.authAction andThen
    authUtils.theonlyliesRefiner andThen
    authUtils.theonlyliesFilter
  )(parse.json) { request =>
    Ok(Json.obj(
      "no_abc" -> false,
      "user" -> request.user,
      "hello" -> "Привет!x",
      "hello2" -> "Ohaio",
      "json_test_fields" -> (request.body \ "test").asOpt[String]
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