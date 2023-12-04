package com.devian.adventofcode

import org.junit.Test

abstract class AdventOfCodeTest(dayNum: Int) {

    val input = readInput(dayNum.toFilename())

    @Test
    abstract fun part1()

    @Test
    abstract fun part2()
}