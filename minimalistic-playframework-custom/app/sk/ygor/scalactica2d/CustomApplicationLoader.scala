package sk.ygor.scalactica2d

import play.api.ApplicationLoader.Context
import play.api._
import play.api.mvc._
import play.api.routing.Router.Routes
import play.api.routing.{Router, SimpleRouter}

import scala.concurrent.{ExecutionContext, Future}

class CustomApplicationLoader extends ApplicationLoader {

  def load(context: Context): Application = {
    val components = new BuiltInComponentsFromContext(context) {
      override def router: Router = new SimpleRouter {
        override def routes: Routes = {
          case _ => new play.api.mvc.Action[String] {
            override def parser: BodyParser[String] = playBodyParsers.tolerantText

            override def apply(request: Request[String]): Future[Result] = Future.successful(
              Results.Ok(
                Seq(
                  s"URI: ${request.method} ${request.uri}",
                  s"User agent: ${request.headers.get("User-Agent").getOrElse("Unknown")}",
                  s"Listening on port: ${configuration.get[String]("play.server.http.port")}",
                  s"Received body of size: ${request.body.length}",
                ).mkString("\n")
              )
            )

            override def executionContext: ExecutionContext = actorSystem.dispatcher
          }
        }
      }

      override def httpFilters: Seq[EssentialFilter] = Seq.empty
    }

    components.application
  }
}