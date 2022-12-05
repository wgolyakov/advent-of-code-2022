fun main() {
	fun parse(input: List<String>): Pair<List<MutableList<Char>>, List<Triple<Int, Int, Int>>> {
		val sep = input.indexOf("")
		val n = input[sep - 1].substringAfterLast(' ').toInt()
		val stacks = List(n) { mutableListOf<Char>() }
		for (s in input.subList(0, sep).reversed()) {
			for (i in 0 until n) {
				val ind = 1 + i * 4
				if (s.length <= ind) break
				val c = s[ind]
				if (c != ' ')
					stacks[i].add(c)
			}
		}
		val steps = mutableListOf<Triple<Int, Int, Int>>()
		for (s in input.subList(sep + 1, input.size)) {
			val (quantity, from, to) = Regex("move (\\d+) from (\\d+) to (\\d+)").matchEntire(s)!!.groupValues
				.takeLast(3).map { it.toInt() }
			steps.add(Triple(quantity, from - 1, to - 1))
		}
		return (stacks to steps)
	}

	fun part1(input: List<String>): String {
		val (stacks, steps) = parse(input)
		for ((quantity, from, to) in steps)
			for (i in 0 until quantity)
				stacks[to].add(stacks[from].removeLast())
		return stacks.map { it.last() }.toCharArray().concatToString()
	}

	fun part2(input: List<String>): String {
		val (stacks, steps) = parse(input)
		for ((quantity, from, to) in steps)
			for (i in quantity downTo 1)
				stacks[to].add(stacks[from].removeAt(stacks[from].size - i))
		return stacks.map { it.last() }.toCharArray().concatToString()
	}

	val testInput = readInput("Day05_test")
	check(part1(testInput) == "CMZ")
	check(part2(testInput) == "MCD")

	val input = readInput("Day05")
	println(part1(input))
	println(part2(input))
}
