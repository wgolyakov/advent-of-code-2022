fun main() {
	fun part1(input: List<String>): Int {
		return input.sumBy {
			when (it) {
				"A X" -> 1 + 3 // Rock, Rock, Draw
				"A Y" -> 2 + 6 // Rock, Paper, Won
				"A Z" -> 3 + 0 // Rock, Scissors, Lose
				"B X" -> 1 + 0 // Paper, Rock, Lose
				"B Y" -> 2 + 3 // Paper, Paper, Draw
				"B Z" -> 3 + 6 // Paper, Scissors, Won
				"C X" -> 1 + 6 // Scissors, Rock, Won
				"C Y" -> 2 + 0 // Scissors, Paper, Lose
				"C Z" -> 3 + 3 // Scissors, Scissors, Draw
				else -> 0
			}
		}
	}

	fun part2(input: List<String>): Int {
		return input.sumBy {
			when (it) {
				"A X" -> 3 + 0 // Rock, Scissors, Lose
				"A Y" -> 1 + 3 // Rock, Rock, Draw
				"A Z" -> 2 + 6 // Rock, Paper, Won
				"B X" -> 1 + 0 // Paper, Rock, Loss
				"B Y" -> 2 + 3 // Paper, Paper, Draw
				"B Z" -> 3 + 6 // Paper, Scissors, Won
				"C X" -> 2 + 0 // Scissors, Paper, Lose
				"C Y" -> 3 + 3 // Scissors, Scissors, Draw
				"C Z" -> 1 + 6 // Scissors, Rock, Won
				else -> 0
			}
		}
	}

	val testInput = readInput("Day02_test")
	check(part1(testInput) == 15)
	check(part2(testInput) == 12)

	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}
