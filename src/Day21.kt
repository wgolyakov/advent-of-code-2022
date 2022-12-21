import kotlin.math.absoluteValue

fun main() {
	fun parse(input: List<String>): Pair<MutableMap<String, Long>, MutableMap<String, List<String>>> {
		val values = mutableMapOf<String, Long>()
		val actions = mutableMapOf<String, List<String>>()
		for (line in input) {
			val (name, expr) = line.split(": ")
			if (expr[0].isDigit())
				values[name] = expr.toLong()
			else
				actions[name] = expr.split(' ')
		}
		return values to actions
	}

	fun evalAction(x: Long, action: String, y: Long) = when (action) {
		"+" -> x + y
		"-" -> x - y
		"*" -> x * y
		"/" -> x / y
		else -> error("Unknown action: $action")
	}

	fun evalRoot(values: MutableMap<String, Long>, actions: MutableMap<String, List<String>>): Long {
		while (actions.isNotEmpty()) {
			val iter = actions.iterator()
			while (iter.hasNext()) {
				val entry = iter.next()
				val (a, action, b) = entry.value
				val x = values[a] ?: continue
				val y = values[b] ?: continue
				values[entry.key] = evalAction(x, action, y)
				iter.remove()
			}
		}
		return values["root"]!!
	}

	fun part1(input: List<String>): Long {
		val (values, actions) = parse(input)
		return evalRoot(values, actions)
	}

	fun simplify(values: MutableMap<String, Long>, actions: MutableMap<String, List<String>>) {
		var changed = true
		while (actions.isNotEmpty() && changed) {
			changed = false
			val iter = actions.iterator()
			while (iter.hasNext()) {
				val entry = iter.next()
				if (entry.key == "root") continue
				val (a, action, b) = entry.value
				if (a == "humn" || b == "humn") continue
				val x = values[a] ?: continue
				val y = values[b] ?: continue
				values[entry.key] = evalAction(x, action, y)
				iter.remove()
				changed = true
			}
		}
	}

	fun f(x: Long, values: MutableMap<String, Long>, actions: MutableMap<String, List<String>>): Long {
		val valuesCopy = values.toMutableMap()
		val actionsCopy = actions.toMutableMap()
		valuesCopy["humn"] = x
		return evalRoot(valuesCopy, actionsCopy)
	}

	fun part2(input: List<String>): Long {
		val (values, actions) = parse(input)
		simplify(values, actions)
		val (rootA, _, rootB) = actions["root"]!!
		actions["root"] = listOf(rootA, "-", rootB)
		var x1 = Long.MIN_VALUE / 3
		var x2 = Long.MAX_VALUE / 3
		var y1: Long
		var y2: Long
		while (x1 != x2 && x1 + 1 != x2) {
			y1 = f(x1, values, actions)
			y2 = f(x2, values, actions)
			//println("$x1, $x2   $y1, $y2")
			if (y1 == 0L) return x1
			if (y2 == 0L) return x2
			if (y1.absoluteValue < y2.absoluteValue)
				x2 = (x1 + x2) / 2
			else
				x1 = (x1 + x2) / 2
		}
		y1 = f(x1, values, actions)
		y2 = f(x2, values, actions)
		if (y1 == 0L) return x1
		if (y2 == 0L) return x2
		error("No result! humn: $x1, $x2, root: $y1, $y2")
	}

	val testInput = readInput("Day21_test")
	check(part1(testInput) == 152L)
	check(part2(testInput) == 301L)
	val input = readInput("Day21")
	println(part1(input))
	println(part2(input))
}
