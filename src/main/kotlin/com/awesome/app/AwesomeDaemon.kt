package com.awesome.app

import com.awesome.loader.GithubRepoLoader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class AwesomeDaemon(
    val githubRepoLoader: GithubRepoLoader
) : InitializingBean {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun afterPropertiesSet() {
//        githubRepoLoader.load()
//        githubRepoLoader.awaitCompletion()
//        log.info("Loading complete!")
    }
}