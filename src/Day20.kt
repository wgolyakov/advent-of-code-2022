fun main() {
	fun part1(input: List<String>): Int {
		val numbers = input.map { it.toInt() }.toMutableList()
		val indToCurrInd = numbers.indices.associateWith { it }.toMutableMap()
		val n = numbers.size
		for ((ind, x) in numbers.toList().withIndex()) {
			val i = indToCurrInd[ind]!!
			numbers.removeAt(i)
			for (entry in indToCurrInd.entries)
				if (entry.value > i) entry.setValue(entry.value - 1)
			var j = (i + x) % (n - 1)
			if (j < 0) j += n - 1
			numbers.add(j, x)
			for (entry in indToCurrInd.entries)
				if (entry.value >= j) entry.setValue(entry.value + 1)
			indToCurrInd[ind] = j
		}
		val zero = numbers.indexOf(0)
		val n1 = numbers[(zero + 1000) % n]
		val n2 = numbers[(zero + 2000) % n]
		val n3 = numbers[(zero + 3000) % n]
		return n1 + n2 + n3
	}

	fun part2(input: List<String>): Long {
		val premixedNumbers = input.map { it.toLong() * 811589153 }
		val numbers = premixedNumbers.toMutableList()
		val indToCurrInd = numbers.indices.associateWith { it }.toMutableMap()
		val n = numbers.size
		for (t in 0 until 10) {
			for ((ind, x) in premixedNumbers.withIndex()) {
				val i = indToCurrInd[ind]!!
				numbers.removeAt(i)
				for (entry in indToCurrInd.entries)
					if (entry.value > i) entry.setValue(entry.value - 1)
				var j = ((x + i) % (n - 1)).toInt()
				if (j < 0) j += n - 1
				numbers.add(j, x)
				for (entry in indToCurrInd.entries)
					if (entry.value >= j) entry.setValue(entry.value + 1)
				indToCurrInd[ind] = j
			}
		}
		val zero = numbers.indexOf(0)
		val n1 = numbers[(zero + 1000) % n]
		val n2 = numbers[(zero + 2000) % n]
		val n3 = numbers[(zero + 3000) % n]
		return n1 + n2 + n3
	}

	val testInput = readInput("Day20_test")
	check(part1(testInput) == 3)
	check(part2(testInput) == 1623178306L)
	val input = readInput("Day20")
	println(part1(input))
	println(part2(input))
}
