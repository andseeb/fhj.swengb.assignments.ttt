package fhj.swengb.assignments.ttt.aseebacher

import javafx.application.Application

import scala.annotation.tailrec
import scala.collection.Set

/**
  * Created by Andreas on 04.12.2015.
  */
object BoardGenerator {
  private def calcPermutatedBoards(players : Seq[Player]) : Set[Map[TMove, Player]] = { //plarsTuple : (Player, Player, Player, Player, Player, Player, Player, Player, Player)
    val cells = Seq[TMove](TopLeft, TopCenter, TopRight, MiddleLeft, MiddleCenter, MiddleRight, BottomLeft, BottomCenter, BottomRight)
    @tailrec def recCalcPermutatedBoards(playersLeft:Seq[Player], cellsLeft:Seq[TMove], board: Map[TMove, Player]) : Map[TMove, Player]= {
      assert(playersLeft.size == cellsLeft.size)
      if (playersLeft.isEmpty) board
      else {
        recCalcPermutatedBoards(playersLeft.tail, cellsLeft.tail, board+(cellsLeft.head->playersLeft.head))
      }
    }

    //bei Aufrufen entweder immer players oder cells durchmischen (= .permutatuons)
    //players ist logischer
    val SetSets = players.permutations.map(playerListMixed => recCalcPermutatedBoards(playerListMixed, cells, Map[TMove, Player]())).toSet
    SetSets
  }



  def generatePossibleEndStates() : Set[Map[TMove, Player]] = {
   val set5 = calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB)) // 4xNone, 3xA, 2xB
   val set6 = calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB)) // 3xNone, 3xA, 3xB
   val set7 = calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB)) // 2xNone, 4xA, 3xB
   val set8 = calcPermutatedBoards(Seq(PlayerNone, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB, PlayerB)) // 1xNone, 4xA, 4xB
   val set9 = calcPermutatedBoards(Seq(PlayerA, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB, PlayerB)) // 0xNone, 5xA, 4xB
   set5 ++ set6 ++ set7 ++ set8 ++ set9
  }

  def generateGames() : Map[Seq[TMove], TicTacToe] = {
    @tailrec def recMergeAndConvert(entriesLeft:Set[Map[TMove, Player]], curMap:Map[Seq[TMove], TicTacToe] ) : Map[Seq[TMove], TicTacToe] = {
      if (entriesLeft == Set.empty) curMap
      else recMergeAndConvert(entriesLeft.tail, curMap+(entriesLeft.head.filter(_._2 != PlayerNone).keySet.toSeq->TicTacToe(entriesLeft.head)))
    }
    recMergeAndConvert(generatePossibleEndStates(), Map[Seq[TMove], TicTacToe]())
  }





  def main(args: Array[String]) {
    val possibleGames = generateGames()
    //println(generatePossibleEndStates.map(state => TicTacToe(state)).mkString("\r\n"))
    println(possibleGames.values.size)
    //println(possibleGames.map(_._2).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerA))
    println(possibleGames.values.count(ttt => ttt.winner.isDefined && ttt.winner.get._1 == PlayerA))
    //println(possibleGames.map(_._2).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerB))
    println(possibleGames.values.count(ttt => ttt.winner.isDefined && ttt.winner.get._1 == PlayerB))
    //println(possibleGames.map(_._2).filter(_.winner.isEmpty))
    println(possibleGames.values.count(_.winner.isEmpty))
    //println("test\ntest")
  }
}



class BoardGenerator