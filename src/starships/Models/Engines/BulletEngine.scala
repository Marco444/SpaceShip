package src.starships.Models.Engines

import processing.core.PApplet
import src.starships.Models.Engines.CollisionEngine.bulletsToBeDrawn
import src.starships.Models.GameElements.Bullets
import src.starships.Models.Others.accessoryFunctions.Vector2
import src.starships.Views.ImageManager
import src.starships.Models.Others.Game


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

