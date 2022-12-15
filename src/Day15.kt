import kotlin.math.absoluteValue

fun main() {
	fun distance(p1: Pair<Int, Int>, p2: Pair<Int, Int>) =
		(p1.first - p2.first).absoluteValue + (p1.second - p2.second).absoluteValue

	fun parse(input: List<String>): Triple<List<Pair<Int, Int>>, Set<Pair<Int, Int>>, List<Int>> {
		val sensors = mutableListOf<Pair<Int, Int>>()
		val beacons = mutableSetOf<Pair<Int, Int>>()
		val closestDist = mutableListOf<Int>()
		for (line in input) {
			val (sensorX, sensorY, beaconX, beaconY) =
				Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
				.matchEntire(line)!!.groupValues.takeLast(4).map { it.toInt() }
			sensors.add(sensorX to sensorY)
			beacons.add(beaconX to beaconY)
			closestDist.add(distance(sensorX to sensorY, beaconX to beaconY))
		}
		return Triple(sensors, beacons, closestDist)
	}

	fun part1(input: List<String>, y: Int): Int {
		val (sensors, beacons, closestDist) = parse(input)
		val noBeaconX = mutableSetOf<Int>()
		for (i in sensors.indices) {
			val sensor = sensors[i]
			val d = closestDist[i]
			var x = sensor.first
			while (distance(x to y, sensor) <= d) {
				if (x to y !in beacons) noBeaconX.add(x)
				x++
			}
			x = sensor.first - 1
			while (distance(x to y, sensor) <= d) {
				if (x to y !in beacons) noBeaconX.add(x)
				x--
			}
		}
		return noBeaconX.size
	}

	fun part2(input: List<String>, limit: Int): Long {
		val (sensors, _, closestDist) = parse(input)

		fun isDistress(x: Int, y: Int) = sensors.indices.all { distance(x to y, sensors[it]) > closestDist[it] }

		for (i in sensors.indices) {
			val (x0, y0) = sensors[i]
			val r = closestDist[i]
			var y: Int
			for (x in 0..limit) {
				y = x + y0 - x0 - r - 1
				if (y in 0..limit && isDistress(x, y))
					return x.toLong() * 4000000 + y
				y = x + y0 - x0 + r + 1
				if (y in 0..limit && isDistress(x, y))
					return x.toLong() * 4000000 + y
				y = -x + y0 + x0 - r - 1
				if (y in 0..limit && isDistress(x, y))
					return x.toLong() * 4000000 + y
				y = -x + y0 + x0 + r + 1
				if (y in 0..limit && isDistress(x, y))
					return x.toLong() * 4000000 + y
			}
		}
		return -1
	}

	val testInput = readInput("Day15_test")
	check(part1(testInput, 10) == 26)
	check(part2(testInput, 20) == 56000011L)
	val input = readInput("Day15")
	println(part1(input, 2000000))
	println(part2(input, 4000000))
}
