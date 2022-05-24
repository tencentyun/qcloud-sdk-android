package com.tencent.cos.xml.constraints;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import androidx.work.NetworkType;
import androidx.work.impl.model.WorkTypeConverters;
import com.tencent.cos.xml.transfer.TransferState;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TransferSpecDao_Impl implements TransferSpecDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTransferSpec;

  private final TransferStateConverters __transferStateConverters = new TransferStateConverters();

  private final SharedSQLiteStatement __preparedStmtOfUpdateTransferSpecWorkerId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTransferSpecServiceEgg;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTransferSpecTransferProgress;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTransferSpecTransferStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTransferSpecUploadId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTransferSpecNetworkConstraints;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAllTransferSpecsNetworkConstraints;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTransferSpec;

  public TransferSpecDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransferSpec = new EntityInsertionAdapter<TransferSpec>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `TransferSpec`(`id`,`region`,`bucket`,`key`,`filePath`,`uploadId`,`headers`,`etag`,`workerId`,`isUpload`,`state`,`complete`,`target`,`transferServiceEgg`,`required_network_type`,`requires_charging`,`requires_device_idle`,`requires_battery_not_low`,`requires_storage_not_low`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TransferSpec value) {
        if (value.id == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.id);
        }
        if (value.region == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.region);
        }
        if (value.bucket == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.bucket);
        }
        if (value.key == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.key);
        }
        if (value.filePath == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.filePath);
        }
        if (value.uploadId == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.uploadId);
        }
        if (value.headers == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.headers);
        }
        if (value.etag == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.etag);
        }
        if (value.workerId == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.workerId);
        }
        final int _tmp;
        _tmp = value.isUpload ? 1 : 0;
        stmt.bindLong(10, _tmp);
        final String _tmp_1;
        _tmp_1 = __transferStateConverters.conver2Str(value.state);
        if (_tmp_1 == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, _tmp_1);
        }
        stmt.bindLong(12, value.complete);
        stmt.bindLong(13, value.target);
        if (value.transferServiceEgg == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.transferServiceEgg);
        }
        final Constraints _tmpConstraints = value.constraints;
        if(_tmpConstraints != null) {
          final int _tmp_2;
          _tmp_2 = WorkTypeConverters.networkTypeToInt(_tmpConstraints.getRequiredNetworkType());
          stmt.bindLong(15, _tmp_2);
          final int _tmp_3;
          _tmp_3 = _tmpConstraints.requiresCharging() ? 1 : 0;
          stmt.bindLong(16, _tmp_3);
          final int _tmp_4;
          _tmp_4 = _tmpConstraints.requiresDeviceIdle() ? 1 : 0;
          stmt.bindLong(17, _tmp_4);
          final int _tmp_5;
          _tmp_5 = _tmpConstraints.requiresBatteryNotLow() ? 1 : 0;
          stmt.bindLong(18, _tmp_5);
          final int _tmp_6;
          _tmp_6 = _tmpConstraints.requiresStorageNotLow() ? 1 : 0;
          stmt.bindLong(19, _tmp_6);
        } else {
          stmt.bindNull(15);
          stmt.bindNull(16);
          stmt.bindNull(17);
          stmt.bindNull(18);
          stmt.bindNull(19);
        }
      }
    };
    this.__preparedStmtOfUpdateTransferSpecWorkerId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE transferspec SET workerId=? WHERE id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTransferSpecServiceEgg = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE transferspec SET transferServiceEgg=? WHERE id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTransferSpecTransferProgress = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE transferspec SET complete=? AND target=? WHERE id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTransferSpecTransferStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE transferspec SET state=? WHERE id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTransferSpecUploadId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE transferspec SET uploadId=? WHERE id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTransferSpecNetworkConstraints = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE transferspec SET required_network_type=? WHERE id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAllTransferSpecsNetworkConstraints = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE transferspec SET required_network_type=?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTransferSpec = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM transferspec WHERE id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertTransferSpec(final TransferSpec transferSpec) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTransferSpec.insert(transferSpec);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTransferSpecWorkerId(final String id, final String workerId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTransferSpecWorkerId.acquire();
    int _argIndex = 1;
    if (workerId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, workerId);
    }
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTransferSpecWorkerId.release(_stmt);
    }
  }

  @Override
  public void updateTransferSpecServiceEgg(final String id, final String serviceEgg) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTransferSpecServiceEgg.acquire();
    int _argIndex = 1;
    if (serviceEgg == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, serviceEgg);
    }
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTransferSpecServiceEgg.release(_stmt);
    }
  }

  @Override
  public void updateTransferSpecTransferProgress(final String id, final long complete,
      final long target) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTransferSpecTransferProgress.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, complete);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, target);
    _argIndex = 3;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTransferSpecTransferProgress.release(_stmt);
    }
  }

  @Override
  public void updateTransferSpecTransferStatus(final String id, final TransferState state) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTransferSpecTransferStatus.acquire();
    int _argIndex = 1;
    final String _tmp;
    _tmp = __transferStateConverters.conver2Str(state);
    if (_tmp == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTransferSpecTransferStatus.release(_stmt);
    }
  }

  @Override
  public void updateTransferSpecUploadId(final String id, final String uploadId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTransferSpecUploadId.acquire();
    int _argIndex = 1;
    if (uploadId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, uploadId);
    }
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTransferSpecUploadId.release(_stmt);
    }
  }

  @Override
  public void updateTransferSpecProgress(final String id, final long complete, final long target) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTransferSpecTransferProgress.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, complete);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, target);
    _argIndex = 3;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTransferSpecTransferProgress.release(_stmt);
    }
  }

  @Override
  public void updateTransferSpecNetworkConstraints(final String id, final NetworkType networkType) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTransferSpecNetworkConstraints.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = WorkTypeConverters.networkTypeToInt(networkType);
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTransferSpecNetworkConstraints.release(_stmt);
    }
  }

  @Override
  public void updateAllTransferSpecsNetworkConstraints(final NetworkType networkType) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAllTransferSpecsNetworkConstraints.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = WorkTypeConverters.networkTypeToInt(networkType);
    _stmt.bindLong(_argIndex, _tmp);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateAllTransferSpecsNetworkConstraints.release(_stmt);
    }
  }

  @Override
  public void deleteTransferSpec(final String id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTransferSpec.acquire();
    int _argIndex = 1;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteTransferSpec.release(_stmt);
    }
  }

  @Override
  public LiveData<List<TransferSpec>> getAllTransferSpecsLiveData() {
    final String _sql = "SELECT * FROM transferspec";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"transferspec"}, false, new Callable<List<TransferSpec>>() {
      @Override
      public List<TransferSpec> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfUploadId = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadId");
          final int _cursorIndexOfHeaders = CursorUtil.getColumnIndexOrThrow(_cursor, "headers");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfWorkerId = CursorUtil.getColumnIndexOrThrow(_cursor, "workerId");
          final int _cursorIndexOfIsUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "isUpload");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "complete");
          final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "target");
          final int _cursorIndexOfTransferServiceEgg = CursorUtil.getColumnIndexOrThrow(_cursor, "transferServiceEgg");
          final int _cursorIndexOfMRequiredNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "required_network_type");
          final int _cursorIndexOfMRequiresCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_charging");
          final int _cursorIndexOfMRequiresDeviceIdle = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_device_idle");
          final int _cursorIndexOfMRequiresBatteryNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_battery_not_low");
          final int _cursorIndexOfMRequiresStorageNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_storage_not_low");
          final List<TransferSpec> _result = new ArrayList<TransferSpec>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TransferSpec _item;
            final Constraints _tmpConstraints;
            _tmpConstraints = new Constraints();
            final NetworkType _tmpMRequiredNetworkType;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMRequiredNetworkType);
            _tmpMRequiredNetworkType = WorkTypeConverters.intToNetworkType(_tmp);
            _tmpConstraints.setRequiredNetworkType(_tmpMRequiredNetworkType);
            final boolean _tmpMRequiresCharging;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfMRequiresCharging);
            _tmpMRequiresCharging = _tmp_1 != 0;
            _tmpConstraints.setRequiresCharging(_tmpMRequiresCharging);
            final boolean _tmpMRequiresDeviceIdle;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMRequiresDeviceIdle);
            _tmpMRequiresDeviceIdle = _tmp_2 != 0;
            _tmpConstraints.setRequiresDeviceIdle(_tmpMRequiresDeviceIdle);
            final boolean _tmpMRequiresBatteryNotLow;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfMRequiresBatteryNotLow);
            _tmpMRequiresBatteryNotLow = _tmp_3 != 0;
            _tmpConstraints.setRequiresBatteryNotLow(_tmpMRequiresBatteryNotLow);
            final boolean _tmpMRequiresStorageNotLow;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfMRequiresStorageNotLow);
            _tmpMRequiresStorageNotLow = _tmp_4 != 0;
            _tmpConstraints.setRequiresStorageNotLow(_tmpMRequiresStorageNotLow);
            _item = new TransferSpec();
            _item.id = _cursor.getString(_cursorIndexOfId);
            _item.region = _cursor.getString(_cursorIndexOfRegion);
            _item.bucket = _cursor.getString(_cursorIndexOfBucket);
            _item.key = _cursor.getString(_cursorIndexOfKey);
            _item.filePath = _cursor.getString(_cursorIndexOfFilePath);
            _item.uploadId = _cursor.getString(_cursorIndexOfUploadId);
            _item.headers = _cursor.getString(_cursorIndexOfHeaders);
            _item.etag = _cursor.getString(_cursorIndexOfEtag);
            _item.workerId = _cursor.getString(_cursorIndexOfWorkerId);
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsUpload);
            _item.isUpload = _tmp_5 != 0;
            final String _tmp_6;
            _tmp_6 = _cursor.getString(_cursorIndexOfState);
            _item.state = __transferStateConverters.convert2Status(_tmp_6);
            _item.complete = _cursor.getLong(_cursorIndexOfComplete);
            _item.target = _cursor.getLong(_cursorIndexOfTarget);
            _item.transferServiceEgg = _cursor.getString(_cursorIndexOfTransferServiceEgg);
            _item.constraints = _tmpConstraints;
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
  public List<TransferSpec> getAllTransferSpecs() {
    final String _sql = "SELECT * FROM transferspec";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
      final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
      final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfUploadId = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadId");
      final int _cursorIndexOfHeaders = CursorUtil.getColumnIndexOrThrow(_cursor, "headers");
      final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
      final int _cursorIndexOfWorkerId = CursorUtil.getColumnIndexOrThrow(_cursor, "workerId");
      final int _cursorIndexOfIsUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "isUpload");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "complete");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "target");
      final int _cursorIndexOfTransferServiceEgg = CursorUtil.getColumnIndexOrThrow(_cursor, "transferServiceEgg");
      final int _cursorIndexOfMRequiredNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "required_network_type");
      final int _cursorIndexOfMRequiresCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_charging");
      final int _cursorIndexOfMRequiresDeviceIdle = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_device_idle");
      final int _cursorIndexOfMRequiresBatteryNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_battery_not_low");
      final int _cursorIndexOfMRequiresStorageNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_storage_not_low");
      final List<TransferSpec> _result = new ArrayList<TransferSpec>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TransferSpec _item;
        final Constraints _tmpConstraints;
        _tmpConstraints = new Constraints();
        final NetworkType _tmpMRequiredNetworkType;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfMRequiredNetworkType);
        _tmpMRequiredNetworkType = WorkTypeConverters.intToNetworkType(_tmp);
        _tmpConstraints.setRequiredNetworkType(_tmpMRequiredNetworkType);
        final boolean _tmpMRequiresCharging;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfMRequiresCharging);
        _tmpMRequiresCharging = _tmp_1 != 0;
        _tmpConstraints.setRequiresCharging(_tmpMRequiresCharging);
        final boolean _tmpMRequiresDeviceIdle;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfMRequiresDeviceIdle);
        _tmpMRequiresDeviceIdle = _tmp_2 != 0;
        _tmpConstraints.setRequiresDeviceIdle(_tmpMRequiresDeviceIdle);
        final boolean _tmpMRequiresBatteryNotLow;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfMRequiresBatteryNotLow);
        _tmpMRequiresBatteryNotLow = _tmp_3 != 0;
        _tmpConstraints.setRequiresBatteryNotLow(_tmpMRequiresBatteryNotLow);
        final boolean _tmpMRequiresStorageNotLow;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfMRequiresStorageNotLow);
        _tmpMRequiresStorageNotLow = _tmp_4 != 0;
        _tmpConstraints.setRequiresStorageNotLow(_tmpMRequiresStorageNotLow);
        _item = new TransferSpec();
        _item.id = _cursor.getString(_cursorIndexOfId);
        _item.region = _cursor.getString(_cursorIndexOfRegion);
        _item.bucket = _cursor.getString(_cursorIndexOfBucket);
        _item.key = _cursor.getString(_cursorIndexOfKey);
        _item.filePath = _cursor.getString(_cursorIndexOfFilePath);
        _item.uploadId = _cursor.getString(_cursorIndexOfUploadId);
        _item.headers = _cursor.getString(_cursorIndexOfHeaders);
        _item.etag = _cursor.getString(_cursorIndexOfEtag);
        _item.workerId = _cursor.getString(_cursorIndexOfWorkerId);
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfIsUpload);
        _item.isUpload = _tmp_5 != 0;
        final String _tmp_6;
        _tmp_6 = _cursor.getString(_cursorIndexOfState);
        _item.state = __transferStateConverters.convert2Status(_tmp_6);
        _item.complete = _cursor.getLong(_cursorIndexOfComplete);
        _item.target = _cursor.getLong(_cursorIndexOfTarget);
        _item.transferServiceEgg = _cursor.getString(_cursorIndexOfTransferServiceEgg);
        _item.constraints = _tmpConstraints;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TransferSpec> getTransferSpecs(final List<String> ids) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM transferspec WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : ids) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
      final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
      final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfUploadId = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadId");
      final int _cursorIndexOfHeaders = CursorUtil.getColumnIndexOrThrow(_cursor, "headers");
      final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
      final int _cursorIndexOfWorkerId = CursorUtil.getColumnIndexOrThrow(_cursor, "workerId");
      final int _cursorIndexOfIsUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "isUpload");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "complete");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "target");
      final int _cursorIndexOfTransferServiceEgg = CursorUtil.getColumnIndexOrThrow(_cursor, "transferServiceEgg");
      final int _cursorIndexOfMRequiredNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "required_network_type");
      final int _cursorIndexOfMRequiresCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_charging");
      final int _cursorIndexOfMRequiresDeviceIdle = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_device_idle");
      final int _cursorIndexOfMRequiresBatteryNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_battery_not_low");
      final int _cursorIndexOfMRequiresStorageNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_storage_not_low");
      final List<TransferSpec> _result = new ArrayList<TransferSpec>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TransferSpec _item_1;
        final Constraints _tmpConstraints;
        _tmpConstraints = new Constraints();
        final NetworkType _tmpMRequiredNetworkType;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfMRequiredNetworkType);
        _tmpMRequiredNetworkType = WorkTypeConverters.intToNetworkType(_tmp);
        _tmpConstraints.setRequiredNetworkType(_tmpMRequiredNetworkType);
        final boolean _tmpMRequiresCharging;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfMRequiresCharging);
        _tmpMRequiresCharging = _tmp_1 != 0;
        _tmpConstraints.setRequiresCharging(_tmpMRequiresCharging);
        final boolean _tmpMRequiresDeviceIdle;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfMRequiresDeviceIdle);
        _tmpMRequiresDeviceIdle = _tmp_2 != 0;
        _tmpConstraints.setRequiresDeviceIdle(_tmpMRequiresDeviceIdle);
        final boolean _tmpMRequiresBatteryNotLow;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfMRequiresBatteryNotLow);
        _tmpMRequiresBatteryNotLow = _tmp_3 != 0;
        _tmpConstraints.setRequiresBatteryNotLow(_tmpMRequiresBatteryNotLow);
        final boolean _tmpMRequiresStorageNotLow;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfMRequiresStorageNotLow);
        _tmpMRequiresStorageNotLow = _tmp_4 != 0;
        _tmpConstraints.setRequiresStorageNotLow(_tmpMRequiresStorageNotLow);
        _item_1 = new TransferSpec();
        _item_1.id = _cursor.getString(_cursorIndexOfId);
        _item_1.region = _cursor.getString(_cursorIndexOfRegion);
        _item_1.bucket = _cursor.getString(_cursorIndexOfBucket);
        _item_1.key = _cursor.getString(_cursorIndexOfKey);
        _item_1.filePath = _cursor.getString(_cursorIndexOfFilePath);
        _item_1.uploadId = _cursor.getString(_cursorIndexOfUploadId);
        _item_1.headers = _cursor.getString(_cursorIndexOfHeaders);
        _item_1.etag = _cursor.getString(_cursorIndexOfEtag);
        _item_1.workerId = _cursor.getString(_cursorIndexOfWorkerId);
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfIsUpload);
        _item_1.isUpload = _tmp_5 != 0;
        final String _tmp_6;
        _tmp_6 = _cursor.getString(_cursorIndexOfState);
        _item_1.state = __transferStateConverters.convert2Status(_tmp_6);
        _item_1.complete = _cursor.getLong(_cursorIndexOfComplete);
        _item_1.target = _cursor.getLong(_cursorIndexOfTarget);
        _item_1.transferServiceEgg = _cursor.getString(_cursorIndexOfTransferServiceEgg);
        _item_1.constraints = _tmpConstraints;
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<TransferState> getTransferSpecsStateLiveData(final String id) {
    final String _sql = "SELECT state FROM transferspec WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"transferspec"}, false, new Callable<TransferState>() {
      @Override
      public TransferState call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final TransferState _result;
          if(_cursor.moveToFirst()) {
            final String _tmp;
            _tmp = _cursor.getString(0);
            _result = __transferStateConverters.convert2Status(_tmp);
          } else {
            _result = null;
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
  public TransferSpec getTransferSpecs(final String id) {
    final String _sql = "SELECT * FROM transferspec WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
      final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
      final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfUploadId = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadId");
      final int _cursorIndexOfHeaders = CursorUtil.getColumnIndexOrThrow(_cursor, "headers");
      final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
      final int _cursorIndexOfWorkerId = CursorUtil.getColumnIndexOrThrow(_cursor, "workerId");
      final int _cursorIndexOfIsUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "isUpload");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "complete");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "target");
      final int _cursorIndexOfTransferServiceEgg = CursorUtil.getColumnIndexOrThrow(_cursor, "transferServiceEgg");
      final int _cursorIndexOfMRequiredNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "required_network_type");
      final int _cursorIndexOfMRequiresCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_charging");
      final int _cursorIndexOfMRequiresDeviceIdle = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_device_idle");
      final int _cursorIndexOfMRequiresBatteryNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_battery_not_low");
      final int _cursorIndexOfMRequiresStorageNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_storage_not_low");
      final TransferSpec _result;
      if(_cursor.moveToFirst()) {
        final Constraints _tmpConstraints;
        _tmpConstraints = new Constraints();
        final NetworkType _tmpMRequiredNetworkType;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfMRequiredNetworkType);
        _tmpMRequiredNetworkType = WorkTypeConverters.intToNetworkType(_tmp);
        _tmpConstraints.setRequiredNetworkType(_tmpMRequiredNetworkType);
        final boolean _tmpMRequiresCharging;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfMRequiresCharging);
        _tmpMRequiresCharging = _tmp_1 != 0;
        _tmpConstraints.setRequiresCharging(_tmpMRequiresCharging);
        final boolean _tmpMRequiresDeviceIdle;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfMRequiresDeviceIdle);
        _tmpMRequiresDeviceIdle = _tmp_2 != 0;
        _tmpConstraints.setRequiresDeviceIdle(_tmpMRequiresDeviceIdle);
        final boolean _tmpMRequiresBatteryNotLow;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfMRequiresBatteryNotLow);
        _tmpMRequiresBatteryNotLow = _tmp_3 != 0;
        _tmpConstraints.setRequiresBatteryNotLow(_tmpMRequiresBatteryNotLow);
        final boolean _tmpMRequiresStorageNotLow;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfMRequiresStorageNotLow);
        _tmpMRequiresStorageNotLow = _tmp_4 != 0;
        _tmpConstraints.setRequiresStorageNotLow(_tmpMRequiresStorageNotLow);
        _result = new TransferSpec();
        _result.id = _cursor.getString(_cursorIndexOfId);
        _result.region = _cursor.getString(_cursorIndexOfRegion);
        _result.bucket = _cursor.getString(_cursorIndexOfBucket);
        _result.key = _cursor.getString(_cursorIndexOfKey);
        _result.filePath = _cursor.getString(_cursorIndexOfFilePath);
        _result.uploadId = _cursor.getString(_cursorIndexOfUploadId);
        _result.headers = _cursor.getString(_cursorIndexOfHeaders);
        _result.etag = _cursor.getString(_cursorIndexOfEtag);
        _result.workerId = _cursor.getString(_cursorIndexOfWorkerId);
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfIsUpload);
        _result.isUpload = _tmp_5 != 0;
        final String _tmp_6;
        _tmp_6 = _cursor.getString(_cursorIndexOfState);
        _result.state = __transferStateConverters.convert2Status(_tmp_6);
        _result.complete = _cursor.getLong(_cursorIndexOfComplete);
        _result.target = _cursor.getLong(_cursorIndexOfTarget);
        _result.transferServiceEgg = _cursor.getString(_cursorIndexOfTransferServiceEgg);
        _result.constraints = _tmpConstraints;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TransferSpec getTransferSpecByEndpoint(final String bucket, final String cosKey,
      final String filePath) {
    final String _sql = "SELECT * FROM transferspec WHERE bucket=? AND `key`=? AND filePath=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (bucket == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, bucket);
    }
    _argIndex = 2;
    if (cosKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, cosKey);
    }
    _argIndex = 3;
    if (filePath == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filePath);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
      final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
      final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfUploadId = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadId");
      final int _cursorIndexOfHeaders = CursorUtil.getColumnIndexOrThrow(_cursor, "headers");
      final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
      final int _cursorIndexOfWorkerId = CursorUtil.getColumnIndexOrThrow(_cursor, "workerId");
      final int _cursorIndexOfIsUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "isUpload");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "complete");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "target");
      final int _cursorIndexOfTransferServiceEgg = CursorUtil.getColumnIndexOrThrow(_cursor, "transferServiceEgg");
      final int _cursorIndexOfMRequiredNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "required_network_type");
      final int _cursorIndexOfMRequiresCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_charging");
      final int _cursorIndexOfMRequiresDeviceIdle = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_device_idle");
      final int _cursorIndexOfMRequiresBatteryNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_battery_not_low");
      final int _cursorIndexOfMRequiresStorageNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_storage_not_low");
      final TransferSpec _result;
      if(_cursor.moveToFirst()) {
        final Constraints _tmpConstraints;
        _tmpConstraints = new Constraints();
        final NetworkType _tmpMRequiredNetworkType;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfMRequiredNetworkType);
        _tmpMRequiredNetworkType = WorkTypeConverters.intToNetworkType(_tmp);
        _tmpConstraints.setRequiredNetworkType(_tmpMRequiredNetworkType);
        final boolean _tmpMRequiresCharging;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfMRequiresCharging);
        _tmpMRequiresCharging = _tmp_1 != 0;
        _tmpConstraints.setRequiresCharging(_tmpMRequiresCharging);
        final boolean _tmpMRequiresDeviceIdle;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfMRequiresDeviceIdle);
        _tmpMRequiresDeviceIdle = _tmp_2 != 0;
        _tmpConstraints.setRequiresDeviceIdle(_tmpMRequiresDeviceIdle);
        final boolean _tmpMRequiresBatteryNotLow;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfMRequiresBatteryNotLow);
        _tmpMRequiresBatteryNotLow = _tmp_3 != 0;
        _tmpConstraints.setRequiresBatteryNotLow(_tmpMRequiresBatteryNotLow);
        final boolean _tmpMRequiresStorageNotLow;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfMRequiresStorageNotLow);
        _tmpMRequiresStorageNotLow = _tmp_4 != 0;
        _tmpConstraints.setRequiresStorageNotLow(_tmpMRequiresStorageNotLow);
        _result = new TransferSpec();
        _result.id = _cursor.getString(_cursorIndexOfId);
        _result.region = _cursor.getString(_cursorIndexOfRegion);
        _result.bucket = _cursor.getString(_cursorIndexOfBucket);
        _result.key = _cursor.getString(_cursorIndexOfKey);
        _result.filePath = _cursor.getString(_cursorIndexOfFilePath);
        _result.uploadId = _cursor.getString(_cursorIndexOfUploadId);
        _result.headers = _cursor.getString(_cursorIndexOfHeaders);
        _result.etag = _cursor.getString(_cursorIndexOfEtag);
        _result.workerId = _cursor.getString(_cursorIndexOfWorkerId);
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfIsUpload);
        _result.isUpload = _tmp_5 != 0;
        final String _tmp_6;
        _tmp_6 = _cursor.getString(_cursorIndexOfState);
        _result.state = __transferStateConverters.convert2Status(_tmp_6);
        _result.complete = _cursor.getLong(_cursorIndexOfComplete);
        _result.target = _cursor.getLong(_cursorIndexOfTarget);
        _result.transferServiceEgg = _cursor.getString(_cursorIndexOfTransferServiceEgg);
        _result.constraints = _tmpConstraints;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<TransferSpec> getTransferSpecsLiveData(final String id) {
    final String _sql = "SELECT * FROM transferspec WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"transferspec"}, false, new Callable<TransferSpec>() {
      @Override
      public TransferSpec call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfUploadId = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadId");
          final int _cursorIndexOfHeaders = CursorUtil.getColumnIndexOrThrow(_cursor, "headers");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfWorkerId = CursorUtil.getColumnIndexOrThrow(_cursor, "workerId");
          final int _cursorIndexOfIsUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "isUpload");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "complete");
          final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "target");
          final int _cursorIndexOfTransferServiceEgg = CursorUtil.getColumnIndexOrThrow(_cursor, "transferServiceEgg");
          final int _cursorIndexOfMRequiredNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "required_network_type");
          final int _cursorIndexOfMRequiresCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_charging");
          final int _cursorIndexOfMRequiresDeviceIdle = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_device_idle");
          final int _cursorIndexOfMRequiresBatteryNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_battery_not_low");
          final int _cursorIndexOfMRequiresStorageNotLow = CursorUtil.getColumnIndexOrThrow(_cursor, "requires_storage_not_low");
          final TransferSpec _result;
          if(_cursor.moveToFirst()) {
            final Constraints _tmpConstraints;
            _tmpConstraints = new Constraints();
            final NetworkType _tmpMRequiredNetworkType;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMRequiredNetworkType);
            _tmpMRequiredNetworkType = WorkTypeConverters.intToNetworkType(_tmp);
            _tmpConstraints.setRequiredNetworkType(_tmpMRequiredNetworkType);
            final boolean _tmpMRequiresCharging;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfMRequiresCharging);
            _tmpMRequiresCharging = _tmp_1 != 0;
            _tmpConstraints.setRequiresCharging(_tmpMRequiresCharging);
            final boolean _tmpMRequiresDeviceIdle;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMRequiresDeviceIdle);
            _tmpMRequiresDeviceIdle = _tmp_2 != 0;
            _tmpConstraints.setRequiresDeviceIdle(_tmpMRequiresDeviceIdle);
            final boolean _tmpMRequiresBatteryNotLow;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfMRequiresBatteryNotLow);
            _tmpMRequiresBatteryNotLow = _tmp_3 != 0;
            _tmpConstraints.setRequiresBatteryNotLow(_tmpMRequiresBatteryNotLow);
            final boolean _tmpMRequiresStorageNotLow;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfMRequiresStorageNotLow);
            _tmpMRequiresStorageNotLow = _tmp_4 != 0;
            _tmpConstraints.setRequiresStorageNotLow(_tmpMRequiresStorageNotLow);
            _result = new TransferSpec();
            _result.id = _cursor.getString(_cursorIndexOfId);
            _result.region = _cursor.getString(_cursorIndexOfRegion);
            _result.bucket = _cursor.getString(_cursorIndexOfBucket);
            _result.key = _cursor.getString(_cursorIndexOfKey);
            _result.filePath = _cursor.getString(_cursorIndexOfFilePath);
            _result.uploadId = _cursor.getString(_cursorIndexOfUploadId);
            _result.headers = _cursor.getString(_cursorIndexOfHeaders);
            _result.etag = _cursor.getString(_cursorIndexOfEtag);
            _result.workerId = _cursor.getString(_cursorIndexOfWorkerId);
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsUpload);
            _result.isUpload = _tmp_5 != 0;
            final String _tmp_6;
            _tmp_6 = _cursor.getString(_cursorIndexOfState);
            _result.state = __transferStateConverters.convert2Status(_tmp_6);
            _result.complete = _cursor.getLong(_cursorIndexOfComplete);
            _result.target = _cursor.getLong(_cursorIndexOfTarget);
            _result.transferServiceEgg = _cursor.getString(_cursorIndexOfTransferServiceEgg);
            _result.constraints = _tmpConstraints;
          } else {
            _result = null;
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
  public void updateTransferSpecsNetworkConstraints(final List<String> ids,
      final NetworkType networkType) {
    __db.assertNotSuspendingTransaction();
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("UPDATE transferspec SET required_network_type=");
    _stringBuilder.append("?");
    _stringBuilder.append(" WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
    int _argIndex = 1;
    final int _tmp;
    _tmp = WorkTypeConverters.networkTypeToInt(networkType);
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    for (String _item : ids) {
      if (_item == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }
}
