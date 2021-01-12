package com.awesome.app

import com.awesome.loader.GithubRepoLoader
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class AwesomeDaemon(
    val githubRepoLoader: GithubRepoLoader
) : InitializingBean {

    override fun afterPropertiesSet() {
        githubRepoLoader.load()
    }
}