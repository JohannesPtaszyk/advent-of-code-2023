import java.lang.IllegalStateException

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val digits = line.filter {
                it.isDigit()
            }
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val numbers = listOf(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
        )
        return input.sumOf { line ->
            val firstNumberString = numbers.map {
                line.indexOf(it) to it
            }.filter {
                it.first != -1
            }.minByOrNull {
                it.first
            }

            val firstDigit = line.firstOrNull {
                it.isDigit()
            }?.let {
                line.indexOf(it) to it
            }

            val lastNumberString = numbers.map {
                line.lastIndexOf(it) to it
            }.filter {
                it.first != -1
            }.maxByOrNull {
                it.first
            }

            val lastDigit = line.lastOrNull {
                it.isDigit()
            }?.let {
                line.lastIndexOf(it) to it
            }

            val first = when {
                firstNumberString != null && firstDigit != null -> {
                    if(firstNumberString.first < firstDigit.first) {
                        numbers.indexOf(firstNumberString.second) + 1
                    } else {
                        firstDigit.second
                    }
                }
                firstNumberString == null && firstDigit != null -> {
                    firstDigit.second
                }
                firstNumberString != null && firstDigit == null -> {
                    numbers.indexOf(firstNumberString.second) + 1
                }
                else -> error("No first found")
            }

            val second = when {
                lastNumberString != null && lastDigit != null -> {
                    if(lastNumberString.first > lastDigit.first) {
                        numbers.indexOf(lastNumberString.second) + 1
                    } else {
                        lastDigit.second
                    }
                }
                lastNumberString == null && lastDigit != null -> {
                    lastDigit.second
                }
                lastNumberString != null && lastDigit == null -> {
                    numbers.indexOf(lastNumberString.second) + 1
                }
                else -> error("No second found")
            }
            "$first$second".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println("TEST")
    part2(testInput).println()
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    println("INPUT")
    part2(input).println()
}
