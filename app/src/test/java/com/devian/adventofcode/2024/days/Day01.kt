package com.devian.adventofcode.`2024`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.sumOfIndexed
import kotlin.math.abs

class Day01 : AdventOfCodeTest(1, 2024) {

    private val test = sortLists(TEST_INPUT)
    private val real = sortLists(input)

    private fun sortLists(lines: List<String>): Pair<List<Int>, List<Int>> {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()

        lines.map { line ->
            line.split("\\s+".toRegex()).let {
                left.add(it[0].toInt())
                right.add(it[1].toInt())
            }
        }

        return left.sorted() to right.sorted()
    }

    override fun part1() {
//        val (data, expected) = test to 11L
        val (data, expected) = real to 2113135L

        val sum = data.first.sumOfIndexed { index, i ->
            abs(data.second[index] - i).toLong()
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    override fun part2() {
//        val (data, expected) = test to 31
        val (data, expected) = real to 19097157

        val sum = data.first.sumOf { left ->
            left * data.second.count { it == left }
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    companion object {
        private val TEST_INPUT = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent().split('\n').map { it.trim() }
    }
}