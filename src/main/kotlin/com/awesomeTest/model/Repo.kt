package com.awesomeTest.model

import java.time.LocalDateTime

data class Repo(
    private val name: String,
    private val description: String,
    private val starsCount: Int,
    private val lastCommit: LocalDateTime
)