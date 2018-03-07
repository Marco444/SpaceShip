package src.starships.Models.Engines

import processing.core.PApplet
import src.starships.Models.Others.Game.spaceShip
import src.starships.Models.GameElements.{Asteroid, Bullets}
import src.starships.Models.Others.accessoryFunctions.{Vector2, dist}
import src.starships.Views.ImageManager

object CollisionEngine {

  /** This is to generate Bullets **/
  var targetPos: Vector[Vector2] = Vector()
  var bulletsToBeDrawn: Vector[Bullets] = Vector()
  /** This is to make the Asteroids Pop **/

  var asteroidsToBeDrawn: Vector[Asteroid] = Vector()

  /** If it is a Set() there should not be bullets repeated ? with the same position ? in other words, colliding bullets ! **/

  def work(graphics: PApplet, imageManager: ImageManager): Unit = {
    proyectillesColliding()
    spaceShipCollisions()
    eliminateCollidingProyectilles(graphics, imageManager)
    eliminateSpaceShip(graphics, imageManager)
  }

  def proyectillesColliding(): Vector[(Asteroid, Bullets)] = {
    for {
      bullets <- bulletsToBeDrawn
      asteroids <- asteroidsToBeDrawn
      if dist(bullets.pos.x, bullets.pos.y, asteroids.pos.x, asteroids.pos.y) < bullets.radius + asteroids.radius
    } yield (asteroids, bullets) //The problem appears to be in the unreliability in checking for difference in ellipses
  }

  def spaceShipCollisions(): Vector[(Boolean, Asteroid)] = asteroidsToBeDrawn.map {
    asteroids =>
      (dist(spaceShip.pos.x, spaceShip.pos.y, asteroids.pos.x, asteroids.pos.y) < spaceShip.radius + asteroids.radius,
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
        spaceShip = spaceShip.collision()
        pair._2.explode(graphics, imageManager)
      }
  }

}