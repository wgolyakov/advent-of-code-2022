fun main() {
	class Monkey(val items: MutableList<Long>, val operation: (Long) -> Long,
				 val throwOper: (Long) -> Int, val divisible: Int) {
		var inspectCount = 0L
	}

	fun parse(input: List<String>): List<Monkey> {
		val monkeys = mutableListOf<Monkey>()
		var i = 0
		while (i < input.size) {
			i++
			val items = input[i++].substringAfter("  Starting items: ")
				.split(", ").map { it.toLong() }.toMutableList()
			val (oper, arg) = input[i++].substringAfter("  Operation: new = old ").split(' ')
			val operation: (Long) -> Long = if (arg == "old") {
				if (oper == "+") { a -> a + a } else { a -> a * a }
			} else {
				val x = arg.toInt()
				if (oper == "+") { a -> a + x } else { a -> a * x }
			}
			val divisible = input[i++].substringAfter("  Test: divisible by ").toInt()
			val trueThrow = input[i++].substringAfter("    If true: throw to monkey ").toInt()
			val falseThrow = input[i++].substringAfter("    If false: throw to monkey ").toInt()
			val throwOper: (Long) -> Int = { a -> if (a % divisible == 0L) trueThrow else falseThrow }
			monkeys.add(Monkey(items, operation, throwOper, divisible))
			i++
		}
		return monkeys
	}

	fun part1(input: List<String>): Long {
		val monkeys = parse(input)
		for (round in 1..20) {
			for (monkey in monkeys) {
				while (monkey.items.isNotEmpty()) {
					var item = monkey.items.removeFirst()
					monkey.inspectCount++
					item = monkey.operation(item)
					item /= 3
					val toMonkey = monkey.throwOper(item)
					monkeys[toMonkey].items.add(item)
				}
			}
		}
		return monkeys.map { it.inspectCount }.sorted().takeLast(2).let { (a, b) -> a * b }
	}

	fun part2(input: List<String>): Long {
		val monkeys = parse(input)
		val mod = monkeys.map { it.divisible.toLong() }.reduce { a, b -> a * b }
		for (round in 1..10000) {
			for (monkey in monkeys) {
				while (monkey.items.isNotEmpty()) {
					var item = monkey.items.removeFirst()
					monkey.inspectCount++
					item = monkey.operation(item)
					while (item >= mod)
						item %= mod
					val toMonkey = monkey.throwOper(item)
					monkeys[toMonkey].items.add(item)
				}
			}
		}
		return monkeys.map { it.inspectCount }.sorted().takeLast(2).let { (a, b) -> a * b }
	}

	val testInput = readInput("Day11_test")
	check(part1(testInput) == 10605L)
	check(part2(testInput) == 2713310158L)
	val input = readInput("Day11")
	println(part1(input))
	println(part2(input))
}
