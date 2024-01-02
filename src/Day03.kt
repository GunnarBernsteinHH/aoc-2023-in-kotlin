/*
* --- Day 3: Gear Ratios ---
* Source: https://adventofcode.com/2023/day/3

*/

/** main function containing sub-functions like a class */
fun main() {

    data class NumberData(
        val value: Int,
        val start: Int,
        val end: Int,
        var isAdjacent: Boolean = false
    )

    class LineData(line: String) {

        // normally this would be immutable lists
        val numbers = mutableListOf<NumberData>()
        val symbolMap = mutableMapOf<Int, Char>()

        // parse line
        init {
            var isNumber = false
            var start = 0
            line.forEachIndexed { pos, c ->
                if (c != '.') {
                    if (c.isDigit()) {
                        if (!isNumber) {
                            isNumber = true
                            start = pos
                        }
                    } else {
                        // is a symbol
                        if (isNumber) {
                            isNumber = false
                            numbers.add(NumberData(line.substring(start, pos).toInt(), start, pos))
                        }
                        symbolMap[pos] = c
                    }
                } else {
                    if (isNumber) {
                        isNumber = false
                        numbers.add(NumberData(line.substring(start, pos).toInt(), start, pos))
                    }
                }
            }
            if (isNumber) {
                // isNumber = false
                numbers.add(NumberData(line.substring(start).toInt(), start, line.length))
            }
        }

    }

    val data = mutableListOf<LineData>()
    fun part1(input: List<String>): Int {

        fun checkNumberIsAdjacentSymbol(numbers: List<NumberData>, posSymbol: Int) {
            numbers.forEach { number ->
                if (number.start <= posSymbol + 1 && number.end >= posSymbol)
                    number.isAdjacent = true
            }
        }

        // parse input into objects
        input.forEach { s ->
            val lineData = LineData(s)
            data.add(lineData)
        }
        // find relevant numbers
        data.forEachIndexed { lineNo, line ->
            line.symbolMap.keys.forEach { pos ->
                if (lineNo > 0) {
                    checkNumberIsAdjacentSymbol(data[lineNo - 1].numbers, pos)
                }
                checkNumberIsAdjacentSymbol(data[lineNo].numbers, pos)
                if (lineNo < data.size - 1) {
                    checkNumberIsAdjacentSymbol(data[lineNo + 1].numbers, pos)
                }
            }
        }
        // add adjacent numbers
        var total = 0
        data.forEach { line ->
            total += line.numbers.filter { it.isAdjacent }.sumOf { it.value }
        }
        return total
    }

    // relies on data
    data class GearNumber (val numberValue1: Int, var numberValue2: Int, val lineNo: Int, val posSymbol: Int, var gearCount: Int )
    fun part2(): Int {
        val gears = mutableListOf<GearNumber>()

        fun checkNumberIsPotentialGear(numbers: List<NumberData>, posSymbol: Int, lineNo: Int) {
            numbers.forEach { number ->
                if (number.start <= posSymbol + 1 && number.end >= posSymbol) {
                    // is potential gear
                    val g = gears.filter { it.lineNo == lineNo && it.posSymbol == posSymbol }
                    if (g.isEmpty()) {
                        gears.add(GearNumber( number.value, 0, lineNo, posSymbol, 1))
                    } else {
                        g[0].gearCount++
                        if (g[0].gearCount == 2) {
                            g[0].numberValue2 = number.value
                        }
                    }
                }
            }
        }

        // find relevant gears: two numbers adjacent to same '*'
        data.forEachIndexed { lineNo, line ->
            line.symbolMap.forEach { (pos, symbol) ->
                if (symbol == '*') {
                    if (lineNo > 0) {
                        // no need to check all numbers, only those which are adjacent are candidates
                        checkNumberIsPotentialGear(data[lineNo - 1].numbers.filter { it.isAdjacent }, pos, lineNo)
                    }
                    checkNumberIsPotentialGear(data[lineNo].numbers.filter { it.isAdjacent }, pos, lineNo)
                    if (lineNo < data.size - 1) {
                        checkNumberIsPotentialGear(data[lineNo + 1].numbers.filter { it.isAdjacent }, pos, lineNo)
                    }
                }
            }
        }
        // add gears
        var total = 0
        gears.filter { it.gearCount == 2 }.forEach { gear ->
            total += gear.numberValue1 * gear.numberValue2
        }
        return total
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("input.day02.test1.txt")
//    check(part2(testInput) == 840)

    val input = readInput("input.day03.txt")
    println("Results for 2023, day 3:")
    println("Result of part 1: " + part1(input))
    println("Result of part 2: " + part2())
}
