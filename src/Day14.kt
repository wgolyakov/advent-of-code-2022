import kotlin.math.min
import kotlin.math.max

fun main() {
	val sandStart = 500 to 0

	fun parse(input: List<String>): Triple<MutableSet<Pair<Int, Int>>, Pair<Int, Int>, Pair<Int, Int>> {
		val rocks = mutableSetOf<Pair<Int, Int>>()
		var minX = sandStart.first
		var minY = sandStart.second
		var maxX = sandStart.first
		var maxY = sandStart.second
		for (line in input) {
			val pairs = line.split(" -> ")
				.map { it.split(',').map(String::toInt).zipWithNext().single() }
			for (pair in pairs) {
				if (pair.first < minX) minX = pair.first
				if (pair.first > maxX) maxX = pair.first
				if (pair.second < minY) minY = pair.second
				if (pair.second > maxY) maxY = pair.second
			}
			for ((p1, p2) in pairs.windowed(2)) {
				if (p1.first == p2.first) {
					val x = p1.first
					for (y in min(p1.second, p2.second)..max(p1.second, p2.second))
						rocks.add(x to y)
				} else if (p1.second == p2.second) {
					val y = p1.second
					for (x in min(p1.first, p2.first)..max(p1.first, p2.first))
						rocks.add(x to y)
				} else error("Wrong input")
			}
		}
		return Triple(rocks, minX to minY, maxX to maxY)
	}

	fun part1(input: List<String>): Int {
		val (rocks, minPoint, maxPoint) = parse(input)
		var sandCount = 0
		var x = sandStart.first
		var y = sandStart.second
		while (true) {
			if (x to y + 1 !in rocks) {
				y++
			} else if (x - 1 to y + 1 !in rocks) {
				x--; y++
			} else if (x + 1 to y + 1 !in rocks) {
				x++; y++
			} else {
				rocks.add(x to y)
				sandCount++
				x = sandStart.first
				y = sandStart.second
			}
			if (x !in minPoint.first..maxPoint.first || y !in minPoint.second..maxPoint.second) {
				return sandCount
			}
		}
	}

	fun part2(input: List<String>): Int {
		val (rocks, _, maxPoint) = parse(input)
		var sandCount = 0
		var x = sandStart.first
		var y = sandStart.second
		while (true) {
			if (x to y + 1 !in rocks && y != maxPoint.second + 1) {
				y++
			} else if (x - 1 to y + 1 !in rocks && y != maxPoint.second + 1) {
				x--; y++
			} else if (x + 1 to y + 1 !in rocks && y != maxPoint.second + 1) {
				x++; y++
			} else {
				rocks.add(x to y)
				sandCount++
				if (x to y == sandStart) return sandCount
				x = sandStart.first
				y = sandStart.second
			}
		}
	}

	val testInput = readInput("Day14_test")
	check(part1(testInput) == 24)
	check(part2(testInput) == 93)
	val input = readInput("Day14")
	println(part1(input))
	println(part2(input))
}
