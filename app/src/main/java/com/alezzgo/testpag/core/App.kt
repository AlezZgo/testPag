package com.alezzgo.testpag.core

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.alezzgo.testpag.core.di.DataModule
import com.alezzgo.testpag.data.local.ChatDao
import com.alezzgo.testpag.data.local.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider{

    @Inject lateinit var factory: CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(factory)
            .build()

}
class CustomWorkerFactory @Inject constructor(val dao: ChatDao) : WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = SyncWorker(
        dao = dao,
        context = appContext,
        params = workerParameters
    )

}