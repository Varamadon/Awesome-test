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

const val testMd =
    "# Awesome Elixir [![Build Status](https://api.travis-ci.org/h4cc/awesome-elixir.svg?branch=master)](https://travis-ci.org/h4cc/awesome-elixir) [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome)\n" +
            "A curated list of amazingly awesome Elixir libraries, resources, and shiny things inspired by [awesome-php](https://github.com/ziadoz/awesome-php).\n" +
            "\n" +
            "If you think a package should be added, please add a :+1: (`:+1:`) at the according issue or create a new one.\n" +
            "\n" +
            "There are [other sites with curated lists of elixir packages](#other-awesome-lists) which you can have a look at.\n" +
            "\n" +
            "- [Awesome Elixir](#awesome-elixir)\n" +
            "    - [Actors](#actors)\n" +
            "    - [Algorithms and Data structures](#algorithms-and-data-structures)\n" +
            "\n" +
            "## Actors\n" +
            "*Libraries and tools for working with actors and such.*\n" +
            "\n" +
            "* [bpe](https://github.com/spawnproc/bpe) - Business Process Engine in Erlang. ([Doc](https://bpe.n2o.dev)).\n" +
            "* [dflow](https://github.com/dalmatinerdb/dflow) - Pipelined flow processing engine.\n" +
            "* [exactor](https://github.com/sasa1977/exactor) - Helpers for easier implementation of actors in Elixir.\n" +
            "* [exos](https://github.com/awetzel/exos) - A Port Wrapper which forwards cast and call to a linked Port.\n" +
            "* [flowex](https://github.com/antonmi/flowex) - Railway Flow-Based Programming with Elixir GenStage.\n" +
            "\n" +
            "## Algorithms and Data structures\n" +
            "*Libraries and implementations of algorithms and data structures.*\n" +
            "\n" +
            "* [array](https://github.com/takscape/elixir-array) - An Elixir wrapper library for Erlang's array.\n" +
            "* [aruspex](https://github.com/dkendal/aruspex) - Aruspex is a configurable constraint solver, written purely in Elixir.\n" +
            "* [bimap](https://githyuhub.com/mkaput/elixir-bimap) - Pure Elixir implementation of [bidirectional maps](https://en.wikipedia.org/wiki/Bidirectional_map) and multimaps.\n" +
            "* [bitmap](https://github.com/hashd/bitmap-elixir) - Pure Elixir implementation of [bitmaps](https://en.wikipedia.org/wiki/Bitmap).\n"