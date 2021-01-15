package com.awesome.view

import com.awesome.model.Repo
import com.awesome.model.Section
import com.awesome.model.service.RepoService
import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.accordion
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.accordion.Accordion
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS


@Component
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@CssImport.Container(
    value = [
        CssImport("./styles/shared-styles.css"),
        CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
    ]
)
class MainView(
    @Autowired private val repoService: RepoService
) : KComposite() {
    private lateinit var searchField: TextField
    private lateinit var minStarsField: TextField
    private lateinit var searchResult: VerticalLayout
    private lateinit var sections: Accordion

    // The main view UI definition
    private val root = ui {
        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        verticalLayout(classNames = "centered-content") {
            isExpand = true
            setWidthFull()

            minStarsField = textField("Minimum stars")
            searchField = textField("Search:")

            searchResult = verticalLayout()
            sections = accordion()
        }
    }

    init {
        searchField.valueChangeMode = ValueChangeMode.EAGER
        searchField.addValueChangeListener {
            refreshSearchResult(searchField.value)
        }
        minStarsField.valueChangeMode = ValueChangeMode.ON_CHANGE
        minStarsField.addValueChangeListener {
            refreshSections()
        }
        searchResult.isExpand = true
        searchResult.setWidthFull()
        sections.isExpand = true
        sections.setWidthFull()
    }

    fun refresh() {
        refreshSections()
        refreshSearchResult(searchField.value)
    }

    private fun refreshSections() {
        val minStars = if (minStarsField.value.isEmpty()) 0 else minStarsField.value.toInt()
        sections.children.forEach { sections.remove(it) }
        val sectionList = repoService.getSections(minStars)
        for (section in sectionList) {
            sections.add(section.title, renderSection(section))
        }
    }

    private fun refreshSearchResult(namePart: String) {
        searchResult.children.forEach { searchResult.remove(it) }
        val repoSet = repoService.findRepoByNamePart(namePart)
        for (repo in repoSet) {
            searchResult.add(renderRepo(repo))
        }
    }

    private fun renderSection(section: Section): VerticalLayout {
        val result = VerticalLayout()
        section.repos
            .map { renderRepo(it) }
            .forEach { result.add(it) }
        return result
    }

    private fun renderRepo(repo: Repo): HorizontalLayout {
        val daysSinceLastCommit = DAYS.between(LocalDate.now(), repo.lastCommit)
        val displayText =
            " ⭐" + repo.starsCount + " \uD83D\uDCC5 " + daysSinceLastCommit + " — " + repo.description
        val link = Anchor(repo.link, repo.name)

        val result =  HorizontalLayout(link, Text(displayText))
        result.setWidthFull()
        return result
    }
}