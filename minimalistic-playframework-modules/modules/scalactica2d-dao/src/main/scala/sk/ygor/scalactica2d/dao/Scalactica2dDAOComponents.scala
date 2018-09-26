package sk.ygor.scalactica2d.dao

import com.softwaremill.macwire.wire
import javax.sql.DataSource

trait Scalactica2dDAOComponents {

  lazy val planetDAOImpl: PlanetDAO = {
    val userDAO = wire[PlanetDAOImpl]
    userDAO.setDataSource(dataSource)
    userDAO
  }

  lazy val planetDAOFake: PlanetDAO = wire[PlanetDAOFake]

  def dataSource: DataSource

}
