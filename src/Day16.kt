import kotlin.random.Random

fun main() {
	fun parse(input: List<String>): Pair<List<List<Int>>, IntArray> {
		val valves = mutableMapOf("AA" to 0)
		val adj = MutableList(input.size) { emptyList<Int>() }
		val rates = IntArray(input.size)
		var count = 1
		for (line in input) {
			val (valve, rate, exits) =
				Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? (.*)")
					.matchEntire(line)!!.groupValues.takeLast(3)
			val valveNum = valves.getOrPut(valve) { count++ }
			rates[valveNum] = rate.toInt()
			adj[valveNum] = exits.split(", ").map { valves.getOrPut(it) { count++ } }
		}
		return adj to rates
	}

	fun part1(input: List<String>): Int {
		val (adj, rate) = parse(input)
		val countWithRate = rate.count { it > 0 }
		val maxRate = rate.max()
		fun isOpen(rate: Int) = maxRate - rate <= Random.nextInt(maxRate * 2)
		var maxPressure = 0
		for (i in 0 until 10000) {
			val visited = BooleanArray(adj.size)
			val opened = BooleanArray(adj.size)
			var openedWithRate = 0
			var time = 30
			var pressure = 0
			var valve = 0
			var prevValve = 0
			while (openedWithRate < countWithRate && time > 0) {
				if (!visited[valve])
					visited[valve] = true
				if (rate[valve] > 0 && !opened[valve] && isOpen(rate[valve])) {
					opened[valve] = true
					openedWithRate++
					time--
					if (time > 0) pressure += time * rate[valve]
				}
				val v = valve
				val ways = adj[valve]
				val unvisited = ways.filter { !visited[it] }
				valve = if (unvisited.isNotEmpty()) unvisited.random() else {
					if (ways.size == 1) ways.first() else ways.filter { it != prevValve }.random()
				}
				time--
				prevValve = v
			}
			if (pressure > maxPressure) maxPressure = pressure
		}
		return maxPressure
	}

	fun part2(input: List<String>): Int {
		val (adj, rate) = parse(input)
		val countWithRate = rate.count { it > 0 }
		val maxRate = rate.max()
		fun isOpen(rate: Int) = maxRate - rate <= Random.nextInt(maxRate * 2)
		var maxPressure = 0
		for (i in 0 until 1000000) {
			val visited = BooleanArray(adj.size)
			val opened = BooleanArray(adj.size)
			var openedWithRate = 0
			var pressure = 0
			var me = 0
			var mePrev = 0
			var meTime = 26
			var elephant = 0
			var elephantPrev = 0
			var elephantTime = 26
			visited[0] = true
			while (openedWithRate < countWithRate && (meTime > 0 || elephantTime > 0)) {
				if (meTime > 0 && rate[me] > 0 && !opened[me] && isOpen(rate[me])) {
					opened[me] = true
					openedWithRate++
					meTime--
					if (meTime > 0) pressure += meTime * rate[me]
				}
				if (elephantTime > 0 && rate[elephant] > 0 && !opened[elephant] && isOpen(rate[elephant])) {
					opened[elephant] = true
					openedWithRate++
					elephantTime--
					if (elephantTime > 0) pressure += elephantTime * rate[elephant]
				}
				if (meTime > 0) {
					val v = me
					val ways = adj[me]
					val unvisited = ways.filter { !visited[it] }
					me = if (unvisited.isNotEmpty()) unvisited.random() else {
						if (ways.size == 1) ways.first() else ways.filter { it != mePrev }.random()
					}
					meTime--
					mePrev = v
					if (!visited[me]) visited[me] = true
				}
				if (elephantTime > 0) {
					val v = elephant
					val ways = adj[elephant]
					val unvisited = ways.filter { !visited[it] }
					elephant = if (unvisited.isNotEmpty()) unvisited.random() else {
						if (ways.size == 1) ways.first() else ways.filter { it != elephantPrev }.random()
					}
					elephantTime--
					elephantPrev = v
					if (!visited[elephant]) visited[elephant] = true
				}
			}
			if (pressure > maxPressure) maxPressure = pressure
		}
		return maxPressure
	}

	val testInput = readInput("Day16_test")
	check(part1(testInput) == 1651)
	check(part2(testInput) == 1707)
	val input = readInput("Day16")
	println(part1(input))
	println(part2(input))
}
