fun main() {

    data class Cube(val color: String, val amount: Int)
    data class Game(val id: String, val sets: List<List<Cube>>)

    fun List<String>.mapToGames() = map {
        val (name, setInput) = it.split(":")
        val id = name.split(" ")[1]
        val sets = setInput.split(";").map { set ->
            set.split(",").map { cube ->
                val (amount, color) = cube.trim().split(" ").map { it.trim() }
                Cube(color, amount.toInt())
            }
        }
        Game(id, sets)
    }

    fun part1(input: List<String>): Int {
        val validAmounts = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )
        val possibleGames = input.mapToGames().filter { game ->
            val filteredSets = game.sets.filter { set ->
                val invalidCube = set.find {
                    val maxCount = validAmounts[it.color]!!
                    it.amount > maxCount
                }
                invalidCube != null
            }
            filteredSets.isEmpty()
        }
        return possibleGames.sumOf { game ->
            game.id.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.mapToGames().map { game ->
            val cubes = game.sets.flatMap { it }
            val colors = cubes.groupBy { it.color }
            val mins = colors.map { it.value.maxOf { it.amount } }
            val power = mins.fold(1) { value, i -> value * i }
            power
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    println("# TEST # ")
    val testResult = part2(testInput)
    testResult.println()
    check(testResult == 2286)

    val input = readInput("Day02")
    println("INPUT")
    part2(input).println()
}
