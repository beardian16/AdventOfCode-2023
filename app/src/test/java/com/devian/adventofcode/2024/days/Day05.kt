package com.devian.adventofcode.`2024`.days

import com.devian.adventofcode.AdventOfCodeTest

class Day05 : AdventOfCodeTest(5, 2024) {

    private val test = buildData(TEST_INPUT)
    private val real = buildData(input)

    private fun buildData(lines: List<String>) =
        lines.indexOf("").let { gapIndex ->
            val before = mutableMapOf<Int, List<Int>>()
            val after = mutableMapOf<Int, List<Int>>()
            lines.subList(0, gapIndex).forEach { rule ->
                rule.split("|").map { it.toInt() }.also { pair ->
                    before[pair[0]] = (before[pair[0]] ?: listOf()) + pair[1]
                    after[pair[1]] = (after[pair[1]] ?: listOf()) + pair[0]
                }
            }
            val edits = lines.subList(gapIndex + 1, lines.size).map {
                it.split(",").map { it.toInt() }
            }
            Rules(before.toMap(), after.toMap()) to edits
        }

    private fun List<Int>.median() = this[size / 2]

    override fun part1() {
        val (data, expected) =
//            test to 143
            real to 4689

        val sum = data.second.sumOf {
            if (data.first.isOrderCorrect(it)) it.median() else 0
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    override fun part2() {
        val (data, expected) =
//            test to 123
            real to 6336

        val sum = data.second.sumOf {
            if (data.first.isOrderCorrect(it)) 0 else data.first.sort(it).median()
        }

        println("SUM: $sum")
        assert(sum == expected)
    }

    private data class Rules(
        val beforeRules: Map<Int, List<Int>>,
        val afterRules: Map<Int, List<Int>>,
    ) {
        fun isOrderCorrect(pages: List<Int>): Boolean {
            return pages.all {
                pages.beforeCorrect(it) && pages.afterCorrect(it)
            }
        }

        fun sort(pages: List<Int>): List<Int> {
            return pages.sortedWith { left, right ->
                if (beforeRules[right]?.contains(left) == true || afterRules[left]?.contains(right) == true) 1 else -1
            }
        }

        private fun List<Int>.beforeCorrect(page: Int): Boolean {
            val pageIndex = indexOf(page)
            beforeRules[page]?.forEach {
                val ruleIndex = indexOf(it)
                if (ruleIndex in 0..pageIndex) return false
            }
            return true
        }

        private fun List<Int>.afterCorrect(page: Int): Boolean {
            val pageIndex = indexOf(page)
            afterRules[page]?.forEach {
                if (indexOf(it) >= pageIndex) return false
            }
            return true
        }
    }

    companion object {
        private val TEST_INPUT = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
        """.trimIndent().split('\n').map { it.trim() }
    }
}