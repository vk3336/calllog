package com.example.calllogger.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CallLogDao _callLogDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `call_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `phoneNumber` TEXT NOT NULL, `contactName` TEXT, `callType` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `geocodedLocation` TEXT, `phoneAccountId` TEXT, `viaNumber` TEXT, `voicemailTranscription` TEXT, `isRead` INTEGER NOT NULL, `isNew` INTEGER NOT NULL, `countryIso` TEXT, `dataUsage` INTEGER, `features` INTEGER NOT NULL, `numberPresentation` INTEGER NOT NULL, `postDialDigits` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `isSynced` INTEGER NOT NULL, `syncAttempts` INTEGER NOT NULL, `lastSyncAttempt` INTEGER NOT NULL, `espoId` TEXT, `isCallCompleted` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '05b873329400b46ab4191b949cd118b0')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `call_logs`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCallLogs = new HashMap<String, TableInfo.Column>(24);
        _columnsCallLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("contactName", new TableInfo.Column("contactName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("callType", new TableInfo.Column("callType", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("geocodedLocation", new TableInfo.Column("geocodedLocation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("phoneAccountId", new TableInfo.Column("phoneAccountId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("viaNumber", new TableInfo.Column("viaNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("voicemailTranscription", new TableInfo.Column("voicemailTranscription", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("isNew", new TableInfo.Column("isNew", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("countryIso", new TableInfo.Column("countryIso", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("dataUsage", new TableInfo.Column("dataUsage", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("features", new TableInfo.Column("features", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("numberPresentation", new TableInfo.Column("numberPresentation", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("postDialDigits", new TableInfo.Column("postDialDigits", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("syncAttempts", new TableInfo.Column("syncAttempts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("lastSyncAttempt", new TableInfo.Column("lastSyncAttempt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("espoId", new TableInfo.Column("espoId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("isCallCompleted", new TableInfo.Column("isCallCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCallLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCallLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCallLogs = new TableInfo("call_logs", _columnsCallLogs, _foreignKeysCallLogs, _indicesCallLogs);
        final TableInfo _existingCallLogs = TableInfo.read(_db, "call_logs");
        if (! _infoCallLogs.equals(_existingCallLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "call_logs(com.example.calllogger.data.CallLogEntity).\n"
                  + " Expected:\n" + _infoCallLogs + "\n"
                  + " Found:\n" + _existingCallLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "05b873329400b46ab4191b949cd118b0", "dc669d36e298a5b1389559f99101913d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "call_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `call_logs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CallLogDao.class, CallLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public CallLogDao callLogDao() {
    if (_callLogDao != null) {
      return _callLogDao;
    } else {
      synchronized(this) {
        if(_callLogDao == null) {
          _callLogDao = new CallLogDao_Impl(this);
        }
        return _callLogDao;
      }
    }
  }
}
