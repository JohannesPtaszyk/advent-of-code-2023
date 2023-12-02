import java.lang.IllegalStateException

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    println("# TEST # ")
    val testResult = part1(testInput)
    testResult.println()
    check(testResult == 281)

    val input = readInput("Day02")
    println("INPUT")
    part2(input).println()
}
