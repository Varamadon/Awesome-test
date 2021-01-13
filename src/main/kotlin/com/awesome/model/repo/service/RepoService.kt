package com.awesome.model.repo.service

import com.awesome.model.repo.Repo

interface RepoService {

    fun findRepoByNamePart(namePart: String): Set<Repo>

    fun saveRepo(repo: Repo)
}