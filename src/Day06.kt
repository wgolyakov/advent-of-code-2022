fun main() {
	fun findMarker(buffer: String, n: Int) = buffer.toList().windowed(n).withIndex()
		.find { (_, s) -> s.toSet().size == n }!!.index + n

	fun part1(input: String) = findMarker(input, 4)

	fun part2(input: String) = findMarker(input, 14)

	val testInput = readFile("Day06_test")
	check(part1(testInput) == 7)
	check(part2(testInput) == 19)

	val input = readFile("Day06")
	println(part1(input))
	println(part2(input))
}
