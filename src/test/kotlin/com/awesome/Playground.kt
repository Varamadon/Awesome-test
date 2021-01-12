package com.awesome

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Playground {

    @Test
    fun test() {
        val testString = "ghklhkjhl"

        val res = if (testString.endsWith("/")) testString.substring(0, testString.length - 1) else testString
        println(res)
    }
}