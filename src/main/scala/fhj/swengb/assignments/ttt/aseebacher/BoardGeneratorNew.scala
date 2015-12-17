package fhj.swengb.assignments.ttt.aseebacher

import scala.collection.Set

object BoardGeneratorNew {

  def mkMoveHistory(moves: Seq[TMove]) : Map[TMove, Player] = {
    def recMkMoveHistory(remainingMoves: Seq[TMove], curHistory:Map[TMove, Player], nextPlayer: Player) : Map[TMove, Player] = {
      if (remainingMoves == Nil) curHistory
      else {
        if (nextPlayer == PlayerA) recMkMoveHistory(remainingMoves.tail, curHistory+(remainingMoves.head->PlayerA), PlayerB)
        else recMkMoveHistory(remainingMoves.tail, curHistory+(remainingMoves.head->PlayerB), PlayerA)
      }
    }
    recMkMoveHistory(moves, Map[TMove, Player]().withDefaultValue(PlayerNone), PlayerA)
  }
  def mkNextPlayer(moves: Seq[TMove]) : Player = {
    moves.size match {
      case 0 => PlayerA
      case 1 => PlayerB
      case 2 => PlayerA
      case 3 => PlayerB
      case 4 => PlayerA
      case 5 => PlayerB
      case 6 => PlayerA
      case 7 => PlayerB
      case 8 => PlayerA
      case 9 => PlayerB
    }
  }

  def generateGames(): Map[Seq[TMove], TicTacToe] = {
    //players.permutations.map(playerListMixed => recCalcPermutatedBoards(playerListMixed, cells, Map[TMove, Player]())).toSet
    val movesList = List( TopLeft, TopCenter, TopRight,
                        MiddleLeft, MiddleCenter, MiddleRight,
                        BottomLeft, BottomCenter, BottomRight)

    //5 Moves
    val fiveMoves = movesList.permutations.map(_.take(5)).toSet.map((moves:Seq[TMove]) => (moves -> TicTacToe(mkMoveHistory(moves), mkNextPlayer(moves)))).filter(_._2.gameOver).toMap
    //6 Moves
    val sixMoves = movesList.permutations.map(_.take(6)).toSet.map((moves:Seq[TMove]) => (moves -> TicTacToe(mkMoveHistory(moves), mkNextPlayer(moves)))).filter(_._2.gameOver).toMap
    //7 Moves
    val sevenMoves = movesList.permutations.map(_.take(7)).toSet.map((moves:Seq[TMove]) => (moves -> TicTacToe(mkMoveHistory(moves), mkNextPlayer(moves)))).filter(_._2.gameOver).toMap
    //8 Moves
    val eightMoves = movesList.permutations.map(_.take(8)).toSet.map((moves:Seq[TMove]) => (moves -> TicTacToe(mkMoveHistory(moves), mkNextPlayer(moves)))).filter(_._2.gameOver).toMap
    //9 Moves
    val nineMoves = movesList.permutations.map(_.take(9)).toSet.map((moves:Seq[TMove]) => (moves -> TicTacToe(mkMoveHistory(moves), mkNextPlayer(moves)))).filter(_._2.gameOver).toMap

    val allMoves = fiveMoves ++ sixMoves ++ sevenMoves ++ eightMoves ++ nineMoves
    //println("allmoves.size:")
    //println(allMoves.size)

    allMoves

  }


  def remainingMoves(moveHistory: Seq[TMove]): Set[TMove] = {
    List(TopLeft, TopCenter, TopRight,
      MiddleLeft, MiddleCenter, MiddleRight,
      BottomLeft, BottomCenter, BottomRight
    ).diff(moveHistory.toList).toSet



  }


  def main(args: Array[String]): Unit = {
    generateGames()
  }


}
