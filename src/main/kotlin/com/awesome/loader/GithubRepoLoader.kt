package com.awesome.loader

import com.awesome.loader.api.GithubApiInteractor
import com.awesome.model.repo.Repo
import com.awesome.model.repo.service.RepoService
import org.commonmark.node.Heading
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Component
class GithubRepoLoader(
    private val repoService: RepoService,
    private val apiInteractor: GithubApiInteractor
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    fun load() {
        val restTemplate = RestTemplate()
        val readme = apiInteractor.getReadmeMD(restTemplate)

        val parser: Parser = Parser.builder().build()
        val parsedReadme = parser.parse(readme)
        val node = parsedReadme.firstChild
        traverseOverHeadings(node)
    }

    fun awaitCompletion() {
        executor.awaitTermination(30, TimeUnit.MINUTES)
    }

    private fun traverseOverHeadings(node: Node?) {
        if (node == null) {
            executor.shutdown()
            return
        }
        if (node is Heading) {
            val child = node.firstChild as Text
            val sectionName = child.literal
            if (sectionName == "Resources") {
                executor.shutdown()
                return
            }
            if (sectionName != "Awesome Elixir ") {
                traverseOverList(sectionName, node.next.next.firstChild)
            }
        }
        traverseOverHeadings(node.next)
    }

    private fun traverseOverList(sectionName: String, node: Node?) {
        if (node == null) return
        val link = node.firstChild?.firstChild
        if (link is Link) {
            executor.execute { loadRepo(link.destination, sectionName) }
        }
        traverseOverList(sectionName, node.next)
    }

    private fun loadRepo(link: String, sectionName: String) {
        try {
            val repoInfo = apiInteractor.getRepoInfo(RestTemplate(), link)
            val repo = Repo(
                repoInfo["name"] as String,
                if (repoInfo["description"] != null) repoInfo["description"] as String else "",
                repoInfo["stargazers_count"] as Int,
                getLocalDateFromISO8601String(repoInfo["pushed_at"] as String),
                sectionName
            )
            repoService.saveRepo(repo)
            log.debug("Saved " + repo.name)
        } catch (e: Exception) {
            log.error("ERROR loading $link" + e.message)
        }
    }

    private fun getLocalDateFromISO8601String(dateString: String): LocalDate {
        val dateStringVals = dateString.split("T")[0].split("-")
        val year = dateStringVals[0].toInt()
        val month = dateStringVals[1].toInt()
        val day = dateStringVals[2].toInt()

        return LocalDate.of(year, month, day)
    }
}