package sk.ygor.scalactica2d.dao

class PlanetDAOFake extends PlanetDAO {

  def fetchAllPlanetNames: Seq[String] = Seq(
    "Mercury",
    "Venus",
    "Earth",
    "Mars",
    "Jupiter",
    "Saturn",
    "Uranus",
    "Neptun",
  )

}
