package fhj.swengb.assignments.ttt.aseebacher


import scala.collection.Set
import scala.util.Properties

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
  override def left: TMove = TopRight
  override def right: TMove = TopCenter
}

case object TopCenter extends TMove {
  override def idx: Int = 1
  override def up: TMove = BottomCenter
  override def down: TMove = MiddleCenter
  override def left: TMove = TopLeft
  override def right: TMove = TopRight
}

case object TopRight extends TMove {
  override def idx: Int = 2
  override def up: TMove = BottomRight
  override def down: TMove = MiddleRight
  override def left: TMove = TopCenter
  override def right: TMove = TopLeft
}

case object MiddleLeft extends TMove {
  override def idx: Int = 3
  override def up: TMove = TopLeft
  override def down: TMove = BottomLeft
  override def left: TMove = MiddleRight
  override def right: TMove = MiddleCenter
}

case object MiddleCenter extends TMove {
  override def idx: Int = 4
  override def up: TMove = TopCenter
  override def down: TMove = BottomCenter
  override def left: TMove = MiddleLeft
  override def right: TMove = MiddleRight
}

case object MiddleRight extends TMove {
  override def idx: Int = 5
  override def up: TMove = TopRight
  override def down: TMove = BottomRight
  override def left: TMove = MiddleCenter
  override def right: TMove = MiddleLeft
}

case object BottomLeft extends TMove {
  override def idx: Int = 6
  override def up: TMove = MiddleLeft
  override def down: TMove = TopLeft
  override def left: TMove = BottomRight
  override def right: TMove = BottomCenter
}

case object BottomCenter extends TMove {
  override def idx: Int = 7
  override def up: TMove = MiddleCenter
  override def down: TMove = TopCenter
  override def left: TMove = BottomLeft
  override def right: TMove = BottomRight
}

case object BottomRight extends TMove {
  override def idx: Int = 8
  override def up: TMove = MiddleRight
  override def down: TMove = TopRight
  override def left: TMove = BottomCenter
  override def right: TMove = BottomLeft
}


/**
  * for a tic tac toe game, there are two players, player A and player B
  */
