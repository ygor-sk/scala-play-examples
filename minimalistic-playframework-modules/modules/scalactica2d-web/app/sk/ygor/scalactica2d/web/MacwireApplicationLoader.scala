package sk.ygor.scalactica2d.web

import javax.sql.DataSource
import org.apache.commons.dbcp.BasicDataSource
import play.api.ApplicationLoader.Context
import play.api.mvc.EssentialFilter
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import sk.ygor.scalactica2d.dao.{PlanetDAO, Scalactica2dDAOComponents}
import sk.ygor.scalactica2d.macros.DebugMacro
import sk.ygor.scalactica2d.services.Scalactica2dServiceComponents

class MacwireApplicationLoader extends ApplicationLoader {

  def load(context: Context): Application = {
    new ApplicationComponents(context).application
  }

  class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context)
    with Scalactica2dWebComponents
    with Scalactica2dServiceComponents
    with Scalactica2dDAOComponents {

    override def httpFilters: Seq[EssentialFilter] = Seq.empty

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

}

