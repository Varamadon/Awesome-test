package com.awesome.loader.api

import com.awesome.loader.MockAuthProvider
import com.awesome.loader.testBrokenRepoLink
import com.awesome.loader.testPassword
import com.awesome.loader.testRepoApiLink
import com.awesome.loader.testRepoCommitsApiLink
import com.awesome.loader.testRepoLink
import com.awesome.loader.testToken
import com.awesome.loader.testUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.lang.IllegalArgumentException

internal class GithubApiInteractorImlTest {
    private val authProvider = MockAuthProvider()
    private val githubApiInteractor = GithubApiInteractorIml(authProvider)

    @Test
    fun testGettingReadme() {
        val restTemplate = mock(RestTemplate::class.java)
        val readme = "ololo"
        val response = ResponseEntity(readme, HttpStatus.OK)
        val headers = HttpHeaders()
        headers.setBasicAuth(testUser, testPassword)
        val entity = HttpEntity<String>(headers)
        `when`(restTemplate.exchange(
            ArgumentMatchers.eq(awesomeReadmeLink),
            ArgumentMatchers.eq(HttpMethod.GET),
            ArgumentMatchers.eq(entity),
            ArgumentMatchers.eq(String::class.java)
        )).thenReturn(response)

        val result = githubApiInteractor.getReadmeMD(restTemplate)
        assertEquals(readme, result)
    }

    @Test
    fun testGettingRepoInfo() {
        val restTemplate = mock(RestTemplate::class.java)
        val repoInfo: Map<*,*> = mapOf(
            Pair("name", "testName"),
            Pair("description", "testDescription"),
            Pair("stargazers_count", 140),
            Pair("last_commit_date", "2017-10-19T17:04:49Z")
        )
        val rawRepoInfo: Map<*,*> = mapOf(
            Pair("name", "testName"),
            Pair("description", "testDescription"),
            Pair("stargazers_count", 140)
        )
        val rawRepoResponse = ResponseEntity(rawRepoInfo, HttpStatus.OK)

        val committer = mapOf(Pair("date", "2017-10-19T17:04:49Z"))
        val innerCommit = mapOf(Pair("committer", committer))
        val lastCommit = mapOf(Pair("commit", innerCommit))
        val commits: List<*> = listOf(lastCommit)
        val commitResponse = ResponseEntity(commits, HttpStatus.OK)

        val headers = HttpHeaders()
        headers.setBearerAuth(testToken)
        val entity = HttpEntity<String>(headers)

        `when`(restTemplate.exchange(
            ArgumentMatchers.eq(testRepoApiLink),
            ArgumentMatchers.eq(HttpMethod.GET),
            ArgumentMatchers.eq(entity),
            ArgumentMatchers.eq(Map::class.java)
        )).thenReturn(rawRepoResponse)

        `when`(restTemplate.exchange(
            ArgumentMatchers.eq(testRepoCommitsApiLink),
            ArgumentMatchers.eq(HttpMethod.GET),
            ArgumentMatchers.eq(entity),
            ArgumentMatchers.eq(List::class.java)
        )).thenReturn(commitResponse)

        val result = githubApiInteractor.getRepoInfo(restTemplate, testRepoLink)
        assertEquals(repoInfo, result)
    }

    @Test
    fun testGettingIncorrectRepoInfo() {
        assertThrows(IllegalArgumentException::class.java) {
            val restTemplate = mock(RestTemplate::class.java)
            githubApiInteractor.getRepoInfo(restTemplate, testBrokenRepoLink)
        }
    }
}