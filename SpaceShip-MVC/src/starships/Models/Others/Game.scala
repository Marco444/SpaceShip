package src.starships.Models.Others

import src.starships.Models.GameElements.SpaceShip
import src.starships.Models.Others.accessoryFunctions.Vector2

object Game {

  //Speeds:
  lazy val asteroidSpeed: Float = 0.25f
  lazy val bulletSpeed: Float = 1
  lazy val bulletAcceleration: Float = 0.5f

  //Circumferences of Elements:
  lazy val asteroidCircum: Float = 20f
  lazy val bulletCircum: Float = 15f
  lazy val spaceShipCircum: Float = 60f

  //Time Parameters /////////////////////////////////////////////////////////////////////////Evil.
  var deltaT: Float = 1
  var startTime: Int = 0
  var lastAsteroidTime: Int = 0

  //Grid Dimensions
  lazy val scale: Int = 2 //This is the scale of the screen
  lazy val x: Int = 400 * scale
  lazy val y: Int = 400 * scale
  lazy val keyDistance: Int = 350

  //Game Elements
  var spaceShip: SpaceShip = SpaceShip(Vector2(x / 2, y / 2), bulletSpeed, Vector2(0, 0), Vector2(0, 0), 10) /////////////////////////////////////////////////////////////////////////Evil.

  //KeyCode that processing does not allow me to write.
  lazy val LEFT: Int = 37
  lazy val UP: Int = 40
  lazy val RIGHT: Int = 39
  lazy val DOWN: Int = 38

}
