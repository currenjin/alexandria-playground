package com.currenjin.seata

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SeataAtApplication

fun main(args: Array<String>) {
    runApplication<SeataAtApplication>(*args)
}
