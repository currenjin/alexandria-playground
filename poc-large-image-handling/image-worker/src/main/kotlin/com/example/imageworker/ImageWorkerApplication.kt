package com.example.imageworker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = ["com.example.common.domain"])
@ConfigurationPropertiesScan
class ImageWorkerApplication

fun main(args: Array<String>) {
    runApplication<ImageWorkerApplication>(*args)
}
