package src.starships.Views

import processing.core.PImage

/**
  * The purpose of this class is to store all the images used in during the program,
  * this way it prevents them to be loaded more than once
  */
case class ImageManager(imageLoader: String => PImage) {
  var images: Map[String, PImage] = Map[String, PImage]()

  def apply(path: String): PImage =  get(path)

  def get(path: String): PImage = {
    if (images.contains(path))
      images(path)
    else {
      val image = imageLoader(path)
      images = images + (path -> image)
      image
    }
  }

  def load(path: String): Unit = images = images + (path -> imageLoader(path))
}
