package knightTour

import knightTour.Constants.n
import knightTour.Constants.w
import knightTour.Constants.h
import knightTour.Constants.r
import processing.core.PApplet

class App : PApplet() {
    private lateinit var board: Array<Array<Int>>
    private lateinit var moves: Array<Pair<Int, Int>>
    private var counter = 0

    override fun settings() {
        smooth(8)
        size(w, h)
    }

    override fun setup() {
        board = Array(n) { Array(n) { -1 } }
        board[0][0] = 0

        moves = arrayOf(2 to 1, 1 to 2, -1 to 2, -2 to 1, -2 to -1, -1 to -2, 1 to -2, 2 to -1)
        knightPath()

        textAlign(CENTER, CENTER)
        textSize(64f)
        frameRate(1f)
    }

    override fun draw() {
        clear()
        background(255)
        drawGrid()
        drawBoard(counter++)
        if (counter > n*n)
            noLoop()
    }

    private fun knightPath() = pathUtil(0, 0, 1)

    private fun pathUtil(x: Int, y: Int, pos: Int): Boolean {
        if (pos == n*n)
            return true

        for (i in 0 until n) {
            val nx = x + moves[i].first
            val ny = y + moves[i].second

            if (isValidMove(nx, ny)) {
                board[nx][ny] = pos
                if (pathUtil(nx, ny, pos+1))
                    return true
                board[nx][ny] = -1
            }
        }

        return false
    }

    private fun isValidMove(x: Int, y: Int) = (x in 0..7) && (y in 0..7) && (board[x][y] == -1)

    private fun drawGrid() {
        for (i in 0 until n) {
            fill(0)
            line(0f, i * r.toFloat(), w.toFloat(), i*r.toFloat())
            line(i*r.toFloat(), 0f, i*r.toFloat(), h.toFloat())
        }
    }

    private fun drawBoard(c: Int) {
        textSize(64f)
        for (i in 0 until n) {
            for (j in 0 until n) {
                val y = i * r + r / 2f
                val x = j * r + r / 2f

                fill(0)
                if (board[i][j] < c)
                    text(board[i][j], x, y)
            }
        }

        textSize(12f)
        fill(color(0, 200, 0))
        text("FPS: $frameRate", 100f, 20f)
    }
}