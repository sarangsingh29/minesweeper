package minesweeper

import kotlin.random.Random

class Cell(var value: Char = '.', var isMine: Boolean = false)

const val MAX = 9

fun main() {

    print("How many mines do you want on the field? ")
    val num = readLine()!!.toInt()

    val arr: Array<Array<Cell>> = Array(MAX) { Array(MAX) { Cell('.', false) } }
    printArray(arr)


    var remainingMines = num
    var ucell = MAX*MAX - num
    var init = false
    while (remainingMines != 0 && ucell != 0) {

        print("Set/unset mines marks or claim a cell as free: ")
        val line = readLine()!!
        val x = line.split(" ")[0].toInt()
        val y = line.split(" ")[1].toInt()
        val markCommand = line.split(" ")[2]

        if(!init) {
            createField(num, x - 1, y - 1, arr)
            init = true
        }

        val elem = arr[y - 1][x - 1]

        when (markCommand) {
            "mine" -> {

                if (elem.value == '.') {
                    elem.value = '*'
                    if (elem.isMine) {

                        remainingMines -= 1
                    }

                } else if (elem.value == '*') {

                    elem.value = '.'
                    if (elem.isMine) {
                        remainingMines += 1
                    }
                }
                printArray(arr)
            }
            "free" -> {
                if (elem.isMine) {

                    printArray(arr, false)
                    println("You stepped on a mine and failed!")

                    return
                } else {

                    val k = recursivelyExploreCells(arr, y - 1, x - 1)
                    ucell -= k
                    printArray(arr)
                }
            }

        }

    }
    println("Congratulations! You found all mines!")
}

fun checkSingleCell(arr: Array<Array<Cell>>, i: Int, j: Int): Int {

    return getCount(i - 1, j - 1, arr) +
            getCount(i - 1, j, arr) +
            getCount(i - 1, j + 1, arr) +
            getCount(i, j - 1, arr) +
            getCount(i, j + 1, arr) +
            getCount(i + 1, j - 1, arr) +
            getCount(i + 1, j, arr) +
            getCount(i + 1, j + 1, arr)

}

fun createField(numOfMines: Int, firstCol: Int, firstRow: Int, arr: Array<Array<Cell>>) {

    repeat(numOfMines) {

        var i: Int
        var j: Int
        do {

            i = Random.nextInt(0, MAX)
            j = Random.nextInt(0, MAX)

        } while ((i == firstRow && j == firstCol) || arr[i][j].isMine)
        arr[i][j].isMine = true
    }
}

fun getCount(i: Int, j: Int, arr: Array<Array<Cell>>): Int {

    val num = arr.lastIndex
    return when {

        (i in 0..num && j in 0..num && arr[i][j].isMine) -> 1
        else -> 0
    }
}

fun printArray(arr: Array<Array<Cell>>, hidden: Boolean = true) {

    println(" |123456789|")
    println("-|---------|")
    for (i in 0..arr.lastIndex) {

        print("${i + 1}|")
        for (j in 0..arr.lastIndex) {

            print(if (arr[i][j].isMine && !hidden) 'X' else arr[i][j].value)
        }
        println("|")
    }
    println("-|---------|")
}

fun recursivelyExploreCells(arr: Array<Array<Cell>>, i: Int, j: Int): Int {

    if(i !in 0..arr.lastIndex || j !in 0..arr.lastIndex) return 0
    val elem = arr[i][j]

    when {
        elem.value in '1'..'8' ->  return 0
        elem.value == '/' -> return 0
        elem.isMine -> return 0
    }

    val surroundingMines = checkSingleCell(arr, i, j)
    if(surroundingMines != 0) {
        elem.value = "$surroundingMines".first()
        return 1
    }

    elem.value = '/'
    return 1 + recursivelyExploreCells(arr, i - 1, j - 1) +
            recursivelyExploreCells(arr, i - 1, j) +
            recursivelyExploreCells(arr, i - 1, j + 1) +
            recursivelyExploreCells(arr, i, j - 1) +
            recursivelyExploreCells(arr, i, j + 1) +
            recursivelyExploreCells(arr, i + 1, j - 1) +
            recursivelyExploreCells(arr, i + 1, j) +
            recursivelyExploreCells(arr, i + 1, j + 1)

}
