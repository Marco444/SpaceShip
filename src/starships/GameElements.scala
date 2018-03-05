package starships

import processing.core.{PApplet, PFont}
import Game.{scale, spaceShip, startTime, x, y}
import starships.Engines.{asteroidsToBeDrawn, bulletsToBeDrawn}
import starships.GameElements.{Asteroid, Bullets, Vector2, random}

import math.sqrt

/** *************************************************************************************************************************************
  * The fact that I must use variables to model state can be "solved" through functional reactive programming. Using Signals, it would
  * be a chance to make some beautiful code and to learn more about this largely un-used technology.
  *
  * The only problem I've encountered with signals and alike is that they are rarely used in actual proyects, I am not saying it's
  * knwoledge you could not apply in a proyect in some way, but it does not seem viable to employ in a large-scale proyect.
  * ************************************************************************************************************************************/


/**
  * When picking up the project to review Scala and improve it, get a refresher by looking into the withinScreen() method of SpaceShips
  * and trying to solve it; when I feel confident, look at parts of the code and ask myself "How can I do it better?". A good starting
  * point can be looking for 'Bajo Acoplamiento, Alta Coesion.'
  *
  * Look into implementing the default Asteroid/Bullet addition to their corresponding lists when they are constructed. Can the same be
  * done with the line that takes them out of the list, ideally this would be done through a destructor, but Scala doesn't support them
  * (the JVM)
  **/
object GameElements {

  case class Vector2(x: Float, y: Float) {

    def *(scalar: Float): Vector2 = Vector2(x * scalar, y * scalar)

    def *(vect: Vector2): Vector2 = Vector2(vect.x * x, vect.y * y)

    def +(vect: Vector2): Vector2 = Vector2(x + vect.x, y + vect.y)

    def -(vect: Vector2): Vector2 = Vector2(x - vect.x, y - vect.y)

    def <(vect: Vector2): Boolean = x < vect.x && y < vect.y

    def >(vect: Vector2): Boolean = x > vect.x && y > vect.y

  }

  def random(lowerBound: Int, upperBound: Int): Int = {
    lazy val rnd = new scala.util.Random
    lowerBound + rnd.nextInt((upperBound - lowerBound) + 1)
  }

