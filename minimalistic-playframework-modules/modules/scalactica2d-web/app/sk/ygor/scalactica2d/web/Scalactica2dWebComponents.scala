package sk.ygor.scalactica2d.web

import com.softwaremill.macwire.wire
import javax.sql.DataSource
import org.apache.commons.dbcp.BasicDataSource
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import router.Routes
import sk.ygor.scalactica2d.dao.{PlanetDAO, Scalactica2dDAOComponents}
import sk.ygor.scalactica2d.macros.DebugMacro
import sk.ygor.scalactica2d.services.Scalactica2dServiceComponents

class Scalactica2dWebComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with Scalactica2dServiceComponents
  with Scalactica2dDAOComponents {

  // https://github.com/adamw/macwire/issues/82
  // https://www.lucidchart.com/techblog/2018/01/19/compile-time-dependency-injection-with-play/
  lazy val router: Router = {
    implicit val prefix: String = "/"
    wire[Routes]
  }

  override def httpFilters: Seq[EssentialFilter] = Seq.empty

  lazy val applicationController: ApplicationController = wire[ApplicationController]

  private val isDatabaseEnabled = configuration.getOptional[Boolean]("database.enabled").getOrElse(false)
  println(DebugMacro.debugParameters(isDatabaseEnabled))

  override def planetDAO: PlanetDAO = if (isDatabaseEnabled) planetDAOImpl else planetDAOFake

  lazy val dataSource: DataSource = {
    val dataSource = new BasicDataSource()
    dataSource.setUrl(configuration.get[String]("jdbc.url"))
    dataSource.setUsername(configuration.get[String]("jdbc.username"))
    dataSource.setPassword(configuration.get[String]("jdbc.password"))
    dataSource
  }


}


