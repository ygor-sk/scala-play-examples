package sk.ygor.scalactica2d

import com.softwaremill.macwire.wire
import play.api.ApplicationLoader.Context
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import router.Routes

class MacwireApplicationLoader extends ApplicationLoader {

  def load(context: Context): Application = {
    val components: BuiltInComponentsFromContext = new BuiltInComponentsFromContext(context) {

      // https://github.com/adamw/macwire/issues/82
      // https://www.lucidchart.com/techblog/2018/01/19/compile-time-dependency-injection-with-play/
      lazy val router: Router = {
        implicit val prefix: String = "/"
        wire[Routes]
      }

      lazy val applicationController: ApplicationController = wire[ApplicationController]

      override def httpFilters: Seq[EssentialFilter] = Seq.empty
    }

    components.application
  }
}