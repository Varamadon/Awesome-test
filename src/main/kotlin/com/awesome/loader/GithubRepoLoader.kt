package com.awesome.loader

import com.awesome.model.Repo
import com.awesome.model.RepoService
import org.commonmark.node.Heading
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.node.Text
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.commonmark.parser.Parser
import java.lang.Exception
import java.time.LocalDate
import java.util.concurrent.Executors


@Component
class GithubRepoLoader(
    private val repoService: RepoService,
    private val authProvider: AuthProvider
) {

    private val testMd =
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
                "    - [Applications](#applications)\n" +
                "    - [Artificial Intelligence](#artificial-intelligence)\n" +
                "    - [Audio and Sounds](#audio-and-sounds)\n" +
                "    - [Authentication](#authentication)\n" +
                "    - [Authorization](#authorization)\n" +
                "    - [Behaviours and Interfaces](#behaviours-and-interfaces)\n" +
                "    - [Benchmarking](#benchmarking)\n" +
                "    - [Bittorrent](#bittorrent)\n" +
                "    - [BSON](#bson)\n" +
                "    - [Build Tools](#build-tools)\n" +
                "    - [Caching](#caching)\n" +
                "    - [Chatting](#chatting)\n" +
                "    - [Cloud Infrastructure and Management](#cloud-infrastructure-and-management)\n" +
                "    - [Code Analysis](#code-analysis)\n" +
                "    - [Command Line Applications](#command-line-applications)\n" +
                "    - [Configuration](#configuration)\n" +
                "    - [Cryptography](#cryptography)\n" +
                "    - [CSV](#csv)\n" +
                "    - [Date and Time](#date-and-time)\n" +
                "    - [Debugging](#debugging)\n" +
                "    - [Deployment](#deployment)\n" +
                "    - [Documentation](#documentation)\n" +
                "    - [Domain-specific language](#domain-specific-language)\n" +
                "    - [ECMAScript](#ecmascript)\n" +
                "    - [Email](#email)\n" +
                "    - [Embedded Systems](#embedded-systems)\n" +
                "    - [Encoding and Compression](#encoding-and-compression)\n" +
                "    - [Errors and Exception Handling](#errors-and-exception-handling)\n" +
                "    - [Eventhandling](#eventhandling)\n" +
                "    - [Examples and funny stuff](#examples-and-funny-stuff)\n" +
                "    - [Feature Flags and Toggles](#feature-flags-and-toggles)\n" +
                "    - [Feeds](#feeds)\n" +
                "    - [Files and Directories](#files-and-directories)\n" +
                "    - [Formulars](#formulars)\n" +
                "    - [Framework Components](#framework-components)\n" +
                "    - [Frameworks](#frameworks)\n" +
                "    - [Games](#games)\n" +
                "    - [Geolocation](#geolocation)\n" +
                "    - [GUI](#gui)\n" +
                "    - [Hardware](#hardware)\n" +
                "    - [HTML](#html)\n" +
                "    - [HTTP](#http)\n" +
                "    - [Images](#images)\n" +
                "    - [Instrumenting / Monitoring](#instrumenting--monitoring)\n" +
                "    - [JSON](#json)\n" +
                "    - [Languages](#languages)\n" +
                "    - [Lexical analysis](#lexical-analysis)\n" +
                "    - [Logging](#logging)\n" +
                "    - [Macros](#macros)\n" +
                "    - [Markdown](#markdown)\n" +
                "    - [Miscellaneous](#miscellaneous)\n" +
                "    - [Native Implemented Functions](#native-implemented-functions)\n" +
                "    - [Natural Language Processing (NLP)](#natural-language-processing-nlp)\n" +
                "    - [Networking](#networking)\n" +
                "    - [Office](#office)\n" +
                "    - [ORM and Datamapping](#orm-and-datamapping)\n" +
                "    - [OTP](#otp)\n" +
                "    - [Package Management](#package-management)\n" +
                "    - [PDF](#pdf)\n" +
                "    - [Protocols](#protocols)\n" +
                "    - [Queue](#queue)\n" +
                "    - [Release Management](#release-management)\n" +
                "    - [REST and API](#rest-and-api)\n" +
                "    - [Search](#search)\n" +
                "    - [Security](#security)\n" +
                "    - [SMS](#sms)\n" +
                "    - [Static Page Generation](#static-page-generation)\n" +
                "    - [Statistics](#statistics)\n" +
                "    - [Templating](#templating)\n" +
                "    - [Testing](#testing)\n" +
                "    - [Text and Numbers](#text-and-numbers)\n" +
                "    - [Third Party APIs](#third-party-apis)\n" +
                "    - [Translations and Internationalizations](#translations-and-internationalizations)\n" +
                "    - [Utilities](#utilities)\n" +
                "    - [Validations](#validations)\n" +
                "    - [Version Control](#version-control)\n" +
                "    - [Video](#video)\n" +
                "    - [WebAssembly](#web-assembly)\n" +
                "    - [XML](#xml)\n" +
                "    - [YAML](#yaml)\n" +
                "- [Resources](#resources)\n" +
                "    - [Books](#books)\n" +
                "    - [Cheat Sheets](#cheat-sheets)\n" +
                "    - [Community](#community)\n" +
                "    - [Editors](#editors)\n" +
                "    - [Newsletters](#newsletters)\n" +
                "    - [Other Awesome Lists](#other-awesome-lists)\n" +
                "    - [Reading](#reading)\n" +
                "    - [Screencasts](#screencasts)\n" +
                "    - [Styleguides](#styleguides)\n" +
                "    - [Websites](#websites)\n" +
                "- [Contributing](#contributing)\n" +
                "\n" +
                "## Actors\n" +
                "*Libraries and tools for working with actors and such.*\n" +
                "\n" +
                "* [bpe](https://github.com/spawnproc/bpe) - Business Process Engine in Erlang. ([Doc](https://bpe.n2o.dev)).\n" +
                "* [dflow](https://github.com/dalmatinerdb/dflow) - Pipelined flow processing engine.\n" +
                "* [exactor](https://github.com/sasa1977/exactor) - Helpers for easier implementation of actors in Elixir.\n" +
                "* [exos](https://github.com/awetzel/exos) - A Port Wrapper which forwards cast and call to a linked Port.\n" +
                "* [flowex](https://github.com/antonmi/flowex) - Railway Flow-Based Programming with Elixir GenStage.\n" +
                "* [mon_handler](https://github.com/tattdcodemonkey/mon_handler) - A minimal GenServer that monitors a given GenEvent handler.\n" +
                "* [pool_ring](https://github.com/camshaft/pool_ring) - Create a pool based on a hash ring.\n" +
                "* [poolboy](https://github.com/devinus/poolboy) - A hunky Erlang worker pool factory.\n" +
                "* [pooler](https://github.com/seth/pooler) - An OTP Process Pool Application.\n" +
                "* [sbroker](https://github.com/fishcakez/sbroker) - Sojourn-time based active queue management library.\n" +
                "* [workex](https://github.com/sasa1977/workex) - Backpressure and flow control in EVM processes.\n" +
                "\n" +
                "## Algorithms and Data structures\n" +
                "*Libraries and implementations of algorithms and data structures.*\n" +
                "\n" +
                "* [array](https://github.com/takscape/elixir-array) - An Elixir wrapper library for Erlang's array.\n" +
                "* [aruspex](https://github.com/dkendal/aruspex) - Aruspex is a configurable constraint solver, written purely in Elixir.\n" +
                "* [bimap](https://github.com/mkaput/elixir-bimap) - Pure Elixir implementation of [bidirectional maps](https://en.wikipedia.org/wiki/Bidirectional_map) and multimaps.\n" +
                "* [bitmap](https://github.com/hashd/bitmap-elixir) - Pure Elixir implementation of [bitmaps](https://en.wikipedia.org/wiki/Bitmap).\n" +
                "* [blocking_queue](https://github.com/joekain/BlockingQueue) - BlockingQueue is a simple queue implemented as a GenServer. It has a fixed maximum length established when it is created.\n" +
                "* [bloomex](https://github.com/gmcabrita/bloomex) - A pure Elixir implementation of Scalable Bloom Filters.\n" +
                "* [clope](https://github.com/ayrat555/clope) - Elixir implementation of [CLOPE](http://www.inf.ufrgs.br/~alvares/CMP259DCBD/clope.pdf): A Fast and Effective Clustering Algorithm for Transactional Data.\n" +
                "* [combination](https://github.com/seantanly/elixir-combination) - Elixir library to generate combinations and permutations from Enumerable collection.\n" +
                "* [count_buffer](https://github.com/camshaft/count_buffer) - Buffer a large set of counters and flush periodically.\n" +
                "* [cuckoo](https://github.com/gmcabrita/cuckoo) - A pure Elixir implementation of [Cuckoo Filters](https://www.cs.cmu.edu/%7Edga/papers/cuckoo-conext2014.pdf).\n" +
                "* [cuid](https://github.com/duailibe/cuid) - Collision-resistant ids optimized for horizontal scaling and sequential lookup performance, written in Elixir.\n" +
                "* [data_morph](https://hex.pm/packages/data_morph) - Create Elixir structs from data.\n" +
                "* [dataframe](https://github.com/JordiPolo/dataframe) - Package providing functionality similar to Python's Pandas or R's data.frame().\n" +
                "* [datastructures](https://github.com/meh/elixir-datastructures) - A collection of protocols, implementations and wrappers to work with data structures.\n" +
                "* [def_memo](https://github.com/os6sense/DefMemo) - A memoization macro (defmemo) for elixir using a genserver backing store.\n" +
                "* [dlist](https://github.com/stocks29/dlist) - Deque implementations in Elixir.\n" +
                "* [eastar](https://github.com/herenowcoder/eastar) - A* graph pathfinding in pure Elixir.\n" +
                "* [ecto_materialized_path](https://github.com/asiniy/ecto_materialized_path) - Tree structure, hierarchy and ancestry for the ecto models.\n" +
                "* [ecto_state_machine](https://github.com/asiniy/ecto_state_machine) - Finite state machine pattern implemented on Elixir and  adopted for Ecto.\n" +
                "* [elistrix](https://github.com/tobz/elistrix) - A latency / fault tolerance library to help isolate your applications from an uncertain world of slow or failed services.\n" +
                "* [emel](https://github.com/mrdimosthenis/emel) - A simple and functional machine learning library written in elixir.\n" +
                "* [erlang-algorithms](https://github.com/aggelgian/erlang-algorithms) - Implementations of popular data structures and algorithms.\n" +
                "* [exconstructor](https://github.com/appcues/exconstructor) - An Elixir library for generating struct constructors that handle external data with ease.\n" +
                "* [exfsm](https://github.com/awetzel/exfsm) - Simple elixir library to define a static FSM.\n" +
                "* [exkad](https://github.com/rozap/exkad) - A [kademlia](https://en.wikipedia.org/wiki/Kademlia) implementation in Elixir.\n" +
                "* [exmatrix](https://github.com/a115/exmatrix) - ExMatrix is a small library for working with matrices, originally developed for testing matrix multiplication in parallel.\n" +
                "* [exor_filter](https://github.com/mpope9/exor_filter) - Nif for xor_filters.  'Faster and Smaller Than Bloom and Cuckoo Filters'.\n" +
                "* [ezcryptex](https://github.com/stocks29/ezcryptex) - Thin layer on top of Cryptex.\n" +
                "* [flow](https://github.com/dashbitco/flow) - Computational parallel flows on top of GenStage.\n" +
                "* [fnv](https://github.com/asaaki/fnv.ex) - Pure Elixir implementation of Fowler–Noll–Vo hash functions.\n" +
                "* [fsm](https://github.com/sasa1977/fsm) - Finite state machine as a functional data structure.\n" +
                "* [fuse](https://github.com/jlouis/fuse) - This application implements a so-called circuit-breaker for Erlang.\n" +
                "* [gen_fsm](https://github.com/pavlos/gen_fsm) - A generic finite state-machine - Elixir wrapper around OTP's gen_fsm.\n" +
                "* [graphmath](https://github.com/crertel/graphmath) - An Elixir library for performing 2D and 3D mathematics.\n"

    private val githubLinkPrefix = "https://github.com/"
    private val githubApiRepoLinkPrefix = "https://api.github.com/repos/"
    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    fun load() {
        val restTemplate = RestTemplate()

        val readmeResponse = restTemplate.exchange(
            "https://raw.githubusercontent.com/h4cc/awesome-elixir/master/README.md",
            HttpMethod.GET,
            getHttpRequestEntity(),
            String::class.java
        )

        val readme = readmeResponse.body ?: ""

        val parser: Parser = Parser.builder().build()
        val parsedReadme = parser.parse(readme)
        val node = parsedReadme.firstChild
        traverseOverHeadings(node)
    }

    private fun traverseOverHeadings(node: Node?) {
        if (node == null) return
        if (node is Heading) {
            val child = node.firstChild as Text
            val sectionName = child.literal
            if (sectionName == "Resources") {
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
            executor.execute { loadRepo(link, sectionName) }
        }
        traverseOverList(sectionName, node.next)
    }

    private fun loadRepo(link: Link, sectionName: String) {
        val dest = link.destination
        if (!dest.startsWith(githubLinkPrefix)) return
        val apiLink = getApiLink(dest)

        val restTemplate = RestTemplate()
        val repoResponse = restTemplate.exchange(
            apiLink,
            HttpMethod.GET,
            getHttpRequestEntity(),
            Map::class.java
        )
        val repoInfo = repoResponse.body ?: return

        val repo = Repo(
            repoInfo["name"] as String,
            if (repoInfo["description"] != null) repoInfo["description"] as String else "",
            repoInfo["stargazers_count"] as Int,
            getLocalDateFromISO8601String(repoInfo["pushed_at"] as String),
            sectionName
        )
        repoService.saveRepo(repo)
        println("Saved " + repo.name)
    }

    private fun getApiLink(link: String): String {
        val pathParts = link.replace(githubLinkPrefix, "").split("/")
        return githubApiRepoLinkPrefix + pathParts[0] + "/" + pathParts[1]
    }

    private fun getHttpRequestEntity(): HttpEntity<String> {
        val headers = HttpHeaders()
        headers.setBearerAuth(authProvider.provideToken())
        return HttpEntity(headers)
    }

    private fun getLocalDateFromISO8601String(dateString: String): LocalDate {
        val dateStringVals = dateString.split("T")[0].split("-")
        val year = dateStringVals[0].toInt()
        val month = dateStringVals[1].toInt()
        val day = dateStringVals[2].toInt()

        return LocalDate.of(year, month, day)
    }
}