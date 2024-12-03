package com.devian.adventofcode.`2024`.days

import com.devian.adventofcode.AdventOfCodeTest

class Day03 : AdventOfCodeTest(3, 2024) {

    private val test = TEST_INPUT.joinToString("")
    private val real = input.joinToString("")

    private fun multiply(token: String): Int {
        if (!MUL_TOKEN.matches(token)) return 0

        val numbers = token.removePrefix("mul(")
            .removeSuffix(")")
            .split(",")
            .mapNotNull { it.toIntOrNull() }

        if (numbers.size != 2) return 0

        return numbers[0] * numbers[1]
    }

    override fun part1() {
        val (data, expected) =
            test to 161
//            real to 156388521

        val sum = MUL_TOKEN.findAll(data).sumOf { match ->
            multiply(match.value).also {
                println("${match.value} = $it")
            }
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    override fun part2() {
        val (data, expected) =
//            test to 48
            real to 75920122

        var enabled = true
        var sum = 0
        val regex = "(mul\\([0-9]{1,3},[0-9]{1,3}\\))|(do\\(\\))|(don't\\(\\))".toRegex()
        regex.findAll(data).forEach { match ->
            when {
                DO_TOKEN.matches(match.value) -> {
                    enabled = true
                }

                DONT_TOKEN.matches(match.value) -> {
                    enabled = false
                }

                enabled -> {
                    sum += multiply(match.value)
                }
            }
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    companion object {
        private val MUL_TOKEN = "mul\\([0-9]{1,3},[0-9]{1,3}\\)".toRegex()
        private val DO_TOKEN = "do\\(\\)".toRegex()
        private val DONT_TOKEN = "don't\\(\\)".toRegex()
        private val TEST_INPUT = """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
        """.trimIndent().split('\n').map { it.trim() }
    }
}