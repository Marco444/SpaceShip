package src.starships.Models.GameElements

import processing.core.PApplet
import src.starships.Models.Others.Game.{deltaT, x, y}
import src.starships.Views.ImageManager
import src.starships.Models.Others.accessoryFunctions.Vector2

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
    lazy val newPos = this.pos + velocity * speed * deltaT
    this.update(newPos, speed, targetPosition, velocity, lives)
  }

  protected def update(Pos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): T

  def withinScreen(): Boolean = pos < Vector2(x, y) && pos > Vector2(0, 0)

  def explode(graphics: PApplet, imageManager: ImageManager): Unit

}