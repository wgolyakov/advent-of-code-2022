import kotlin.math.min

fun main() {
	val right = 0
	val down = 1
	val left = 2
	val up = 3

	fun parseMap(input: List<String>): List<String> {
		val maxSize = input.take(input.size - 2).maxOf { it.length }
		val map = mutableListOf<String>()
		for (line in input.take(input.size - 2))
			map.add(line.padEnd(maxSize, ' '))
		return map
	}

	fun move1(map: List<String>, x: Int, y: Int, dx: Int, dy: Int): Pair<Int, Int> {
		var x2 = x + dx
		var y2 = y + dy
		if (x2 < 0) x2 = map[y].length - 1
		if (x2 >= map[y].length) x2 = 0
		if (y2 < 0) y2 = map.size - 1
		if (y2 >= map.size) y2 = 0
		return x2 to y2
	}

	fun part1(input: List<String>): Int {
		val map = parseMap(input)
		val path = input.last()
		var facing = right
		var x = map[0].indexOf('.')
		var y = 0
		var p = 0
		while (p < path.length) {
			when (path[p]) {
				'L' -> {
					if (facing == right) facing = up else facing--
					p++
				}
				'R' -> {
					if (facing == up) facing = right else facing++
					p++
				}
				else -> {
					val l = path.indexOf('L', p).let { if (it == -1) path.length else it }
					val r = path.indexOf('R', p).let { if (it == -1) path.length else it }
					val next = min(l, r)
					val distance = path.substring(p ,next).toInt()
					for (m in 0 until distance) {
						val (dx, dy) = when (facing) {
							right -> 1 to 0
							down -> 0 to 1
							left -> -1 to 0
							up -> 0 to -1
							else -> error("Unknown facing: $facing")
						}
						var t = move1(map, x, y, dx, dy)
						while (map[t.second][t.first] == ' ')
							t = move1(map, t.first, t.second, dx, dy)
						if (map[t.second][t.first] == '.') {
							x = t.first
							y = t.second
						} else {
							break
						}
					}
					p = next
				}
			}
		}
		return (y + 1) * 1000 + (x + 1) * 4 + facing
	}

	class Face(val num: Int) {
		val neighbours = Array(4) { this }
		var cx = -1
		var cy = -1
		var xTransform = Array<(Int, Int) -> Int>(4) { { x, y -> x } }
		var yTransform = Array<(Int, Int) -> Int>(4) { { x, y -> y } }
		var facingTransform = IntArray(4) { it }
	}

	fun createTestCube(): List<Face> {
		val a = 4
		val cube = List(6) { Face(it) }

		cube[0].cx = 2
		cube[0].cy = 0
		cube[0].neighbours[right] = cube[1]
		cube[0].facingTransform[right] = left
		cube[0].xTransform[right] = { x, y -> a * 4 - 1 }
		cube[0].yTransform[right] = { x, y -> a * 3 - 1 - y }
		cube[0].neighbours[down] = cube[4]
		cube[0].facingTransform[down] = down
		cube[0].xTransform[down] = { x, y -> x }
		cube[0].yTransform[down] = { x, y -> y + 1 }
		cube[0].neighbours[left] = cube[3]
		cube[0].facingTransform[left] = down
		cube[0].xTransform[left] = { x, y -> a + y }
		cube[0].yTransform[left] = { x, y -> a }
		cube[0].neighbours[up] = cube[5]
		cube[0].facingTransform[up] = down
		cube[0].xTransform[up] = { x, y -> a - 1 - (x - a * 2) }
		cube[0].yTransform[up] = { x, y -> a }

		cube[1].cx = 3
		cube[1].cy = 2
		cube[1].neighbours[right] = cube[0]
		cube[1].facingTransform[right] = left
		cube[1].xTransform[right] = { x, y -> a * 3 - 1 }
		cube[1].yTransform[right] = { x, y -> a - 1 - (y - a * 2) }
		cube[1].neighbours[down] = cube[5]
		cube[1].facingTransform[down] = right
		cube[1].xTransform[down] = { x, y -> 0 }
		cube[1].yTransform[down] = { x, y -> a * 2 - 1 - (x - a * 3) }
		cube[1].neighbours[left] = cube[2]
		cube[1].facingTransform[left] = left
		cube[1].xTransform[left] = { x, y -> x - 1 }
		cube[1].yTransform[left] = { x, y -> y }
		cube[1].neighbours[up] = cube[4]
		cube[1].facingTransform[up] = left
		cube[1].xTransform[up] = { x, y -> a - 1 - (x - a * 2) }
		cube[1].yTransform[up] = { x, y -> a }

		cube[2].cx = 2
		cube[2].cy = 2
		cube[2].neighbours[right] = cube[1]
		cube[2].facingTransform[right] = right
		cube[2].xTransform[right] = { x, y -> x + 1 }
		cube[2].yTransform[right] = { x, y -> y }
		cube[2].neighbours[down] = cube[5]
		cube[2].facingTransform[down] = up
		cube[2].xTransform[down] = { x, y -> a * 3 - 1 - x }
		cube[2].yTransform[down] = { x, y -> a * 2 - 1 }
		cube[2].neighbours[left] = cube[3]
		cube[2].facingTransform[left] = up
		cube[2].xTransform[left] = { x, y -> a * 2 - 1 - (y - a * 2) }
		cube[2].yTransform[left] = { x, y -> a * 2 - 1 }
		cube[2].neighbours[up] = cube[4]
		cube[2].facingTransform[up] = up
		cube[2].xTransform[up] = { x, y -> x }
		cube[2].yTransform[up] = { x, y -> y - 1 }

		cube[3].cx = 1
		cube[3].cy = 1
		cube[3].neighbours[right] = cube[4]
		cube[3].facingTransform[right] = right
		cube[3].xTransform[right] = { x, y -> x + 1 }
		cube[3].yTransform[right] = { x, y -> y }
		cube[3].neighbours[down] = cube[2]
		cube[3].facingTransform[down] = right
		cube[3].xTransform[down] = { x, y -> a * 2 }
		cube[3].yTransform[down] = { x, y -> a * 2 + (a * 2 - 1 - x) }
		cube[3].neighbours[left] = cube[5]
		cube[3].facingTransform[left] = left
		cube[3].xTransform[left] = { x, y -> x - 1 }
		cube[3].yTransform[left] = { x, y -> y }
		cube[3].neighbours[up] = cube[0]
		cube[3].facingTransform[up] = right
		cube[3].xTransform[up] = { x, y -> a * 2 }
		cube[3].yTransform[up] = { x, y -> x - a }

		cube[4].cx = 2
		cube[4].cy = 1
		cube[4].neighbours[right] = cube[1]
		cube[4].facingTransform[right] = down
		cube[4].xTransform[right] = { x, y -> a * 4 - 1 - (y - a) }
		cube[4].yTransform[right] = { x, y -> a * 2 }
		cube[4].neighbours[down] = cube[2]
		cube[4].facingTransform[down] = down
		cube[4].xTransform[down] = { x, y -> x }
		cube[4].yTransform[down] = { x, y -> y + 1 }
		cube[4].neighbours[left] = cube[3]
		cube[4].facingTransform[left] = left
		cube[4].xTransform[left] = { x, y -> x - 1 }
		cube[4].yTransform[left] = { x, y -> y }
		cube[4].neighbours[up] = cube[0]
		cube[4].facingTransform[up] = up
		cube[4].xTransform[up] = { x, y -> x }
		cube[4].yTransform[up] = { x, y -> y - 1 }

		cube[5].cx = 0
		cube[5].cy = 1
		cube[5].neighbours[right] = cube[3]
		cube[5].facingTransform[right] = right
		cube[5].xTransform[right] = { x, y -> x + 1 }
		cube[5].yTransform[right] = { x, y -> y }
		cube[5].neighbours[down] = cube[2]
		cube[5].facingTransform[down] = up
		cube[5].xTransform[down] = { x, y -> a * 3 - 1 - x }
		cube[5].yTransform[down] = { x, y -> a * 3 - 1 }
		cube[5].neighbours[left] = cube[1]
		cube[5].facingTransform[left] = up
		cube[5].xTransform[left] = { x, y -> a * 4 - 1 - (y - a) }
		cube[5].yTransform[left] = { x, y -> a * 3 - 1 }
		cube[5].neighbours[up] = cube[0]
		cube[5].facingTransform[up] = down
		cube[5].xTransform[up] = { x, y -> a * 3 - 1 - x }
		cube[5].yTransform[up] = { x, y -> 0 }

		return cube
	}

	fun createCube(): List<Face> {
		val a = 50
		val cube = List(6) { Face(it) }

		cube[0].cx = 1
		cube[0].cy = 0
		cube[0].neighbours[right] = cube[1]
		cube[0].facingTransform[right] = right
		cube[0].xTransform[right] = { x, y -> x + 1 }
		cube[0].yTransform[right] = { x, y -> y }
		cube[0].neighbours[down] = cube[4]
		cube[0].facingTransform[down] = down
		cube[0].xTransform[down] = { x, y -> x }
		cube[0].yTransform[down] = { x, y -> y + 1 }
		cube[0].neighbours[left] = cube[3]
		cube[0].facingTransform[left] = right
		cube[0].xTransform[left] = { x, y -> 0 }
		cube[0].yTransform[left] = { x, y -> a * 3 - 1 - y }
		cube[0].neighbours[up] = cube[5]
		cube[0].facingTransform[up] = right
		cube[0].xTransform[up] = { x, y -> 0 }
		cube[0].yTransform[up] = { x, y -> a * 3 + (x - a) }

		cube[1].cx = 2
		cube[1].cy = 0
		cube[1].neighbours[right] = cube[2]
		cube[1].facingTransform[right] = left
		cube[1].xTransform[right] = { x, y -> a * 2 - 1 }
		cube[1].yTransform[right] = { x, y -> a * 3 - 1 - y }
		cube[1].neighbours[down] = cube[4]
		cube[1].facingTransform[down] = left
		cube[1].xTransform[down] = { x, y -> a * 2 - 1 }
		cube[1].yTransform[down] = { x, y -> a + (x - a * 2) }
		cube[1].neighbours[left] = cube[0]
		cube[1].facingTransform[left] = left
		cube[1].xTransform[left] = { x, y -> x - 1 }
		cube[1].yTransform[left] = { x, y -> y }
		cube[1].neighbours[up] = cube[5]
		cube[1].facingTransform[up] = up
		cube[1].xTransform[up] = { x, y -> x - a * 2 }
		cube[1].yTransform[up] = { x, y -> a * 4 - 1 }

		cube[2].cx = 1
		cube[2].cy = 2
		cube[2].neighbours[right] = cube[1]
		cube[2].facingTransform[right] = left
		cube[2].xTransform[right] = { x, y -> a * 3 - 1 }
		cube[2].yTransform[right] = { x, y -> a - 1 - (y - a * 2) }
		cube[2].neighbours[down] = cube[5]
		cube[2].facingTransform[down] = left
		cube[2].xTransform[down] = { x, y -> a - 1 }
		cube[2].yTransform[down] = { x, y -> a * 3 + (x - a) }
		cube[2].neighbours[left] = cube[3]
		cube[2].facingTransform[left] = left
		cube[2].xTransform[left] = { x, y -> x - 1 }
		cube[2].yTransform[left] = { x, y -> y }
		cube[2].neighbours[up] = cube[4]
		cube[2].facingTransform[up] = up
		cube[2].xTransform[up] = { x, y -> x }
		cube[2].yTransform[up] = { x, y -> y - 1 }

		cube[3].cx = 0
		cube[3].cy = 2
		cube[3].neighbours[right] = cube[2]
		cube[3].facingTransform[right] = right
		cube[3].xTransform[right] = { x, y -> x + 1 }
		cube[3].yTransform[right] = { x, y -> y }
		cube[3].neighbours[down] = cube[5]
		cube[3].facingTransform[down] = down
		cube[3].xTransform[down] = { x, y -> x }
		cube[3].yTransform[down] = { x, y -> y + 1 }
		cube[3].neighbours[left] = cube[0]
		cube[3].facingTransform[left] = right
		cube[3].xTransform[left] = { x, y -> a }
		cube[3].yTransform[left] = { x, y -> a - 1 - (y - a * 2) }
		cube[3].neighbours[up] = cube[4]
		cube[3].facingTransform[up] = right
		cube[3].xTransform[up] = { x, y -> a }
		cube[3].yTransform[up] = { x, y -> a + x }

		cube[4].cx = 1
		cube[4].cy = 1
		cube[4].neighbours[right] = cube[1]
		cube[4].facingTransform[right] = up
		cube[4].xTransform[right] = { x, y -> a * 2 + (y - a) }
		cube[4].yTransform[right] = { x, y -> a - 1 }
		cube[4].neighbours[down] = cube[2]
		cube[4].facingTransform[down] = down
		cube[4].xTransform[down] = { x, y -> x }
		cube[4].yTransform[down] = { x, y -> y + 1 }
		cube[4].neighbours[left] = cube[3]
		cube[4].facingTransform[left] = down
		cube[4].xTransform[left] = { x, y -> y - a }
		cube[4].yTransform[left] = { x, y -> a * 2 }
		cube[4].neighbours[up] = cube[0]
		cube[4].facingTransform[up] = up
		cube[4].xTransform[up] = { x, y -> x }
		cube[4].yTransform[up] = { x, y -> y - 1 }

		cube[5].cx = 0
		cube[5].cy = 3
		cube[5].neighbours[right] = cube[2]
		cube[5].facingTransform[right] = up
		cube[5].xTransform[right] = { x, y -> a + (y - a * 3) }
		cube[5].yTransform[right] = { x, y -> a * 3 - 1 }
		cube[5].neighbours[down] = cube[1]
		cube[5].facingTransform[down] = down
		cube[5].xTransform[down] = { x, y -> a * 2 + x }
		cube[5].yTransform[down] = { x, y -> 0 }
		cube[5].neighbours[left] = cube[0]
		cube[5].facingTransform[left] = down
		cube[5].xTransform[left] = { x, y -> a + (y - a * 3) }
		cube[5].yTransform[left] = { x, y -> 0 }
		cube[5].neighbours[up] = cube[3]
		cube[5].facingTransform[up] = up
		cube[5].xTransform[up] = { x, y -> x }
		cube[5].yTransform[up] = { x, y -> y - 1 }

		return cube
	}

	fun part2(input: List<String>, cube: List<Face>): Int {
		val map = parseMap(input)
		val width = map[0].length
		val height = map.size
		val a = min(width, height) / 3
		val faces = mutableMapOf<Pair<Int, Int>, Face>()
		for (face in cube) faces[face.cx to face.cy] = face
		val path = input.last()
		var facing = right
		var x = map[0].indexOf('.')
		var y = 0
		var p = 0
		while (p < path.length) {
			when (path[p]) {
				'L' -> {
					if (facing == right) facing = up else facing--
					p++
				}
				'R' -> {
					if (facing == up) facing = right else facing++
					p++
				}
				else -> {
					val l = path.indexOf('L', p).let { if (it == -1) path.length else it }
					val r = path.indexOf('R', p).let { if (it == -1) path.length else it }
					val next = min(l, r)
					val distance = path.substring(p ,next).toInt()
					for (m in 0 until distance) {
						val (dx, dy) = when (facing) {
							right -> 1 to 0
							down -> 0 to 1
							left -> -1 to 0
							up -> 0 to -1
							else -> error("Unknown facing: $facing")
						}
						var x2 = x + dx
						var y2 = y + dy
						var facing2 = facing
						if (x2 !in 0 until width || y2 !in 0 until height || map[y2][x2] == ' ') {
							val face = faces[(x / a) to (y / a)]!!
							x2 = face.xTransform[facing](x, y)
							y2 = face.yTransform[facing](x, y)
							facing2 = face.facingTransform[facing]
						}
						if (map[y2][x2] == '.') {
							x = x2
							y = y2
							facing = facing2
						} else {
							break
						}
					}
					p = next
				}
			}
		}
		return (y + 1) * 1000 + (x + 1) * 4 + facing
	}

	val testInput = readInput("Day22_test")
	check(part1(testInput) == 6032)
	check(part2(testInput, createTestCube()) == 5031)
	val input = readInput("Day22")
	println(part1(input))
	println(part2(input, createCube()))
}
