package sk.ygor.scalactica2d

import javax.sql.DataSource

import scala.collection.mutable.ListBuffer

class UserDAO {

  private var dataSource: DataSource = _

  def fetchAllUserNames: Seq[String] = {
    val resultSet = dataSource.getConnection.prepareStatement("select name from user").executeQuery()
    val result = ListBuffer[String]()
    while (resultSet.next()) {
      result.append(resultSet.getString("name"))
    }
    result.toList
  }

  def setDataSource(dataSource: DataSource): Unit = {
    this.dataSource = dataSource
  }

}
