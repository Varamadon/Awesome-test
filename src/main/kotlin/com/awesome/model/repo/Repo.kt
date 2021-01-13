package com.awesome.model.repo

import java.time.LocalDate


data class Repo(
    val name: String,
    val description: String,
    val starsCount: Int,
    val lastCommit: LocalDate,
    val section: String
)