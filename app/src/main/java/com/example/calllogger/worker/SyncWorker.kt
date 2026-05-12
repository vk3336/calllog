package com.example.calllogger.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.calllogger.data.AppDatabase
import com.example.calllogger.network.RetrofitClient
import com.example.calllogger.service.EspoSyncManager
import com.example.calllogger.util.ConfigManager

/**
 * WorkManager worker that runs every 1-2 hours.
 * - Reads new call logs from Android system
 * - Syncs pending records to EspoCRM in batches
 * - Does nothing if there are no pending records
 * - Respects Android battery optimization (Doze mode)
 */
class SyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "SyncWorker"
        const val WORK_NAME = "espo_sync_worker"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "SyncWorker started")

        val configManager = ConfigManager.getInstance(context)

        // Skip if ESPO is not configured or sync is disabled
        if (!configManager.isEspoConfigured()) {
            Log.d(TAG, "ESPO not configured, skipping sync")
            return Result.success()
        }

        if (!configManager.isSyncEnabled) {
            Log.d(TAG, "Sync disabled, skipping")
            return Result.success()
        }

        return try {
            val database = AppDatabase.getDatabase(context)

            // Reset any stuck IN_PROGRESS rows from a previous crash
            database.callLogDao().resetStuckInProgressRows()

            val pendingCount = database.callLogDao().getPendingCallLogs().size
            Log.d(TAG, "Pending calls to sync: $pendingCount")

            if (pendingCount == 0) {
                Log.d(TAG, "No pending calls, nothing to do")
                return Result.success()
            }

            // Get the cached API service (reuses existing HTTP connection pool)
            val baseUrl = configManager.espoBaseUrl!!
            val apiKey = configManager.espoApiKey!!
            val finalUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
            val espoApi = RetrofitClient.getInstance().createEspoApiService(finalUrl, apiKey)

            val syncManager = EspoSyncManager(context)
            syncManager.syncPendingCalls(espoApi)

            Log.d(TAG, "SyncWorker completed successfully")
            Result.success()

        } catch (e: Exception) {
            Log.e(TAG, "SyncWorker failed: ${e.message}", e)
            // Retry up to 3 times with exponential backoff
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
