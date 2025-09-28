package com.currenjin

object InsertionSort {
    fun sort(array: Array<Int>): Array<Int> {
        for (i in 1 until array.size) {
            var j = i
            while (j > 0 && array[j - 1] > array[j]) {
                val temp = array[j]
                array[j] = array[j - 1]
                array[j - 1] = temp
                j--
            }
        }
        return array
    }
}
