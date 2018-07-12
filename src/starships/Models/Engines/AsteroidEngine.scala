package src.starships.Models.Engines

import processing.core.PApplet
import src.starships.Models.Others.Game.spaceShip
import src.starships.Models.GameElements.Asteroid
import src.starships.Views.ImageManager
import src.starships.Models.Others.accessoryFunctions.{Vector2, random}
import src.starships.Models.Engines.CollisionEngine.asteroidsToBeDrawn
import src.starships.Models.Others.Game

/** If it is a Set() there should not be bullets repeated ? with the same position ? in other words, colliding bullets ! **/
object AsteroidEngine {

  def createNewAsteroid(): Unit = {
    lazy val asteroidPos = randomAsteroidPos() //Create a random position for an Asteroid
    Asteroid(asteroidPos, Game.asteroidSpeed, spaceShip.pos, asteroidTargetPos(asteroidPos), 1) //Finally create the new Asteroid
    //What if one was to define Asteroid as having a position passed curried, that way one would just create a base asteoroid
    //and then assign a position to each instance of it?
  }

  def asteroidTargetPos(asteroidPos: Vector2): Vector2 = spaceShip.pos - asteroidPos

  def drawAsteroids(graphics: PApplet, imageManager: ImageManager): Unit = asteroidsToBeDrawn.foreach {
    asteroid =>
      asteroid.moveToTargetPosition(graphics, imageManager)
      asteroid.delList()
  }

  def randomAsteroidPos(): Vector2 = random(0, 3) match {
    case 0 =>  upperLimitPos
    case 1 =>  leftMostLimitPos
    case 2 => lowerLimitPos
    case _ => rightMostLimitPos
  }

  def upperLimitPos: Vector2 = Vector2(random(0, Game.x), Game.asteroidCircum)
  def leftMostLimitPos: Vector2 = Vector2(Game.asteroidCircum, random(0, Game.y))
  def lowerLimitPos: Vector2 = Vector2(Game.x - Game.asteroidCircum, random(0, Game.y))
  def rightMostLimitPos: Vector2 = Vector2(random(0, Game.x), Game.y - Game.asteroidCircum)

}
