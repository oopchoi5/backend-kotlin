package com.controlcv.backendkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class BackendKotlinApplication

fun main(args: Array<String>) {
    runApplication<BackendKotlinApplication>(*args)
}
