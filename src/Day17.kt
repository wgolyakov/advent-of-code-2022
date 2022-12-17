fun main() {
	val rocks = listOf(
		listOf(
			"####"
		),
		listOf(
			".#.",
			"###",
			".#.",
		),
		listOf(
			"..#",
			"..#",
			"###",
		),
		listOf(
			"#",
			"#",
			"#",
			"#",
		),
		listOf(
			"##",
			"##",
		),
	)

	fun emptyChamber() = mutableListOf(
		StringBuilder("+-------+"),
	)

	fun addLayers(chamber: MutableList<StringBuilder>, n: Int) {
		if (n >= 0) {
			for (i in 0 until n)
				chamber.add(0, StringBuilder("|.......|"))
		} else {
			for (i in 0 until -n)
				chamber.removeAt(0)
		}
	}

	fun drawRock(chamber: MutableList<StringBuilder>, rock: List<String>, x: Int, y: Int, color: Char = '#') {
		for (i in rock.indices) {
			val rockLayer = rock[i]
			val chamberLayer = chamber[y + i]
			for (j in rockLayer.indices)
				if (rockLayer[j] != '.') chamberLayer[x + j] = color
		}
	}

	fun eraseRock(chamber: MutableList<StringBuilder>, rock: List<String>, x: Int, y: Int) =
		drawRock(chamber, rock, x, y, '.')

	fun emptySpace(chamber: MutableList<StringBuilder>, rock: List<String>, x: Int, y: Int): Boolean {
		for (i in rock.indices) {
			val rockLayer = rock[i]
			val chamberLayer = chamber[y + i]
			for (j in rockLayer.indices)
				if (rockLayer[j] != '.' && chamberLayer[x + j] != '.') return false
		}
		return true
	}

	fun moveRock(chamber: MutableList<StringBuilder>, rock: List<String>, x: Int, y: Int, dx: Int, dy: Int): Boolean {
		eraseRock(chamber, rock, x, y)
		return if (emptySpace(chamber, rock, x + dx, y + dy)) {
			drawRock(chamber, rock, x + dx, y + dy)
			true
		} else {
			drawRock(chamber, rock, x, y)
			false
		}
	}

	fun part1(jets: String): Int {
		val chamber = emptyChamber()
		var towerY = chamber.size - 1
		var j = 0
		for (r in 0 until 2022) {
			val rock = rocks[r % rocks.size]
			val newLayersCount = 3 - towerY + rock.size
			addLayers(chamber, newLayersCount)
			towerY += newLayersCount
			var x = 3
			var y = 0
			drawRock(chamber, rock, x, y)
			while (true) {
				val jet = jets[j++ % jets.length]
				val dx = if (jet == '<') -1 else 1
				if (moveRock(chamber, rock, x, y, dx, 0))
					x += dx
				if (moveRock(chamber, rock, x, y, 0, 1))
					y++
				else
					break
			}
			if (y < towerY) towerY = y
		}
		return chamber.size - towerY - 1
	}

	fun part2(jets: String): Long {
		val chamber = emptyChamber()
		val maxChamberSaveSize = 1000000
		var towerY = chamber.size - 1
		var towerSize = 0L
		val towerStat = mutableMapOf<Int, MutableList<Pair<Long, Long>>>()
		val rockStat = mutableMapOf<Int, MutableList<Pair<Long, Long>>>()
		var j = 0L
		for (r in 0 until 1000000000000) {
			if (r % rocks.size == 0L) {
				val jMod = (j % jets.length).toInt()
				val towerList = towerStat.getOrPut(jMod) { mutableListOf() }
				val lastTowerSize = if (towerList.isNotEmpty()) towerList.last().first else 0L
				val dt = towerSize - lastTowerSize
				towerList.add(towerSize to dt)
				val rockList = rockStat.getOrPut(jMod) { mutableListOf() }
				val lastRock = if (rockList.isNotEmpty()) rockList.last().first else 0L
				val dr = r - lastRock
				rockList.add(r to dr)
				if (towerList.size >= 3 && rockList.size >= 3) {
					if ((1000000000000 - r) % dr == 0L) {
						return towerSize + ((1000000000000 - r) / dr) * dt
					}
				}
			}
			val rock = rocks[(r % rocks.size).toInt()]
			val newLayersCount = 3 - towerY + rock.size
			addLayers(chamber, newLayersCount)
			towerY += newLayersCount
			var x = 3
			var y = 0
			drawRock(chamber, rock, x, y)
			while (true) {
				val jet = jets[(j++ % jets.length).toInt()]
				val dx = if (jet == '<') -1 else 1
				if (moveRock(chamber, rock, x, y, dx, 0))
					x += dx
				if (moveRock(chamber, rock, x, y, 0, 1))
					y++
				else
					break
			}
			if (y < towerY) {
				towerSize += towerY - y
				towerY = y
			}
			while (chamber.size > maxChamberSaveSize) chamber.removeLast()
		}
		return towerSize
	}

	val testInput = readFile("Day17_test")
	check(part1(testInput) == 3068)
	check(part2(testInput) == 1514285714288)
	val input = readFile("Day17")
	println(part1(input))
	println(part2(input))
}
