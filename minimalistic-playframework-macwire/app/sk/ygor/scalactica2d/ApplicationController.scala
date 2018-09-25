package sk.ygor.scalactica2d

import play.api.Configuration
import play.api.mvc._

class ApplicationController(configuration: Configuration,
                            controllerComponents: ControllerComponents)
  extends AbstractController(controllerComponents) {

  def foo(path: String): Action[String] = Action(parse.tolerantText) { request =>
    Ok(
      Seq(
        s"URI: ${request.method} ${request.uri}",
        s"User agent: ${request.headers.get("User-Agent").getOrElse("Unknown")}",
        s"Listening on port: ${configuration.get[String]("play.server.http.port")}",
        s"Received body of size: ${request.body.length}",
      ).mkString("\n")
    )
  }
}
