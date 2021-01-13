package com.awesome.loader.api.auth

interface AuthProvider {
    fun provideToken(): String

    fun provideBasicCredentials(): Pair<String, String>
}