package com.awesome.model.service.impl

import com.awesome.model.Repo
import com.awesome.model.Section
import com.awesome.model.service.RepoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.PriorityBlockingQueue

@Service
class InMemoryRepoService : RepoService {

    private val log = LoggerFactory.getLogger(javaClass)

    private val repoListByNamePart: MutableMap<String, MutableSet<Repo>> = ConcurrentHashMap()
    private val reposBySectionTitle: MutableMap<String, PriorityBlockingQueue<Repo>> = ConcurrentHashMap()

    override fun findRepoByNamePart(namePart: String): Set<Repo> {
        return repoListByNamePart.getOrDefault(namePart, mutableSetOf())
    }

    override fun saveRepo(repo: Repo, sectionTitle: String) {
        log.debug("Start saving " + repo.name)
        saveRepoToSection(repo, sectionTitle)
        log.debug("Saved " + repo.name + " to sections")
        saveRepoByParts(repo)
        log.debug("Finished saving " + repo.name)
    }

    private fun saveRepoToSection(repo: Repo, sectionTitle: String) {
        val repos = reposBySectionTitle.getOrDefault(sectionTitle, getPriorityBlockingQueue())
        repos.add(repo)
        reposBySectionTitle[sectionTitle] = repos
    }

    private fun saveRepoByParts(repo: Repo) {
        val nameParts = getNameParts(repo.name)
        for (namePart in nameParts) {
            val repoSet = repoListByNamePart.getOrDefault(namePart, CopyOnWriteArraySet())
            repoSet.add(repo)
            repoListByNamePart[namePart] = repoSet
        }
    }

    override fun getSections(minStars: Int): List<Section> {
        val result = mutableListOf<Section>()
        for (sectionTitle in reposBySectionTitle.keys) {
            val repos = PriorityBlockingQueue(reposBySectionTitle[sectionTitle] ?: getPriorityBlockingQueue())
            val eligibleRepos = mutableListOf<Repo>()
            while (!repos.isEmpty()) {
                val nextRepo = repos.poll()
                if (nextRepo.starsCount >= minStars) {
                    eligibleRepos.add(nextRepo)
                }
            }
            if (eligibleRepos.isNotEmpty()) {
                result.add(Section(sectionTitle, eligibleRepos.toList()))
            }
        }
        result.sortWith { o1, o2 -> o1.title.compareTo(o2.title) }
        return result.toList()
    }

    private fun getPriorityBlockingQueue(): PriorityBlockingQueue<Repo> {
        return PriorityBlockingQueue(200, Comparator.comparing(Repo::name))
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