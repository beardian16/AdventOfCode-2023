package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.println
import org.junit.Test
import kotlin.math.abs
import kotlin.math.roundToInt

class Day10 : AdventOfCodeTest(10) {

    private val test1 = buildMap1(TEST_INPUT_1)
    private val test2 = buildMap1(TEST_INPUT_2)
    private val test3 = buildMap1(TEST_INPUT_3)
    private val test4 = buildMap1(TEST_INPUT_4)
    private val test5 = buildMap1(TEST_INPUT_5)
    private val real = buildMap1(input)

    override fun part1() {
//        val (data, expected) = test1 to 4
//        val (data, expected) = test2 to 8
        val (data, expected) = test5 to 8
//        val (data, expected) = real to 6846

        val start = data.getStartingPoint()

        var a = start
        var b = start
        var steps = 0
        var (aNext, bNext) = data.findConnections(start)
        while (steps == 0 || a != b) {
            steps++
            if (aNext == bNext) break

            val aDirection = aNext.getDirection(a)!!
            a = aNext
            aNext = a.getNext(PIPES[data[a.second][a.first]]!!.nextDirection(aDirection)!!)

            val bDirection = bNext.getDirection(b)!!
            b = bNext
            bNext = b.getNext(PIPES[data[b.second][b.first]]!!.nextDirection(bDirection)!!)
        }

        println("steps: $steps")
        assert(steps == expected)
    }

    override fun part2() {
//        val (data, expected) = test1 to 1
//        val (data, expected) = test2 to 1
//        val (data, expected) = test3 to 4
//        val (data, expected) = test4 to 4
//        val (data, expected) = test5 to 8
        val (data, expected) = real to 325

        val start = data.getStartingPoint()
        var a = start
        var steps = 0
        var (aNext, bNext) = data.findConnections(start)
        val path = mutableListOf(start, aNext)
        while (steps == 0 || a != start) {
            steps++
            if (aNext == bNext) break

            val aDirection = aNext.getDirection(a)!!
            a = aNext
            aNext = a.getNext(PIPES[data[a.second][a.first]]!!.nextDirection(aDirection)!!)
            path.add(aNext)
        }

        val lattice = path.toList()
        val n = lattice.size
        var area = 0.0
        for (i in 0 until n - 1) area += (lattice[i].first * lattice[i + 1].second - lattice[i + 1].first * lattice[i].second)
        area += (lattice[n - 1].first * lattice[0].second - lattice[0].first * lattice[n - 1].second)
        val pipesArea = abs(area) / 2.0

        val b = path.size
        val i = (pipesArea - (b / 2.0) + 1).roundToInt()
        i.println()
        assert(i == expected)
    }

    private fun buildMap1(lines: List<String>) =
        lines.map { line ->
            line.toCharArray()
        }.toTypedArray()

    private fun Array<CharArray>.getStartingPoint(): Pair<Int, Int> {
        var startingX = -1
        val startingY = indexOfFirst { row ->
            startingX = row.indexOf('S')
            startingX >= 0
        }
        return startingX to startingY
    }

    private fun Array<CharArray>.findConnections(start: Pair<Int, Int>): List<Pair<Int, Int>> {
        val yRange =
            (start.second - 1).coerceAtLeast(0)..(start.second + 1).coerceAtMost(indices.last)
        val xRange =
            (start.first - 1).coerceAtLeast(0)..(start.first + 1).coerceAtMost(first().indices.last)

        val surrounding = yRange.flatMap { y -> xRange.map { x -> x to y } }

        return surrounding.filter { (x, y) ->
            if (x == start.first) {
                if (y < start.second) {
                    this[y][x] in "|7F"
                } else {
                    this[y][x] in "|LJ"
                }
            } else if (y == start.second) {
                if (x < start.first) {
                    this[y][x] in "-LF"
                } else {
                    this[y][x] in "-7J"
                }
            } else false
        }
    }

    private fun Pair<Int, Int>.getDirection(other: Pair<Int, Int>): Direction? =
        when {
            other.second == second && other.first == first -> null
            other.second == second -> if (other.first > first) Direction.E else Direction.W
            other.second > second -> Direction.S
            else -> Direction.N
        }

    private fun Pair<Int, Int>.getNext(direction: Direction) =
        when (direction) {
            Direction.N -> first to second - 1
            Direction.S -> first to second + 1
            Direction.E -> first + 1 to second
            Direction.W -> first - 1 to second
        }

    enum class Direction {
        N, S, E, W
    }

    data class Pipe(private val directions: List<Direction>) {
        fun nextDirection(enterDirection: Direction): Direction? {
            if (enterDirection !in directions) return null
            return directions.firstOrNull { it != enterDirection }
        }
    }

    companion object {
        private val PIPES = mapOf(
            '|' to Pipe(listOf(Direction.N, Direction.S)),
            '-' to Pipe(listOf(Direction.E, Direction.W)),
            'F' to Pipe(listOf(Direction.S, Direction.E)),
            '7' to Pipe(listOf(Direction.S, Direction.W)),
            'J' to Pipe(listOf(Direction.N, Direction.W)),
            'L' to Pipe(listOf(Direction.N, Direction.E)),
        )

        private val TEST_INPUT_1 = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
        """.trimIndent().split('\n').map { it.trim() }

        private val TEST_INPUT_2 = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
        """.trimIndent().split('\n').map { it.trim() }

        private val TEST_INPUT_3 = """
            ...........
            .S-------7.
            .|F-----7|.
            .||OOOOO||.
            .||OOOOO||.
            .|L-7OF-J|.
            .|II|O|II|.
            .L--JOL--J.
            .....O.....
        """.trimIndent().split('\n').map { it.trim() }

        private val TEST_INPUT_4 = """
            ..........
            .S------7.
            .|F----7|.
            .||OOOO||.
            .||OOOO||.
            .|L-7F-J|.
            .|II||II|.
            .L--JL--J.
            ..........
        """.trimIndent().split('\n').map { it.trim() }

        private val TEST_INPUT_5 = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
        """.trimIndent().split('\n').map { it.trim() }
    }
}