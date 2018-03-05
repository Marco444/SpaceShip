package starships

import processing.core._
import starships.GameElements._
import starships.Engines.{AsteroidEngine, BulletEngine, CollisionEngine, asteroidsToBeDrawn, bulletsToBeDrawn, targetPos}
import starships.Game.{bulletSpeed, x, y}

object Game {

  //Speeds:
  lazy val asteroidSpeed: Float = 0.25f
  lazy val bulletSpeed: Float = 0.7f
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

class Graphics extends PApplet {
  val imageManager = ImageManager(loadImage)

  override def settings(): Unit = {
    size(Game.x, Game.y)
    Game.startTime = millis()
  }

  override def draw(): Unit = {
    if (isGaming) gamingScreen()
    else initScreen()
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

    GameValues.work(this)
  }

  def initScreen(): Unit = {
    restartGame()
  }

  def isGaming: Boolean = Game.spaceShip.isAlive

  def restartGame(): Unit = {
    Game.spaceShip = Game.spaceShip.update(Vector2(x / 2, y / 2), bulletSpeed, Vector2(0, 0), Vector2(0, 0), 10) //Make spaceShip pop in original position
    asteroidsToBeDrawn = Vector()
    bulletsToBeDrawn = Vector()
    GameValues.findHighestScore()
    Game.startTime = millis() // Restart the time, along with the game
  }

  /** This is to make the bullets draw **/
  override def mouseClicked(): Unit = {
    targetPos = targetPos :+ Vector2(mouseX, mouseY)
    BulletEngine.createNewBullet(Vector2(mouseX, mouseY))
    AsteroidEngine.createAsteroids(1)
  }

  def asteroidsPop(graphics: Graphics): Unit = {
    if (millis() - Game.lastAsteroidTime > 2000) {
      AsteroidEngine.createAsteroids(2) //Though it's better to reduce the time asteroidPops, more acuratte asteroids.
      Game.lastAsteroidTime = millis()
    }
  }


  override def keyPressed(): Unit = keyCode match {
    case Game.LEFT => Game.spaceShip = Game.spaceShip.moveTo(Game.spaceShip.moveLeftBy(Game.keyDistance).pos, this, imageManager)
    case Game.RIGHT => Game.spaceShip = Game.spaceShip.moveTo(Game.spaceShip.moveRightBy(Game.keyDistance).pos, this, imageManager)
    case Game.UP => Game.spaceShip = Game.spaceShip.moveTo(Game.spaceShip.moveUpBy(Game.keyDistance).pos, this, imageManager)
    case Game.DOWN => Game.spaceShip = Game.spaceShip.moveTo(Game.spaceShip.moveDownBy(Game.keyDistance).pos, this, imageManager)
    case _ =>
  }

}