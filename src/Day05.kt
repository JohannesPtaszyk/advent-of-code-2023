import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main() {
    fun getAlmanac(input: List<String>): Almanac {
        return input.fold(Almanac(emptyList(), emptyList())) { almanac, line: String ->
            when {
                line.isBlank() -> almanac
                line.startsWith("seeds") -> {
                    val seeds = line.split(":")
                        .last()
                        .trim()
                        .split(" ")
                        .map { it.toLong() }
                    almanac.copy(seeds = seeds)
                }

                line.contains("map") -> {
                    val name = line.split(" map:").first()
                    val (source, destination) = name.split("-to-")
                    val mapping = Almanac.Mapping(
                        name = name,
                        sourceCategory = source,
                        destinationCategory = destination,
                        entries = emptyList(),
                    )
                    almanac.copy(mappings = almanac.mappings + mapping)
                }

                else -> {
                    val (
                        destinationRangeStart,
                        sourceRangeStart,
                        rangeLength
                    ) = line.split(" ").map { it.toLong() }
                    val entry = Almanac.Mapping.Entry(
                        destinationRangeStart,
                        sourceRangeStart,
                        rangeLength.toInt()
                    )
                    val updatedMaps = almanac.mappings.toMutableList()
                    val updatedMap = updatedMaps.removeLast().let {
                        it.copy(entries = it.entries + entry)
                    }
                    updatedMaps.add(updatedMap)
                    almanac.copy(mappings = updatedMaps)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val almanac = getAlmanac(input)
        var currentMap: Almanac.Mapping? = almanac.mappings.first()
        var mapping = almanac.seeds
        do {
            mapping = mapping.map { from ->
                // First I tried to generate a mapping based on zipping source and destination ranges
                // resulting in overflow.
                // Then i tried to only get the index in range to then map it to another range for destination.
                // That resulted in terrible performance due to iterating through huge lists.
                // Finally replacing with basic calculations got me from ~9 seconds to 5ms per run.
                currentMap!!.entries.forEach {
                    if (from in it.sourceRange) {
                        val offset = from - it.sourceRangeStart
                        return@map it.destinationRangeStart + offset
                    }
                }
                from
            }
            currentMap = almanac.mappings.find { it.sourceCategory == currentMap!!.destinationCategory }
        } while (currentMap != null)
        return mapping.min().toInt()
    }

    fun part2(input: List<String>): Long {
        val almanac = getAlmanac(input)
        val seedRanges = almanac.seeds.chunked(2) { chunked ->
            val start = chunked.first()
            val end = start + chunked.last()
            start..<end
        }
        // TODO: Map ranges based on start and end, instead of one item at a time
        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    println("# TEST # ")
    val testResult = part2(testInput)
    testResult.println()
    check(testResult == 46L)

    val input = readInput("Day05")
    println("INPUT")
    part2(input).println()
}

data class Almanac(
    val seeds: List<Long>,
    val mappings: List<Mapping>
) {
    data class Mapping(
        val name: String,
        val sourceCategory: String,
        val destinationCategory: String,
        val entries: List<Entry>
    ) {
        data class Entry(
            val destinationRangeStart: Long,
            val sourceRangeStart: Long,
            val rangeLength: Int,
        ) {
            val sourceRange = sourceRangeStart..(sourceRangeStart + rangeLength)
        }
    }
}
