import javafx.geometry.Point3D
import javafx.scene.transform.Rotate
import javafx.scene.transform.Transform
import kotlin.math.min
import kotlin.math.roundToInt

fun main() {
	val right = 0
	val down = 1
	val left = 2
	val up = 3

	val positionToMove = mapOf(
		right to (1 to 0),
		down to (0 to 1),
		left to (-1 to 0),
		up to (0 to -1),
	)

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
						val (dx, dy) = positionToMove[facing]!!
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

	class Face(val cx: Int, val cy: Int) {
		val neighbours = Array(4) { this }
		var vector = Point3D(0.0, 0.0, 0.0)
		var transform: Transform = Rotate()
		var facing = IntArray(4) { it }
	}

	val reverse = mapOf(
		right to left,
		down to up,
		left to right,
		up to down,
	)

	val rotation = mapOf(
		right to Rotate(-90.0, Rotate.Y_AXIS),
		left to Rotate(90.0, Rotate.Y_AXIS),
		up to Rotate(-90.0, Rotate.X_AXIS),
		down to Rotate(90.0, Rotate.X_AXIS),
	)

	fun rotateTransform(transform: Transform, facing: Int) = transform.createConcatenation(rotation[facing]!!)

	val epsilon = 1.0e-5

	fun round(p: Point3D): Point3D {
		val x = if (-epsilon < p.x && p.x < epsilon) 0.0 else p.x
		val y = if (-epsilon < p.y && p.y < epsilon) 0.0 else p.y
		val z = if (-epsilon < p.z && p.z < epsilon) 0.0 else p.z
		return Point3D(x, y, z)
	}

	fun findStraightNeighboursBfs(faces: Map<Pair<Int, Int>, Face>, face0: Face) {
		val queue = ArrayDeque<Face>()
		queue.addLast(face0)
		face0.vector = Point3D(0.0, 0.0, -1.0)
		while (queue.isNotEmpty()) {
			val curr = queue.removeFirst()
			for ((facing, delta) in positionToMove) {
				val (dx, dy) = delta
				if (curr.neighbours[facing] !== curr) continue
				val neighbour = faces[curr.cx + dx to curr.cy + dy] ?: continue
				val revFacing = reverse[facing]!!
				curr.neighbours[facing] = neighbour
				curr.facing[facing] = facing
				neighbour.neighbours[revFacing] = curr
				neighbour.transform = rotateTransform(curr.transform, facing)
				neighbour.vector = round(neighbour.transform.transform(face0.vector))
				neighbour.facing[revFacing] = revFacing
				queue.addLast(neighbour)
			}
		}
	}

	fun findOtherNeighbours(faces: Map<Pair<Int, Int>, Face>, face0: Face) {
		for (face in faces.values) {
			for ((facing, _) in positionToMove) {
				if (face.neighbours[facing] !== face) continue
				val neighbourTransform = rotateTransform(face.transform, facing)
				val neighbourVector = round(neighbourTransform.transform(face0.vector))
				val neighbour = faces.values.find { it.vector == neighbourVector }!!
				face.neighbours[facing] = neighbour
			}
		}
	}

	fun findNeighboursFacing(faces: Map<Pair<Int, Int>, Face>) {
		for (face in faces.values) {
			for ((facing, _) in positionToMove) {
				val neighbour = face.neighbours[facing]
				val neighbourFacing = neighbour.neighbours.withIndex().find { n -> n.value === face }!!.index
				face.facing[facing] = reverse[neighbourFacing]!!
				neighbour.facing[neighbourFacing] = reverse[facing]!!
			}
		}
	}

	fun getPointOnBorder(p: Point3D, facing: Int): Pair<Double, Double> {
		return when (facing) {
			right -> -1.0 to p.y
			left -> 1.0 to p.y
			up -> p.x to 1.0
			down -> p.x to -1.0
			else -> error("Unknown facing: $facing")
		}
	}

	fun initFaces(map: List<String>, width: Int, height: Int, a: Int): Map<Pair<Int, Int>, Face> {
		val cubeWidth = width / a
		val cubeHeight = height / a
		val faces = mutableMapOf<Pair<Int, Int>, Face>()
		for (cy in 0 until cubeHeight)
			for (cx in 0 until cubeWidth)
				if (map[cy * a][cx * a] != ' ')
					faces[cx to cy] = Face(cx, cy)
		return faces
	}

	fun part2(input: List<String>): Int {
		val map = parseMap(input)
		val width = map[0].length
		val height = map.size
		val a = min(width, height) / 3
		val faces = initFaces(map, width, height, a)
		val x0 = map[0].indexOf('.')
		val face0 = faces[x0 / a to 0]!!
		findStraightNeighboursBfs(faces, face0)
		findOtherNeighbours(faces, face0)
		findNeighboursFacing(faces)

		val a2 = (a.toDouble() - 1) / 2
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
						val (dx, dy) = positionToMove[facing]!!
						var x2 = x + dx
						var y2 = y + dy
						var facing2 = facing
						if (x2 !in 0 until width || y2 !in 0 until height || map[y2][x2] == ' ') {
							val cx = x / a
							val cy = y / a
							val face = faces[cx to cy]!!
							val face2 = face.neighbours[facing]
							facing2 = face.facing[facing]
							val x1 = (x % a).toDouble() / a2 - 1
							val y1 = (y % a).toDouble() / a2 - 1
							val p3d = face.transform.transform(Point3D(x1, y1, -1.0))
							val pNearBorder = face2.transform.createInverse().transform(p3d)
							val (xBorder, yBorder) = getPointOnBorder(pNearBorder, facing2)
							x2 = face2.cx * a + ((xBorder + 1) * a2).roundToInt()
							y2 = face2.cy * a + ((yBorder + 1) * a2).roundToInt()
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
	check(part2(testInput) == 5031)
	val input = readInput("Day22")
	println(part1(input))
	println(part2(input))
}
