package src.starships.Models.Engines

import processing.core.PApplet
import src.starships.Models.Engines.CollisionEngine.bulletsToBeDrawn
import src.starships.Models.GameElements.Bullets
import src.starships.Models.Others.accessoryFunctions.Vector2
import src.starships.Views.ImageManager
import src.starships.Models.Others.Game


object BulletEngine {

  def createNewBullet(targetPos: Vector2): Unit = Bullets( Game.spaceShip.pos + Vector2(0, -Game.spaceShip.radius - 1 * Game.scale), Game.bulletSpeed, targetPos, targetPos - newBulletInitialPosition, 1)

  def newBulletInitialPosition: Vector2 = Game.spaceShip.pos + Vector2(0, -Game.spaceShip.radius - 1 * Game.scale)

  def drawBullets(graphics: PApplet, imageManager: ImageManager): Unit = bulletsToBeDrawn.foreach {
    bullet =>
      bullet.moveToTargetPosition(graphics, imageManager)
      bullet.delList()
  }

}

