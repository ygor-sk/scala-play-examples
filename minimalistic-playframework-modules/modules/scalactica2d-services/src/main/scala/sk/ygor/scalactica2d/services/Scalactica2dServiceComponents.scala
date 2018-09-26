package sk.ygor.scalactica2d.services

import com.softwaremill.macwire.wire
import sk.ygor.scalactica2d.dao.PlanetDAO

trait Scalactica2dServiceComponents {

  lazy val planetService: PlanetService = wire[PlanetService]

  def planetDAO: PlanetDAO

}
