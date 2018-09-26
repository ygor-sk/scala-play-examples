package sk.ygor.scalactica2d
import com.softwaremill.macwire.wire
import org.apache.commons.dbcp.BasicDataSource
import play.api.Configuration

trait DaoComponents {

  lazy val planetDAO: PlanetDAO = {
    val planetDAO = wire[PlanetDAO]
    planetDAO.setDataSource(dataSource)
    planetDAO
  }

  lazy val dataSource: BasicDataSource = {
    val dataSource = new BasicDataSource()
    dataSource.setUrl(configuration.get[String]("jdbc.url"))
    dataSource.setUsername(configuration.get[String]("jdbc.username"))
    dataSource.setPassword(configuration.get[String]("jdbc.password"))
    dataSource
  }

  def configuration: Configuration

}
