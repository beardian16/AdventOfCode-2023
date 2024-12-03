package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.sumOfIndexed

class Day03 : AdventOfCodeTest(3) {

//    private val lines = buildLines(TEST_INPUT)
    private val lines = buildLines(input)

    override fun part1() {
        val lastRowIndex = lines.size - 1
        val sum = lines.sumOfIndexed { i, line ->
            line.numbers.sumOf { range ->
                val adjacentRange = (range.first - 1)..(range.last + 1)
                val number = line.raw.substring(range).toLong()
                if (i > 0 && lines[i - 1].symbols.any { it in adjacentRange }
                    || line.symbols.contains(range.first - 1) || line.symbols.contains(range.last + 1)
                    || i < lastRowIndex && lines[i + 1].symbols.any { it in adjacentRange }
                ) {
                    number
                } else {
                    0L
                }
            }
        }

        println("SUM: $sum")
    }

    override fun part2() {
        val lastRowIndex = lines.size - 1
        val gears = mutableMapOf<Pair<Int, Int>, Gear>()
        lines.forEachIndexed { i, line ->
            line.numbers.forEach { range ->
                val adjacentRange = (range.first - 1)..(range.last + 1)
                val number = line.raw.substring(range).toInt()
                if (i > 0 && lines[i - 1].symbols.any { it in adjacentRange }
                    || line.symbols.contains(range.first - 1) || line.symbols.contains(range.last + 1)
                    || i < lastRowIndex && lines[i + 1].symbols.any { it in adjacentRange }
                ) {
                    fun addGear(row: Int, column: Int) {
                        gears[row to column] = gears[row to column]?.let {
                            Gear(it.value * number, it.count + 1)
                        } ?: Gear(number, 1)
                    }

                    if (adjacentRange.first >= 0 && line.raw[adjacentRange.first] == '*') {
                        addGear(i, adjacentRange.first)
                    }

                    if (adjacentRange.last <= line.raw.length - 1 && line.raw[adjacentRange.last] == '*') {
                        addGear(i, adjacentRange.last)
                    }

                    gearCheck@ for (j in adjacentRange) {
                        if (j !in line.raw.indices) continue@gearCheck

                        if (i > 0 && lines[i - 1].raw[j] == '*') {
                            addGear(i - 1, j)
                        }

                        if (i < lastRowIndex && lines[i + 1].raw[j] == '*') {
                            addGear(i + 1, j)
                        }
                    }
                }
            }
        }

        val sum = gears.values.sumOf {
            if (it.count == 2) it.value else 0
        }

        println(gears)
        println("SUM: $sum")
    }

    private fun Char.isSymbol(): Boolean = this != '.' && !isLetterOrDigit()

    private fun buildLines(input: List<String> = this.input) =
        input.mapIndexed { _, line ->
            val numbers = mutableListOf<IntRange>()
            val symbols = mutableListOf<Int>()
            var startIndex = -1
            var endIndex = -1

            line.forEachIndexed { j, c ->
                if (c.isDigit()) {
                    if (startIndex < 0) {
                        startIndex = j
                    }
                    endIndex = j
                } else {
                    if (c.isSymbol()) {
                        symbols.add(j)
                    }
                    if (startIndex >= 0 && endIndex >= 0) {
                        numbers.add(startIndex..endIndex)
                    }
                    startIndex = -1
                    endIndex = -1
                }
            }

            if (startIndex >= 0 && endIndex >= 0) {
                numbers.add(startIndex..endIndex)
            }

//            println("")
//            println(line)
//            println(numbers)
//            println(symbols)
            Line(line, numbers, symbols)
        }

    data class Line(
        val raw: String,
        val numbers: List<IntRange>,
        val symbols: List<Int>,
    )

    data class Part(
        val value: Int,
        val location: IntRange,
    )

    data class Gear(
        val value: Int,
        val count: Int,
    )

    companion object {
        private val TEST_INPUT = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        """.trimIndent().split('\n').map { it.trim() }
    }
}