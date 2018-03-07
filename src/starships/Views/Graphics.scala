package src.starships.Views

import processing.core.PApplet
import src.starships.Models.Engines.CollisionEngine.{asteroidsToBeDrawn, bulletsToBeDrawn}
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

    Game.spaceShip.work(this, imageManager)
    BulletEngine.drawBullets(this, imageManager)
    AsteroidEngine.drawAsteroids(this, imageManager)
    CollisionEngine.work(this, imageManager)
    asteroidsPop(this)

    UiGameValues.work(this)
  }

  def asteroidsPop(graphics: Graphics): Unit = {
    if (millis() - Game.lastAsteroidTime > 2000) {
      AsteroidEngine.createAsteroids(2) //Though it's better to reduce the time asteroidPops, more acuratte asteroids.
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
    UiGameValues.findHighestScore()
    Game.startTime = millis() // Restart the time, along with the game
  }

  def isGaming: Boolean = Game.spaceShip.isAlive

}
