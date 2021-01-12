package com.awesome.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.awesome"])
class AwesomeTestApplication

fun main(args: Array<String>) {
    runApplication<AwesomeTestApplication>(*args)
}
