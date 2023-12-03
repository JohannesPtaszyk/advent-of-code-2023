fun main() {
    fun parseSchematic(input: List<String>) = input.flatMapIndexed { x: Int, string: String ->
        val parts: MutableList<Part> = mutableListOf()
        string.forEachIndexed { y, char ->
            val coordinate = Coordinate(x, y)
            when {
                char.isDigit() -> {
                    val currentNumber = parts.lastOrNull() as? Part.Number
                    if (currentNumber != null) parts.removeLast()
                    val number = currentNumber?.let {
                        it.copy(
                            value = "${it.value}$char".toInt(),
                            coordinates = it.coordinates + Coordinate(x, y)
                        )
                    } ?: Part.Number(char.digitToInt(), listOf(Coordinate(x, y)))
                    parts.add(number)
                }

                char == '.' -> parts.add(Part.Dot(coordinate))
                else -> parts.add(Part.Symbol(char, coordinate))
            }
        }
        parts
    }

    fun part1(input: List<String>): Int {
        val schematic = parseSchematic(input)
        val numbers = schematic.filterIsInstance<Part.Number>()
        val symbols = schematic.filterIsInstance<Part.Symbol>()

        return numbers.filter { number ->
            symbols.find { number.adjacentCoordinates.contains(it.coordinate) } != null
        }.map {
            it.value
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val schematic = parseSchematic(input)
        val numbers = schematic.filterIsInstance<Part.Number>()
        val symbols = schematic.filterIsInstance<Part.Symbol>()

        return symbols.filter {
            it.isGearSymbol()
        }.sumOf { part ->
            numbers.filter {
                it.adjacentCoordinates.contains(part.coordinate)
            }.takeIf { it.size == 2 }?.fold(1) { value, i ->
                value * i.value
            }?.toInt() ?: 0
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    println("# TEST # ")
    val testResult = part2(testInput)
    testResult.println()
    check(testResult == 467835)

    val input = readInput("Day03")
    println("INPUT")
    part2(input).println()
}

data class Coordinate(val x: Int, val y: Int)

val Coordinate.adjacentCoordinates: Set<Coordinate>
    get() = setOf(
        copy(x = x - 1, y = y - 1),
        copy(x = x - 1, y = y),
        copy(x = x - 1, y = y + 1),
        copy(x = x, y = y - 1),
        copy(x = x, y = y + 1),
        copy(x = x + 1, y = y - 1),
        copy(x = x + 1, y = y),
        copy(x = x + 1, y = y + 1),
    )

val Part.Number.adjacentCoordinates: List<Coordinate>
    get() = coordinates.flatMap {
        it.adjacentCoordinates
    }.toSet().filter {
        !coordinates.contains(it)
    }


sealed interface Part {
    data class Number(val value: Int, val coordinates: List<Coordinate>) : Part
    data class Symbol(val value: Char, val coordinate: Coordinate) : Part {
        fun isGearSymbol() = value == '*'
    }

    data class Dot(val coordinate: Coordinate) : Part
}