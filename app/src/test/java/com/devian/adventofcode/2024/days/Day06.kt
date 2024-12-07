package com.devian.adventofcode.`2024`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.Point

class Day06 : AdventOfCodeTest(6, 2024) {

    private val test = buildData(TEST_INPUT)
    private val real = buildData(input)

    private fun buildData(lines: List<String>) =
        lines.map { it.toMutableList() } to lines.findGuard()?.let {
            Position(it, Direction.North)
        }

    private fun List<String>.findGuard(): Point? {
        forEachIndexed { j, line ->
            line.indexOfFirst { it == '^' }
                .takeIf { it >= 0 }
                ?.let {
                    return Point(it, j)
                }
        }
        return null
    }

    private fun List<MutableList<Char>>.isPointInBounds(point: Point) =
        point.y in indices && point.x in first().indices

    private fun List<MutableList<Char>>.nextPosition(guardPosition: Position): Position {
        val nextPosition = guardPosition.next()

        return if (!isPointInBounds(nextPosition.point)
            || this[nextPosition.point.y][nextPosition.point.x] != '#'
        ) {
            nextPosition
        } else {
            guardPosition.rotate()
        }
    }

    private fun List<MutableList<Char>>.stepsToExit(guardStart: Position): Int {
        val guardPoints = mutableSetOf<Point>()
        val guardPositions = mutableSetOf<Position>()

        var itr = guardStart
        while (isPointInBounds(itr.point)) {
            if (guardPositions.contains(itr)) {
                return -1
            }

            guardPoints.add(itr.point)
            guardPositions.add(itr)
            itr = nextPosition(itr)
        }

        return guardPoints.size
    }

    override fun part1() {
        val (data, expected) =
            test to 41
//            real to 5145

        val lab = data.first
        val guardStart = data.second ?: return

        val sum = lab.stepsToExit(guardStart)

        println("SUM: $sum")
        assert(sum == expected)
    }

    override fun part2() {
        val (data, expected) =
//            test to 6
            real to 1523

        val lab = data.first
        val guardStart = data.second ?: return
        lab[guardStart.point.y][guardStart.point.x] = '.'

        val obstacles = mutableSetOf<Point>()
        val failed = mutableSetOf<Point>()

        var itr = guardStart

        while (lab.isPointInBounds(itr.point)) {
            // Determine next step
            val next = lab.nextPosition(itr)

            if (next.point != itr.point
                && lab.isPointInBounds(next.point)
                && !obstacles.contains(next.point)
                && !failed.contains(next.point)
            ) {
                // counts steps to exit with new obstacle
                lab[next.point.y][next.point.x] = '#'
                val stepsToExit = lab.stepsToExit(itr)
                lab[next.point.y][next.point.x] = '.'
                if (stepsToExit < 0) {
                    obstacles.add(next.point)
                } else {
                    failed.add(next.point)
                }
            }

            itr = next
        }

        val sum = obstacles.size

        println("SUM: $sum")
        assert(sum == expected)
    }

    sealed class Direction(val deltaX: Int, val deltaY: Int) {
        data object North : Direction(0, -1)
        data object East : Direction(1, 0)
        data object South : Direction(0, 1)
        data object West : Direction(-1, 0)

        fun rotate() = when (this) {
            North -> East
            East -> South
            South -> West
            West -> North
        }
    }

    data class Position(
        val point: Point,
        val direction: Direction,
    ) {
        fun next() = copy(
            point = Point(
                point.x + direction.deltaX,
                point.y + direction.deltaY,
            )
        )

        fun rotate() = copy(
            direction = direction.rotate()
        )
    }

    companion object {
        //guard at (4,6)
        private val TEST_INPUT = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            .....##...
        """.trimIndent().split('\n').map { it.trim() }
    }
}