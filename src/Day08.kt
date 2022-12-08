fun main() {
	fun parse(input: List<String>): List<List<Int>> {
		val grid = mutableListOf<List<Int>>()
		for (line in input)
			grid.add(line.map { it.digitToInt() })
		return grid
	}

	fun part1(input: List<String>): Int {
		val grid = parse(input)
		val h = grid.size
		val w = grid[0].size
		val visible = Array(h) { BooleanArray(w) }
		var max = -1

		fun checkTree(i: Int, j: Int) {
			if (grid[i][j] > max) visible[i][j] = true
			if (max < grid[i][j]) max = grid[i][j]
		}

		for (i in 0 until h) {
			max = -1
			for (j in 0 until w) checkTree(i, j)
			max = -1
			for (j in w - 1 downTo 0) checkTree(i, j)
		}
		for (j in 0 until w) {
			max = -1
			for (i in 0 until h) checkTree(i, j)
			max = -1
			for (i in h - 1 downTo 0) checkTree(i, j)
		}
		return visible.sumOf { line -> line.count { it } }
	}

	fun part2(input: List<String>): Int {
		val grid = parse(input)
		val h = grid.size
		val w = grid[0].size
		val score = Array(h) { IntArray(w) { 1 } }
		var distance = IntArray(10)

		fun checkTree(i: Int, j: Int) {
			score[i][j] *= distance[grid[i][j]]
			for (k in 0..9)
				if (k <= grid[i][j])
					distance[k] = 1
				else
					distance[k]++
		}

		for (i in 0 until h) {
			distance = IntArray(10)
			for (j in 0 until w) checkTree(i, j)
			distance = IntArray(10)
			for (j in w - 1 downTo 0) checkTree(i, j)
		}
		for (j in 0 until w) {
			distance = IntArray(10)
			for (i in 0 until h) checkTree(i, j)
			distance = IntArray(10)
			for (i in h - 1 downTo 0) checkTree(i, j)
		}
		return score.maxOf { it.max() }
	}

	val testInput = readInput("Day08_test")
	check(part1(testInput) == 21)
	check(part2(testInput) == 8)

	val input = readInput("Day08")
	println(part1(input))
	println(part2(input))
}
