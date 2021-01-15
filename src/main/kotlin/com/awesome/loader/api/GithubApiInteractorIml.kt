package com.awesome.loader.api

import com.awesome.loader.api.auth.AuthProvider
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.lang.IllegalArgumentException

const val githubLinkPrefix = "https://github.com/"
const val githubApiCommitsPostfix = "/commits"
const val githubApiRepoLinkPrefix = "https://api.github.com/repos/"
const val awesomeReadmeLink = "https://raw.githubusercontent.com/h4cc/awesome-elixir/master/README.md"

@Component
class GithubApiInteractorIml(
    private val authProvider: AuthProvider
): GithubApiInteractor {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getReadmeMD(restTemplate: RestTemplate): String {
        val readmeResponse = restTemplate.exchange(
            awesomeReadmeLink,
            HttpMethod.GET,
            getHttpRequestEntity(),
            String::class.java
        )
        log.debug("Loaded readme")
        return readmeResponse.body ?: ""
    }

    override fun getRepoInfo(restTemplate: RestTemplate, repoLink: String): Map<*, *> {
        if (!repoLink.startsWith(githubLinkPrefix)) throw IllegalArgumentException("This is not github repo")
        val apiLink = getApiLink(repoLink)
        val repoResponse = restTemplate.exchange(
            apiLink,
            HttpMethod.GET,
            getHttpRequestEntity(),
            Map::class.java
        )
        log.debug("Loaded $repoLink")

        val commitsResponse = restTemplate.exchange(
            apiLink + githubApiCommitsPostfix,
            HttpMethod.GET,
            getHttpRequestEntity(),
            List::class.java
        )
        val repoInfo = repoResponse.body ?: throw IllegalArgumentException("Loaded empty repo info")
        val commitsInfo = commitsResponse.body ?: throw IllegalArgumentException("Loaded empty commits info")
        @Suppress("UNCHECKED_CAST") val lastCommitDate =
            (commitsInfo[0] as Map<String, Map<String, Map<String, String>>>)["commit"]?.get("committer")?.get("date")
                ?: throw IllegalArgumentException("Last commit date info is not present")
        val repoInfoWithDate = repoInfo.toMutableMap()
        repoInfoWithDate["last_commit_date"] = lastCommitDate
        return repoInfoWithDate.toMap()
    }

    private fun getHttpRequestEntity(): HttpEntity<String> {
        val headers = HttpHeaders()
        headers.setBearerAuth(authProvider.provideToken())
        return HttpEntity(headers)
    }

    private fun getApiLink(link: String): String {
        val pathParts = link.replace(githubLinkPrefix, "").split("/")
        return githubApiRepoLinkPrefix + pathParts[0] + "/" + pathParts[1]
    }
}