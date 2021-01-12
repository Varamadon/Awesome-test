package com.awesome.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AwesomeTestApplication

fun main(args: Array<String>) {
    runApplication<AwesomeTestApplication>(*args)
}
