/*
* --- Day 2: Cube Conundrum ---
* Source: https://adventofcode.com/2023/day/2

*/

/** main function containing sub-functions like a class */
fun main() {

    data class CubeSet(val red: Int, val green: Int, val blue: Int)
    data class Game(val id: Int, val cubeSets: List<CubeSet>)

    /** Parse input and return a list of class Game with their cube throws */
    fun parse(input: List<String>) = input.map { line ->
        // First Split String to separate Game and cube throws
        // Game 1: 7 red, 14 blue; 2 blue, 3 red, 3 green; 4 green, 12 blue, 15 red; 3 green, 12 blue, 3 red; 11 red, 2 green
        // interesting, one can split in destructive declaration
        val (game, cubeSets) = line.split(":")
        // use regex to get game id
        val id = "Game (\\d+)".toRegex().find(game)!!.groupValues[1].toInt()
        // use regex to get set cubes
        // interesting part is the map function to create a list
        cubeSets.split(";").map { cubeSet ->
            val red = "(\\d+) red".toRegex().find(cubeSet)?.groupValues?.get(1)?.toInt() ?: 0
            val green = "(\\d+) green".toRegex().find(cubeSet)?.groupValues?.get(1)?.toInt() ?: 0
            val blue = "(\\d+) blue".toRegex().find(cubeSet)?.groupValues?.get(1)?.toInt() ?: 0
            CubeSet(red, green, blue)
        }.let {
            Game(id, it)
        }

    }

    /* Solution from Artem Khvastunov, Kotlin Advent of Code
    * https://www.youtube.com/watch?v=CCr6yWMkiZU&list=PLlFc5cFwUnmzk0wvYW4aTl57F2VNkFisU&index=2 */
    fun part1(input: List<String>): Int {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        val games = parse (input)
        val sumOfGameIds = games.filter {
            // filter games not possible with the number of cubes
            it.cubeSets.none {
                it.red > maxRed || it.green > maxGreen || it.blue > maxBlue
            }
        }.sumOf { it.id } // sum ids of the game as required result
        return sumOfGameIds
    }

    fun part2(input: List<String>): Int {
        val games = parse (input)
        var result = 0
        games.forEach {game ->
            var redRequired = 0
            var greenRequired = 0
            var blueRequired = 0
            game.cubeSets.forEach {
                if (redRequired < it.red) redRequired = it.red
                if (greenRequired < it.green) greenRequired = it.green
                if (blueRequired < it.blue) blueRequired = it.blue
            }
            result += redRequired * greenRequired * blueRequired
        }
        return result
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("input.day02.test1.txt")
    check(part2(testInput) == 840)

    val input = readInput("input.day02.txt")
    println("Results for 2023, day 2:")
    println("Result of part 1: " + part1(input))
    println("Result of part 2: " + part2(input))
}
