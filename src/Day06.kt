fun main() {
    fun getBoatRaces(input: List<String>): List<BoatRace> {
        val (timeRow, distanceRow) = input.map { it.removeDuplicateWhiteSpaces() }
        val times = timeRow.split(" ").let {
            it.subList(1, it.size)
        }
        val distances = distanceRow.split(" ").let {
            it.subList(1, it.size)
        }
        return times.zip(distances).map {
            BoatRace(it.first.toLong(), it.second.toLong())
        }
    }

    fun part1(input: List<String>): Int {
        return getBoatRaces(input).map {
            it.getAllRecordScenarios().size
        }.fold(1) { acc: Int, i: Int ->
            acc * i
        }
    }

    fun getBoatRace(input: List<String>): BoatRace {
        val (timeRow, distanceRow) = input.map { it.removeDuplicateWhiteSpaces() }
        val time = timeRow.split(" ").let {
            it.subList(1, it.size)
        }.joinToString("").toLong()
        val distance = distanceRow.split(" ").let {
            it.subList(1, it.size)
        }.joinToString("").toLong()
        return BoatRace(time, distance)
    }

    fun part2(input: List<String>): Int {
        val race = getBoatRace(input)
        return race.getAllRecordScenariosOptimized().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    println("# TEST # ")
    val testResult = part2(testInput)
    testResult.println()
    check(testResult == 71503)

    val input = readInput("Day06")
    println("INPUT")
    part2(input).println()
}

data class BoatRace(
    val durationMs: Long,
    val distanceMillimeters: Long
) {
    fun getAllRecordScenarios(): List<Long> {
        return (0..durationMs).map {
            it * (durationMs - it)
        }.filter {
            it > distanceMillimeters
        }.also {
            it.println()
        }
    }

    /**
     * Optimized version introduced for part 2
     */
    fun getAllRecordScenariosOptimized(): List<Long> {
        val firstWin = (0..durationMs).first { it * (durationMs - it) > distanceMillimeters }
        val lastWin = (durationMs downTo  0).first { it * (durationMs - it) > distanceMillimeters }
        return (firstWin..lastWin).toList()
    }
}
