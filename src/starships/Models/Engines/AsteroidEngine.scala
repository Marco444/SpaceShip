package src.starships.Models.Engines

import processing.core.PApplet
import src.starships.Models.Others.Game.spaceShip
import src.starships.Models.GameElements.Asteroid
import src.starships.Models.UI.ImageManager
import src.starships.Models.Others.accessoryFunctions.{Vector2, random}
import src.starships.Models.Engines.CollisionEngine.asteroidsToBeDrawn
import src.starships.Models.Others.Game

/** If it is a Set() there should not be bullets repeated ? with the same position ? in other words, colliding bullets ! **/
object AsteroidEngine {

  def createNewAsteroid(): Unit = {
    lazy val asteroidPos = randomAsteroidPos() //Create a random position for an Asteroid
    lazy val asteroidTargetPos: Vector2 = spaceShip.pos - asteroidPos //Define the target position of the Asteroid.
    Asteroid(asteroidPos, Game.asteroidSpeed, spaceShip.pos, asteroidTargetPos, 1) //Finally create the new Asteroid
  }

  def createAsteroids(asteroidNumber: Int): Unit = {
    if (asteroidNumber > 0) {
      createNewAsteroid()
      createAsteroids(asteroidNumber - 1)
    }
  }

  def drawAsteroids(graphics: PApplet, imageManager: ImageManager): Unit = asteroidsToBeDrawn.foreach {
    asteroid =>
      asteroid.moveToTargetPosition(graphics, imageManager)
      asteroid.delList()
  }

  def randomAsteroidPos(): Vector2 = {
    lazy val rand: Int = random(0, 3)
    if (rand == 0) Vector2(random(0, Game.x), Game.asteroidCircum)

    /** A random position in upper limit **/
    if (rand == 1) Vector2(Game.asteroidCircum, random(0, Game.y))

    /** A random position in left-most limit **/
    if (rand == 2) Vector2(Game.x - Game.asteroidCircum, random(0, Game.y))

    /** A random position in right-most limit **/
    else Vector2(random(0, Game.x), Game.y - Game.asteroidCircum)

    /** A random position in lower limit **/
  }
}