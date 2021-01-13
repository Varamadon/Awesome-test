package com.awesome.loader

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class GithubRepoLoaderTest {

    private val repoService = MockRepoService()
    private val apiInteractor = MockApiInteractor()
    private val repoLoader = GithubRepoLoader(repoService, apiInteractor)

    @Test
    fun test() {
        repoLoader.load()
        repoLoader.awaitCompletion()
        println(apiInteractor.loadedCount)
        println(repoService.savedCount)
        assertEquals(apiInteractor.loadedCount.get(), repoService.savedCount.get())
    }
}
