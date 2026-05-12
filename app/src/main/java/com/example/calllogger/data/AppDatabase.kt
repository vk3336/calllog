package com.example.calllogger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [CallLogEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun callLogDao(): CallLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE call_logs ADD COLUMN geocodedLocation TEXT")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN phoneAccountId TEXT")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN viaNumber TEXT")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN voicemailTranscription TEXT")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN isRead INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN isNew INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN countryIso TEXT")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN dataUsage INTEGER")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN features INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN numberPresentation INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN postDialDigits TEXT")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE call_logs ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE call_logs ADD COLUMN isCallCompleted INTEGER NOT NULL DEFAULT 1")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add systemCallId for dedup + unique index
                database.execSQL("ALTER TABLE call_logs ADD COLUMN systemCallId TEXT")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_call_logs_systemCallId ON call_logs(systemCallId)")
                // Add syncStatus column; migrate old isSynced=1 → SYNCED(2), else PENDING(0)
                database.execSQL("ALTER TABLE call_logs ADD COLUMN syncStatus INTEGER NOT NULL DEFAULT 0")
                database.execSQL("UPDATE call_logs SET syncStatus = 2 WHERE isSynced = 1")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "call_log_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
