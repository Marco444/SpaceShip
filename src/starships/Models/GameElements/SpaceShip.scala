package src.starships.Models.GameElements

import processing.core.PApplet
import src.starships.Models.Others.Game.{deltaT, scale, x, y}
import src.starships.Views.ImageManager
import src.starships.Models.Others.Game
import src.starships.Models.Others.accessoryFunctions._

case class SpaceShip(override val pos: Vector2, override val speed: Float,
                     override val targetPosition: Vector2, override val velocity: Vector2, override val lives: Int)
  extends MovingObject[SpaceShip](pos, speed, targetPosition, velocity, lives) {

  override lazy val diameter: Float = 60f * scale

  def moveLeftBy(distance: Float): SpaceShip = SpaceShip(Vector2(pos.x - distance, pos.y), speed, Vector2(0, 0), velocity, lives)

  def moveRightBy(distance: Float): SpaceShip = SpaceShip(Vector2(pos.x + distance, pos.y), speed, Vector2(0, 0), velocity, lives)

  def moveDownBy(distance: Float): SpaceShip = SpaceShip(Vector2(pos.x, pos.y - distance), speed, Vector2(0, 0), velocity, lives)

  def moveUpBy(distance: Float): SpaceShip = SpaceShip(Vector2(pos.x, pos.y + distance), speed, Vector2(0, 0), velocity, lives)

  override def explode(graphics: PApplet, imageManager: ImageManager): Unit = graphics.image(imageManager("img/explosion.png"), pos.x - radius, pos.y - radius, diameter, diameter)

  override def update(newPos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): SpaceShip = SpaceShip(newPos, speed, targetPosition, velocity, lives)

  protected def newSpaceShip(targetPosition: Vector2): Unit = Game.spaceShip = this.updatePosition(targetPosition)

  protected def updatePosition(targetPosition: Vector2): SpaceShip = this.update(this.newPos(spaceShipVelocity(targetPosition)), speed, targetPosition, velocity, this.lives)

  protected def spaceShipVelocity(targetPosition: Vector2): Vector2 = targetPosition - this.pos

  protected def newPos(spaceShipVelocity: Vector2): Vector2 = this.pos + this.spaceShipVelocity(targetPosition) * speed * deltaT

  def isAlive: Boolean = lives > 0

  def collision(): Unit = Game.spaceShip = this.update(this.pos, this.speed, this.targetPosition, this.velocity, this.lives - 1)

  def moveTo(targetPosition: Vector2, graphics: PApplet, imageManager: ImageManager): Unit = if (this.withinScreen()) newSpaceShip(targetPosition)


  def draw(graphics: PApplet, imageManager: ImageManager): Unit =
    if (this.isAlive) graphics.image(imageManager("img/spaceShip.png"), pos.x - radius, pos.y - radius, diameter, diameter)
    else explode(graphics, imageManager)

  def maintainWithinScreen(): Unit = {
    if (pos.x >= x) Game.spaceShip = this.moveLeftBy(radius)
    if (pos.x <= 0) Game.spaceShip = this.moveRightBy(radius)
    if (pos.y >= y) Game.spaceShip = this.moveDownBy(radius)
    if (pos.y <= 0) Game.spaceShip = this.moveUpBy(radius)
  }

}
