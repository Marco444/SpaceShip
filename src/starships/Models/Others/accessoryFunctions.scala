package src.starships.Models.Others

import scala.math.sqrt

object accessoryFunctions {

  def random(lowerBound: Int, upperBound: Int): Int = {
    lazy val rnd = new scala.util.Random
    lowerBound + rnd.nextInt((upperBound - lowerBound) + 1)
  }

  //I've implemented myself as Processing doesn't let me use it.
  def dist(x1: Float, y1: Float, x2: Float, y2: Float): Double = sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))

  case class Vector2(x: Float, y: Float) {

    def *(scalar: Float): Vector2 = Vector2(x * scalar, y * scalar)

    def *(vect: Vector2): Vector2 = Vector2(vect.x * x, vect.y * y)

    def +(vect: Vector2): Vector2 = Vector2(x + vect.x, y + vect.y)

    def -(vect: Vector2): Vector2 = Vector2(x - vect.x, y - vect.y)

    def <(vect: Vector2): Boolean = x < vect.x && y < vect.y

    def >(vect: Vector2): Boolean = x > vect.x && y > vect.y

  }

}
