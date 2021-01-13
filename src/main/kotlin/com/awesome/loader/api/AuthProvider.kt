package com.awesome.loader.api

interface AuthProvider {
    fun provideToken(): String

    fun provideBasicCredentials(): Pair<String, String>
}