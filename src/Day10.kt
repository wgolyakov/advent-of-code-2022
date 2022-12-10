import kotlin.math.absoluteValue

fun main() {
	fun part1(input: List<String>): Int {
		var x = 1
		var cycle = 0
		var strengths = 0
		for (line in input) {
			if (line == "noop") {
				cycle++
				if ((cycle - 20) % 40 == 0) strengths += cycle * x
			} else {
				val a = line.substringAfter("addx ").toInt()
				cycle++
				if ((cycle - 20) % 40 == 0) strengths += cycle * x
				cycle++
				if ((cycle - 20) % 40 == 0) strengths += cycle * x
				x += a
			}
		}
		return strengths
	}

	fun part2(input: List<String>): String {
		var x = 1
		var cycle = 0
		val screen = List(6) { StringBuilder(".".repeat(40)) }
		for (line in input) {
			if (line == "noop") {
				val c = cycle % 40
				if ((c - x).absoluteValue <= 1)
					screen[cycle / 40][c] = '#'
				cycle++
			} else {
				val a = line.substringAfter("addx ").toInt()
				var c = cycle % 40
				if ((c - x).absoluteValue <= 1)
					screen[cycle / 40][c] = '#'
				cycle++
				c = cycle % 40
				if ((c - x).absoluteValue <= 1)
					screen[cycle / 40][c] = '#'
				cycle++
				x += a
			}
		}
		return screen.joinToString("\n")
	}

	val testInput = readInput("Day10_test")
	check(part1(testInput) == 13140)
	check(part2(testInput) ==
		"##..##..##..##..##..##..##..##..##..##..\n" +
		"###...###...###...###...###...###...###.\n" +
		"####....####....####....####....####....\n" +
		"#####.....#####.....#####.....#####.....\n" +
		"######......######......######......####\n" +
		"#######.......#######.......#######....."
	)
	val input = readInput("Day10")
	println(part1(input))
	println(part2(input))
}
