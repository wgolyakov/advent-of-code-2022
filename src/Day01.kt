fun main() {
	fun part1(input: String) = input.split(System.lineSeparator().repeat(2))
		.maxOf { it.split(System.lineSeparator()).sumOf(String::toInt) }

	fun part2(input: String) = input.split(System.lineSeparator().repeat(2))
		.map { it.split(System.lineSeparator()).sumOf(String::toInt) }.sorted().takeLast(3).sum()

	val testInput = readFile("Day01_test")
	check(part1(testInput) == 24000)
	check(part2(testInput) == 45000)

	val input = readFile("Day01")
	println(part1(input))
	println(part2(input))
}
