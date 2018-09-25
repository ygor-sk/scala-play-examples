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

      lazy val router: Router = wire[Routes]

      lazy val applicationController: ApplicationController = wire[ApplicationController]

      override def httpFilters: Seq[EssentialFilter] = Seq.empty
    }

    components.application
  }
}