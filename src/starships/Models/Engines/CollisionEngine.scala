package src.starships.Models.Engines

import processing.core.PApplet
import src.starships.Models.Others.Game.spaceShip
import src.starships.Models.GameElements.{Asteroid, Bullets}
import src.starships.Models.Others.accessoryFunctions.{Vector2, dist}
import src.starships.Views.ImageManager

object CollisionEngine {

  var targetPos: Vector[Vector2] = Vector()
  var bulletsToBeDrawn: Vector[Bullets] = Vector()
  var asteroidsToBeDrawn: Vector[Asteroid] = Vector()

  def work(graphics: PApplet, imageManager: ImageManager): Unit = {
    eliminateCollidingProyectilles(graphics, imageManager)
    eliminateSpaceShip(graphics, imageManager)
  }

  def proyectillesColliding: Vector[(Asteroid, Bullets)] = {
    for {
      bullets <- bulletsToBeDrawn
      asteroids <- asteroidsToBeDrawn
      if dist(bullets.pos.x, bullets.pos.y, asteroids.pos.x, asteroids.pos.y) < bullets.radius + asteroids.radius
    } yield (asteroids, bullets)
  }

  def spaceShipCollisions: Vector[(Boolean, Asteroid)] = asteroidsToBeDrawn.map {
    asteroids =>
      ( dist(spaceShip.pos.x, spaceShip.pos.y, asteroids.pos.x, asteroids.pos.y) < spaceShip.radius + asteroids.radius,
        asteroids) // Yields true iff the distance between the spaceShip and the asteroid is smaller than the sum of their radii.
  }

  def eliminateCollidingProyectilles(graphics: PApplet, imageManager: ImageManager): Unit =
    proyectillesColliding.foreach(pair => collisionBetween(pair, graphics, imageManager))

  def collisionBetween(tuple: (Asteroid, Bullets), graphics: PApplet, imageManager: ImageManager): Unit = {
    tuple._1.explode(graphics, imageManager)
    tuple._2.explode(graphics, imageManager)
  }

  def eliminateSpaceShip(graphics: PApplet, imageManager: ImageManager): Unit = spaceShipCollisions.foreach{
    pair =>
      if (pair._1) {
        spaceShip.collision()
        pair._2.explode(graphics, imageManager)
      }
  }

}