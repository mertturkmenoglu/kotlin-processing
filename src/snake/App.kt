package snake

import processing.core.PApplet

class App : PApplet() {
    private var n = 10
    private var dir = "right"
    private val startX = 0
    private val startY = 250
    private val r = 10
    private val coords = ArrayList<Pair<Int, Int>>()
    private var fruitX = 0
    private var fruitY = 0

    override fun settings() {
        size(600, 600)
    }

    override fun setup() {
        frameRate(10f)
        stroke(255)
        strokeWeight(10f)
        fruitCoordinate()

        for (i in 0 until n) {
            coords.add(startX + i * r to startY)
        }
    }

    override fun draw() {
        background(0)

        for (i in 0 until n -1) {
            line(coords[i].first.toFloat(), coords[i].second.toFloat(),
                coords[i+1].first.toFloat(), coords[i+1].second.toFloat())
        }

        point(fruitX.toFloat(), fruitY.toFloat())
        updateSnake()
        gameStatus()
        checkFruit()
    }

    private fun updateSnake() {
        for (i in 0 until n - 1) {
            coords[i] = coords[i+1]
        }

        coords[n -1] = coords[n - 2]

        val i = n - 1

        coords[i] = with(coords[i]) { when (dir) {
            "right" -> (first + r) to second
            "up"-> first to (second - r)
            "left" -> (first - r) to second
            "down" ->first to (second + r)
            else -> coords[i]
        } }
    }

    private fun gameStatus() {
        val (x, y) = coords.last()
        if ( (x !in 0..width) || (y !in 0..height) || snakeCollision()) {
            println((x !in 0..width))
            println(y !in 0..height)
            kotlin.io.println(snakeCollision())
            println(coords)
            noLoop()
            println("Game over")
        }
    }

    private fun snakeCollision(): Boolean {
        val head = coords.last()
        return coords.dropLast(1).any { it.first == head.first && it.second == head.second }
    }

    private fun checkFruit() {
        val (headX, headY) = coords.last()

        if (headX == fruitX && headY == fruitY) {
            coords.add(0, coords[0])
            n++
            fruitCoordinate()
        }
    }

    private fun fruitCoordinate() {
        fruitX = floor(random(10f, (width - 10) / 10f)) * 10
        fruitY = floor(random(10f, (height - 10) / 10f)) * 10
    }

    override fun keyPressed() {
        dir = when (key) {
            'a', 'A' -> if (dir != "right") "left" else dir
            'd', 'D'-> if (dir != "left") "right" else dir
            'w', 'W' -> if (dir != "down") "up" else dir
            's', 'S' -> if (dir != "up") "down" else dir
            else -> dir
        }
    }

}