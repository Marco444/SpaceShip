package src.starships.Models.GameElements

import processing.core.PApplet
import src.starships.Views.ImageManager
import src.starships.Models.Others.Game
import src.starships.Models.Others.accessoryFunctions.Vector2
import src.starships.Models.Engines.CollisionEngine.bulletsToBeDrawn

case class Bullets(override val pos: Vector2, override val speed: Float, override val targetPosition: Vector2,override val velocity: Vector2, override val lives: Int)
  extends MovingObject[Bullets](pos, speed, targetPosition, velocity, lives) {

  override lazy val diameter: Float = Game.bulletCircum * Game.scale

  bulletsToBeDrawn = bulletsToBeDrawn :+ this

  def delList(): Unit = bulletsToBeDrawn = bulletsToBeDrawn diff Vector(this)

  override def update(newPos: Vector2, speed: Float, targetPosition: Vector2, velocity: Vector2, lives: Int): Bullets = Bullets(newPos, speed, targetPosition, velocity, lives)


  def draw(graphics: PApplet, imageManager: ImageManager): Unit = {
    graphics.image(imageManager("img/bullets.png"), pos.x - radius, pos.y - radius, diameter, diameter)
  }

  override def explode(graphics: PApplet, imageManager: ImageManager): Unit = {
    graphics.image(imageManager("img/explosion.png"), pos.x - radius, pos.y - radius, explosionDiameter, explosionDiameter)
    this.delList()
  }

}
