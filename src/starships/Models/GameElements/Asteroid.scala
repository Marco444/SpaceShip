package src.starships.Models.GameElements

import processing.core.PApplet
import src.starships.Models.Others.Game.{asteroidCircum, scale}
import src.starships.Views.ImageManager
import src.starships.Models.Others.accessoryFunctions.Vector2
import src.starships.Models.Engines.CollisionEngine.asteroidsToBeDrawn

case class Asteroid(override val pos: Vector2, override val speed: Float,
                    override val targetPosition: Vector2, override val velocity: Vector2, override val lives: Int)
  extends MovingObject[Asteroid](pos, speed, targetPosition, velocity, lives) {

  override lazy val diameter: Float = asteroidCircum * scale

  asteroidsToBeDrawn = asteroidsToBeDrawn :+ this

  def delList(): Unit = asteroidsToBeDrawn = asteroidsToBeDrawn diff Vector(this)

  override def update(newPos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): Asteroid =  Asteroid(newPos, speed, targetPosition, velocity, lives)

  def draw(graphics: PApplet, imageManager: ImageManager): Unit = graphics.image(imageManager("img/asteroid.png"), pos.x - radius, pos.y - radius, diameter, diameter)

  override def explode(graphics: PApplet, imageManager: ImageManager): Unit = {
    graphics.image(imageManager("img/explosion.png"), pos.x - radius, pos.y - radius, explosionDiameter, explosionDiameter)
    this.delList()
  }
}