package com.devian.adventofcode.days

import com.devian.adventofcode.AdventOfCodeTest
import kotlin.math.pow
import kotlin.math.roundToInt

class Day04 : AdventOfCodeTest(4) {

    private val tesLines = buildLines(TEST_INPUT)
    private val realLines = buildLines(input)

//    private val lines = tesLines
    private val lines = realLines

    override fun part1() {
        val sum = lines.sumOf { card ->
            val count = card.numbers.count { it in card.winners }
            if (count > 0) 2.0.pow(count - 1.0).roundToInt() else 0
        }

        println("SUM: $sum")
    }

    override fun part2() {
        val numCopies = IntArray(lines.size) { 0 }
        lines.forEachIndexed { index, card ->
            val count = card.numbers.count { it in card.winners }
            if (count > 0) {
                for (i in 1..count) {
                    numCopies[index + i] += (1 + numCopies[index])
                }
            }
        }

        val sum = numCopies.sumOf { it + 1 }

        println(numCopies.joinToString())
        println("SUM: $sum")
    }

    private fun buildLines(input: List<String>) =
        input.map { line ->
            val lineInput = line.split(": ")[1].split(" | ")
            Card(
                lineInput[0].trim().split(Regex(" +")).map { it.toInt() },
                lineInput[1].trim().split(Regex(" +")).map { it.toInt() },
            )
        }

    data class Card(
        val winners: List<Int>,
        val numbers: List<Int>,
    )

    companion object {
        private val TEST_INPUT = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent().split('\n').map { it.trim() }
    }
}