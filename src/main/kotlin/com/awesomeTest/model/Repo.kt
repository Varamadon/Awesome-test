package com.awesomeTest.model

import java.time.LocalDateTime

data class Repo(
    val name: String,
    val description: String,
    val starsCount: Int,
    val lastCommit: LocalDateTime
)