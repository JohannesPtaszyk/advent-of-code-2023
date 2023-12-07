fun main() {
    fun part1(input: List<String>): Int {
        val hands = input.map(Hand::fromString).sortedWith { first: Hand, second: Hand ->
            val typeComparison = first.type.strength.compareTo(second.type.strength)
            if(typeComparison != 0) {
                typeComparison
            } else {
                first.cards.zip(second.cards)
                    .map {
                        cards1.indexOf(it.first).compareTo(cards1.indexOf(it.second))
                    }
                    .firstOrNull { it != 0 } ?: 0
            }
        }
        hands.forEach {
           it.println()
        }
        return hands.foldIndexed(0) { index: Int, acc: Int, hand: Hand ->
            acc + ((index + 1) * hand.bid)
        }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map(Hand::fromStringV2).sortedWith { first: Hand, second: Hand ->
            val typeComparison = first.type.strength.compareTo(second.type.strength)
            if(typeComparison != 0) {
                typeComparison
            } else {
                first.cards.zip(second.cards)
                    .map {
                        cards2.indexOf(it.first).compareTo(cards2.indexOf(it.second))
                    }
                    .firstOrNull { it != 0 } ?: 0
            }
        }
        hands.forEach {
            it.println()
        }
        return hands.foldIndexed(0) { index: Int, acc: Int, hand: Hand ->
            acc + ((index + 1) * hand.bid)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    println("# TEST # ")
    val testResult = part2(testInput)
    testResult.println()
    check(testResult == 5905)

    val input = readInput("Day07")
    println("INPUT")
    part2(input).println()
}

val cards1 = (2..9).map(Int::digitToChar) + listOf('T', 'J', 'Q', 'K', 'A')

val cards2 = listOf('J') + (2..9).map(Int::digitToChar) + listOf('T', 'Q', 'K', 'A')

fun List<Char>.group(): Collection<List<Char>> {
    return groupBy { it }.values
}

data class Hand(val cards: List<Char>, val type: Type, val bid: Int) {

    enum class Type(
        val strength: Int,
        val rule: (cards: List<Char>) -> Boolean,
    ) {
        FIVE_OF_A_KIND(
            strength = 5,
            rule = { cards ->
                cards.all { it == cards.first() }
            },
        ),
        FOUR_OF_A_KIND(
            strength = 4,
            rule = { cards ->
                cards.group().any { it.size == 4 }
            },
        ),
        FULL_HOUSE(
            strength = 3,
            rule = { cards ->
                val groups = cards.group()
                groups.any { it.size == 3 } && groups.any { it.size == 2 }
            },
        ),
        THREE_OF_A_KIND(
            strength = 2,
            rule = { cards ->
                val groups = cards.group()
                groups.any { it.size == 3 } && groups.none { it.size == 2 }
            },
        ),
        TWO_PAIR(
            strength = 1,
            rule = { cards ->
                cards.group().filter { it.size == 2 }.size == 2
            },
        ),
        ONE_PAIR(
            strength = 0,
            rule = { cards ->
                cards.group().filter { it.size == 2 }.size == 1
            },
        ),
        HIGH_CARD(
            strength = -1,
            rule = { cards ->
                cards.group().all { it.size == 1 }
            },
        )
    }

    companion object {
        fun fromString(input: String): Hand {
            val (cardInput, bidInput) = input.split(" ")
            val cards = cardInput.toCharArray().asList()
            val bid = bidInput.toInt()
            val type = Type.entries.find { it.rule(cards) }
            return Hand(
                cards = cards,
                type = type ?: error("No Type for hand found"),
                bid = bid,
            )
        }

        fun fromStringV2(input: String): Hand {
            val (cardInput, bidInput) = input.split(" ")
            val cards = cardInput.toCharArray().asList()
            val bid = bidInput.toInt()
            val bestType = buildList {
                cards.forEach { jokerReplacement ->
                    add(cards.map {
                        if(it == 'J'){
                            jokerReplacement
                        } else {
                            it
                        }
                    })
                }
            }.map {
                Type.entries.find { type ->
                    type.rule(it)
                } ?: error("No Type for hand found")
            }.maxBy { it.strength }
            return Hand(
                cards = cards,
                type = bestType,
                bid = bid,
            )
        }
    }
}
