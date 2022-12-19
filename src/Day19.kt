typealias Cost = MutableList<Int>

fun main() {
	fun cost(ore: Int, clay: Int, obsidian: Int, geode: Int = 0) = mutableListOf(ore, clay, obsidian, geode)

	class State(val t: Int = 0,
				val resources: Cost = cost(0, 0, 0, 0),
				val robots: Cost = cost(1, 0, 0, 0))

	class Blueprint(val num: Int, val robotCosts: List<Cost>) {
		var maxGeodes = 0
		var maxT = 0
		var maxState = State()
		val allMaxStates = mutableListOf<State>()
	}

	fun parse(input: List<String>): List<Blueprint> {
		val blueprints = mutableListOf<Blueprint>()
		for (line in input) {
			val b = Regex("Blueprint (\\d+): " +
					"Each ore robot costs (\\d+) ore. " +
					"Each clay robot costs (\\d+) ore. " +
					"Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
					"Each geode robot costs (\\d+) ore and (\\d+) obsidian.")
				.matchEntire(line)!!.groupValues.takeLast(7).map { it.toInt() }
			val blueprint = Blueprint(b[0], listOf(
				cost(b[1], 0, 0),
				cost(b[2], 0, 0),
				cost(b[3], b[4], 0),
				cost(b[5], 0, b[6])))
			blueprints.add(blueprint)
		}
		return blueprints
	}

	fun canBuild(resources: Cost, robotCost: Cost): Boolean {
		for (i in 0 until 4)
			if (resources[i] < robotCost[i]) return false
		return true
	}

	fun addResources(resources: Cost, r: Cost) { for (i in 0 until 4) resources[i] += r[i] }

	fun removeResources(resources: Cost, r: Cost) { for (i in 0 until 4) resources[i] -= r[i] }

	fun traverse(b: Blueprint, state: State, findAllMaximums: Boolean = false) {
		if (state.t == b.maxT) {
			if (state.resources[3] > b.maxGeodes) {
				b.maxGeodes = state.resources[3]
				b.maxState = state
			} else if (findAllMaximums && state.resources[3] == b.maxGeodes) {
				b.allMaxStates.add(state)
			}
			return
		}
		var canBuildCount = 0
		for (r in 0 until 4) {
			val robotCost = b.robotCosts[r]
			if (canBuild(state.resources, robotCost)) {
				canBuildCount++
				// Start building robot
				val resources = state.resources.toMutableList()
				val robots = state.robots.toMutableList()
				removeResources(resources, robotCost)
				// Robots collects resources
				addResources(resources, robots)
				// Stop building robot
				robots[r]++
				traverse(b, State(state.t + 1, resources, robots), findAllMaximums)
			}
		}
		if (canBuildCount <= 1) {
			// Don't build robot
			val resources = state.resources.toMutableList()
			val robots = state.robots.toMutableList()
			// Robots collects resources
			addResources(resources, robots)
			traverse(b, State(state.t + 1, resources, robots), findAllMaximums)
		}
	}

	fun part1(input: List<String>): Int {
		val blueprints = parse(input)
		for (b in blueprints) {
			b.maxT = 24
			traverse(b, State())
			println("${b.num}  t: ${b.maxState.t}  res:${b.maxState.resources}  rob:${b.maxState.robots}")
		}
		println(blueprints.joinToString { it.maxGeodes.toString() })
		return blueprints.sumOf { it.num * it.maxGeodes }
	}

	fun part2(input: List<String>, firstT: Int): Int {
		val blueprints = parse(input.take(3))
		for (b in blueprints) {
			b.maxT = firstT
			traverse(b, State())
			println("${b.num}  t: ${b.maxState.t}  res:${b.maxState.resources}  rob:${b.maxState.robots}")
			traverse(b, State(), true)
			b.maxT = 32
			for (state in b.allMaxStates)
				traverse(b, state)
		}
		println(blueprints.joinToString { it.maxGeodes.toString() })
		return blueprints.map { it.maxGeodes }.reduce { a, b -> a * b }
	}

	val testInput = readInput("Day19_test")
	check(part1(testInput) == 33)
	check(part2(testInput, 25) == 56 * 62)
	val input = readInput("Day19")
	println(part1(input))
	println(part2(input, 26))
}
