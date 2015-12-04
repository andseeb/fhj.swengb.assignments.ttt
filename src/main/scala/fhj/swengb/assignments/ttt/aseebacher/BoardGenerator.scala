package fhj.swengb.assignments.ttt.aseebacher

import javafx.application.Application

import scala.annotation.tailrec

/**
  * Created by Andreas on 04.12.2015.
  */
object BoardGenerator {
  def main(args: Array[String]) {





    def calcPermutatedBoards(players : Seq[Player]) : Set[Map[TMove, Player]] = { //plarsTuple : (Player, Player, Player, Player, Player, Player, Player, Player, Player)
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


      println(players)
      println(players.permutations.size)
      println(players.permutations.toList.tail.head)
      println("-" * 80)
      val SetSets = players.permutations.map(playerListMixed => recCalcPermutatedBoards(playerListMixed, cells, Map[TMove, Player]())).toSet

      SetSets


    }




    def mergeSets(setsFrom: Set[Set[Map[TMove, Player]]]) : Set[Map[TMove, Player]] = {
      @tailrec def recMergeSets(setsFrom: Set[Set[Map[TMove, Player]]], curOutputSet:Set[Map[TMove, Player]]) : Set[Map[TMove, Player]] = {
        if (setsFrom.isEmpty) curOutputSet
        else recMergeSets(setsFrom.tail, curOutputSet ++ setsFrom.head)
      }
      recMergeSets(setsFrom, Set[Map[TMove, Player]]())
    }

    //mergeSets(SetSets)

    //println(calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB))) // 4xNone, 3xA, 2xB
    val possibleGames = mergeSets(
      Set(
        calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB)), // 4xNone, 3xA, 2xB
        calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB)), // 3xNone, 3xA, 3xB
        calcPermutatedBoards(Seq(PlayerNone, PlayerNone, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB)), // 2xNone, 4xA, 3xB
        calcPermutatedBoards(Seq(PlayerNone, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB, PlayerB)), // 1xNone, 4xA, 4xB
        calcPermutatedBoards(Seq(PlayerA, PlayerA, PlayerA, PlayerA, PlayerA, PlayerB, PlayerB, PlayerB, PlayerB)) // 0xNone, 5xA, 4xB
      )
    )


    println(possibleGames.size)











  }
}