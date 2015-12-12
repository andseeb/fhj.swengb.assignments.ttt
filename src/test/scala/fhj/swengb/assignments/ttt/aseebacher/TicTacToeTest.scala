package fhj.swengb.assignments.ttt.aseebacher

import org.junit.Assert._
import org.junit.Test

import scala.util.Properties

/**
  * Tests the tic tac toe game engine.
  */
class TicTacToeTest {

  /**
    * contains all possible games as keys and the according game.
    */
  lazy val allGames: Map[Seq[TMove], TicTacToe] = TicTacToe.mkGames()

  @Test def setOnEmptyRemaingMoves(): Unit = {
    val t = TicTacToe().turn(TopCenter, PlayerA)
    assertEquals(8, t.remainingMoves.size)
  }

  @Test def testGameOver(): Unit = {
    assertFalse(TicTacToe().gameOver)
  }

  @Test def testGameOver2(): Unit = {
    val gameOver = TicTacToe(
      Map(
        TopLeft -> PlayerB,
        TopCenter -> PlayerA,
        TopRight -> PlayerB,
        MiddleLeft -> PlayerA,
        MiddleCenter -> PlayerB,
        MiddleRight -> PlayerA,
        BottomLeft -> PlayerB,
        BottomCenter -> PlayerA,
        BottomRight -> PlayerB
      )
    ).gameOver
    assertTrue(gameOver)
  }

  // implement yourself more tests



  @Test def playTest1(): Unit = {
    val t = TicTacToe.play(TicTacToe(), Seq(TopLeft, BottomCenter))
    assert(t.moveHistory.get(TopLeft).contains(PlayerA))
    assert(t.moveHistory.get(BottomCenter).contains(PlayerB))
  }

  @Test def playTest2(): Unit = {
    val t = TicTacToe.play(TicTacToe(), Seq(BottomCenter, TopLeft, BottomRight, MiddleCenter, TopCenter))
    assert(t.moveHistory.get(BottomCenter).contains(PlayerA))
    assert(t.moveHistory.get(TopLeft).contains(PlayerB))
    assert(t.moveHistory.get(BottomRight).contains(PlayerA))
    assert(t.moveHistory.get(MiddleCenter).contains(PlayerB))
    assert(t.moveHistory.get(TopCenter).contains(PlayerA))
  }


  @Test def mkGamesTest(): Unit = {
    // numbers source: https://de.wikipedia.org/wiki/Tic-Tac-Toe#Strategie_und_Taktik
    val possibleGames = TicTacToe.mkGames()
    assertSame(possibleGames.values.size, 31896)
    assertSame(possibleGames.values.count(ttt => ttt.winner.isDefined && ttt.winner.get._1 == PlayerA), 16398)
    assertSame(possibleGames.values.count(ttt => ttt.winner.isDefined && ttt.winner.get._1 == PlayerB), 9738)
    assertSame(possibleGames.values.count(_.winner.isEmpty), 5760)
  }


  @Test def asStringTest(): Unit = {
    val shouldBe : String =
      s"|---|---|---|${Properties.lineSeparator}" +
      s"| O | X |   |${Properties.lineSeparator}" +
      s"|---|---|---|${Properties.lineSeparator}" +
      s"|   | O |   |${Properties.lineSeparator}" +
      s"|---|---|---|${Properties.lineSeparator}" +
      s"|   | X | X |${Properties.lineSeparator}" +
      s"|---|---|---|${Properties.lineSeparator}"
    val tString = TicTacToe.play(TicTacToe(), Seq(BottomCenter, TopLeft, BottomRight, MiddleCenter, TopCenter)).asString()
    assertTrue(shouldBe == tString)
  }

  @Test def toStringTest(): Unit = {
    val as = TicTacToe.play(TicTacToe(), Seq(BottomCenter, TopLeft, BottomRight, MiddleCenter, TopCenter)).asString()
    val to = TicTacToe.play(TicTacToe(), Seq(BottomCenter, TopLeft, BottomRight, MiddleCenter, TopCenter)).toString()
    assert(as == to)
  }


  @Test def nextGamesTest(): Unit = {
    val t = TicTacToe.play(TicTacToe(), Seq(BottomCenter, TopLeft, BottomRight, MiddleCenter, TopCenter))
    assert(t.nextGames.size == 4)
    assert(t.nextGames.map(_.moveHistory) == Set(
      Map(BottomRight -> PlayerA, TopLeft -> PlayerB, TopRight -> PlayerB, TopCenter -> PlayerA, BottomCenter -> PlayerA, MiddleCenter -> PlayerB),
      Map(MiddleRight -> PlayerB, BottomRight -> PlayerA, TopCenter -> PlayerA, BottomCenter -> PlayerA, TopLeft -> PlayerB, MiddleCenter -> PlayerB),
      Map(MiddleLeft -> PlayerB, BottomRight -> PlayerA, TopCenter -> PlayerA, BottomCenter -> PlayerA, TopLeft -> PlayerB, MiddleCenter -> PlayerB),
      Map(BottomLeft -> PlayerB, BottomRight -> PlayerA, TopCenter -> PlayerA, BottomCenter -> PlayerA, TopLeft -> PlayerB, MiddleCenter -> PlayerB)
    ))
  }



  @Test def turnTest(): Unit = {
    val t = TicTacToe().turn(TopLeft, PlayerA).turn(BottomCenter, PlayerB)
    assert(t.moveHistory.get(TopLeft).contains(PlayerA))
    // TODO: why are there values like Some(PlayerNone)
    assert(t.moveHistory.get(BottomCenter).contains(PlayerB))
  }



  @Test def areAllMovesOfB1InB2Test_1(): Unit = {
    val board1 = TicTacToe.play(TicTacToe(), Seq(TopLeft, BottomCenter))
    val board2 = TicTacToe.play(TicTacToe(), Seq(TopLeft, BottomCenter, BottomRight))
    assertTrue(TicTacToe().areAllMovesOfB1InB2(board1, board2))
  }

  @Test def areAllMovesOfB1InB2Test_2(): Unit = {
    val board1 = TicTacToe.play(TicTacToe(), Seq(TopLeft, BottomLeft))
    val board2 = TicTacToe.play(TicTacToe(), Seq(TopLeft, BottomCenter, BottomRight))
    assertFalse(TicTacToe().areAllMovesOfB1InB2(board1, board2))
  }

  @Test def areAllMovesOfB1InB2Test_3(): Unit = {
    val board1 = TicTacToe.play(TicTacToe(), Seq(TopLeft, BottomLeft))
    val board2 = TicTacToe.play(TicTacToe(), Seq(TopLeft))
    assertFalse(TicTacToe().areAllMovesOfB1InB2(board1, board2))
  }


  @Test def findBestNextMoveTest(): Unit = {
    val t = TicTacToe.play(TicTacToe(), Seq(BottomCenter, TopLeft, BottomRight, MiddleCenter, TopCenter))
    val possibleGames: Map[Seq[TMove], TicTacToe] = ???
    t.findBestNextMove(PlayerB, possibleGames)
  }

}
