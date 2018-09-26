package sk.ygor.scalactica2d

import javax.sql.DataSource

class UserDAO {

  private var dataSource: DataSource = _

  def setDataSource(dataSource: DataSource): Unit = {
    this.dataSource = dataSource
  }

}
