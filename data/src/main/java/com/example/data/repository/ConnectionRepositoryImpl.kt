package com.example.data.repository

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.example.data.managers.NetworkConnectionManager
import com.example.domain.settings.repository.ConnectionRepository
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
    private val networkConnectionManager: NetworkConnectionManager
) : ConnectionRepository {

    override fun checkInternetConnection(): Boolean {
        return networkConnectionManager.isInternetAvailable()
    }
}
