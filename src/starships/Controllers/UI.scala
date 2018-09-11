package src.starships.Controllers

import src.starships.Models.Engines.{AsteroidEngine, BulletEngine}
import src.starships.Models.Others.Game
import src.starships.Models.Others.accessoryFunctions.Vector2
import src.starships.Models.Engines.CollisionEngine.targetPos
import src.starships.Views.Graphics

class UI extends Graphics {

  override def keyPressed(): Unit = keyCode match {
    case Game.LEFT => Game.spaceShip.moveTo(Game.spaceShip.moveLeftBy(Game.keyDistance).pos, this, imageManager)
    case Game.RIGHT => Game.spaceShip.moveTo(Game.spaceShip.moveRightBy(Game.keyDistance).pos, this, imageManager)
    case Game.UP => Game.spaceShip.moveTo(Game.spaceShip.moveUpBy(Game.keyDistance).pos, this, imageManager)
    case Game.DOWN => Game.spaceShip.moveTo(Game.spaceShip.moveDownBy(Game.keyDistance).pos, this, imageManager)
    case _ =>
  }

  /** This is to make the bullets draw **/
  override def mouseClicked(): Unit = {
    targetPos = targetPos :+ Vector2(mouseX, mouseY)
    BulletEngine.createNewBullet(Vector2(mouseX, mouseY))
    AsteroidEngine.createNewAsteroid()
  }

}
