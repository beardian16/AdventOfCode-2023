package com.devian.adventofcode.days

import com.devian.adventofcode.AdventOfCodeTest

class Day01 : AdventOfCodeTest(1) {

    override fun part1() {
        val sum = input.sumOf { line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            "$first$last".toInt()
        }

        println("SUM: $sum")
    }

    override fun part2() {
        val pairs = mapOf(
            "0" to 0,
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
            "zero" to 0,
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )

        val sum = input.sumOf { line ->
            val first = line
                .findAnyOf(pairs.keys, 0)
                ?.second
                ?.let { pairs[it] }
                ?: 0
            val last = line
                .findLastAnyOf(pairs.keys, line.lastIndex)
                ?.second
                ?.let { pairs[it] }
                ?: 0
            "$first$last".toInt()
        }

        println("SUM: $sum")
    }
}