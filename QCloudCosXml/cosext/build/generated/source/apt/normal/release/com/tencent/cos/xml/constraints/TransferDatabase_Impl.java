package com.tencent.cos.xml.constraints;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TransferDatabase_Impl extends TransferDatabase {
  private volatile TransferSpecDao _transferSpecDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `TransferSpec` (`id` TEXT NOT NULL, `region` TEXT NOT NULL, `bucket` TEXT NOT NULL, `key` TEXT NOT NULL, `filePath` TEXT NOT NULL, `uploadId` TEXT, `headers` TEXT, `etag` TEXT, `workerId` TEXT, `isUpload` INTEGER NOT NULL, `state` TEXT NOT NULL, `complete` INTEGER NOT NULL, `target` INTEGER NOT NULL, `transferServiceEgg` TEXT, `required_network_type` INTEGER, `requires_charging` INTEGER NOT NULL, `requires_device_idle` INTEGER NOT NULL, `requires_battery_not_low` INTEGER NOT NULL, `requires_storage_not_low` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7bb2f484bfa00d3e813a509f1cdf9851')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `TransferSpec`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
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
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTransferSpec = new HashMap<String, TableInfo.Column>(19);
        _columnsTransferSpec.put("id", new TableInfo.Column("id", "TEXT", true, 1));
        _columnsTransferSpec.put("region", new TableInfo.Column("region", "TEXT", true, 0));
        _columnsTransferSpec.put("bucket", new TableInfo.Column("bucket", "TEXT", true, 0));
        _columnsTransferSpec.put("key", new TableInfo.Column("key", "TEXT", true, 0));
        _columnsTransferSpec.put("filePath", new TableInfo.Column("filePath", "TEXT", true, 0));
        _columnsTransferSpec.put("uploadId", new TableInfo.Column("uploadId", "TEXT", false, 0));
        _columnsTransferSpec.put("headers", new TableInfo.Column("headers", "TEXT", false, 0));
        _columnsTransferSpec.put("etag", new TableInfo.Column("etag", "TEXT", false, 0));
        _columnsTransferSpec.put("workerId", new TableInfo.Column("workerId", "TEXT", false, 0));
        _columnsTransferSpec.put("isUpload", new TableInfo.Column("isUpload", "INTEGER", true, 0));
        _columnsTransferSpec.put("state", new TableInfo.Column("state", "TEXT", true, 0));
        _columnsTransferSpec.put("complete", new TableInfo.Column("complete", "INTEGER", true, 0));
        _columnsTransferSpec.put("target", new TableInfo.Column("target", "INTEGER", true, 0));
        _columnsTransferSpec.put("transferServiceEgg", new TableInfo.Column("transferServiceEgg", "TEXT", false, 0));
        _columnsTransferSpec.put("required_network_type", new TableInfo.Column("required_network_type", "INTEGER", false, 0));
        _columnsTransferSpec.put("requires_charging", new TableInfo.Column("requires_charging", "INTEGER", true, 0));
        _columnsTransferSpec.put("requires_device_idle", new TableInfo.Column("requires_device_idle", "INTEGER", true, 0));
        _columnsTransferSpec.put("requires_battery_not_low", new TableInfo.Column("requires_battery_not_low", "INTEGER", true, 0));
        _columnsTransferSpec.put("requires_storage_not_low", new TableInfo.Column("requires_storage_not_low", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransferSpec = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransferSpec = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTransferSpec = new TableInfo("TransferSpec", _columnsTransferSpec, _foreignKeysTransferSpec, _indicesTransferSpec);
        final TableInfo _existingTransferSpec = TableInfo.read(_db, "TransferSpec");
        if (! _infoTransferSpec.equals(_existingTransferSpec)) {
          throw new IllegalStateException("Migration didn't properly handle TransferSpec(com.tencent.cos.xml.constraints.TransferSpec).\n"
                  + " Expected:\n" + _infoTransferSpec + "\n"
                  + " Found:\n" + _existingTransferSpec);
        }
      }
    }, "7bb2f484bfa00d3e813a509f1cdf9851", "9e33ea8df258bdafab796f926cc73f84");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "TransferSpec");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `TransferSpec`");
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
  public TransferSpecDao transferSpecDao() {
    if (_transferSpecDao != null) {
      return _transferSpecDao;
    } else {
      synchronized(this) {
        if(_transferSpecDao == null) {
          _transferSpecDao = new TransferSpecDao_Impl(this);
        }
        return _transferSpecDao;
      }
    }
  }
}
