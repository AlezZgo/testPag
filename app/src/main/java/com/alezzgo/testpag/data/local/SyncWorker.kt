package com.alezzgo.testpag.data.local

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alezzgo.testpag.core.di.DataModule
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted val dao: ChatDao,
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context,params) {

    companion object{
        const val TAG = "SyncWorker"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        while (true){
            Log.d(TAG, "doWork: works")
            delay(1000)

            val chat = dao.chat(0).first()
            Log.d(TAG, "doWork: chat=$chat")


        }
        return Result.failure()
    }



}