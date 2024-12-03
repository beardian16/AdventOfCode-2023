package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest
import com.devian.adventofcode.println
import org.junit.Test

class Day05 : AdventOfCodeTest(5) {

    private val testLines = buildSeedMap(TEST_INPUT)
    private val realLines = buildSeedMap(input)

//        private val seedMap = testLines
    private val seedMap = realLines

    override fun part1() {
        val min = seedMap.seedRanges.flatMap { seedRange ->
            seedRange.map { seed ->
                var next = seed
                seedMap.rangeMaps.forEach { ranges ->
                    next = ranges.firstOrNull {
                        next in it.srcRange
                    }?.let {
                        next - it.srcRange.first + it.destRange.first
                    } ?: next
                }
                next
            }
        }.minOf { it }
        println("MIN: $min")
    }

    override fun part2() {
        //MIN: 382895070
        seedMap.println()
        var min = Long.MAX_VALUE

//        seedMap.rangeMaps.fold(seedMap.seedRanges.map { it.first }) { acc, map -> acc }
//        /*
        seedMap.seedRanges.forEach { seedRange ->
            seedRange.forEach { seed ->
                var next = seed
                seedMap.rangeMaps.forEach { ranges ->
                    next = ranges.firstOrNull {
                        next in it.srcRange
                    }?.let {
                        next - it.srcRange.first + it.destRange.first
                    } ?: next
                }
                if (next < min) {
                    "Changing min: $min -> $next".println()
                    min = next
                }
            }
        }
        // */
        println("MIN: $min")
    }

    @Test
    fun part2a() {
        seedMap.seedRanges.println()
        seedMap.rangeMaps.joinToString("\n").println()
        var min = Long.MAX_VALUE
        println("MIN: $min")
    }

    private fun buildSeedMap(input: List<String>): SeedMap {
        val initialSeedRanges = mutableListOf<LongRange>()
        var pending = mutableListOf<Ranges>()
        val ranges = mutableListOf<List<Ranges>>()

        input.forEachIndexed { i, line ->
            when {
                i == 0 -> {
                    val initialSeeds = line.split(": ")[1].split(' ').map { it.toLong() }
                    initialSeedRanges.addAll(
                        // Part 1
//                        initialSeeds.map { it..it }
                        // Part 2
                        initialSeeds.chunked(2) { it[0] until (it[0] + it[1]) }.also {
                            it.forEach { r1 ->
                                it.forEach { r2 ->
                                    if (r1 != r2 && (r2.first in r1 || r2.last in r1)) {
                                        println("Found overlap")
                                    }
                                }
                            }
                        }
                    )
                }

                line.isBlank() && pending.isNotEmpty() -> {
                    ranges.add(pending)
                    pending = mutableListOf()
                }

                line.firstOrNull()?.isDigit() == true -> {
                    val (dest, src, size) = line.split(' ').map { it.toLong() }
                    pending.add(
                        Ranges(
                            srcRange = src until (src + size),
                            destRange = dest until (dest + size),
                        )
                    )
                }
            }
        }

        ranges.add(pending)

//        seeds.println()
//        ranges.println()

        return SeedMap(initialSeedRanges, ranges)
    }

    data class Ranges(
        val srcRange: LongRange,
        val destRange: LongRange,
    )

    data class SeedMap(
        val seedRanges: List<LongRange>,
        val rangeMaps: List<List<Ranges>>,
    )

    companion object {
        private val TEST_INPUT = """
            seeds: 79 14 55 13
            
            seed-to-soil map:
            50 98 2
            52 50 48
            
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
            
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
            
            water-to-light map:
            88 18 7
            18 25 70
            
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
            
            temperature-to-humidity map:
            0 69 1
            1 0 69
            
            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent().split('\n').map { it.trim() }
    }
}