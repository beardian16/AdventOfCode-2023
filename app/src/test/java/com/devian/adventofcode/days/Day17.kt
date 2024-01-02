package com.devian.adventofcode.days

import com.devian.adventofcode.AdventOfCodeTest

class Day17 : AdventOfCodeTest(17) {

    private val test = buildMap1(TEST_INPUT)
    private val real = buildMap1(input)

    override fun part1() {
        val (data, expected) = test to 1
//        val (data, expected) = real to 1
        val sum = 0
        println("SUM: $sum")
        assert(sum == expected)
    }

    override fun part2() {
        val (data, expected) = test to 2
//        val (data, expected) = real to 2
        val sum = 0
        println("SUM: $sum")
        assert(sum == expected)
    }

    private fun buildMap1(lines: List<String>) =
        lines.map { line ->
            line
        }

    companion object {
        private val TEST_INPUT = """
        """.trimIndent().split('\n').map { it.trim() }
    }
}
