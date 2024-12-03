package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.Point
import kotlin.math.abs

class Day11 : AdventOfCodeTest(11) {

    override fun part1() {
        val (universe, expected) = expand1(TEST_INPUT) to 374L
//        val (universe, expected) = expand1(input) to 9769724L

        val galaxies = universe.flatMapIndexed { y, line ->
            line.toCharArray()
                .mapIndexed { x, c -> if (c == '#') Point(x, y) else null }
                .filterNotNull()
        }

        val sum = galaxies
            .flatMapIndexed { i, l ->
                if (i == galaxies.indices.last) listOf(null)
                else galaxies
                    .subList(i + 1, galaxies.indices.last + 1)
                    .map { r -> l to r }
            }
            .filterNotNull()
            .sumOf { (l, r) ->
                l.stepsToPoint(r, 2L)
                    .also {
                        println("$l to $r ==> $it")
                    }
            }
        println("SUM: $sum")
        assert(sum == expected)
    }

    override fun part2() {
//        val (universe, emptySpace, expected) = Triple(expand2(TEST_INPUT), 2L, 374L)
//        val (universe, emptySpace, expected) = Triple(expand2(TEST_INPUT), 10L, 1030L)
//        val (universe, emptySpace, expected) = Triple(expand2(TEST_INPUT), 100L, 8410L)
        val (universe, emptySpace, expected) = Triple(expand2(input), 1_000_000L, 603020563700L)

        val galaxies = universe.flatMapIndexed { y, line ->
            line.toCharArray()
                .mapIndexed { x, c -> if (c == '#') Point(x, y) else null }
                .filterNotNull()
        }

        val sum = galaxies
            .flatMapIndexed { i, l ->
                if (i == galaxies.indices.last) listOf(null)
                else galaxies
                    .subList(i + 1, galaxies.indices.last + 1)
                    .map { r -> l to r }
            }
            .filterNotNull()
            .sumOf { (l, r) ->
                universe.stepsToPoint(l, r, emptySpace)
            }
        println("SUM: $sum")
        assert(sum == expected)
    }

    private fun Point.stepsToPoint(other: Point, emptyValue: Long): Long {
        if (other == this) return 0

        val xDiff = abs(other.x - x)
        val yDiff = abs(other.y - y)

        val nextStep = if (yDiff > xDiff) {
            copy(y = if (other.y > y) y + 1 else y - 1)
        } else {
            copy(x = if (other.x > x) x + 1 else x - 1)
        }

        return 1 + nextStep.stepsToPoint(other, emptyValue)
    }

    private fun List<String>.stepsToPoint(start: Point, end: Point, emptyValue: Long): Long {
        if (end == start) return 0

        val xDiff = abs(end.x - start.x)
        val yDiff = abs(end.y - start.y)

        val nextStep = if (yDiff > xDiff) {
            start.copy(y = if (end.y > start.y) start.y + 1 else start.y - 1)
        } else {
            start.copy(x = if (end.x > start.x) start.x + 1 else start.x - 1)
        }

        val stepAmount =
            if (yDiff > xDiff) {
                if (this[nextStep.y][nextStep.x] in "^*") emptyValue else 1
            } else {
                if (this[nextStep.y][nextStep.x] in ">*") emptyValue else 1
            }

        return stepAmount + stepsToPoint(nextStep, end, emptyValue)
    }

    private fun expand1(lines: List<String>): List<String> {
        val emptyColumns = lines.first().indices.filter { x ->
            lines.all { line -> line[x] == '.' }
        }

        val emptyRows = lines.indices.filter { y ->
            lines[y].all { it == '.' }
        }

        return lines.flatMapIndexed { y, line ->
            line
                .flatMapIndexed { x, cell ->
                    if (x in emptyColumns)
                        listOf(cell, cell)
                    else
                        listOf(cell)
                }
                .joinToString("")
                .let { row ->
                    if (y in emptyRows)
                        listOf(row, row)
                    else
                        listOf(row)
                }
        }
    }

    private fun expand2(lines: List<String>): List<String> {
        val emptyColumns = lines.first().indices.filter { x ->
            lines.all { line -> line[x] == '.' }
        }

        val emptyRows = lines.indices.filter { y ->
            lines[y].all { it == '.' }
        }

        return lines.mapIndexed { y, line ->
            line.mapIndexed { x, cell ->
                if (x in emptyColumns && y in emptyRows) '*'
                else if (x in emptyColumns) '>'
                else if (y in emptyRows) '^'
                else cell
            }.joinToString("")
        }
    }

    companion object {
        private val TEST_INPUT = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent().split('\n').map { it.trim() }
    }
}
