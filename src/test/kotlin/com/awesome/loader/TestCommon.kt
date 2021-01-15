package com.awesome.loader

import com.awesome.loader.api.GithubApiInteractor
import com.awesome.loader.api.auth.AuthProvider
import com.awesome.loader.api.githubLinkPrefix
import com.awesome.model.Repo
import com.awesome.model.Section
import com.awesome.model.service.RepoService
import org.springframework.web.client.RestTemplate
import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicInteger

class MockRepoService : RepoService {
    val savedCount = AtomicInteger(0)

    override fun findRepoByNamePart(namePart: String): Set<Repo> {
        return emptySet()
    }

    override fun saveRepo(repo: Repo, sectionTitle: String) {
        savedCount.incrementAndGet()
    }

    override fun getSections(minStars: Int): List<Section> {
        return emptyList()
    }
}

class MockApiInteractor : GithubApiInteractor {
    val loadedCount = AtomicInteger(0)

    override fun getReadmeMD(restTemplate: RestTemplate): String {
        return testMd
    }

    override fun getRepoInfo(restTemplate: RestTemplate, repoLink: String): Map<*, *> {
        if (!repoLink.startsWith(githubLinkPrefix)) throw IllegalArgumentException()
        return mapOf(
            Pair("name", "testName" + loadedCount.getAndIncrement()),
            Pair("description", "testDescription"),
            Pair("stargazers_count", 140),
            Pair("last_commit_date", "2017-10-19T17:04:49Z")
        )
    }
}

const val testToken = "testToken"
const val testUser = "testUser"
const val testPassword = "testPassword"

class MockAuthProvider: AuthProvider {
    override fun provideToken(): String {
        return testToken
    }

    override fun provideBasicCredentials(): Pair<String, String> {
        return Pair(testUser, testPassword)
    }

}

const val testRepoLink = "https://github.com/spawnproc/bpe/ololo/v1"
const val testRepoApiLink = "https://api.github.com/repos/spawnproc/bpe"
const val testRepoCommitsApiLink = "https://api.github.com/repos/spawnproc/bpe/commits"
const val testBrokenRepoLink = "https://gittrtrthub.com/spawnproc/bpe"

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