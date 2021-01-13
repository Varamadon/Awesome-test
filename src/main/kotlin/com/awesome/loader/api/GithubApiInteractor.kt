package com.awesome.loader.api

import org.springframework.web.client.RestTemplate

interface GithubApiInteractor {

    fun getReadmeMD(restTemplate : RestTemplate): String

    fun getRepoInfo(restTemplate : RestTemplate, repoLink: String): Map<*,*>
}