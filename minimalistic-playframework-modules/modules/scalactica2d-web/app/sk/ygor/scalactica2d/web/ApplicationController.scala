package sk.ygor.scalactica2d.web

import play.api.Configuration
import play.api.mvc._
import sk.ygor.scalactica2d.macros.DebugMacro
import sk.ygor.scalactica2d.services.PlanetService

class ApplicationController(configuration: Configuration,
                            controllerComponents: ControllerComponents,
                            planetService: PlanetService)
  extends AbstractController(controllerComponents) {

  def foo(path: String): Action[String] = Action(parse.tolerantText) { request =>
    Ok(
      Seq(
        s"URI: ${request.method} ${request.uri}",
        s"User agent: ${request.headers.get("User-Agent").getOrElse("Unknown")}",
        s"Listening on port: ${configuration.get[String]("play.server.http.port")}",
        s"Received body of size: ${request.body.length}",
        s"Planet names: ${planetService.planetNamesQuoted.mkString(", ")}",
        s"Debugging variables: ${DebugMacro.debugParameters(request.host, request.contentType)}"
      ).mkString("\n")
    )
  }
}
