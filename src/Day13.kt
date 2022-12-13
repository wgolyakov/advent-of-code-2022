import kotlin.math.max

fun main() {
	var index = 0

	fun parseList(input: String): List<Any> {
		if (input[index] != '[') error("Wrong input: $index")
		index++
		val list = mutableListOf<Any>()
		while (index < input.length) {
			val c = input[index]
			if (c == '[') {
				list.add(parseList(input))
			} else if (c == ']') {
				index++
				return list
			} else if (c == ',') {
				index++
			} else if (c.isDigit()) {
				var i = index + 1
				while (input[i].isDigit()) i++
				val num = input.substring(index, i).toInt()
				list.add(num)
				index = i
			}
		}
		return list
	}

	fun parseInput(input: List<String>): List<Pair<List<Any>, List<Any>>> {
		val pairs = mutableListOf<Pair<List<Any>, List<Any>>>()
		for (i in input.indices step 3) {
			index = 0
			val list1 = parseList(input[i])
			index = 0
			val list2 = parseList(input[i + 1])
			pairs.add(list1 to list2)
		}
		return pairs
	}

	class ListComparator: Comparator<List<Any>> {
		override fun compare(o1: List<Any>, o2: List<Any>): Int {
			for (i in 0 until max(o1.size, o2.size)) {
				if (i >= o1.size) return -1
				if (i >= o2.size) return 1
				val a1 = o1[i]
				val a2 = o2[i]
				if (a1 is Int) {
					if (a2 is Int) {
						if (a1 < a2) return -1
						if (a1 > a2) return 1
					} else {
						val c = compare(listOf(a1), a2 as List<Any>)
						if (c != 0) return c
					}
				} else {
					if (a2 is Int) {
						val c = compare(a1 as List<Any>, listOf(a2))
						if (c != 0) return c
					} else {
						val c = compare(a1 as List<Any>, a2 as List<Any>)
						if (c != 0) return c
					}
				}
			}
			return 0
		}
	}

	fun part1(input: List<String>): Int {
		val pairs = parseInput(input)
		val comparator = ListComparator()
		return pairs.withIndex()
			.filter { comparator.compare(it.value.first, it.value.second) <= 0 }.sumOf { it.index + 1 }
	}

	fun part2(input: List<String>): Int {
		val pairs = parseInput(input)
		val list = mutableListOf<List<Any>>()
		for (pair in pairs) {
			list.add(pair.first)
			list.add(pair.second)
		}
		val divider1 = listOf(listOf(2))
		val divider2 = listOf(listOf(6))
		list.add(divider1)
		list.add(divider2)
		list.sortWith(ListComparator())
		return (list.indexOf(divider1) + 1) * (list.indexOf(divider2) + 1)
	}

	val testInput = readInput("Day13_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 140)
	val input = readInput("Day13")
	println(part1(input))
	println(part2(input))
}
