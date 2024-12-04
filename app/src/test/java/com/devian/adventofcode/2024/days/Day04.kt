package com.devian.adventofcode.`2024`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.Point

class Day04 : AdventOfCodeTest(4, 2024) {

    private val test = buildData(TEST_INPUT)
    private val real = buildData(input)

    private fun buildData(lines: List<String>) = lines//.joinToString("")

    private fun getPoints(startPoint: Point, endPoint: Point): List<Point> {
        val points = mutableListOf<Point>()
        val xRange =
            if (startPoint.x < endPoint.x) startPoint.x..endPoint.x
            else startPoint.x downTo endPoint.x
        val yRange =
            if (startPoint.y < endPoint.y) startPoint.y..endPoint.y
            else startPoint.y downTo endPoint.y

        when {
            startPoint.x != endPoint.x && startPoint.y != endPoint.y -> {
                val yList = yRange.toList()
                xRange.forEachIndexed { index, i ->
                    yList.getOrNull(index)?.let {
                        points.add(Point(i, it))
                    }
                }
            }

            startPoint.y == endPoint.y -> {
                for (i in xRange) {
                    points.add(Point(i, endPoint.y))
                }
            }

            else -> {
                for (j in yRange) {
                    points.add(Point(endPoint.x, j))
                }
            }
        }

        return points.toList()
    }

    private fun isWord(data: List<String>, startPoint: Point, endPoint: Point): Boolean {
        val points = getPoints(startPoint, endPoint)
        if (points.size < 4) return false

//        if (isXmas) {
////        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
//            print(points.joinToString { "(${it.x},${it.y})" })
//            print(" - ")
//            points.forEach {
//                print(data[it.y][it.x])
//            }
//            println()
//        }

        return data[points[0].y][points[0].x] == 'X'
                && data[points[1].y][points[1].x] == 'M'
                && data[points[2].y][points[2].x] == 'A'
                && data[points[3].y][points[3].x] == 'S'
    }

    private fun countWords(data: List<String>, x: Int, y: Int): Int {
        val start = Point(x, y)
        val miny = y - 3
        val maxy = y + 3
        val minx = x - 3
        val maxx = x + 3
        val points = listOf(
            Point(x, miny),
            Point(x, maxy),
            Point(minx, y),
            Point(maxx, y),
            Point(minx, miny),
            Point(maxx, miny),
            Point(maxx, maxy),
            Point(minx, maxy),
        ).mapNotNull {
            it.takeIf { it.x in data[y].indices && it.y in data.indices }
        }

        return points.count {
//            println(" > Point: (${it.x}, ${it.y})")
            isWord(data, start, it)
        }
    }

    override fun part1() {
        val (data, expected) =
//            test to 18
            real to 2336

        var sum = 0
        for (j in data.indices) {
            for (i in data[j].indices) {
                if (data[j][i] == 'X') {
//                    println("X @ ($i,$j)")
                    sum += countWords(data, i, j)
                }
            }
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    private fun isMas(data: List<String>, startPoint: Point, endPoint: Point): Boolean =
        (data[startPoint.y][startPoint.x] == 'M' && data[endPoint.y][endPoint.x] == 'S')
                || (data[startPoint.y][startPoint.x] == 'S' && data[endPoint.y][endPoint.x] == 'M')

    private fun isXMas(data: List<String>, x: Int, y: Int): Boolean {
        if (x !in 1..data[y].length - 2 || y !in 1..data.size - 2)
            return false

        return isMas(data, Point(x - 1, y - 1), Point(x + 1, y + 1))
                && isMas(data, Point(x - 1, y + 1), Point(x + 1, y - 1))
    }

    override fun part2() {
        val (data, expected) =
//            test to 9
            real to 1831

        var sum = 0
        for (j in data.indices) {
            for (i in data[j].indices) {
                if (data[j][i] == 'A') {
//                    println("X @ ($i,$j)")
                    if (isXMas(data, i, j)) sum++
                }
            }
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    companion object {
        private const val WORD = "XMAS"
        private val TEST_INPUT = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent().split('\n').map { it.trim() }
    }
}