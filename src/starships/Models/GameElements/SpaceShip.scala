package src.starships.Models.GameElements

import processing.core.PApplet
import src.starships.Models.Others.Game.{deltaT, scale, x, y}
import src.starships.Models.UI.ImageManager
import src.starships.Models.Others.Game
import src.starships.Models.Others.accessoryFunctions._

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
    lazy val newPos = this.pos + spaceShipVelocity * speed * deltaT
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
