fun main() {
	fun parse(input: List<String>): Map<List<String>, Int> {
		val dirSizes = mutableMapOf<List<String>, Int>()
		val path = mutableListOf<String>()
		for (line in input) {
			if (line[0] == '$') {
				if (line.startsWith("\$ cd ")) {
					val dir = line.substringAfter("\$ cd ")
					if (dir == "..")
						path.removeLast()
					else
						path.add(dir)
				}
			} else {
				if (!line.startsWith("dir ")) {
					val fileSize = line.substringBefore(' ').toInt()
					for (i in path.indices) {
						val dir = path.subList(0, i + 1).toList()
						dirSizes[dir] = (dirSizes[dir] ?: 0) + fileSize
					}
				}
			}
		}
		return dirSizes
	}

	fun part1(input: List<String>) = parse(input).values.filter { it <= 100000 }.sum()

	fun part2(input: List<String>): Int {
		val dirSizes = parse(input)
		val used = dirSizes[listOf("/")] ?: 0
		val unused = 70000000 - used
		val toDelete = 30000000 - unused
		return dirSizes.values.filter { it >= toDelete }.min()
	}

	val testInput = readInput("Day07_test")
	check(part1(testInput) == 95437)
	check(part2(testInput) == 24933642)

	val input = readInput("Day07")
	println(part1(input))
	println(part2(input))
}
