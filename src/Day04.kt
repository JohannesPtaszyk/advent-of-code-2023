fun main() {
    fun getCards(input: List<String>) = input.map { cardInput ->
        val (name, input) = cardInput.split(":").map { it.trim() }
        val (winningNumberInput, selectedNumberInput) = input.split("|").map { it.trim() }
        val winningNumbers = winningNumberInput.split(" ").mapNotNull { it.toIntOrNull() }
        val selectedNumbers = selectedNumberInput.split(" ").mapNotNull { it.toIntOrNull() }
        Card(name.split(" ").last().toInt(), winningNumbers, selectedNumbers)
    }

    fun part1(input: List<String>): Int {
        val cards: List<Card> = getCards(input)
        return cards.map { card ->
            card.calculatePoints().also {
                println("${card.id}: $it")
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val cards: List<Card> = getCards(input)
        val cardsWithDuplicates = cards.toMutableList()
        cards.forEach { card ->
            val winningCount = card.getSelectedWinningNumbers().size
            val first = cards.indexOfFirst { it.id == card.id } + 1
            val last = cards.indexOfLast { it.id == card.id + winningCount }
            val additionalCards = cards.subList(first, last + 1)
            val cardCount = cardsWithDuplicates.count { it.id == card.id }
            repeat(cardCount) {
                cardsWithDuplicates.addAll(additionalCards)
            }

        }
        return cardsWithDuplicates.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println("# TEST # ")
    val testResult = part2(testInput)
    testResult.println()
    check(testResult == 30)

    val input = readInput("Day04")
    println("INPUT")
    part2(input).println()
}

data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val selectedNumbers: List<Int>,
) {
    fun calculatePoints(): Int {
        return getSelectedWinningNumbers()
            .takeIf { it.isNotEmpty() }
            ?.dropLast(1)
            ?.fold(1) { acc, _ -> acc * 2 }
            ?: 0
    }

    fun getSelectedWinningNumbers() = selectedNumbers.filter { winningNumbers.contains(it) }
}
