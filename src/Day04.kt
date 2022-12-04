fun main() {
	fun part1(input: List<String>): Int {
		return input.count {
			val (r1, r2) = it.split(',')
				.map { r -> r.split('-').map(String::toInt).let { (a, b) -> a..b } }
			(r1.first in r2 && r1.last in r2) || (r2.first in r1 && r2.last in r1)
		}
	}

	fun part2(input: List<String>): Int {
		return input.count {
			val (r1, r2) = it.split(',')
				.map { r -> r.split('-').map(String::toInt).let { (a, b) -> a..b } }
			r1.first in r2 || r1.last in r2 || r2.first in r1 || r2.last in r1
		}
	}

	val testInput = readInput("Day04_test")
	check(part1(testInput) == 2)
	check(part2(testInput) == 4)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}
