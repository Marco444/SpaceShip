
package src.starships.Views

import processing.core.{PApplet, PFont}
import src.starships.Models.Others.Game
import src.starships.Models.Others.Game.{startTime, x, y}


case class UiGameValues(graphics: PApplet) {
  var score: Int = 0
  var highestScore: Int = 0
  val timesNewRoman: PFont = graphics.createFont("TimesNewRoman", 16, true)
  graphics.textFont(timesNewRoman, 13)
  graphics.fill(0)

  def showLives(applet: PApplet): Unit = {
    applet.text("Lives: ", 5, x - 16)
    applet.text(Game.spaceShip.lives, 50, x - 16)
  }

  def showScore(applet: PApplet): Unit = {
    applet.text("Time Alive (Score): ", 200, x - 16)
    score = (applet.millis() - startTime) / 100
    applet.text(score, 350, x - 16)
  }

  def showHighestScore(applet: PApplet): Unit = {
    applet.text("Highest Score: ", x - 300, y - 16)
    applet.text(highestScore, x - 200, y - 16)
  }

  def findHighestScore(): Unit = {
    if (highestScore < score) {
      highestScore = score
    }
  }
}