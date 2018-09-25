package sk.ygor.scalactica2d

import play.api.ApplicationLoader.Context
import play.api._
import play.api.mvc._
import play.api.routing.Router
import router.Routes

class CustomApplicationLoader extends ApplicationLoader {

  def load(context: Context): Application = {
    val components = new BuiltInComponentsFromContext(context) {
      override def router: Router = new Routes(
        httpErrorHandler,
        new ApplicationController(configuration, executionContext, playBodyParsers)
      )

      override def httpFilters: Seq[EssentialFilter] = Seq.empty
    }

    new DefaultApplication(
      components.environment,
      components.applicationLifecycle,
      components.injector,
      components.configuration,
      components.requestFactory,
      components.httpRequestHandler,
      components.httpErrorHandler,
      components.actorSystem,
      components.materializer,
    )
  }
}