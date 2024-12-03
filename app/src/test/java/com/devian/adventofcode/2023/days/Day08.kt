package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest
import kotlin.math.max

class Day08 : AdventOfCodeTest(8) {

    private val desert1 = buildMap1(TEST_INPUT_1)
    private val desert2 = buildMap1(TEST_INPUT_2)
    private val desert3 = buildMap1(TEST_INPUT_3)
    private val desertReal = buildMap1(input)

    override fun part1() {
//        val (desert, expected) = desert1 to 2
//        val (desert, expected) = desert2 to 6
        val (desert, expected) = desertReal to 18023
        val steps = desert.countSteps(desert.nodes["AAA"]!!) { it == "ZZZ" }

        println("STEPS: $steps")
        assert(steps == expected)
    }

    override fun part2() {
//        val desert = desert1
//        val desert = desert2
//        val desert = desert3
        val desert = desertReal

        val paths = desert.nodes.keys
            .filter { address -> address.endsWith("A") }
            .map {
                desert.countSteps(desert.nodes[it]!!) { address ->
                    address.endsWith("Z")
                }.toLong()
            }

        val sum = paths.findLCM()
        println("SUM: $sum")
        assert(sum == 14449445933179)
    }

    private fun DesertMap.countSteps(
        startingNodes: List<Node>,
        checkIfEnd: (String) -> Boolean,
    ): Int {
        var wandering = true
        var steps = 0
        var workingNodes = startingNodes
        while (wandering) directions.forEach { d ->
            steps++

            workingNodes = workingNodes.map {
                nodes[if (d == 'L') it.left else it.right]!!
            }

            if (workingNodes.all { checkIfEnd(it.address) }) {
                wandering = false
                return@forEach
            }
        }

        return steps
    }

    private fun DesertMap.countSteps(
        startingNode: Node,
        checkIfEnd: (String) -> Boolean,
    ): Int = countSteps(listOf(startingNode), checkIfEnd)

    private fun findLCM(a: Long, b: Long): Long {
        val largest = max(a, b)
        val maxLcm = a * b
        var lcm = largest
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += largest
        }

        return maxLcm
    }

    private fun List<Long>.findLCM(): Long {
        var lcm = firstOrNull() ?: return 0L
        for (i in 1 until size) {
            lcm = findLCM(lcm, this[i])
        }
        return lcm
    }

    private fun buildMap1(lines: List<String>) =
        DesertMap(
            lines[0],
            lines.mapIndexedNotNull { index, line ->
                if (index < 2) return@mapIndexedNotNull null
                val (node, left, right) = line.filterNot { it in "( )" }.split("=", ",")
                node to Node(node, left, right)
            }.toMap()
        )

    data class Node(
        val address: String,
        val left: String,
        val right: String,
    )

    data class DesertMap(
        val directions: String,
        val nodes: Map<String, Node>,
    )

    companion object {
        private val TEST_INPUT_1 = """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().split('\n').map { it.trim() }

        private val TEST_INPUT_2 = """
            LLR
            
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().split('\n').map { it.trim() }

        private val TEST_INPUT_3 = """
            LR
            
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent().split('\n').map { it.trim() }
    }
}