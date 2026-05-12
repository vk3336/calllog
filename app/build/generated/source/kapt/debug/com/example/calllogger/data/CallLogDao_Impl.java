package com.example.calllogger.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CallLogDao_Impl implements CallLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CallLogEntity> __insertionAdapterOfCallLogEntity;

  private final EntityDeletionOrUpdateAdapter<CallLogEntity> __updateAdapterOfCallLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfResetFailedToPending;

  private final SharedSQLiteStatement __preparedStmtOfClaimForSync;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsFailed;

  private final SharedSQLiteStatement __preparedStmtOfResetStuckInProgressRows;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldCallLogs;

  public CallLogDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCallLogEntity = new EntityInsertionAdapter<CallLogEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `call_logs` (`id`,`systemCallId`,`phoneNumber`,`contactName`,`callType`,`duration`,`timestamp`,`geocodedLocation`,`phoneAccountId`,`viaNumber`,`voicemailTranscription`,`isRead`,`isNew`,`countryIso`,`dataUsage`,`features`,`numberPresentation`,`postDialDigits`,`createdAt`,`updatedAt`,`syncStatus`,`syncAttempts`,`lastSyncAttempt`,`espoId`,`isCallCompleted`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CallLogEntity value) {
        stmt.bindLong(1, value.getId());
        if (value.getSystemCallId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSystemCallId());
        }
        if (value.getPhoneNumber() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPhoneNumber());
        }
        if (value.getContactName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getContactName());
        }
        stmt.bindLong(5, value.getCallType());
        stmt.bindLong(6, value.getDuration());
        stmt.bindLong(7, value.getTimestamp());
        if (value.getGeocodedLocation() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getGeocodedLocation());
        }
        if (value.getPhoneAccountId() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getPhoneAccountId());
        }
        if (value.getViaNumber() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getViaNumber());
        }
        if (value.getVoicemailTranscription() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getVoicemailTranscription());
        }
        stmt.bindLong(12, value.isRead());
        stmt.bindLong(13, value.isNew());
        if (value.getCountryIso() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getCountryIso());
        }
        if (value.getDataUsage() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindLong(15, value.getDataUsage());
        }
        stmt.bindLong(16, value.getFeatures());
        stmt.bindLong(17, value.getNumberPresentation());
        if (value.getPostDialDigits() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getPostDialDigits());
        }
        stmt.bindLong(19, value.getCreatedAt());
        stmt.bindLong(20, value.getUpdatedAt());
        stmt.bindLong(21, value.getSyncStatus());
        stmt.bindLong(22, value.getSyncAttempts());
        stmt.bindLong(23, value.getLastSyncAttempt());
        if (value.getEspoId() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getEspoId());
        }
        final int _tmp = value.isCallCompleted() ? 1 : 0;
        stmt.bindLong(25, _tmp);
      }
    };
    this.__updateAdapterOfCallLogEntity = new EntityDeletionOrUpdateAdapter<CallLogEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `call_logs` SET `id` = ?,`systemCallId` = ?,`phoneNumber` = ?,`contactName` = ?,`callType` = ?,`duration` = ?,`timestamp` = ?,`geocodedLocation` = ?,`phoneAccountId` = ?,`viaNumber` = ?,`voicemailTranscription` = ?,`isRead` = ?,`isNew` = ?,`countryIso` = ?,`dataUsage` = ?,`features` = ?,`numberPresentation` = ?,`postDialDigits` = ?,`createdAt` = ?,`updatedAt` = ?,`syncStatus` = ?,`syncAttempts` = ?,`lastSyncAttempt` = ?,`espoId` = ?,`isCallCompleted` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CallLogEntity value) {
        stmt.bindLong(1, value.getId());
        if (value.getSystemCallId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSystemCallId());
        }
        if (value.getPhoneNumber() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPhoneNumber());
        }
        if (value.getContactName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getContactName());
        }
        stmt.bindLong(5, value.getCallType());
        stmt.bindLong(6, value.getDuration());
        stmt.bindLong(7, value.getTimestamp());
        if (value.getGeocodedLocation() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getGeocodedLocation());
        }
        if (value.getPhoneAccountId() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getPhoneAccountId());
        }
        if (value.getViaNumber() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getViaNumber());
        }
        if (value.getVoicemailTranscription() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getVoicemailTranscription());
        }
        stmt.bindLong(12, value.isRead());
        stmt.bindLong(13, value.isNew());
        if (value.getCountryIso() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getCountryIso());
        }
        if (value.getDataUsage() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindLong(15, value.getDataUsage());
        }
        stmt.bindLong(16, value.getFeatures());
        stmt.bindLong(17, value.getNumberPresentation());
        if (value.getPostDialDigits() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getPostDialDigits());
        }
        stmt.bindLong(19, value.getCreatedAt());
        stmt.bindLong(20, value.getUpdatedAt());
        stmt.bindLong(21, value.getSyncStatus());
        stmt.bindLong(22, value.getSyncAttempts());
        stmt.bindLong(23, value.getLastSyncAttempt());
        if (value.getEspoId() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getEspoId());
        }
        final int _tmp = value.isCallCompleted() ? 1 : 0;
        stmt.bindLong(25, _tmp);
        stmt.bindLong(26, value.getId());
      }
    };
    this.__preparedStmtOfResetFailedToPending = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE call_logs SET syncStatus = 0 WHERE syncStatus = 3 AND syncAttempts < 3";
        return _query;
      }
    };
    this.__preparedStmtOfClaimForSync = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE call_logs SET syncStatus = 1 WHERE id = ? AND syncStatus = 0";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE call_logs SET syncStatus = 2, espoId = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAsFailed = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE call_logs SET syncStatus = 3, syncAttempts = syncAttempts + 1, lastSyncAttempt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetStuckInProgressRows = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE call_logs SET syncStatus = 0 WHERE syncStatus = 1";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldCallLogs = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM call_logs WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCallLog(final CallLogEntity callLog,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfCallLogEntity.insertAndReturnId(callLog);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCallLog(final CallLogEntity callLog,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCallLogEntity.handle(callLog);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object resetFailedToPending(final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetFailedToPending.acquire();
        __db.beginTransaction();
        try {
          final java.lang.Integer _result = _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
          __preparedStmtOfResetFailedToPending.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object claimForSync(final long id, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClaimForSync.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          final java.lang.Integer _result = _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
          __preparedStmtOfClaimForSync.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsSynced(final long id, final String espoId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsSynced.acquire();
        int _argIndex = 1;
        if (espoId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, espoId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfMarkAsSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsFailed(final long id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsFailed.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfMarkAsFailed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetStuckInProgressRows(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetStuckInProgressRows.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfResetStuckInProgressRows.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldCallLogs(final long cutoffTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldCallLogs.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cutoffTime);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteOldCallLogs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CallLogEntity>> getAllCallLogs() {
    final String _sql = "SELECT * FROM call_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"call_logs"}, new Callable<List<CallLogEntity>>() {
      @Override
      public List<CallLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSystemCallId = CursorUtil.getColumnIndexOrThrow(_cursor, "systemCallId");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGeocodedLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "geocodedLocation");
          final int _cursorIndexOfPhoneAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneAccountId");
          final int _cursorIndexOfViaNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "viaNumber");
          final int _cursorIndexOfVoicemailTranscription = CursorUtil.getColumnIndexOrThrow(_cursor, "voicemailTranscription");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfIsNew = CursorUtil.getColumnIndexOrThrow(_cursor, "isNew");
          final int _cursorIndexOfCountryIso = CursorUtil.getColumnIndexOrThrow(_cursor, "countryIso");
          final int _cursorIndexOfDataUsage = CursorUtil.getColumnIndexOrThrow(_cursor, "dataUsage");
          final int _cursorIndexOfFeatures = CursorUtil.getColumnIndexOrThrow(_cursor, "features");
          final int _cursorIndexOfNumberPresentation = CursorUtil.getColumnIndexOrThrow(_cursor, "numberPresentation");
          final int _cursorIndexOfPostDialDigits = CursorUtil.getColumnIndexOrThrow(_cursor, "postDialDigits");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfSyncAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "syncAttempts");
          final int _cursorIndexOfLastSyncAttempt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncAttempt");
          final int _cursorIndexOfEspoId = CursorUtil.getColumnIndexOrThrow(_cursor, "espoId");
          final int _cursorIndexOfIsCallCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallCompleted");
          final List<CallLogEntity> _result = new ArrayList<CallLogEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final CallLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSystemCallId;
            if (_cursor.isNull(_cursorIndexOfSystemCallId)) {
              _tmpSystemCallId = null;
            } else {
              _tmpSystemCallId = _cursor.getString(_cursorIndexOfSystemCallId);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final int _tmpCallType;
            _tmpCallType = _cursor.getInt(_cursorIndexOfCallType);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGeocodedLocation;
            if (_cursor.isNull(_cursorIndexOfGeocodedLocation)) {
              _tmpGeocodedLocation = null;
            } else {
              _tmpGeocodedLocation = _cursor.getString(_cursorIndexOfGeocodedLocation);
            }
            final String _tmpPhoneAccountId;
            if (_cursor.isNull(_cursorIndexOfPhoneAccountId)) {
              _tmpPhoneAccountId = null;
            } else {
              _tmpPhoneAccountId = _cursor.getString(_cursorIndexOfPhoneAccountId);
            }
            final String _tmpViaNumber;
            if (_cursor.isNull(_cursorIndexOfViaNumber)) {
              _tmpViaNumber = null;
            } else {
              _tmpViaNumber = _cursor.getString(_cursorIndexOfViaNumber);
            }
            final String _tmpVoicemailTranscription;
            if (_cursor.isNull(_cursorIndexOfVoicemailTranscription)) {
              _tmpVoicemailTranscription = null;
            } else {
              _tmpVoicemailTranscription = _cursor.getString(_cursorIndexOfVoicemailTranscription);
            }
            final int _tmpIsRead;
            _tmpIsRead = _cursor.getInt(_cursorIndexOfIsRead);
            final int _tmpIsNew;
            _tmpIsNew = _cursor.getInt(_cursorIndexOfIsNew);
            final String _tmpCountryIso;
            if (_cursor.isNull(_cursorIndexOfCountryIso)) {
              _tmpCountryIso = null;
            } else {
              _tmpCountryIso = _cursor.getString(_cursorIndexOfCountryIso);
            }
            final Long _tmpDataUsage;
            if (_cursor.isNull(_cursorIndexOfDataUsage)) {
              _tmpDataUsage = null;
            } else {
              _tmpDataUsage = _cursor.getLong(_cursorIndexOfDataUsage);
            }
            final int _tmpFeatures;
            _tmpFeatures = _cursor.getInt(_cursorIndexOfFeatures);
            final int _tmpNumberPresentation;
            _tmpNumberPresentation = _cursor.getInt(_cursorIndexOfNumberPresentation);
            final String _tmpPostDialDigits;
            if (_cursor.isNull(_cursorIndexOfPostDialDigits)) {
              _tmpPostDialDigits = null;
            } else {
              _tmpPostDialDigits = _cursor.getString(_cursorIndexOfPostDialDigits);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getInt(_cursorIndexOfSyncStatus);
            final int _tmpSyncAttempts;
            _tmpSyncAttempts = _cursor.getInt(_cursorIndexOfSyncAttempts);
            final long _tmpLastSyncAttempt;
            _tmpLastSyncAttempt = _cursor.getLong(_cursorIndexOfLastSyncAttempt);
            final String _tmpEspoId;
            if (_cursor.isNull(_cursorIndexOfEspoId)) {
              _tmpEspoId = null;
            } else {
              _tmpEspoId = _cursor.getString(_cursorIndexOfEspoId);
            }
            final boolean _tmpIsCallCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCallCompleted);
            _tmpIsCallCompleted = _tmp != 0;
            _item = new CallLogEntity(_tmpId,_tmpSystemCallId,_tmpPhoneNumber,_tmpContactName,_tmpCallType,_tmpDuration,_tmpTimestamp,_tmpGeocodedLocation,_tmpPhoneAccountId,_tmpViaNumber,_tmpVoicemailTranscription,_tmpIsRead,_tmpIsNew,_tmpCountryIso,_tmpDataUsage,_tmpFeatures,_tmpNumberPresentation,_tmpPostDialDigits,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncStatus,_tmpSyncAttempts,_tmpLastSyncAttempt,_tmpEspoId,_tmpIsCallCompleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPendingCallLogs(final Continuation<? super List<CallLogEntity>> $completion) {
    final String _sql = "SELECT * FROM call_logs WHERE syncStatus = 0 ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CallLogEntity>>() {
      @Override
      public List<CallLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSystemCallId = CursorUtil.getColumnIndexOrThrow(_cursor, "systemCallId");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGeocodedLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "geocodedLocation");
          final int _cursorIndexOfPhoneAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneAccountId");
          final int _cursorIndexOfViaNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "viaNumber");
          final int _cursorIndexOfVoicemailTranscription = CursorUtil.getColumnIndexOrThrow(_cursor, "voicemailTranscription");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfIsNew = CursorUtil.getColumnIndexOrThrow(_cursor, "isNew");
          final int _cursorIndexOfCountryIso = CursorUtil.getColumnIndexOrThrow(_cursor, "countryIso");
          final int _cursorIndexOfDataUsage = CursorUtil.getColumnIndexOrThrow(_cursor, "dataUsage");
          final int _cursorIndexOfFeatures = CursorUtil.getColumnIndexOrThrow(_cursor, "features");
          final int _cursorIndexOfNumberPresentation = CursorUtil.getColumnIndexOrThrow(_cursor, "numberPresentation");
          final int _cursorIndexOfPostDialDigits = CursorUtil.getColumnIndexOrThrow(_cursor, "postDialDigits");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfSyncAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "syncAttempts");
          final int _cursorIndexOfLastSyncAttempt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncAttempt");
          final int _cursorIndexOfEspoId = CursorUtil.getColumnIndexOrThrow(_cursor, "espoId");
          final int _cursorIndexOfIsCallCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallCompleted");
          final List<CallLogEntity> _result = new ArrayList<CallLogEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final CallLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSystemCallId;
            if (_cursor.isNull(_cursorIndexOfSystemCallId)) {
              _tmpSystemCallId = null;
            } else {
              _tmpSystemCallId = _cursor.getString(_cursorIndexOfSystemCallId);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final int _tmpCallType;
            _tmpCallType = _cursor.getInt(_cursorIndexOfCallType);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGeocodedLocation;
            if (_cursor.isNull(_cursorIndexOfGeocodedLocation)) {
              _tmpGeocodedLocation = null;
            } else {
              _tmpGeocodedLocation = _cursor.getString(_cursorIndexOfGeocodedLocation);
            }
            final String _tmpPhoneAccountId;
            if (_cursor.isNull(_cursorIndexOfPhoneAccountId)) {
              _tmpPhoneAccountId = null;
            } else {
              _tmpPhoneAccountId = _cursor.getString(_cursorIndexOfPhoneAccountId);
            }
            final String _tmpViaNumber;
            if (_cursor.isNull(_cursorIndexOfViaNumber)) {
              _tmpViaNumber = null;
            } else {
              _tmpViaNumber = _cursor.getString(_cursorIndexOfViaNumber);
            }
            final String _tmpVoicemailTranscription;
            if (_cursor.isNull(_cursorIndexOfVoicemailTranscription)) {
              _tmpVoicemailTranscription = null;
            } else {
              _tmpVoicemailTranscription = _cursor.getString(_cursorIndexOfVoicemailTranscription);
            }
            final int _tmpIsRead;
            _tmpIsRead = _cursor.getInt(_cursorIndexOfIsRead);
            final int _tmpIsNew;
            _tmpIsNew = _cursor.getInt(_cursorIndexOfIsNew);
            final String _tmpCountryIso;
            if (_cursor.isNull(_cursorIndexOfCountryIso)) {
              _tmpCountryIso = null;
            } else {
              _tmpCountryIso = _cursor.getString(_cursorIndexOfCountryIso);
            }
            final Long _tmpDataUsage;
            if (_cursor.isNull(_cursorIndexOfDataUsage)) {
              _tmpDataUsage = null;
            } else {
              _tmpDataUsage = _cursor.getLong(_cursorIndexOfDataUsage);
            }
            final int _tmpFeatures;
            _tmpFeatures = _cursor.getInt(_cursorIndexOfFeatures);
            final int _tmpNumberPresentation;
            _tmpNumberPresentation = _cursor.getInt(_cursorIndexOfNumberPresentation);
            final String _tmpPostDialDigits;
            if (_cursor.isNull(_cursorIndexOfPostDialDigits)) {
              _tmpPostDialDigits = null;
            } else {
              _tmpPostDialDigits = _cursor.getString(_cursorIndexOfPostDialDigits);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getInt(_cursorIndexOfSyncStatus);
            final int _tmpSyncAttempts;
            _tmpSyncAttempts = _cursor.getInt(_cursorIndexOfSyncAttempts);
            final long _tmpLastSyncAttempt;
            _tmpLastSyncAttempt = _cursor.getLong(_cursorIndexOfLastSyncAttempt);
            final String _tmpEspoId;
            if (_cursor.isNull(_cursorIndexOfEspoId)) {
              _tmpEspoId = null;
            } else {
              _tmpEspoId = _cursor.getString(_cursorIndexOfEspoId);
            }
            final boolean _tmpIsCallCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCallCompleted);
            _tmpIsCallCompleted = _tmp != 0;
            _item = new CallLogEntity(_tmpId,_tmpSystemCallId,_tmpPhoneNumber,_tmpContactName,_tmpCallType,_tmpDuration,_tmpTimestamp,_tmpGeocodedLocation,_tmpPhoneAccountId,_tmpViaNumber,_tmpVoicemailTranscription,_tmpIsRead,_tmpIsNew,_tmpCountryIso,_tmpDataUsage,_tmpFeatures,_tmpNumberPresentation,_tmpPostDialDigits,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncStatus,_tmpSyncAttempts,_tmpLastSyncAttempt,_tmpEspoId,_tmpIsCallCompleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getFailedCallLogsForRetry(
      final Continuation<? super List<CallLogEntity>> $completion) {
    final String _sql = "SELECT * FROM call_logs WHERE syncStatus = 3 AND syncAttempts < 3 ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CallLogEntity>>() {
      @Override
      public List<CallLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSystemCallId = CursorUtil.getColumnIndexOrThrow(_cursor, "systemCallId");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGeocodedLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "geocodedLocation");
          final int _cursorIndexOfPhoneAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneAccountId");
          final int _cursorIndexOfViaNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "viaNumber");
          final int _cursorIndexOfVoicemailTranscription = CursorUtil.getColumnIndexOrThrow(_cursor, "voicemailTranscription");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfIsNew = CursorUtil.getColumnIndexOrThrow(_cursor, "isNew");
          final int _cursorIndexOfCountryIso = CursorUtil.getColumnIndexOrThrow(_cursor, "countryIso");
          final int _cursorIndexOfDataUsage = CursorUtil.getColumnIndexOrThrow(_cursor, "dataUsage");
          final int _cursorIndexOfFeatures = CursorUtil.getColumnIndexOrThrow(_cursor, "features");
          final int _cursorIndexOfNumberPresentation = CursorUtil.getColumnIndexOrThrow(_cursor, "numberPresentation");
          final int _cursorIndexOfPostDialDigits = CursorUtil.getColumnIndexOrThrow(_cursor, "postDialDigits");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfSyncAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "syncAttempts");
          final int _cursorIndexOfLastSyncAttempt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncAttempt");
          final int _cursorIndexOfEspoId = CursorUtil.getColumnIndexOrThrow(_cursor, "espoId");
          final int _cursorIndexOfIsCallCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallCompleted");
          final List<CallLogEntity> _result = new ArrayList<CallLogEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final CallLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSystemCallId;
            if (_cursor.isNull(_cursorIndexOfSystemCallId)) {
              _tmpSystemCallId = null;
            } else {
              _tmpSystemCallId = _cursor.getString(_cursorIndexOfSystemCallId);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final int _tmpCallType;
            _tmpCallType = _cursor.getInt(_cursorIndexOfCallType);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGeocodedLocation;
            if (_cursor.isNull(_cursorIndexOfGeocodedLocation)) {
              _tmpGeocodedLocation = null;
            } else {
              _tmpGeocodedLocation = _cursor.getString(_cursorIndexOfGeocodedLocation);
            }
            final String _tmpPhoneAccountId;
            if (_cursor.isNull(_cursorIndexOfPhoneAccountId)) {
              _tmpPhoneAccountId = null;
            } else {
              _tmpPhoneAccountId = _cursor.getString(_cursorIndexOfPhoneAccountId);
            }
            final String _tmpViaNumber;
            if (_cursor.isNull(_cursorIndexOfViaNumber)) {
              _tmpViaNumber = null;
            } else {
              _tmpViaNumber = _cursor.getString(_cursorIndexOfViaNumber);
            }
            final String _tmpVoicemailTranscription;
            if (_cursor.isNull(_cursorIndexOfVoicemailTranscription)) {
              _tmpVoicemailTranscription = null;
            } else {
              _tmpVoicemailTranscription = _cursor.getString(_cursorIndexOfVoicemailTranscription);
            }
            final int _tmpIsRead;
            _tmpIsRead = _cursor.getInt(_cursorIndexOfIsRead);
            final int _tmpIsNew;
            _tmpIsNew = _cursor.getInt(_cursorIndexOfIsNew);
            final String _tmpCountryIso;
            if (_cursor.isNull(_cursorIndexOfCountryIso)) {
              _tmpCountryIso = null;
            } else {
              _tmpCountryIso = _cursor.getString(_cursorIndexOfCountryIso);
            }
            final Long _tmpDataUsage;
            if (_cursor.isNull(_cursorIndexOfDataUsage)) {
              _tmpDataUsage = null;
            } else {
              _tmpDataUsage = _cursor.getLong(_cursorIndexOfDataUsage);
            }
            final int _tmpFeatures;
            _tmpFeatures = _cursor.getInt(_cursorIndexOfFeatures);
            final int _tmpNumberPresentation;
            _tmpNumberPresentation = _cursor.getInt(_cursorIndexOfNumberPresentation);
            final String _tmpPostDialDigits;
            if (_cursor.isNull(_cursorIndexOfPostDialDigits)) {
              _tmpPostDialDigits = null;
            } else {
              _tmpPostDialDigits = _cursor.getString(_cursorIndexOfPostDialDigits);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getInt(_cursorIndexOfSyncStatus);
            final int _tmpSyncAttempts;
            _tmpSyncAttempts = _cursor.getInt(_cursorIndexOfSyncAttempts);
            final long _tmpLastSyncAttempt;
            _tmpLastSyncAttempt = _cursor.getLong(_cursorIndexOfLastSyncAttempt);
            final String _tmpEspoId;
            if (_cursor.isNull(_cursorIndexOfEspoId)) {
              _tmpEspoId = null;
            } else {
              _tmpEspoId = _cursor.getString(_cursorIndexOfEspoId);
            }
            final boolean _tmpIsCallCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCallCompleted);
            _tmpIsCallCompleted = _tmp != 0;
            _item = new CallLogEntity(_tmpId,_tmpSystemCallId,_tmpPhoneNumber,_tmpContactName,_tmpCallType,_tmpDuration,_tmpTimestamp,_tmpGeocodedLocation,_tmpPhoneAccountId,_tmpViaNumber,_tmpVoicemailTranscription,_tmpIsRead,_tmpIsNew,_tmpCountryIso,_tmpDataUsage,_tmpFeatures,_tmpNumberPresentation,_tmpPostDialDigits,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncStatus,_tmpSyncAttempts,_tmpLastSyncAttempt,_tmpEspoId,_tmpIsCallCompleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCallLogByNumberAndTime(final String phoneNumber, final long timestamp,
      final Continuation<? super CallLogEntity> $completion) {
    final String _sql = "SELECT * FROM call_logs WHERE phoneNumber = ? AND timestamp = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (phoneNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, phoneNumber);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, timestamp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CallLogEntity>() {
      @Override
      public CallLogEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSystemCallId = CursorUtil.getColumnIndexOrThrow(_cursor, "systemCallId");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGeocodedLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "geocodedLocation");
          final int _cursorIndexOfPhoneAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneAccountId");
          final int _cursorIndexOfViaNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "viaNumber");
          final int _cursorIndexOfVoicemailTranscription = CursorUtil.getColumnIndexOrThrow(_cursor, "voicemailTranscription");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfIsNew = CursorUtil.getColumnIndexOrThrow(_cursor, "isNew");
          final int _cursorIndexOfCountryIso = CursorUtil.getColumnIndexOrThrow(_cursor, "countryIso");
          final int _cursorIndexOfDataUsage = CursorUtil.getColumnIndexOrThrow(_cursor, "dataUsage");
          final int _cursorIndexOfFeatures = CursorUtil.getColumnIndexOrThrow(_cursor, "features");
          final int _cursorIndexOfNumberPresentation = CursorUtil.getColumnIndexOrThrow(_cursor, "numberPresentation");
          final int _cursorIndexOfPostDialDigits = CursorUtil.getColumnIndexOrThrow(_cursor, "postDialDigits");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfSyncAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "syncAttempts");
          final int _cursorIndexOfLastSyncAttempt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncAttempt");
          final int _cursorIndexOfEspoId = CursorUtil.getColumnIndexOrThrow(_cursor, "espoId");
          final int _cursorIndexOfIsCallCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallCompleted");
          final CallLogEntity _result;
          if(_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSystemCallId;
            if (_cursor.isNull(_cursorIndexOfSystemCallId)) {
              _tmpSystemCallId = null;
            } else {
              _tmpSystemCallId = _cursor.getString(_cursorIndexOfSystemCallId);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final int _tmpCallType;
            _tmpCallType = _cursor.getInt(_cursorIndexOfCallType);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGeocodedLocation;
            if (_cursor.isNull(_cursorIndexOfGeocodedLocation)) {
              _tmpGeocodedLocation = null;
            } else {
              _tmpGeocodedLocation = _cursor.getString(_cursorIndexOfGeocodedLocation);
            }
            final String _tmpPhoneAccountId;
            if (_cursor.isNull(_cursorIndexOfPhoneAccountId)) {
              _tmpPhoneAccountId = null;
            } else {
              _tmpPhoneAccountId = _cursor.getString(_cursorIndexOfPhoneAccountId);
            }
            final String _tmpViaNumber;
            if (_cursor.isNull(_cursorIndexOfViaNumber)) {
              _tmpViaNumber = null;
            } else {
              _tmpViaNumber = _cursor.getString(_cursorIndexOfViaNumber);
            }
            final String _tmpVoicemailTranscription;
            if (_cursor.isNull(_cursorIndexOfVoicemailTranscription)) {
              _tmpVoicemailTranscription = null;
            } else {
              _tmpVoicemailTranscription = _cursor.getString(_cursorIndexOfVoicemailTranscription);
            }
            final int _tmpIsRead;
            _tmpIsRead = _cursor.getInt(_cursorIndexOfIsRead);
            final int _tmpIsNew;
            _tmpIsNew = _cursor.getInt(_cursorIndexOfIsNew);
            final String _tmpCountryIso;
            if (_cursor.isNull(_cursorIndexOfCountryIso)) {
              _tmpCountryIso = null;
            } else {
              _tmpCountryIso = _cursor.getString(_cursorIndexOfCountryIso);
            }
            final Long _tmpDataUsage;
            if (_cursor.isNull(_cursorIndexOfDataUsage)) {
              _tmpDataUsage = null;
            } else {
              _tmpDataUsage = _cursor.getLong(_cursorIndexOfDataUsage);
            }
            final int _tmpFeatures;
            _tmpFeatures = _cursor.getInt(_cursorIndexOfFeatures);
            final int _tmpNumberPresentation;
            _tmpNumberPresentation = _cursor.getInt(_cursorIndexOfNumberPresentation);
            final String _tmpPostDialDigits;
            if (_cursor.isNull(_cursorIndexOfPostDialDigits)) {
              _tmpPostDialDigits = null;
            } else {
              _tmpPostDialDigits = _cursor.getString(_cursorIndexOfPostDialDigits);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getInt(_cursorIndexOfSyncStatus);
            final int _tmpSyncAttempts;
            _tmpSyncAttempts = _cursor.getInt(_cursorIndexOfSyncAttempts);
            final long _tmpLastSyncAttempt;
            _tmpLastSyncAttempt = _cursor.getLong(_cursorIndexOfLastSyncAttempt);
            final String _tmpEspoId;
            if (_cursor.isNull(_cursorIndexOfEspoId)) {
              _tmpEspoId = null;
            } else {
              _tmpEspoId = _cursor.getString(_cursorIndexOfEspoId);
            }
            final boolean _tmpIsCallCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCallCompleted);
            _tmpIsCallCompleted = _tmp != 0;
            _result = new CallLogEntity(_tmpId,_tmpSystemCallId,_tmpPhoneNumber,_tmpContactName,_tmpCallType,_tmpDuration,_tmpTimestamp,_tmpGeocodedLocation,_tmpPhoneAccountId,_tmpViaNumber,_tmpVoicemailTranscription,_tmpIsRead,_tmpIsNew,_tmpCountryIso,_tmpDataUsage,_tmpFeatures,_tmpNumberPresentation,_tmpPostDialDigits,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncStatus,_tmpSyncAttempts,_tmpLastSyncAttempt,_tmpEspoId,_tmpIsCallCompleted);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCallLogBySystemId(final String systemCallId,
      final Continuation<? super CallLogEntity> $completion) {
    final String _sql = "SELECT * FROM call_logs WHERE systemCallId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (systemCallId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, systemCallId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CallLogEntity>() {
      @Override
      public CallLogEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSystemCallId = CursorUtil.getColumnIndexOrThrow(_cursor, "systemCallId");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGeocodedLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "geocodedLocation");
          final int _cursorIndexOfPhoneAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneAccountId");
          final int _cursorIndexOfViaNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "viaNumber");
          final int _cursorIndexOfVoicemailTranscription = CursorUtil.getColumnIndexOrThrow(_cursor, "voicemailTranscription");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfIsNew = CursorUtil.getColumnIndexOrThrow(_cursor, "isNew");
          final int _cursorIndexOfCountryIso = CursorUtil.getColumnIndexOrThrow(_cursor, "countryIso");
          final int _cursorIndexOfDataUsage = CursorUtil.getColumnIndexOrThrow(_cursor, "dataUsage");
          final int _cursorIndexOfFeatures = CursorUtil.getColumnIndexOrThrow(_cursor, "features");
          final int _cursorIndexOfNumberPresentation = CursorUtil.getColumnIndexOrThrow(_cursor, "numberPresentation");
          final int _cursorIndexOfPostDialDigits = CursorUtil.getColumnIndexOrThrow(_cursor, "postDialDigits");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfSyncAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "syncAttempts");
          final int _cursorIndexOfLastSyncAttempt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncAttempt");
          final int _cursorIndexOfEspoId = CursorUtil.getColumnIndexOrThrow(_cursor, "espoId");
          final int _cursorIndexOfIsCallCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallCompleted");
          final CallLogEntity _result;
          if(_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSystemCallId;
            if (_cursor.isNull(_cursorIndexOfSystemCallId)) {
              _tmpSystemCallId = null;
            } else {
              _tmpSystemCallId = _cursor.getString(_cursorIndexOfSystemCallId);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final int _tmpCallType;
            _tmpCallType = _cursor.getInt(_cursorIndexOfCallType);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGeocodedLocation;
            if (_cursor.isNull(_cursorIndexOfGeocodedLocation)) {
              _tmpGeocodedLocation = null;
            } else {
              _tmpGeocodedLocation = _cursor.getString(_cursorIndexOfGeocodedLocation);
            }
            final String _tmpPhoneAccountId;
            if (_cursor.isNull(_cursorIndexOfPhoneAccountId)) {
              _tmpPhoneAccountId = null;
            } else {
              _tmpPhoneAccountId = _cursor.getString(_cursorIndexOfPhoneAccountId);
            }
            final String _tmpViaNumber;
            if (_cursor.isNull(_cursorIndexOfViaNumber)) {
              _tmpViaNumber = null;
            } else {
              _tmpViaNumber = _cursor.getString(_cursorIndexOfViaNumber);
            }
            final String _tmpVoicemailTranscription;
            if (_cursor.isNull(_cursorIndexOfVoicemailTranscription)) {
              _tmpVoicemailTranscription = null;
            } else {
              _tmpVoicemailTranscription = _cursor.getString(_cursorIndexOfVoicemailTranscription);
            }
            final int _tmpIsRead;
            _tmpIsRead = _cursor.getInt(_cursorIndexOfIsRead);
            final int _tmpIsNew;
            _tmpIsNew = _cursor.getInt(_cursorIndexOfIsNew);
            final String _tmpCountryIso;
            if (_cursor.isNull(_cursorIndexOfCountryIso)) {
              _tmpCountryIso = null;
            } else {
              _tmpCountryIso = _cursor.getString(_cursorIndexOfCountryIso);
            }
            final Long _tmpDataUsage;
            if (_cursor.isNull(_cursorIndexOfDataUsage)) {
              _tmpDataUsage = null;
            } else {
              _tmpDataUsage = _cursor.getLong(_cursorIndexOfDataUsage);
            }
            final int _tmpFeatures;
            _tmpFeatures = _cursor.getInt(_cursorIndexOfFeatures);
            final int _tmpNumberPresentation;
            _tmpNumberPresentation = _cursor.getInt(_cursorIndexOfNumberPresentation);
            final String _tmpPostDialDigits;
            if (_cursor.isNull(_cursorIndexOfPostDialDigits)) {
              _tmpPostDialDigits = null;
            } else {
              _tmpPostDialDigits = _cursor.getString(_cursorIndexOfPostDialDigits);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getInt(_cursorIndexOfSyncStatus);
            final int _tmpSyncAttempts;
            _tmpSyncAttempts = _cursor.getInt(_cursorIndexOfSyncAttempts);
            final long _tmpLastSyncAttempt;
            _tmpLastSyncAttempt = _cursor.getLong(_cursorIndexOfLastSyncAttempt);
            final String _tmpEspoId;
            if (_cursor.isNull(_cursorIndexOfEspoId)) {
              _tmpEspoId = null;
            } else {
              _tmpEspoId = _cursor.getString(_cursorIndexOfEspoId);
            }
            final boolean _tmpIsCallCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCallCompleted);
            _tmpIsCallCompleted = _tmp != 0;
            _result = new CallLogEntity(_tmpId,_tmpSystemCallId,_tmpPhoneNumber,_tmpContactName,_tmpCallType,_tmpDuration,_tmpTimestamp,_tmpGeocodedLocation,_tmpPhoneAccountId,_tmpViaNumber,_tmpVoicemailTranscription,_tmpIsRead,_tmpIsNew,_tmpCountryIso,_tmpDataUsage,_tmpFeatures,_tmpNumberPresentation,_tmpPostDialDigits,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncStatus,_tmpSyncAttempts,_tmpLastSyncAttempt,_tmpEspoId,_tmpIsCallCompleted);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCallLogCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM call_logs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSyncedCallLogCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM call_logs WHERE syncStatus = 2";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
