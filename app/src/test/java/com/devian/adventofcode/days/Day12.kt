package com.devian.adventofcode.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.flatMapNotNull

class Day12 : AdventOfCodeTest(12) {

    private val test1 = buildRows1(TEST_INPUT_1)
    private val test2 = buildRows1(TEST_INPUT_2)
    private val real = buildRows1(input)

    override fun part1() {
        val (data, expected) = test1 to 1
//        val (data, expected) = test2 to 1
//        val (data, expected) = real to 1
        val sum = 0
        data.forEach(::println)
        println()

        data.forEach { row ->
            val unknowns = row.springs.indices
                .filter { row.springs[it].isUnknown }
                .also(::println)
            for (i in row.springs.indices) {
                getMinGroupSize(row.springs, i).also(::println)
            }

            println()
            getVariants(row.springs, unknowns).forEach {
                println(it.joinToString(""))
            }

            println()
//            row.groupSizes.forEach { size ->
//                println("Looking for Damaged group of size $size")
//            }
            var lastIndex = -1
            val groupLocations = mutableListOf<Int>()
            val sizeItr = row.groupSizes.iterator()
            while (sizeItr.hasNext()) {
                val size = sizeItr.nextInt()
                println("Looking for Damaged group of size $size")
                var i = lastIndex + 1
                while (i in row.springs.indices) {
                    when (row.springs[i].condition) {
                        Condition.Damaged -> {
                            // group should fit, move to end of group
                            val group = row.springs.subList(i, i + size)
                            if (group.all { it.isDamaged || it.isUnknown } && !row.springs[i + size].isDamaged) {
                                row.springs[i + size]
                                groupLocations.add(i)
                                lastIndex = i + size
                                break
                            } else {
                                throw RuntimeException("Group is too big!")
                            }
                        }

                        Condition.Operational -> {
                            lastIndex = i
                            i++
                        }

                        Condition.Unknown -> {
                            val group = row.springs.subList(i, i + size)
                            if (group.all { it.isDamaged || it.isUnknown } && !row.springs[i + size].isDamaged) {
                                groupLocations.add(i)
                                lastIndex = i + size
                                break
                            } else {
                                i++
                            }
                        }
                    }
                }
            }

            println()
            val damagedSprings = groupLocations.flatMapIndexed { i, g ->
                println("groups[$i] starts @ $g")
                IntArray(row.groupSizes[i]) {
                    g + it
                }.toList()
            }.also(::println)

            println()
            for (g in row.springs.indices) {
                print(if (g in damagedSprings) '#' else '.')
            }
            println()
        }

        println()
        println("SUM: $sum")
//        assert(sum == expected)
    }

    override fun part2() {
        val (data, expected) = test1 to 2
//        val (data, expected) = real to 2
        val sum = 0
        println("SUM: $sum")
        assert(sum == expected)
    }

    private fun getVariants(
        springs: List<Spring>,
        unknowns: List<Int>,
        currentGroupSize: Int = 0,
        groupSizes: IntArray = IntArray(0),
    ): List<List<Spring>> {
//        println(springs)
//        println(unknowns)
        val i = unknowns.firstOrNull() ?: return listOf(springs)

        val first = springs.subList(0, i)//.also(::print)
        val last = springs.subList(i + 1, springs.size)//.also(::println)



        return emptyList()
        /*
        return listOf(Spring(Condition.Damaged), Spring(Condition.Operational))
            .flatMapNotNull {
                getVariants(
                    first + it + last,
                    unknowns.drop(1),
                    currentGroupSize,
                    groupSizes
                )
            }
        // */
    }

    private fun getMinGroupSize(springs: List<Spring>, start: Int): Int {
        for (i in start..springs.indices.last) {
            if (!springs[i].isDamaged) return i - start
        }

        return springs.indices.last - start + 1
    }

    private fun buildRows1(lines: List<String>) =
        lines.map { line ->
            val (springs, groupSizes) = line.split(" ")
            Row(
                springs.map(::Spring).toList(),
                groupSizes.split(",").map { it.toInt() }.toIntArray(),
            )
        }

    enum class Condition {
        Damaged, Operational, Unknown
    }

    data class Spring(val condition: Condition) {
        constructor(value: Char) : this(
            when (value) {
                '#' -> Condition.Damaged
                '.' -> Condition.Operational
                else -> Condition.Unknown
            }
        )

        val isDamaged: Boolean = condition == Condition.Damaged
        val isOperational: Boolean = condition == Condition.Operational
        val isUnknown: Boolean = condition == Condition.Unknown

        override fun toString(): String {
            return when (condition) {
                Condition.Damaged -> "#"
                Condition.Operational -> "."
                Condition.Unknown -> "?"
            }
        }
    }

    class Row(
        val springs: List<Spring>,
        val groupSizes: IntArray,
    ) {
        override fun toString(): String {
            return "${springs.joinToString("")}  ${groupSizes.joinToString(",")}"
        }
    }

    companion object {
        private val TEST_INPUT_1 = """
            ?###???????? 3,2,1
        """.trimIndent().split('\n').map { it.trim() }

        private val TEST_INPUT_2 = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent().split('\n').map { it.trim() }
    }
}
