package ticTacToe

import ticTacToe.Constants.colCount
import ticTacToe.Constants.rowCount
import processing.core.PApplet

class App: PApplet() {
    private lateinit var board: Array<Array<Char>>
    private var status: Int = 0 // 0: Continue 1: Win 2: Tie
    private var playerTurn: Int = 1

    override fun settings() {
        size(Constants.w, Constants.h)
    }

    override fun setup() {
        init()
        textAlign(CENTER, CENTER)
    }

    override fun draw() {
        clear()
        checkGameStatus()
        drawBoard()
        fill(255)
        textSize(32f)

        val statusText = when (status) {
            0 -> "Player: $playerTurn"
            1 -> "Player $playerTurn wins. Click to play again."
            else -> "Tie. Click to play again."
        }

        text(statusText, 320f, 20f)
    }

    private fun init() {
        board = Array(rowCount) { Array(colCount) { 'E' } }
        status = 0
        playerTurn = 1
    }

    private fun checkGameStatus() {
        // Row&Col check
        for (i in 0 until rowCount) {
            if (((board[i][0] != 'E') && (board[i][0] == board[i][1] && board[i][1] == board[i][2])) ||
                ((board[0][i] != 'E') && (board[0][i] == board[1][i] && board[1][i] == board[2][i]))) {
                if (status == 0) {
                    playerTurn = if (playerTurn == 1) 2 else 1
                }

                status = 1
                return
            }
        }

        // Diagonal check
        if (((board[0][0] != 'E') && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            || (board[0][2] != 'E' && board[0][2] == board[1][1] && board[1][1] == board[2][0])) {
            if (status == 0) {
                playerTurn = if (playerTurn == 1) 2 else 1
            }
            status = 1
            return
        }

        // Is board full?
        for (i in 0 until rowCount) {
            for (j in 0 until colCount) {
                if (board[i][j] == 'E') {
                    status = 0
                    return
                }
            }
        }


        // Board is full. Tie.
        status = 2
    }

    private fun drawBoard() {
        for (i in 0 until rowCount) {
            for (j in 0 until colCount) {
                val value = board[i][j]

                fill(255)
                rect(j * 200f+20, i * 200f + 40, 200f, 200f)

                if (value == 'E')
                    continue
                textSize(128f)
                fill(50)
                text(value.toString(), j * 200f + 100+20, i * 200f + 120)
            }
        }
    }

    override fun mouseClicked() {
        if (status != 0) {
            init()
            return
        }

        if (mouseX < 0 || mouseX > width-20 || mouseY < 40 || mouseY > height)
            return

        val i = (mouseY - 40) / 200
        val j = (mouseX - 20) / 200


        if (board[i][j] != 'E') {
            return
        }

        // Valid click
        board[i][j] = if (playerTurn == 1) 'X' else 'O'
        playerTurn = if (playerTurn == 1) 2 else 1
    }
}