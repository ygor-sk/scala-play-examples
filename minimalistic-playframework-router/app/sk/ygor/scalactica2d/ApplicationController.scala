package sk.ygor.scalactica2d

import play.api.Configuration
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class ApplicationController(configuration: Configuration,
                            executionContext: ExecutionContext,
                            playBodyParsers: PlayBodyParsers) {

  def foo(path: String): Action[String] = new play.api.mvc.Action[String] {
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

    override def executionContext: ExecutionContext = ApplicationController.this.executionContext

  }
}
