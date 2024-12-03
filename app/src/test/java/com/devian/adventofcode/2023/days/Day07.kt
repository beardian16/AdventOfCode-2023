package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.println
import com.devian.adventofcode.sumOfIndexed

class Day07 : AdventOfCodeTest(7) {

    override fun part1() {
        val sum = buildList1(input)
            .sortedWith { left, right ->
                compare1(left, right)
            }.sumOfIndexed { i, hand ->
                (i + 1) * hand.bid
            }
        println("SUM: $sum")
        assert(sum == 253954294L)
    }

    override fun part2() {
        val sum = buildList2(input)
            .sortedWith { left, right ->
                compare2(left, right)
            }.sumOfIndexed { i, hand ->
                (i + 1) * hand.bid
            }
        println("SUM: $sum")
    }

    private fun getType1(cards: String): WinType {
        val cardCount = mutableMapOf<Char, Int>()
        repeat(5) { i ->
            cardCount[cards[i]] = (cardCount[cards[i]] ?: 0) + 1
        }

        return when (cardCount.size) {
            1 -> WinType.FIVE
            2 -> if (cardCount.values.any { it in 2..3 }) WinType.FULL else WinType.FOUR
            3 -> if (cardCount.values.any { it == 3 }) WinType.THREE else WinType.TWO
            4 -> WinType.ONE
            else -> WinType.HIGH
        }
    }

    private fun buildList1(lines: List<String>) = lines.map {
        val (cards, bid) = it.split(' ')
        Hand(cards, getType1(cards), bid.toLong())
    }

    private fun getType2(cards: String): WinType {
        val cardCount = mutableMapOf<Char, Int>()
        var numJokers = 0
        repeat(5) { i ->
            if (cards[i] == 'J') {
                numJokers++
            } else {
                cardCount[cards[i]] = (cardCount[cards[i]] ?: 0) + 1
            }
        }

        if (numJokers == 5) return WinType.FIVE

        cardCount.forEach { (k, v) ->
            cardCount[k] = v + numJokers
        }

        return when (cardCount.size) {
            1 -> WinType.FIVE
            2 -> if (cardCount.values.any { it == 4 }) WinType.FOUR else WinType.FULL
            3 -> if (cardCount.values.any { it == 3 }) WinType.THREE else WinType.TWO
            4 -> WinType.ONE
            else -> WinType.HIGH
        }
    }

    private fun buildList2(lines: List<String>) = lines.map {
        val (cards, bid) = it.split(' ')
        Hand(cards, getType2(cards), bid.toLong())
    }

    private fun compare1(left: Hand, right: Hand): Int {
        fun Char.toCardValue(): Int =
            when (this) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 11
                'T' -> 10
                '9' -> 9
                '8' -> 8
                '7' -> 7
                '6' -> 6
                '5' -> 5
                '4' -> 4
                '3' -> 3
                '2' -> 2
                else -> 0
            }

        val iWin = 1
//            val iWin = -1
        val iLose = iWin * -1

        if (left.winType.ordinal < right.winType.ordinal) {
            return iWin
        } else if (left.winType.ordinal > right.winType.ordinal) {
            return iLose
        } else {
            repeat(5) { i ->
                val myCard = left.cards[i].toCardValue()
                val otherCard = right.cards[i].toCardValue()
                if (myCard > otherCard) {
                    return iWin
                } else if (myCard < otherCard) {
                    return iLose
                }
            }
        }

        return 0
    }

    private fun compare2(left: Hand, right: Hand): Int {
        fun Char.toCardValue(): Int =
            when (this) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'T' -> 10
                '9' -> 9
                '8' -> 8
                '7' -> 7
                '6' -> 6
                '5' -> 5
                '4' -> 4
                '3' -> 3
                '2' -> 2
                'J' -> 1
                else -> 0
            }

        val iWin = 1
        val iLose = iWin * -1

        if (left.winType.ordinal < right.winType.ordinal) {
            return iWin
        } else if (left.winType.ordinal > right.winType.ordinal) {
            return iLose
        } else {
            repeat(5) { i ->
                val myCard = left.cards[i].toCardValue()
                val otherCard = right.cards[i].toCardValue()
                if (myCard > otherCard) {
                    return iWin
                } else if (myCard < otherCard) {
                    return iLose
                }
            }
        }

        return 0
    }

    data class Hand(
        val cards: String,
        val winType: WinType,
        val bid: Long,
    )

    enum class WinType {
        FIVE, FOUR, FULL, THREE, TWO, ONE, HIGH
    }

    companion object {
        private val TEST_INPUT = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent().split('\n').map { it.trim() }
    }
}