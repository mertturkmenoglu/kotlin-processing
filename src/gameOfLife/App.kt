package gameOfLife

import processing.core.PApplet
import gameOfLife.Constants.w
import gameOfLife.Constants.h
import gameOfLife.Constants.r
import gameOfLife.Constants.rowCount
import gameOfLife.Constants.colCount

class App : PApplet() {
    private lateinit var board: Array<Array<Int>>
    private lateinit var next: Array<Array<Int>>
    private val probAlive = 60
    private var pause: Boolean = false

    override fun settings() {
        smooth(8)
        size(w, h)
    }

    override fun setup() {
        board = Array(rowCount) { Array(colCount) { if (random(100f) > probAlive) 0 else 1 } }
        next = Array(rowCount) { i -> Array(colCount) { j -> board[i][j] } }
        pause = false
        frameRate(30f)
    }

    override fun draw() {
        clear()
        background(0)
        drawBoard()
        if (!pause) {
            makeComputation()
        }
    }

    private fun drawBoard() {
        for (i in 0 until rowCount) {
            for (j in 0 until colCount) {
                if (board[i][j] == 1) {
                    strokeWeight(0f)
                    fill(color(0, 200, 0))
                    val y = i * r.toFloat()
                    val x = j * r.toFloat()

                    rect(x, y, r - 1f, r - 1f)
                }
            }
        }

        fill(255)
        text("FPS: $frameRate", 0f, 20f)
    }

    private fun makeComputation() {
        for (i in 0 until rowCount) {
            for (j in 0 until colCount) {
                val neighbors = aliveNeighborCount(i, j)
                next[i][j] = if (board[i][j] == 0) {
                    if (neighbors == 3) 1 else 0
                } else {
                    if (neighbors !in listOf(2, 3)) 0 else 1
                }
            }
        }

        for (i in 0 until rowCount) {
            for (j in 0 until colCount) {
                board[i][j] = next[i][j]
            }
        }
    }

    private fun aliveNeighborCount(i: Int, j: Int): Int {
        val indices = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
        var sum = 0

        for (e in indices) {
            val x = i + e.first
            val y = j + e.second

            if (x !in (0 until rowCount) || y !in (0 until colCount))
                continue

            if (board[x][y] == 1) {
                sum++
            }
        }

        return sum
    }

    override fun mouseClicked() {
        if (!pause)
            return

        val i = mouseY / r
        val j = mouseX / r

        if (mouseButton == LEFT) {
            board[i][j] = if (board[i][j] == 1) 0 else 1
            return
        }

        if (mouseButton == RIGHT) {
            println("($i, $j) ==> ${aliveNeighborCount(i, j)}")
        }

    }

    override fun keyPressed() {
        when (key) {
            ' ' -> pause = !pause
            'c', 'C' -> board.forEach { it.fill(0) }
            'r', 'R' -> setup()
            'n', 'N' -> {
                makeComputation()
                pause = true
            }
            'u' -> frameRate(frameRate + 3)
            'd' -> frameRate(frameRate - 3)
        }
    }
}