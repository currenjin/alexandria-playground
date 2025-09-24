package com.currenjin

class BinarySearch {
    companion object {
        fun search(array: Array<Int>, target: Int): Int {
            return if (array[0] == target) 0 else 1
        }
    }

}
