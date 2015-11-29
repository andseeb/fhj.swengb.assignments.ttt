package fhj.swengb.assignments.ttt.aseebacher


import scala.collection.Set
import util.Properties

/**
  * models the different moves the game allows
  *
  * each move is made by either player a or player b.
  */
sealed trait TMove {
  def idx: Int
  def up: TMove
  def down: TMove
  def left: TMove
  def right: TMove
}

case object TopLeft extends TMove {
  override def idx: Int = 0
  override def up: TMove = BottomLeft
  override def down: TMove = MiddleLeft
  override def left: TMove = ???
  override def right: TMove = ???
}

case object TopCenter extends TMove {
  override def idx: Int = 1
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}

case object TopRight extends TMove {
  override def idx: Int = 2
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}

case object MiddleLeft extends TMove {
  override def idx: Int = 3
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}

case object MiddleCenter extends TMove {
  override def idx: Int = 4
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}

case object MiddleRight extends TMove {
  override def idx: Int = 5
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}

case object BottomLeft extends TMove {
  override def idx: Int = 6
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}

case object BottomCenter extends TMove {
  override def idx: Int = 7
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}

case object BottomRight extends TMove {
  override def idx: Int = 8
  override def up: TMove = ???
  override def down: TMove = ???
  override def left: TMove = ???
  override def right: TMove = ???
}


/**
  * for a tic tac toe game, there are two players, player A and player B
  */
sealed trait Player {
  def switch() : Player
  val sign: String
}

case object PlayerA extends Player {
  def switch() = PlayerB
  val sign = "X"
}

case object PlayerB extends Player {
  def switch() = PlayerA
  val sign = "O"
}

case object PlayerNone extends Player {
  def switch() = PlayerA // TODO: can be used to find out who starts
  val sign = " "
}

object TicTacToe {

  /**
    * creates an empty tic tac toe game
    *
    * @return
    */
  def apply():TicTacToe = {
    def recAddToMap(moves:Seq[TMove], map:Map[TMove, Player]) : Map[TMove, Player] = {
      if (moves == Nil) map
      else recAddToMap(moves.tail, map+(moves.head->PlayerNone))
    }

    val emptyBoard = recAddToMap(Seq(TopLeft, TopCenter, TopRight,
                    MiddleLeft, MiddleCenter, MiddleRight,
                    BottomLeft, BottomCenter, BottomRight),
      Map[TMove, Player]())
    TicTacToe(emptyBoard)

  }
  //TicTacToe(Map[TMove, Player]().withDefaultValue(PlayerNone))

  /**
    * For a given tic tac toe game, this function applies all moves to the game.
    *
    * @param t
    * @param moves
    * @return
    */
  /*def recPlay(t: TicTacToe, moves: Seq[TMove]) : TicTacToe = {
      if (moves == Nil) t
      else recPlay(TicTacToe(
        (t.moveHistory + (moves.head -> t.nextPlayer)), t.nextPlayer.switch()
      ), moves.tail)
    }*/
  def play(t: TicTacToe, moves: Seq[TMove]): TicTacToe = {
    /*def recPlay(t: TicTacToe, moves: Seq[TMove]) : TicTacToe = {
      if (moves == Nil) t
      else recPlay(TicTacToe(
        (t.moveHistory + (moves.head -> t.nextPlayer)), t.nextPlayer.switch()
      ), moves.tail)
    }*/
    def recPlay(t: TicTacToe, moves: Seq[TMove]) : TicTacToe = {
      if (moves == Nil) t
      else recPlay(t.turn(moves.head, t.nextPlayer), moves.tail)
    }
    recPlay(t, moves)
  }

  /**
    * creates all possible games.
    *
    * @return
    */
  lazy val possibleGames: Map[Seq[TMove], TicTacToe] = ???

}

/**
  * Models the well known tic tac toe game.
  *
  * The map holds the information which player controls which field.
  *
  * The nextplayer parameter defines which player makes the next move.
  */
case class TicTacToe(moveHistory: Map[TMove, Player],
                     nextPlayer: Player = PlayerA) {

  /**
    * outputs a representation of the tic tac toe like this:
    *
    * |---|---|---|
    * | x | o | x |
    * |---|---|---|
    * | x | o | x |
    * |---|---|---|
    * | x | o | x |
    * |---|---|---|
    *
    * @return
    */
  def asString(): String = {

    val gridLine = s"|---|---|---|$Properties.lineSeparator"

    ( gridLine +
      s"| ${moveHistory(TopLeft).sign} | ${moveHistory(TopCenter).sign} | ${moveHistory(TopRight).sign} |$Properties.lineSeparator" +
      gridLine +
      s"| ${moveHistory(MiddleLeft).sign} | ${moveHistory(MiddleCenter).sign} | ${moveHistory(MiddleRight).sign} |$Properties.lineSeparator" +
      gridLine +
      s"| ${moveHistory(BottomLeft).sign} | ${moveHistory(BottomCenter).sign} | ${moveHistory(BottomRight).sign} |$Properties.lineSeparator" +
      gridLine)
  }

  /**
    * is true if the game is over.
    *
    * The game is over if either of a player wins or there is a draw.
    */
  val gameOver : Boolean = ???

  /**
    * the moves which are still to be played on this tic tac toe.
    */
  val remainingMoves: Set[TMove] = moveHistory.filter(_._2 == PlayerNone).keySet

  /**
    * given a tic tac toe game, this function returns all
    * games which can be derived by making the next turn. that means one of the
    * possible turns is taken and added to the set.
    */
  lazy val nextGames: Set[TicTacToe] = ???

  /**
    * Either there is no winner, or PlayerA or PlayerB won the game.
    *
    * The set of moves contains all moves which contributed to the result.
    */
  def winner: Option[(Player, Set[TMove])] = {
    if (!gameOver) None
    else {
      for (firstLevelCell <- Seq(TopLeft, TopCenter, TopRight, MiddleLeft, BottomLeft)) {
        for (secondLevel <- )
      }
    }
  }

  /**
    * returns a copy of the current game, but with the move applied to the tic tac toe game.
    *
    * @param move to be played
    * @param player the player
    * @return
    */
  def turn(move: TMove, player: Player): TicTacToe = {
    TicTacToe(moveHistory + (move -> nextPlayer), nextPlayer.switch()) //overwrite value of key
  }

}


