fun main() {
	fun part1(input: List<String>): Int {
		return input.sumOf {
			val a = it.substring(0, it.length / 2).toSet()
			val b = it.substring(it.length / 2, it.length).toSet()
			val c = a.intersect(b).first()
			c.code + if (c.isLowerCase()) 1 - 'a'.code else 27 - 'A'.code
		}
	}

	fun part2(input: List<String>): Int {
		return input.windowed(3, 3).sumOf { (s1, s2, s3) ->
			val c = s1.toSet().intersect(s2.toSet()).intersect(s3.toSet()).first()
			c.code + if (c.isLowerCase()) 1 - 'a'.code else 27 - 'A'.code
		}
	}

	val testInput = readInput("Day03_test")
	check(part1(testInput) == 157)
	check(part2(testInput) == 70)

	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}
