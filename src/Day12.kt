fun main() {
	fun parse(input: List<String>): Triple<List<CharArray>, Pair<Int, Int>, Pair<Int, Int>> {
		var start = 0 to 0
		var stop = 0 to 0
		val map = List(input.size) { CharArray(input[0].length) }
		for (i in input.indices) {
			for (j in input[i].indices) {
				if (input[i][j] == 'S') {
					start = i to j
					map[i][j] = 'a'
				} else if (input[i][j] == 'E') {
					stop = i to j
					map[i][j] = 'z'
				} else {
					map[i][j] = input[i][j]
				}
			}
		}
		return Triple(map, start, stop)
	}

	fun part1(input: List<String>): Int {
		val (map, start, stop) = parse(input)
		val distanceMap = List(map.size) { MutableList(map[0].size) { -1 } }
		val queue = ArrayDeque<Pair<Int, Int>>()
		distanceMap[start.first][start.second] = 0
		queue.addLast(start)
		while (queue.isNotEmpty()) {
			val curr = queue.removeFirst()
			if (curr == stop)
				break
			val neighbors = listOf(
				curr.first + 1 to curr.second,
				curr.first to curr.second + 1,
				curr.first - 1 to curr.second,
				curr.first to curr.second - 1)
				.filter { it.first >= 0 && it.second >= 0 && it.first < map.size && it.second < map[0].size }
				.filter { map[it.first][it.second] - map[curr.first][curr.second] <= 1 }
			for (next in neighbors) {
				if (distanceMap[next.first][next.second] == -1) {
					distanceMap[next.first][next.second] = distanceMap[curr.first][curr.second] + 1
					queue.addLast(next)
				}
			}
		}
		return distanceMap[stop.first][stop.second]
	}

	fun part2(input: List<String>): Int {
		val (map, _, start) = parse(input)
		var minDistance = Int.MAX_VALUE
		val distanceMap = List(map.size) { MutableList(map[0].size) { -1 } }
		val queue = ArrayDeque<Pair<Int, Int>>()
		distanceMap[start.first][start.second] = 0
		queue.addLast(start)
		while (queue.isNotEmpty()) {
			val curr = queue.removeFirst()
			if (map[curr.first][curr.second] == 'a' && distanceMap[curr.first][curr.second] < minDistance)
				minDistance = distanceMap[curr.first][curr.second]
			val neighbors = listOf(
				curr.first + 1 to curr.second,
				curr.first to curr.second + 1,
				curr.first - 1 to curr.second,
				curr.first to curr.second - 1)
				.filter { it.first >= 0 && it.second >= 0 && it.first < map.size && it.second < map[0].size }
				.filter { map[curr.first][curr.second] - map[it.first][it.second] <= 1 }
			for (next in neighbors) {
				if (distanceMap[next.first][next.second] == -1) {
					distanceMap[next.first][next.second] = distanceMap[curr.first][curr.second] + 1
					queue.addLast(next)
				}
			}
		}
		return minDistance
	}

	val testInput = readInput("Day12_test")
	check(part1(testInput) == 31)
	check(part2(testInput) == 29)
	val input = readInput("Day12")
	println(part1(input))
	println(part2(input))
}
