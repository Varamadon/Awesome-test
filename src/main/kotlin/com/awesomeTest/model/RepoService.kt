package com.awesomeTest.model

interface RepoService {

    fun findRepoByNamePart(namePart: String): Set<Repo>

    fun saveRepo(repo: Repo)
}