  //I've implemented myself as Processing doesn't let me use it.
  def dist(x1: Float, y1: Float, x2: Float, y2: Float): Double = sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))

  abstract class MovingObject[T <: MovingObject[T]](val pos: Vector2, val speed: Float, val targetPosition: Vector2, val velocity: Vector2, val lives: Int) {

    val diameter: Float
    val radius: Float = diameter / 2
    val explosionDiameter: Float = diameter * 2

    protected def draw(graphics: PApplet, imageManager: ImageManager): Unit

    def moveToTargetPosition(graphics: PApplet, imageManager: ImageManager): T = {
      lazy val newElement: T = this.updatePosition()
      if (this.withinScreen()) newElement.draw(graphics, imageManager)
      newElement
    }

    def updatePosition(): T = {
      lazy val newPos = this.pos + velocity * speed * Game.deltaT
      this.update(newPos, speed, targetPosition, velocity, lives)
    }

    protected def update(Pos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): T

    def withinScreen(): Boolean = pos < Vector2(x, y) && pos > Vector2(0, 0)

    def explode(graphics: PApplet, imageManager: ImageManager): Unit

  }


  case class SpaceShip(override val pos: Vector2, override val speed: Float,
                       override val targetPosition: Vector2, override val velocity: Vector2, override val lives: Int)
    extends MovingObject[SpaceShip](pos, speed, targetPosition, velocity, lives) {

    override lazy val diameter: Float = 60f * scale

    def moveLeftBy(distance: Int): SpaceShip =
      SpaceShip(Vector2(this.pos.x - distance, this.pos.y), this.speed, Vector2(0, 0), this.velocity, this.lives)

    def moveRightBy(distance: Int): SpaceShip =
      SpaceShip(Vector2(this.pos.x + distance, this.pos.y), this.speed, Vector2(0, 0), this.velocity, this.lives)

    def moveDownBy(distance: Int): SpaceShip =
      SpaceShip(Vector2(this.pos.x, this.pos.y - distance), this.speed, Vector2(0, 0), this.velocity, this.lives)

    def moveUpBy(distance: Int): SpaceShip =
      SpaceShip(Vector2(this.pos.x, this.pos.y + distance), this.speed, Vector2(0, 0), this.velocity, this.lives)

    def draw(graphics: PApplet, imageManager: ImageManager): Unit =
      if (this.isAlive) graphics.image(imageManager("img/spaceShip.png"), pos.x - radius, pos.y - radius, diameter, diameter)
      else explode(graphics, imageManager)

    override def explode(graphics: PApplet, imageManager: ImageManager): Unit = {
      graphics.image(imageManager("img/explosion.png"), pos.x - radius, pos.y - radius, diameter, diameter)

    }

    override def update(newPos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): SpaceShip = {
      SpaceShip(newPos, speed, targetPosition, velocity, lives)
    }

    def maintainWithinScreen(): SpaceShip = { //Can be done with pattern matching, more scalastic.
      if (pos.x >= x) this.update(Vector2(x - radius, pos.y), speed, this.targetPosition, this.velocity, this.lives)
      if (pos.x <= 0) this.update(Vector2(x + radius, pos.y), speed, this.targetPosition, this.velocity, this.lives)
      if (pos.y >= y) this.update(Vector2(pos.x, y - radius), speed, this.targetPosition, this.velocity, this.lives)
      if (pos.y <= 0) this.update(Vector2(pos.x, y + radius), speed, this.targetPosition, this.velocity, this.lives)
      else this
    }

    def moveTo(targetPosition: Vector2, graphics: PApplet, imageManager: ImageManager): SpaceShip = {
      /** It is nearly the same implementation as moveToTargetPosition() in MovingObject[T]  **/
      lazy val newSpaceShip: SpaceShip = this.updatePosition(targetPosition)
      if (this.withinScreen()) newSpaceShip.draw(graphics, imageManager)
      newSpaceShip
    }

    def updatePosition(targetPosition: Vector2): SpaceShip = {
      lazy val spaceShipVelocity = targetPosition - this.pos
      lazy val newPos = this.pos + spaceShipVelocity * speed * Game.deltaT
      this.update(newPos, speed, targetPosition, velocity, this.lives)
    }

    def isAlive: Boolean = lives > 0

    def collision(): SpaceShip = {
      this.update(this.pos, this.speed, this.targetPosition, this.velocity, this.lives - 1)
    }

    def work(graphics: PApplet, imageManager: ImageManager): Unit = {
      Game.spaceShip = Game.spaceShip.maintainWithinScreen() //It doesnot work.
      Game.spaceShip.draw(graphics, imageManager)
    }

  }

  case class Asteroid(override val pos: Vector2, override val speed: Float,
                      override val targetPosition: Vector2, override val velocity: Vector2, override val lives: Int)
    extends MovingObject[Asteroid](pos, speed, targetPosition, velocity, lives) {


    override lazy val diameter: Float = Game.asteroidCircum * scale

    asteroidsToBeDrawn = asteroidsToBeDrawn :+ this

    def delList(): Unit = asteroidsToBeDrawn = asteroidsToBeDrawn diff Vector(this)

    override def update(newPos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): Asteroid = {
      Asteroid(newPos, speed, targetPosition, velocity, lives)
    }

    def draw(graphics: PApplet, imageManager: ImageManager): Unit = {
      graphics.image(imageManager("img/asteroid.png"), pos.x - radius, pos.y - radius, diameter, diameter)
    }

    override def explode(graphics: PApplet, imageManager: ImageManager): Unit = {
      graphics.image(imageManager("img/explosion.png"), pos.x - radius, pos.y - radius, explosionDiameter, explosionDiameter)
      asteroidsToBeDrawn = asteroidsToBeDrawn diff Vector(this)
    }
  }

  case class Bullets(override val pos: Vector2, override val speed: Float, override val targetPosition: Vector2
                     , override val velocity: Vector2, override val lives: Int)
    extends MovingObject[Bullets](pos, speed, targetPosition, velocity, lives) {

    override lazy val diameter: Float = Game.bulletCircum * scale

    //bulletsToBeDrawn = bulletsToBeDrawn :+ this

    def delList(): Unit = bulletsToBeDrawn = bulletsToBeDrawn diff Vector(this)

    override def update(newPos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): Bullets = {
      Bullets(newPos, speed, targetPosition, velocity, lives)
    }

    def draw(graphics: PApplet, imageManager: ImageManager): Unit = {
      graphics.image(imageManager("img/bullets.png"), pos.x - radius, pos.y - radius, diameter, diameter)
    }

    override def explode(graphics: PApplet, imageManager: ImageManager): Unit = {
      graphics.image(imageManager("img/explosion.png"), pos.x - radius, pos.y - radius, explosionDiameter, explosionDiameter)
      bulletsToBeDrawn = bulletsToBeDrawn diff Vector(this)
    }

  }

}

object Engines { //I cannot make it work in a separate file.

  /** This is to generate Bullets **/
  var targetPos: Vector[Vector2] = Vector()
  var bulletsToBeDrawn: Vector[Bullets] = Vector()

  /** If it is a Set() there should not be bullets repeated ? with the same position ? in other words, colliding bullets ! **/

  object BulletEngine {

    def createNewBullet(targetPos: Vector2): Unit = {
      lazy val bulletInitialPosition: Vector2 = Game.spaceShip.pos + Vector2(0, -Game.spaceShip.radius - 1 * Game.scale)
      lazy val bullet = Bullets(bulletInitialPosition, Game.bulletSpeed, targetPos, targetPos - bulletInitialPosition, 1)
      bulletsToBeDrawn = bulletsToBeDrawn :+ bullet
    }

