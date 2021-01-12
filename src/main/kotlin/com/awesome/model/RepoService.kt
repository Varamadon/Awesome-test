package com.awesome.model

interface RepoService {

    fun findRepoByNamePart(namePart: String): Set<Repo>

    fun saveRepo(repo: Repo)
}