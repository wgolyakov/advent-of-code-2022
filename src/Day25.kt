import kotlin.math.max

fun main() {
	val toDecimalDigit = mapOf(
		'2' to 2,
		'1' to 1,
		'0' to 0,
		'-' to -1,
		'=' to -2,
	)

	val toSnafuDigit = mapOf(
		0 to "0",
		1 to "1",
		2 to "2",
		3 to "1=",
		4 to "1-",
		5 to "10",
		6 to "11",
		7 to "12",
		8 to "2=",
		9 to "2-",
	)

	fun toDecimal(snafu: String): Long {
		var decimal = 0L
		var p = 1L
		for (c in snafu.reversed()) {
			decimal += p * toDecimalDigit[c]!!
			p *= 5
		}
		return decimal
	}

	fun toSnafu(decimal: Long): String {
		var n = decimal
		val snafu = StringBuilder()
		var r = 0
		do {
			val d = (n % 5).toInt() + r
			n /= 5
			val s = toSnafuDigit[d]!!
			snafu.insert(0, s.last())
			r = if (s.length > 1) s[0].digitToInt() else 0
		} while (n > 0)
		if (r != 0) snafu.insert(0, toSnafuDigit[r]!!)
		return snafu.toString()
	}

	fun part1(input: List<String>) = toSnafu(input.sumOf { toDecimal(it) })

	val snafuDigitsSum = mapOf(
		('2' to '2') to "1-",
		('2' to '1') to "1=",
		('2' to '0') to "2",
		('2' to '-') to "1",
		('2' to '=') to "0",
		('1' to '2') to "1=",
		('1' to '1') to "2",
		('1' to '0') to "1",
		('1' to '-') to "0",
		('1' to '=') to "-",
		('0' to '2') to "2",
		('0' to '1') to "1",
		('0' to '0') to "0",
		('0' to '-') to "-",
		('0' to '=') to "=",
		('-' to '2') to "1",
		('-' to '1') to "0",
		('-' to '0') to "-",
		('-' to '-') to "=",
		('-' to '=') to "-2",
		('=' to '2') to "0",
		('=' to '1') to "-",
		('=' to '0') to "=",
		('=' to '-') to "-2",
		('=' to '=') to "-1",
	)

	fun plus(snafu1: String, snafu2: String): String {
		val sum = StringBuilder()
		var r = '0'
		for (i in 0 until max(snafu1.length, snafu2.length)) {
			val d1 = if (i < snafu1.length) snafu1[snafu1.length - 1 - i] else '0'
			val d2 = if (i < snafu2.length) snafu2[snafu2.length - 1 - i] else '0'
			val s1 = snafuDigitsSum[d1 to d2]!!
			val s2 = snafuDigitsSum[s1.last() to r]!!
			sum.insert(0, s2.last())
			val r1 = if (s1.length > 1) s1[0] else '0'
			val r2 = if (s2.length > 1) s2[0] else '0'
			r = snafuDigitsSum[r1 to r2]!![0]
		}
		if (r != '0') sum.insert(0, r)
		return sum.toString()
	}

	fun part2(input: List<String>) = input.reduce { a, b -> plus(a, b) }

	val testInput = readInput("Day25_test")
	check(part1(testInput) == "2=-1=0")
	check(part2(testInput) == "2=-1=0")
	val input = readInput("Day25")
	println(part1(input))
	println(part2(input))
}
