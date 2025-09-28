package com.currenjin

object InsertionSort {
    fun sort(array: Array<Int>): Array<Int> {
        for (i in 1 until array.size) {
            var j = i
            while (j > 0 && array[j - 1] > array[j]) {
                swap(array, j - 1, j)
                j--
            }
        }
        return array
    }

    private fun swap(array: Array<Int>, i: Int, j: Int) {
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }
}
