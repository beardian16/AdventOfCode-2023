package com.devian.adventofcode

import org.junit.Test

abstract class AdventOfCodeTest(
    dayNum: Int,
    year: Int = 2023,
) {

    val input = readInput(year, dayNum.toFilename())

    @Test
    abstract fun part1()

    @Test
    abstract fun part2()
}