import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
	fun part1(input: List<String>): Int {
		var x = 0
		var y = 0
		var a = 0
		var b = 0
		val tailPositions = mutableSetOf(a to b)
		for (line in input) {
			val direction = line[0]
			val steps = line.substring(2).toInt()
			for (s in 0 until steps) {
				when (direction) {
					'R' -> x++
					'L' -> x--
					'U' -> y++
					'D' -> y--
				}
				if ((x - a).absoluteValue > 1 || (y - b).absoluteValue > 1) {
					a += (x - a).sign
					b += (y - b).sign
				}
				tailPositions.add(a to b)
			}
		}
		return tailPositions.size
	}

	fun part2(input: List<String>): Int {
		val x = IntArray(10)
		val y = IntArray(10)
		val tailPositions = mutableSetOf(x.last() to y.last())
		for (line in input) {
			val direction = line[0]
			val steps = line.substring(2).toInt()
			for (s in 0 until steps) {
				when (direction) {
					'R' -> x[0]++
					'L' -> x[0]--
					'U' -> y[0]++
					'D' -> y[0]--
				}
				for ((i, j) in (0..9).windowed(2)) {
					if ((x[i] - x[j]).absoluteValue > 1 || (y[i] - y[j]).absoluteValue > 1) {
						x[j] += (x[i] - x[j]).sign
						y[j] += (y[i] - y[j]).sign
					}
				}
				tailPositions.add(x.last() to y.last())
			}
		}
		return tailPositions.size
	}

	val testInput = readInput("Day09_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 1)
	val testInput2 = readInput("Day09_test2")
	check(part2(testInput2) == 36)

	val input = readInput("Day09")
	println(part1(input))
	println(part2(input))
}
