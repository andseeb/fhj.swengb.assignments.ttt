package fhj.swengb.assignments.ttt.aseebacher

import java.awt.Desktop
import java.net.{URI, URL}
import java.util.ResourceBundle
import javafx.application.Application
import javafx.event.{EventHandler, ActionEvent}
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.Alert.AlertType
import javafx.scene.control._
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{FlowPane, GridPane, BorderPane}
import javafx.scene.web.WebView
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import fhj.swengb.assignments.ttt.aseebacher
import fhj.swengb.assignments.ttt.aseebacher.TMove


import scala.compat.Platform
import scala.util.Properties
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
  @FXML var BtnTopLeft : Button = _
  @FXML var BtnTopCenter : Button = _
  @FXML var BtnTopRight : Button = _
  @FXML var BtnMiddleLeft : Button = _
  @FXML var BtnMiddleCenter : Button = _
  @FXML var BtnMiddleRight : Button = _
  @FXML var BtnBottomLeft : Button = _
  @FXML var BtnBottomCenter : Button = _
  @FXML var BtnBottomRight : Button = _
  @FXML var difficultyGroup : ToggleGroup = _
  @FXML var ButtonGrid : GridPane = _
  
  
  var tttInstance = TicTacToe()
  //TODO: lazy??
  var possibleGames = TicTacToe.mkGames()

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    val url: String = "about:blank"
    //val url = Students.mfuchs.gitHubUser.avatarUrl.toString
    //borderPane.setCenter(new ImageView(new Image(url)))
  }

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
  // TODO : figure out why this doesn't work
  val buttonMap = Map(
    (TopLeft, BtnTopLeft),
    (TopCenter, BtnTopCenter),
    (TopRight, BtnTopRight),
    (MiddleLeft, BtnMiddleLeft),
    (MiddleCenter, BtnMiddleCenter),
    (MiddleRight, BtnMiddleRight),
    (BottomLeft, BtnBottomLeft),
    (BottomCenter, BtnBottomCenter),
    (BottomRight, BtnBottomRight)
  )
  def convertMoveToButton(move:TMove): Button = {
    // for some reason, a map doesn't work
    if (move == TopLeft) BtnTopLeft
    else if (move == TopCenter) BtnTopCenter
    else if (move == TopRight) BtnTopRight
    else if (move == MiddleLeft) BtnMiddleLeft
    else if (move == MiddleCenter) BtnMiddleCenter
    else if (move == MiddleRight) BtnMiddleRight
    else if (move == BottomLeft) BtnBottomLeft
    else if (move == BottomCenter) BtnBottomCenter
    else if (move == BottomRight) BtnBottomRight
    else {
      assert(false) // TODO: throw exception instead
      BtnTopLeft
    }
  }

  def processClick(evt:ActionEvent): Unit = {
    // player input
    if (!tttInstance.gameOver) {
      tttInstance = execMove(movesMap(evt.getSource.asInstanceOf[Button].getId), tttInstance, evt.getSource.asInstanceOf[Button])
    }
    // AI magic
    if (!tttInstance.gameOver) {
      val nextMove = tttInstance.findBestNextMove(PlayerB, difficultyGroup.getSelectedToggle.getUserData.toString.toInt, possibleGames)
      tttInstance = execMove(nextMove, tttInstance, convertMoveToButton(nextMove))
    }

    if (tttInstance.winner.isDefined) {
      // TODO: einfärben
    }
    if (tttInstance.gameOver) {
      // TODO: add reset button
    }



    println(evt.getSource.asInstanceOf[Button].getId + " -> " + movesMap(evt.getSource.asInstanceOf[Button].getId))
    // TODO: create as junit test
    println("'" + evt.getSource.asInstanceOf[Button].getStyleClass.toArray.toList.mkString(",") + "'")
    println(tttInstance.moveHistory)
    println("has won: " + tttInstance.winner.getOrElse("no"))
    println("game over: " + tttInstance.gameOver)
    println(Properties.lineSeparator)

  }

  def execMove(move: TMove, tttInstance: TicTacToe, button: Button) : TicTacToe = {
    button.setDisable(true)
    button.getStyleClass.add(tttInstance.nextPlayer.toString)
    tttInstance.turn(move, tttInstance.nextPlayer)
  }


  def resetBoard(): Unit = {
    for (button <- ButtonGrid.getChildren.toArray().toList) {
      button.asInstanceOf[Button].setDisable(false)
      button.asInstanceOf[Button].getStyleClass.remove("PlayerA")
      button.asInstanceOf[Button].getStyleClass.remove("PlayerB")
    }
    tttInstance = TicTacToe()
  }

  def closeApp(): Unit = {
    javafx.application.Platform.exit()
  }

  def showAboutWindow(): Unit ={
    val alert : Alert = new Alert(AlertType.INFORMATION)
    alert.setTitle("About TicTacToe")
    alert.setHeaderText("Tic Tac Toe")
    // http://bekwam.blogspot.co.at/2015/07/dont-just-tell-me-show-me-with-custom.html
    var fp : FlowPane  = new FlowPane()
    var lbl : Label = new Label(
      "This Tic Tac Toe application was developed using Scala and JavaFx 8." + Properties.lineSeparator +
      "The project repository containg the source code is located at:"
    )
    val repoUrl = "https://github.com/andseeb/fhj.swengb.assignments.ttt"
    var link : Hyperlink = new Hyperlink(repoUrl)
    fp.getChildren().addAll( lbl, link)

    link.setOnAction(new EventHandler[ActionEvent] {
      def handle(event: ActionEvent) = {
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().browse(new URI(repoUrl))
        }
      }
    })

    alert.getDialogPane().contentProperty().set( fp )
    alert.showAndWait()
  }



}

