/*
* --- Day 1: Trebuchet?! ---
* Source: https://adventofcode.com/2023/day/1
*
*/

fun main() {

    // val numbersAsString = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val numberMap = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach { line ->
            // line.println()
            val firstValue: Int = line.first { it.isDigit() }.digitToInt() * 10
            val lastValue: Int = line.last { it.isDigit() }.digitToInt()
            sum += firstValue + lastValue
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        // create reversed map for reversed search of number words
        val numberMapReversed = mutableMapOf<String, Int>()
        numberMap.forEach { (s, i) -> numberMapReversed[s.reversed()] = i }
        // create list of map (possible performance gain)
        val numbersAsString = numberMap.keys
        val numbersAsStringReversed = numberMapReversed.keys

        // create new list which can be evaluated by part1 (digit only) function
        val reworkedInput = mutableListOf<String>()
        input.forEach { line ->
            // find first number word
            val found = line.findAnyOf(numbersAsString,0,true)
            // find last number word
            val foundReversed = line.reversed().findAnyOf(numbersAsStringReversed,0,true)
            // insert digits in front of word
            val lineNew = StringBuilder(line)
            foundReversed?.let { lineNew.insert(line.length - foundReversed.first, numberMapReversed[foundReversed.second]) }
            found?.let { lineNew.insert(found.first, numberMap[found.second]) }
            reworkedInput.add(lineNew.toString())
        }
        return part1(reworkedInput)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("input.day01.txt")
    println("Results for 2023, day 1:")
    println("Result of part 1: " + part1(input))
    println("Result of part 2: " + part2(input))
}
