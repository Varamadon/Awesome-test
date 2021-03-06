package com.awesome.model.service

import com.awesome.model.Repo
import com.awesome.model.Section
import com.awesome.model.service.impl.InMemoryRepoService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class InMemoryRepoServiceTest {
    private val service = InMemoryRepoService()

    private val rabbitRepo = Repo("rabbit", "testLink", "super lib1", 5, LocalDate.now())
    private val legRepo = Repo("leg", "testLink", "super lib2", 15, LocalDate.now())
    private val octopusRepo = Repo("octopus", "testLink", "super lib3", 25, LocalDate.now())
    private val horseRepo = Repo("horse", "testLink", "super lib4", 35, LocalDate.now())
    private val abbotRepo = Repo("abbot", "testLink", "super lib5", 45, LocalDate.now())
    private val legendaryRepo = Repo("legendary", "testLink", "super lib", 55, LocalDate.now())
    private val eggsRepo = Repo("eggs", "testLink", "super lib", 65, LocalDate.now())
    private val ornamentsRepo = Repo("ornaments", "testLink", "super lib", 75, LocalDate.now())
    private val octoberRepo = Repo("october", "testLink", "super lib", 85, LocalDate.now())

    private val repos = listOf(
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

    @BeforeEach
    fun setUp() {
        for (repo in repos) {
            when {
                repo.starsCount < 30 -> service.saveRepo(repo, "Small")
                repo.starsCount < 60 -> service.saveRepo(repo, "Medium")
                else -> service.saveRepo(repo, "All")
            }
        }
    }

    @Test
    fun testAllSections() {
        val sections = service.getSections(0)
        printSections(sections)

        assertTrue(sections.map { it.title }.contains("All"))
        assertTrue(sections.map { it.title }.contains("Small"))
        assertTrue(sections.map { it.title }.contains("Medium"))
    }

    @Test
    fun testBigSections() {
        val sections = service.getSections(50)
        printSections(sections)

        assertTrue(sections.map { it.title }.contains("All"))
        assertTrue(!sections.map { it.title }.contains("Small"))
        assertTrue(sections.map { it.title }.contains("Medium"))
    }

    @Test
    fun testMultipleSectionsGetting() {
        val sections = service.getSections(50)
        printSections(sections)

        assertTrue(sections.map { it.title }.contains("All"))
        assertTrue(!sections.map { it.title }.contains("Small"))
        assertTrue(sections.map { it.title }.contains("Medium"))

        val sections2 = service.getSections(50)
        printSections(sections)

        assertTrue(sections2.map { it.title }.contains("All"))
        assertTrue(!sections2.map { it.title }.contains("Small"))
        assertTrue(sections2.map { it.title }.contains("Medium"))
    }

    private fun printSections(sections: List<Section>) {
        for (section in sections) {
            println(section.title)
            for (repo in section.repos) {
                println("  " + repo.name)
            }
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