    def drawBullets(graphics: PApplet, imageManager: ImageManager): Unit = bulletsToBeDrawn.foreach {
      bullet =>
        lazy val newBullet: Bullets = bullet.moveToTargetPosition(graphics, imageManager) //Move the bullet one step closer to its target position.
        bulletsToBeDrawn = bulletsToBeDrawn :+ newBullet
        bullet.delList() //Take old bullet from list of bulletsToBeDrawn.
    }

  }

  /** This is to make the Asteroids Pop **/

  var asteroidsToBeDrawn: Vector[Asteroid] = Vector()

  /** If it is a Set() there should not be bullets repeated ? with the same position ? in other words, colliding bullets ! **/
  object AsteroidEngine {

    def createNewAsteroid(): Unit = {
      lazy val asteroidPos = randomAsteroidPos() //Create a random position for an Asteroid
      lazy val asteroidTargetPos: Vector2 = Game.spaceShip.pos - asteroidPos //Define the target position of the Asteroid.
      Asteroid(asteroidPos, Game.asteroidSpeed, Game.spaceShip.pos, asteroidTargetPos, 1) //Finally create the new Asteroid
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
      if (rand == 0) Vector2(GameElements.random(0, Game.x), Game.asteroidCircum)

      /** A random position in upper limit **/
      if (rand == 1) Vector2(Game.asteroidCircum, GameElements.random(0, Game.y))

      /** A random position in left-most limit **/
      if (rand == 2) Vector2(x - Game.asteroidCircum, GameElements.random(0, Game.y))

      /** A random position in right-most limit **/
      else Vector2(GameElements.random(0, Game.x), y - Game.asteroidCircum)

      /** A random position in lower limit **/
    }
  }

  /**
    * NOTE: The generators can be merged into one, the functionality is largely the same. Nonetheless, I do not know how much added benefit there is
    * for the reduced clarity, especially if I want to try out new things within this project
    **/


  /** This is to find out what elements are colliding with each other **/

  //http://happycoding.io/tutorials/processing/collision-detection
  object CollisionEngine {

    def proyectillesColliding(): Vector[(Asteroid, Bullets)] = {
      for {
        bullets <- bulletsToBeDrawn
        asteroids <- asteroidsToBeDrawn
        if GameElements.dist(bullets.pos.x, bullets.pos.y, asteroids.pos.x, asteroids.pos.y) < bullets.radius + asteroids.radius
      } yield (asteroids, bullets) //The problem appears to be in the unreliability in checking for difference in ellipses
    }

    def spaceShipCollisions(): Vector[(Boolean, Asteroid)] = asteroidsToBeDrawn.map {
      asteroids =>
        (GameElements.dist(spaceShip.pos.x, spaceShip.pos.y, asteroids.pos.x, asteroids.pos.y) < spaceShip.radius + asteroids.radius,
          asteroids)
    }

    def eliminateCollidingProyectilles(graphics: PApplet, imageManager: ImageManager): Unit = proyectillesColliding() match {
      case Vector() =>
      case xs => xs.foreach {
        pair =>
          pair._1.explode(graphics, imageManager)
          pair._2.explode(graphics, imageManager)
      }
    }

    def eliminateSpaceShip(graphics: PApplet, imageManager: ImageManager): Unit = spaceShipCollisions().foreach {
      pair =>
        if (pair._1) {
          Game.spaceShip = spaceShip.collision()
          pair._2.explode(graphics, imageManager)
        }
    }

    def work(graphics: PApplet, imageManager: ImageManager): Unit = {
      proyectillesColliding()
      spaceShipCollisions()
      eliminateCollidingProyectilles(graphics, imageManager)
      eliminateSpaceShip(graphics, imageManager)
    }

  }


}


object GameValues {
  var score: Int = 0
  var highestScore: Int = 0

  def showLives(applet: PApplet): Unit = {
    applet.text("Lives: ", 5, x - 16)
    applet.text(Game.spaceShip.lives, 50, x - 16)
  }

  def showScore(applet: PApplet): Unit = {
    applet.text("Time Alive (Score): ", 200, x - 16)
    score = (applet.millis() - startTime) / 100
    applet.text(score, 350, x - 16)
  }

  def showHighestScore(applet: PApplet): Unit = {
    applet.text("Highest Score: ", x - 300, y - 16)
    applet.text(highestScore, x - 200, y - 16)
  }

  def findHighestScore(): Unit = {
    if (highestScore > score) {
      highestScore = highestScore
    }
    else {
      highestScore = score
    }
  }

  def work(graphics: PApplet): Unit = {
    val timesNewRoman: PFont = graphics.createFont("TimesNewRoman", 16, true)
    graphics.textFont(timesNewRoman, 13)
    graphics.fill(0)

    showScore(graphics)
    showLives(graphics)
    showHighestScore(graphics)
  }
}