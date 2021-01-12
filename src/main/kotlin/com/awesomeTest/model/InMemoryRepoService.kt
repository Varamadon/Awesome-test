package com.awesomeTest.model

import org.springframework.stereotype.Service

@Service
class InMemoryRepoService : RepoService {

    private val repoListByNamePart: MutableMap<String, MutableSet<Repo>> = mutableMapOf()

    override fun findRepoByNamePart(namePart: String): Set<Repo> {
        return repoListByNamePart.getOrDefault(namePart, mutableSetOf())
    }

    override fun saveRepo(repo: Repo) {
        val nameParts = getNameParts(repo.name)
        for (namePart in nameParts) {
            val repoSet = repoListByNamePart.getOrDefault(namePart, mutableSetOf())
            repoSet.add(repo)
            repoListByNamePart[namePart] = repoSet
        }
    }

    private fun getNameParts(name: String): List<String> {
        val result = mutableListOf<String>()
        val length = name.length
        for (i in 1..length) {
            var start = 0
            var end = i
            while (end <= length) {
                val namePart = name.substring(start, end)
                result.add(namePart)
                start++
                end++
            }
        }
        return result
    }
}