package com.currenjin

object InsertionSort {
    fun sort(array: Array<Int>): Array<Int> {
        if (array.size == 2 && array[0] > array[1]) {
            val temp = array[0]
            array[0] = array[1]
            array[1] = temp
        }
        return array
    }
}
