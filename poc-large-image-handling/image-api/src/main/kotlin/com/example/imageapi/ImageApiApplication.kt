package com.example.imageapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = ["com.example.common.domain"])
@ConfigurationPropertiesScan
class ImageApiApplication

fun main(args: Array<String>) {
    runApplication<ImageApiApplication>(*args)
}
