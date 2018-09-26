package sk.ygor.scalactica2d.web

import com.softwaremill.macwire.wire
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc.ControllerComponents
import play.api.routing.Router
import router.Routes
import sk.ygor.scalactica2d.services.PlanetService

trait Scalactica2dWebComponents {

  // https://github.com/adamw/macwire/issues/82
  // https://www.lucidchart.com/techblog/2018/01/19/compile-time-dependency-injection-with-play/
  lazy val router: Router = {
    implicit val prefix: String = "/"
    wire[Routes]
  }

  lazy val applicationController: ApplicationController = wire[ApplicationController]

  def httpErrorHandler: HttpErrorHandler

  def configuration: Configuration

  def controllerComponents: ControllerComponents

  def planetService: PlanetService

}


