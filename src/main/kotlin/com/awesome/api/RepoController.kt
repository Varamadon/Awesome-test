package com.awesome.api

import com.awesome.model.Repo
import com.awesome.model.RepoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class RepoController(
    private val repoService: RepoService
) {

    @GetMapping("/find")
    fun findRepos(
        @RequestParam(required = false, defaultValue = "") namePart: String,
        @RequestParam(required = false, defaultValue = "0") minStars: Int
    ): ResponseEntity<List<Repo>> {
        val resultList = repoService.findRepoByNamePart(namePart)
            .filter {
                it -> it.starsCount >= minStars
            }

        return ResponseEntity(resultList, HttpStatus.OK)
    }
}