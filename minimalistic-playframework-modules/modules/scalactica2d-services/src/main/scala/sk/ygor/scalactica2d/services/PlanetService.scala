package sk.ygor.scalactica2d.services

import sk.ygor.scalactica2d.dao.PlanetDAO
import util.StringUtils

class PlanetService(planetDAO: PlanetDAO) {

  def planetNamesQuoted: Seq[String] = planetDAO.fetchAllPlanetNames.map(StringUtils.quoteString)

}
