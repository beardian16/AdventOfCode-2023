package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest

class Day09 : AdventOfCodeTest(9) {

    private val test = buildMap1(TEST_INPUT)
    private val real = buildMap1(input)

    override fun part1() {
//        val (histories, expected) = test to 114L
        val (histories, expected) = real to 1789635132L
        val sum = histories.sumOf { history ->
            history.next()
        }

        println("STEPS: $sum")
        assert(sum == expected)
    }

    override fun part2() {
//        val (histories, expected) = test to 2L
        val (histories, expected) = real to 913L
        val sum = histories.sumOf { history ->
            history.previous()
        }
        println("SUM: $sum")
        assert(sum == expected)
    }

    private fun List<Long>.diffList(): List<Long> = mapIndexedNotNull { i, n ->
        if (i == size - 1) return@mapIndexedNotNull null
        this[i + 1] - n
    }

    private fun List<Long>.next(): Long {
        if (all { it == 0L }) return 0L
        return last() + diffList().next()
    }

    private fun List<Long>.previous(): Long {
        if (all { it == 0L }) return 0L
        return first() - diffList().previous()
    }

    private fun buildMap1(lines: List<String>) =
        lines.map { line ->
            line.split(" ").map { it.toLong() }
        }

    companion object {
        private val TEST_INPUT = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent().split('\n').map { it.trim() }
    }
}