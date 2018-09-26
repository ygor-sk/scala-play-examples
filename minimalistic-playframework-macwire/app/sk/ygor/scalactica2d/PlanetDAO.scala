package sk.ygor.scalactica2d

import java.sql.Connection

import javax.sql.DataSource

import scala.collection.mutable.ListBuffer

class PlanetDAO {

  private var dataSource: DataSource = _

  def fetchAllPlanetNames: Seq[String] = {
    var connection: Connection = null
    try {
      connection = dataSource.getConnection
      val resultSet = connection.prepareStatement("select name from planet").executeQuery()
      new Iterator[String] {
        override def hasNext: Boolean = resultSet.next()
        override def next(): String = resultSet.getString("name")
      }.toSeq
    } finally {
      if (connection != null) {
        connection.close()
      }
    }
  }

  def setDataSource(dataSource: DataSource): Unit = {
    this.dataSource = dataSource
  }

}
