package src.starships.Models.Others

import processing.core.PApplet

object Main {

  def main(args: Array[String]): Unit = {
    PApplet.main("src.starships.Controllers.UI")

    /** It should be loading the PApplet of "src.starships.Views.Graphics", but I cannot
      * arrange it such that the user input is taken into account (the Controllers's code)
      **/
  }
}