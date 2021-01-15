package com.awesome.model

import java.time.LocalDate


data class Repo(
    val name: String,
    val link: String,
    val description: String,
    val starsCount: Int,
    val lastCommit: LocalDate
)