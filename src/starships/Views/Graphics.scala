package src.starships.Views

import processing.core.PApplet
import src.starships.Models.Engines.CollisionEngine.{asteroidsToBeDrawn, bulletsToBeDrawn, eliminateCollidingProyectilles, eliminateSpaceShip}
import src.starships.Models.Engines.{AsteroidEngine, BulletEngine, CollisionEngine}
import src.starships.Models.Others.Game
import src.starships.Models.Others.accessoryFunctions.Vector2

class Graphics extends PApplet {
  val imageManager = ImageManager(loadImage)

  override def settings(): Unit = {
    size(Game.x, Game.y)
    Game.startTime = millis()
  }

  override def draw(): Unit = {
    if (isGaming) gamingScreen()
    else restartGame()
  }

  def gamingScreen(): Unit = {
    clear()
    background(230)
    Game.deltaT = 1 / frameRate

    ///SpaceShip.
    Game.spaceShip.maintainWithinScreen()
    Game.spaceShip.draw(this, imageManager)

    //Make asteroid and bullets show
    BulletEngine.drawBullets(this, imageManager)
    AsteroidEngine.drawAsteroids(this, imageManager)
    asteroidsPop(this)

    //Make colliding objects collide.
    CollisionEngine.eliminateCollidingProyectilles(this, imageManager)
    CollisionEngine.eliminateSpaceShip(this, imageManager)

    ///Show stuff for the game.
    UiGameValues(this).showHighestScore(this)
    UiGameValues(this).showLives(this)
    UiGameValues(this).showScore(this)
  }

  def asteroidsPop(graphics: Graphics): Unit = {
    if (millis() - Game.lastAsteroidTime > 2000) {
      AsteroidEngine.createNewAsteroid()
      Game.lastAsteroidTime = millis()
    }
  }

  def initScreen(): Unit = {
    restartGame()
  }

  def restartGame(): Unit = {
    Game.spaceShip = Game.spaceShip.update(Vector2(Game.x / 2, Game.y / 2), Game.bulletSpeed, Vector2(0, 0), Vector2(0, 0), 10) //Make spaceShip pop in original position
    asteroidsToBeDrawn = Vector()
    bulletsToBeDrawn = Vector()
    UiGameValues(this).findHighestScore()
    Game.startTime = millis() // Restart the time, along with the game
  }

  def isGaming: Boolean = Game.spaceShip.isAlive

}
