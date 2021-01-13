package com.awesome.model.service

import com.awesome.model.Repo
import com.awesome.model.Section

interface RepoService {

    fun findRepoByNamePart(namePart: String): Set<Repo>

    fun saveRepo(repo: Repo, sectionTitle: String)

    fun getSections(minStars: Int): List<Section>
}