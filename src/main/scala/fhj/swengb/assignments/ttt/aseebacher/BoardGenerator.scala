package fhj.swengb.assignments.ttt.aseebacher

import javafx.application.Application

import scala.annotation.tailrec
import scala.collection.Set

/**
  * Created by Andreas on 04.12.2015.
  */
object BoardGenerator {
  private def calcPermutatedBoards(players : Seq[Player]) : Set[Map[TMove, Player]] = { //plarsTuple : (Player, Player, Player, Player, Player, Player, Player, Player, Player)
    //val players = Seq[Player](plarsTuple._1, plarsTuple._2, plarsTuple._3, plarsTuple._4, plarsTuple._5, plarsTuple._6, plarsTuple._7, plarsTuple._8, plarsTuple._9)
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


    /*println(players)
    println(players.permutations.size)
    println(players.permutations.toList.tail.head)
    println("-" * 80)*/
    val SetSets = players.permutations.map(playerListMixed => recCalcPermutatedBoards(playerListMixed, cells, Map[TMove, Player]())).toSet

    SetSets


  }

  private def mergeSets(setsFrom: Set[Set[Map[TMove, Player]]]) : Set[Map[TMove, Player]] = {
    @tailrec def recMergeSets(setsFrom: Set[Set[Map[TMove, Player]]], curOutputSet:Set[Map[TMove, Player]]) : Set[Map[TMove, Player]] = {
      if (setsFrom.isEmpty) curOutputSet
      else recMergeSets(setsFrom.tail, curOutputSet ++ setsFrom.head)
    }
    recMergeSets(setsFrom, Set[Map[TMove, Player]]())
  }



  def generatePossibleEndStates() : Set[Map[TMove, Player]] = {
    mergeSets(
      Set(
        calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB)), // 4xNone, 3xA, 2xB
        calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB)), // 3xNone, 3xA, 3xB
        calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB)), // 2xNone, 4xA, 3xB
        calcPermutatedBoards(Seq(PlayerNone, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB, PlayerB)), // 1xNone, 4xA, 4xB
        calcPermutatedBoards(Seq(PlayerA, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB, PlayerB)) // 0xNone, 5xA, 4xB
      )
    )//.map(state => TicTacToe(state))
  }

  def generateGames() : Map[Seq[TMove], TicTacToe] = {
    def recMergeAndConvert(entriesLeft:Set[Map[TMove, Player]], curMap:Map[Seq[TMove], TicTacToe] ) : Map[Seq[TMove], TicTacToe] = {
      //println(entriesLeft)
      if (entriesLeft == Set.empty) curMap
      else recMergeAndConvert(entriesLeft.tail, curMap+(entriesLeft.head.filter(_._2 != PlayerNone).keySet.toSeq->TicTacToe(entriesLeft.head)))
    }

    recMergeAndConvert(generatePossibleEndStates(), Map[Seq[TMove], TicTacToe]())
  }

  def main(args: Array[String]) {
    println(generatePossibleEndStates.map(state => TicTacToe(state)).mkString("\r\n"))
    println(generatePossibleEndStates.map(state => TicTacToe(state)).size)



    println(generatePossibleEndStates.map(state => TicTacToe(state)).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerA))
    println(generatePossibleEndStates.map(state => TicTacToe(state)).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerA).size)


    println(generatePossibleEndStates.map(state => TicTacToe(state)).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerB))
    println(generatePossibleEndStates.map(state => TicTacToe(state)).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerB).size)

    println(generatePossibleEndStates.map(state => TicTacToe(state)).filter(_.winner.isDefined).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerNone))
    println(generatePossibleEndStates.map(state => TicTacToe(state)).filter(_.winner.isDefined).filter(_.winner.get._1 == PlayerNone).size)

    println("test\ntest")
  }
}



class BoardGenerator