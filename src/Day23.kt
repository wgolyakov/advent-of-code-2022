private enum class Direction {
	NORTH, SOUTH, WEST, EAST
}

fun main() {
	fun parse(input: List<String>): Set<Pair<Int, Int>> {
		val elves = mutableSetOf<Pair<Int, Int>>()
		for (y in input.indices)
			for (x in input[y].indices)
				if (input[y][x] == '#') elves.add(x to y)
		return elves
	}

	val checkPositionsToStay = listOf(-1 to -1, 0 to -1, 1 to -1, -1 to 0, 1 to 0, -1 to 1, 0 to 1, 1 to 1)

	val directionsOrder = mapOf(
		Direction.NORTH to listOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST),
		Direction.SOUTH to listOf(Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.NORTH),
		Direction.WEST to listOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH),
		Direction.EAST to listOf(Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST),
	)

	val checkPositionsToMove = mapOf(
		Direction.NORTH to listOf(-1 to -1, 0 to -1, 1 to -1),
		Direction.SOUTH to listOf(-1 to 1, 0 to 1, 1 to 1),
		Direction.WEST to listOf(-1 to -1, -1 to 0, -1 to 1),
		Direction.EAST to listOf(1 to -1, 1 to 0, 1 to 1),
	)

	val positionToMove = mapOf(
		Direction.NORTH to (0 to -1),
		Direction.SOUTH to (0 to 1),
		Direction.WEST to (-1 to 0),
		Direction.EAST to (1 to 0),
	)

	val nextDirection = mapOf(
		Direction.NORTH to Direction.SOUTH,
		Direction.SOUTH to Direction.WEST,
		Direction.WEST to Direction.EAST,
		Direction.EAST to Direction.NORTH,
	)

	fun round(elves: Set<Pair<Int, Int>>, firstDirection: MutableList<Direction>): Set<Pair<Int, Int>> {
		// first half of round
		val nextElves = mutableSetOf<Pair<Int, Int>>()
		val propose = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()
		for ((x, y) in elves) {
			if (checkPositionsToStay.all { (dx, dy) -> x + dx to y + dy !in elves }) {
				nextElves.add(x to y)
			} else {
				var canMove = false
				for (direction in directionsOrder[firstDirection[0]]!!) {
					if (checkPositionsToMove[direction]!!.all { (dx, dy) -> x + dx to y + dy !in elves }) {
						val (dx, dy) = positionToMove[direction]!!
						propose.getOrPut(x + dx to y + dy) { mutableListOf() }.add(x to y)
						canMove = true
						break
					}
				}
				if (!canMove) nextElves.add(x to y)
			}
		}
		// second half of round
		for ((tile, fromList) in propose) {
			if (fromList.size == 1)
				nextElves.add(tile)
			else
				nextElves.addAll(fromList)
		}
		firstDirection[0] = nextDirection[firstDirection[0]]!!
		return nextElves
	}

	fun part1(input: List<String>): Int {
		var elves = parse(input)
		val firstDirection = mutableListOf(Direction.NORTH)
		for (round in 0 until 10)
			elves = round(elves, firstDirection)
		val xMin = elves.minOf { it.first }
		val xMax = elves.maxOf { it.first }
		val yMin = elves.minOf { it.second }
		val yMax = elves.maxOf { it.second }
		var emptyCount = 0
		for (x in xMin..xMax)
			for (y in yMin..yMax)
				if (x to y !in elves) emptyCount++
		return emptyCount
	}

	fun part2(input: List<String>): Int {
		var elves = parse(input)
		val firstDirection = mutableListOf(Direction.NORTH)
		var round = 0
		do {
			val nextElves = round(elves, firstDirection)
			round++
			val changed = nextElves != elves
			elves = nextElves
		} while (changed)
		return round
	}

	val testInput = readInput("Day23_test")
	check(part1(testInput) == 110)
	check(part2(testInput) == 20)
	val input = readInput("Day23")
	println(part1(input))
	println(part2(input))
}
