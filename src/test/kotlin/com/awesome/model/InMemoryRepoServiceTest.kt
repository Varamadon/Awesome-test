package com.awesome.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class InMemoryRepoServiceTest {
    private val service = InMemoryRepoService()

    private val rabbitRepo = Repo("rabbit", "super lib1", 5, LocalDate.now(), "All")
    private val legRepo = Repo("leg", "super lib2", 5, LocalDate.now(), "All")
    private val octopusRepo = Repo("octopus", "super lib3", 5, LocalDate.now(), "All")
    private val horseRepo = Repo("horse", "super lib4", 5, LocalDate.now(), "All")
    private val abbotRepo = Repo("abbot", "super lib5", 5, LocalDate.now(), "All")
    private val legendaryRepo = Repo("legendary", "super lib", 5, LocalDate.now(), "All")
    private val eggsRepo = Repo("eggs", "super lib", 5, LocalDate.now(), "All")
    private val ornamentsRepo = Repo("ornaments", "super lib", 5, LocalDate.now(), "All")
    private val octoberRepo = Repo("october", "super lib", 5, LocalDate.now(), "All")

    @BeforeEach
    fun setUp() {
        val repos = listOf(
            rabbitRepo,
            legRepo,
            octopusRepo,
            horseRepo,
            abbotRepo,
            legendaryRepo,
            eggsRepo,
            ornamentsRepo,
            octoberRepo
        )

        for (repo in repos) {
            service.saveRepo(repo)
        }
    }

    @Test
    fun testEmpty() {
        val result = InMemoryRepoService().findRepoByNamePart("abb")
        assertEquals(emptySet<Repo>(), result)
    }

    @Test
    fun testOlolo() {

        val result = service.findRepoByNamePart("ololo")
        assertEquals(emptySet<Repo>(), result)
    }

    @Test
    fun testAbb() {
        val result = service.findRepoByNamePart("abb")
        assertEquals(setOf(rabbitRepo, abbotRepo), result)
    }

    @Test
    fun testRabbit() {
        val result = service.findRepoByNamePart("rabbit")
        assertEquals(setOf(rabbitRepo), result)
    }

    @Test
    fun testEg() {
        val result = service.findRepoByNamePart("eg")
        assertEquals(setOf(eggsRepo, legRepo, legendaryRepo), result)
    }
}