// TODO: readd sealed keyword
trait Player {
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
    TicTacToe(Map[TMove, Player]().withDefaultValue(PlayerNone))
  }

  /**
    * For a given tic tac toe game, this function applies all moves to the game.
    * The first element of the sequence is also the first move.
    *
    * @param t
    * @param moves
    * @return
    */
  def play(t: TicTacToe, moves: Seq[TMove]): TicTacToe = {
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
  def mkGames(): Map[Seq[TMove], TicTacToe] = BoardGeneratorNew.generateGames()
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

    val gridLine = s"|---|---|---|${Properties.lineSeparator}"

    gridLine +
    s"| ${moveHistory(TopLeft).sign} | ${moveHistory(TopCenter).sign} | ${moveHistory(TopRight).sign} |${Properties.lineSeparator}" +
    gridLine +
    s"| ${moveHistory(MiddleLeft).sign} | ${moveHistory(MiddleCenter).sign} | ${moveHistory(MiddleRight).sign} |${Properties.lineSeparator}" +
    gridLine +
    s"| ${moveHistory(BottomLeft).sign} | ${moveHistory(BottomCenter).sign} | ${moveHistory(BottomRight).sign} |${Properties.lineSeparator}" +
    gridLine
  }
  override def toString = asString()

  /**
    * is true if the game is over.
    *
    * The game is over if either a player wins or there is a draw.
    */
  lazy val gameOver : Boolean = { // is set as "lazy" so that it can access "remainingMoves"
    winner match {
      case None => {
        if (remainingMoves.isEmpty) true // draw
        else false
      }
      case Some(_) => true // a player has won
    }
  }

  /**
    * the moves which are still to be played on this tic tac toe.
    */
  val remainingMoves: Set[TMove] = {
    List(TopLeft, TopCenter, TopRight,
      MiddleLeft, MiddleCenter, MiddleRight,
      BottomLeft, BottomCenter, BottomRight
    ).diff(moveHistory.keySet.toList).toSet



  }

  /**
    * Takes the TicTacToe game and returns all
    * games which can be derived by making the next turn. that means one of the
    * possible turns is taken and added to the set.
    */
  val nextGames: Set[TicTacToe] = {
    remainingMoves.map((move:TMove) => TicTacToe(moveHistory + (move -> nextPlayer), nextPlayer.switch()))
  }

  /**
    * Either there is no winner, or PlayerA or PlayerB won the game.
    *
    * The set of moves contains all moves which contributed to the result.
    */
  def winner: Option[(Player, Set[TMove])] = {
    def containsSameValidPlayer(field1: TMove, field2: TMove, field3: TMove): Option[Player] = {
      if (moveHistory(field1) != PlayerNone && moveHistory(field1).sign == moveHistory(field2).sign && moveHistory(field1).sign == moveHistory(field3).sign) {
        Some(moveHistory(field1))
      }
      else None
    }
    def diagonalMoves(move:TMove) : (TMove, TMove) = {
      move match {
        case TopLeft => (MiddleCenter, BottomRight)
        case TopRight => (MiddleCenter, BottomLeft)
        case BottomLeft => (TopRight, MiddleCenter)
      }
    }
    def recCheckAll(moves : Seq[TMove]) : Option[(Player, Set[TMove])]  = {
      assert(!moves.contains(MiddleCenter) && !moves.contains(MiddleRight) &&
        !moves.contains(BottomCenter) && !moves.contains(BottomRight))
      if (moves == Nil) None // No winner yet OR draw
      else {
        if (containsSameValidPlayer(moves.head.left, moves.head, moves.head.right).isDefined) {
          Some(moveHistory(moves.head), moveHistory.filter(_._2 == moveHistory(moves.head)).keySet)
        } else if (containsSameValidPlayer(moves.head.up, moves.head, moves.head.down).isDefined) {
          Some(moveHistory(moves.head), moveHistory.filter(_._2 == moveHistory(moves.head)).keySet)
        } else if ((moves.head == TopLeft | moves.head == TopRight | moves.head == BottomLeft) && containsSameValidPlayer(moves.head, diagonalMoves(moves.head)._1, diagonalMoves(moves.head)._2).isDefined) {
          Some(moveHistory(moves.head), moveHistory.filter(_._2 == moveHistory(moves.head)).keySet)
        } else {
          recCheckAll(moves.tail)
        }
      }
    }
    if (moveHistory.size <= 4) None
    else {
      val importantMoves = moveHistory.keySet.filter((move:TMove) => Seq(TopLeft, TopCenter, TopRight, MiddleLeft, BottomLeft).contains(move)).toList
      assert(importantMoves.contains(MiddleCenter) == false && importantMoves.contains(MiddleRight) == false &&
        importantMoves.contains(BottomCenter) == false && importantMoves.contains(BottomRight) == false)
      recCheckAll(importantMoves)
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


  /**
    * returns all possible "game Over" games.
    *
    * @param player the player
    * @return
    */
  def findBestNextMove(player: Player, possibleGames: Map[Seq[TMove], TicTacToe], difficulty:Int = -1): TMove = {
    difficulty match {
      case 1 => remainingMoves.head // easy(random) mode
      case 2 => ??? // hard mode
      case _ => { // medium difficulty (slow)
        // Für jeden verfügb. Move                            //wo PlayerB der Gewinner ist  // und wo schon gleiche Moves sind
        val bla = remainingMoves.map(move => (move -> possibleGames.filter(_._2.winner.isDefined).filter(_._2.winner.get._1 == PlayerB).filter(b => areAllMovesOfB1InB2(this, b._2))) ) //.filter(_._2.isEmpty == false)
        //println(bla)
        if (bla.filter(_._2.isEmpty == false).isEmpty) {
          // can't win
          //TODO: draw
          remainingMoves.head
        } else {
          bla.filter(_._2.isEmpty == false).head._1
        }


      }
    }

  }


  def areAllMovesOfB1InB2(board1: TicTacToe, board2: TicTacToe) : Boolean = {
    (board1.moveHistory.keySet diff board2.moveHistory.keySet).isEmpty
  }


}
