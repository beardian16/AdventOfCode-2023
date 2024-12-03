package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest

class Day02 : AdventOfCodeTest(2) {

    private val games: List<Game> = getGameInput()
    private val rMax = 12
    private val gMax = 13
    private val bMax = 14

    override fun part1() {
        val sum = games.sumOf { game ->
            println(game)
            if (game.pulls.all { it.r <= rMax && it.g <= gMax && it.b <= bMax }) {
                game.id
            } else 0
        }

        println("SUM: $sum")
    }

    override fun part2() {
        val sum = games.sumOf { game ->
            var r = 0
            var g = 0
            var b = 0

            game.pulls.forEach { pull ->
                if (pull.r > r) r = pull.r
                if (pull.g > g) g = pull.g
                if (pull.b > b) b = pull.b
            }

            r * g * b
        }

        println("SUM: $sum")
    }

    private fun getGameInput(): List<Game> =
        input.map { input ->
            val (game, pulls) = input.split(':')
            Game(
                game.trim().split(' ')[1].toInt(),
                pulls.split(';').map { pull ->
                    var r = 0
                    var g = 0
                    var b = 0
                    pull.trim().split(',').forEach { cubes ->
                        val (count, color) = cubes.trim().split(' ')
                        when (color) {
                            "red" -> r = count.toInt()
                            "green" -> g = count.toInt()
                            "blue" -> b = count.toInt()
                        }
                    }
                    Pull(r, g, b)
                }
            )
        }

    data class Pull(val r: Int, val g: Int, val b: Int)

    data class Game(val id: Int, val pulls: List<Pull>)
}