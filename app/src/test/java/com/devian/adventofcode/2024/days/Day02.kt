package com.devian.adventofcode.`2024`.days

import com.devian.adventofcode.AdventOfCodeTest
import kotlin.math.abs

class Day02 : AdventOfCodeTest(2, 2024) {

    private val test = buildReports(TEST_INPUT)
    private val real = buildReports(input)

    private fun buildReports(lines: List<String>) =
        lines.map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }

    private fun List<Int>.isSafe(): Boolean {
        print("[${joinToString(" ")}]")
        if (size < 2) return true

        val firstDif = this[1] - this[0]
        if (abs(firstDif) !in DIFF_RANGE) {
            println(" : Unsafe")
            return false
        }

        for (i in 1 until size) {
            val dif = this[i] - this[i - 1]
            if (abs(dif) !in DIFF_RANGE
                || (firstDif < 0 && dif > 0)
                || (firstDif > 0 && dif < 0)
            ) {
                println(" : Unsafe")
                return false
            }
        }

        println(" : Safe")
        return true
    }

    private fun List<Int>.isSafe(damperCapacity: Int): Boolean {
        if (damperCapacity == 0) return isSafe()

        if (!isSafe()) {
            for (i in indices) {
                toMutableList().apply {
                    removeAt(i)
                    print(" >> ")
                    if (isSafe(damperCapacity - 1)) {
                        return true
                    }
                }
            }
            return false
        }

        return true
    }

    override fun part1() {
        val (data, expected) =
//            test to 2
            real to 218

        val sum = data.count { it.isSafe() }

        println("SUM: $sum")
        assert(sum == expected)
    }

    override fun part2() {
        val testInput = """
            1 3 2 4 3 5
        """.trimIndent().split('\n').map { it.trim() }
        val extra = buildReports(testInput)
//        val (data, expected) = test to 4
//        val (data, expected) = extra to 1
        val (data, expected) = real to 290

        val sum = data.count { it.isSafe(1) }

        println("SUM: $sum")
        assert(sum == expected)
    }

    companion object {
        val DIFF_RANGE = 1..3

        private val TEST_INPUT = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent().split('\n').map { it.trim() }
    }
}