package com.example.domain.settings.repository

interface ConnectionRepository {

    fun checkInternetConnection(): Boolean
}