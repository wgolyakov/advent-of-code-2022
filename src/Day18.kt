fun main() {
	fun parse(input: List<String>) = input.map { it.split(',').map(String::toInt) }.toSet()

	val surfaces = listOf(
		listOf(1, 0, 0),
		listOf(-1, 0, 0),
		listOf(0, 1, 0),
		listOf(0, -1, 0),
		listOf(0, 0, 1),
		listOf(0, 0, -1),
	)

	fun part1(input: List<String>): Int {
		val cubes = parse(input)
		var unconnectedCount = 0
		for (cube in cubes) {
			for (surface in surfaces) {
				val neighbour = cube.zip(surface).map { it.first + it.second }
				if (neighbour !in cubes) unconnectedCount ++
			}
		}
		return unconnectedCount
	}

	fun part2(input: List<String>): Int {
		val cubes = parse(input)
		val minX = cubes.minOf { it[0] }
		val maxX = cubes.maxOf { it[0] }
		val minY = cubes.minOf { it[1] }
		val maxY = cubes.maxOf { it[1] }
		val minZ = cubes.minOf { it[2] }
		val maxZ = cubes.maxOf { it[2] }
		val rangeX = minX - 1..maxX + 1
		val rangeY = minY - 1..maxY + 1
		val rangeZ = minZ - 1..maxZ + 1
		val a = listOf(minX - 1, minY - 1, minZ - 1)
		val freeAir = mutableSetOf(a)
		val queue = ArrayDeque<List<Int>>()
		queue.addLast(a)
		while (queue.isNotEmpty()) {
			val curr = queue.removeFirst()
			val neighbours = surfaces.map { surface -> curr.zip(surface).map { it.first + it.second } }
				.filter { it[0] in rangeX && it[1] in rangeY && it[2] in rangeZ && it !in cubes }
			for (next in neighbours) {
				if (next !in freeAir) {
					freeAir.add(next)
					queue.addLast(next)
				}
			}
		}
		var unconnectedCount = 0
		for (cube in cubes) {
			for (surface in surfaces) {
				val neighbour = cube.zip(surface).map { it.first + it.second }
				if (neighbour in freeAir) unconnectedCount ++
			}
		}
		return unconnectedCount
	}

	val testInput = readInput("Day18_test")
	check(part1(testInput) == 64)
	check(part2(testInput) == 58)
	val input = readInput("Day18")
	println(part1(input))
	println(part2(input))
}
