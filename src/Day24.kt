fun main() {
	class Cell {
		val obstacles = mutableListOf<Char>()
		var distance = -1
	}

	class Point(val x: Int, val y: Int, val t: Int)

	fun parse(input: List<String>): List<List<Cell>> {
		val map = List(input.size) { List(input[0].length) { Cell() } }
		for (y in input.indices)
			for (x in input[y].indices)
				if (input[y][x] != '.') map[y][x].obstacles.add(input[y][x])
		return map
	}

	val positionToMove = mapOf(
		'#' to (0 to 0),
		'^' to (0 to -1),
		'v' to (0 to 1),
		'<' to (-1 to 0),
		'>' to (1 to 0),
	)

	fun addFrame(mapInTime: MutableList<List<List<Cell>>>) {
		val curr = mapInTime.last()
		val next = List(curr.size) { List(curr[0].size) { Cell() } }
		for (y in curr.indices) {
			for (x in curr[y].indices) {
				for (c in curr[y][x].obstacles) {
					val (dx, dy) = positionToMove[c]!!
					var nx = x + dx
					var ny = y + dy
					if (c != '#') {
						if (nx <= 0) nx = curr[y].size - 2
						if (nx >= curr[y].size - 1) nx = 1
						if (ny <= 0) ny = curr.size - 2
						if (ny >= curr.size - 1) ny = 1
					}
					next[ny][nx].obstacles.add(c)
				}
			}
		}
		mapInTime.add(next)
	}

	fun trip(map: List<List<Cell>>, start: Point, stop: Point): List<List<Cell>>? {
		val mapInTime = mutableListOf(map)
		val queue = ArrayDeque<Point>()
		queue.addLast(start)
		while (queue.isNotEmpty()) {
			val curr = queue.removeFirst()
			if (curr.x == stop.x && curr.y == stop.y)
				return mapInTime[curr.t]
			if (mapInTime.size <= curr.t + 1)
				addFrame(mapInTime)
			val neighbors = listOf(
				Point(curr.x + 1, curr.y, curr.t + 1),
				Point(curr.x - 1, curr.y, curr.t + 1),
				Point(curr.x, curr.y + 1, curr.t + 1),
				Point(curr.x, curr.y - 1, curr.t + 1),
				Point(curr.x, curr.y, curr.t + 1),
			).filter { it.x in map[0].indices && it.y in map.indices }
				.filter { mapInTime[it.t][it.y][it.x].obstacles.isEmpty() }
			for (next in neighbors) {
				if (mapInTime[next.t][next.y][next.x].distance == -1) {
					mapInTime[next.t][next.y][next.x].distance = mapInTime[curr.t][curr.y][curr.x].distance + 1
					queue.addLast(next)
				}
			}
		}
		return null
	}

	fun part1(input: List<String>): Int {
		var map = parse(input)
		val start = Point(1, 0, 0)
		val stop =  Point(map[0].size - 2, map.size - 1, 0)
		map[start.y][start.x].distance = 0
		map = trip(map, start, stop) ?: return -1
		return map[stop.y][stop.x].distance
	}

	fun part2(input: List<String>): Int {
		var map = parse(input)
		val start = Point(1, 0, 0)
		val stop =  Point(map[0].size - 2, map.size - 1, 0)
		map[start.y][start.x].distance = 0
		map = trip(map, start, stop) ?: return -1
		map = trip(map, stop, start) ?: return -1
		map = trip(map, start, stop) ?: return -1
		return map[stop.y][stop.x].distance
	}

	val testInput = readInput("Day24_test")
	check(part1(testInput) == 18)
	check(part2(testInput) == 54)
	val input = readInput("Day24")
	println(part1(input))
	println(part2(input))
}
