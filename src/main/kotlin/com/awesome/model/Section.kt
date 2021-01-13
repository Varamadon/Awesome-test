package com.awesome.model

import com.awesome.model.Repo

data class Section(
    val title: String,
    val repos: List<Repo>
)