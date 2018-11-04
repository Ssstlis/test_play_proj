package actions

import com.google.inject.{Inject, Singleton}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

@Singleton
class AuthUtils @Inject()(
  defaultParser: BodyParsers.Default
)(implicit ec: ExecutionContext) {

  abstract class AbstractActionBuilder[T[_]] extends ActionBuilder[T, AnyContent] {
    override protected def executionContext = ec
    override def parser = defaultParser
  }

  abstract class AbstractActionRefiner[T[_], R[_]] extends ActionRefiner[T, R] {
    override protected def executionContext = ec
  }

  abstract class AbstractActionFilter[T[_]] extends ActionFilter[T] {
    override protected def executionContext = ec
  }

  val authAction = new AbstractActionBuilder[UserRequest] {
    override def invokeBlock[A](
      request: Request[A],
      block: UserRequest[A] => Future[Result]
    ) = {
      request.headers.get("test-header")
        .map { header =>
          /*DB.findUser(header)*/
          Future { Some(header) }(ec).flatMap { userO =>
            userO.map { user =>
              block(UserRequest(user, request))
            }.getOrElse(Future.successful(Results.GatewayTimeout))
          }.recover { case _ => Results.ImATeapot}
        }
        .getOrElse(Future.successful(Results.BadRequest))
    }
  }

  var theonlyliesRefiner = new AbstractActionRefiner[UserRequest, UserRequest] {
    override protected def refine[A](input: UserRequest[A]) = Future.successful {
      if (input.user == "theonlylies") {
        Right(input)
      } else {
        Left(Results.Forbidden)
      }
    }
  }

  val theonlyliesFilter = new AbstractActionFilter[UserRequest] {
    override protected def filter[A](input: UserRequest[A]) = Future.successful {
      input.headers.get("abc").fold {
        Option(Results.Forbidden)
      } { _ =>
        None
      }
    }
  }
}