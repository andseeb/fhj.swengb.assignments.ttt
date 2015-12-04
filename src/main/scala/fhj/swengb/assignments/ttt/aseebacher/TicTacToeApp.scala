package fhj.swengb.assignments.ttt.aseebacher

import java.net.URL
import java.util.ResourceBundle
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.{Button, Control}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.BorderPane
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import fhj.swengb.assignments.ttt.aseebacher
import fhj.swengb.assignments.ttt.aseebacher.TMove


import scala.util.control.NonFatal

object TicTacToeApp {
  def main(args: Array[String]) {
    Application.launch(classOf[TicTacToeApp], args: _*)
  }
}

class TicTacToeApp extends javafx.application.Application {


  val Fxml = "/fhj/swengb/assignments/ttt/TicTacToeApp.fxml"
  val Css = "fhj/swengb/assignments/ttt/TicTacToeApp.css"

  val loader = new FXMLLoader(getClass.getResource(Fxml))

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("Tic Tac Toe")
      loader.load[Parent]() // side effect
      val scene = new Scene(loader.getRoot[Parent])
      stage.setScene(scene)
      stage.getScene.getStylesheets.add(Css)
      stage.show()
      stage.setMinWidth(stage.getWidth)
      stage.setMinHeight(stage.getHeight)
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }

}


class TicTacToeController extends Initializable {
  //@FXML var borderPane: BorderPane = _
  var TTTInstance = TicTacToe()

  override def initialize(location: URL, resources: ResourceBundle): Unit = {


    val url: String = "about:blank"
    //val url = Students.mfuchs.gitHubUser.avatarUrl.toString

    //borderPane.setCenter(new ImageView(new Image(url)))
  }

  //val ttt = TicTacToe()

  val movesMap = Map(
    ("btn-topleft", TopLeft),
    ("btn-topcenter", TopCenter),
    ("btn-topright", TopRight),

    ("btn-middleleft", MiddleLeft),
    ("btn-middlecenter", MiddleCenter),
    ("btn-middleright", MiddleRight),

    ("btn-bottomleft", BottomLeft),
    ("btn-bottomcenter", BottomCenter),
    ("btn-bottomright", BottomRight)

  )

  def execMove(evt:ActionEvent): Unit = {
    println(evt.getSource.asInstanceOf[Button].getId + " -> " + movesMap(evt.getSource.asInstanceOf[Button].getId))
    // TODO: create as junit test

    TTTInstance = TTTInstance.turn(movesMap(evt.getSource.asInstanceOf[Button].getId), TTTInstance.nextPlayer)
    evt.getSource.asInstanceOf[Button].setDisable(true)
    evt.getSource.asInstanceOf[Button].getStyleClass.add(TTTInstance.nextPlayer.switch.toString) // nextPlayer.switch = Spieler, der gerade dran war
    println("'" + evt.getSource.asInstanceOf[Button].getStyleClass.toArray.toList.mkString(",") + "'")
    println(TTTInstance.moveHistory)
    println("has won: " + TTTInstance.winner.getOrElse("no"))
    println("game over: " + TTTInstance.gameOver)

  }

}

