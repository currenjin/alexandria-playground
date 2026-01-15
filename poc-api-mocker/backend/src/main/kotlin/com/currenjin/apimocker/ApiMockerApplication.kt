package com.currenjin.apimocker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiMockerApplication

fun main(args: Array<String>) {
    runApplication<ApiMockerApplication>(*args)
}
