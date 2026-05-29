package com.nanys.care.data.local.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nanys.care.data.local.entity.ChildEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class ChildDao_Impl implements ChildDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChildEntity> __insertionAdapterOfChildEntity;

  private final EntityDeletionOrUpdateAdapter<ChildEntity> __updateAdapterOfChildEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public ChildDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChildEntity = new EntityInsertionAdapter<ChildEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `children` (`id`,`tutorEmail`,`name`,`age`,`specialNeeds`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChildEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTutorEmail());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getAge());
        statement.bindString(5, entity.getSpecialNeeds());
      }
    };
    this.__updateAdapterOfChildEntity = new EntityDeletionOrUpdateAdapter<ChildEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `children` SET `id` = ?,`tutorEmail` = ?,`name` = ?,`age` = ?,`specialNeeds` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChildEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTutorEmail());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getAge());
        statement.bindString(5, entity.getSpecialNeeds());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM children WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ChildEntity child, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfChildEntity.insertAndReturnId(child);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ChildEntity child, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChildEntity.handle(child);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ChildEntity>> observeByTutor(final String tutorEmail) {
    final String _sql = "SELECT * FROM children WHERE tutorEmail = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tutorEmail);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"children"}, new Callable<List<ChildEntity>>() {
      @Override
      @NonNull
      public List<ChildEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfSpecialNeeds = CursorUtil.getColumnIndexOrThrow(_cursor, "specialNeeds");
          final List<ChildEntity> _result = new ArrayList<ChildEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChildEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpSpecialNeeds;
            _tmpSpecialNeeds = _cursor.getString(_cursorIndexOfSpecialNeeds);
            _item = new ChildEntity(_tmpId,_tmpTutorEmail,_tmpName,_tmpAge,_tmpSpecialNeeds);
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
  public Object getByTutor(final String tutorEmail,
      final Continuation<? super List<ChildEntity>> $completion) {
    final String _sql = "SELECT * FROM children WHERE tutorEmail = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tutorEmail);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ChildEntity>>() {
      @Override
      @NonNull
      public List<ChildEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfSpecialNeeds = CursorUtil.getColumnIndexOrThrow(_cursor, "specialNeeds");
          final List<ChildEntity> _result = new ArrayList<ChildEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChildEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpSpecialNeeds;
            _tmpSpecialNeeds = _cursor.getString(_cursorIndexOfSpecialNeeds);
            _item = new ChildEntity(_tmpId,_tmpTutorEmail,_tmpName,_tmpAge,_tmpSpecialNeeds);
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
  public Object getById(final long id, final Continuation<? super ChildEntity> $completion) {
    final String _sql = "SELECT * FROM children WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChildEntity>() {
      @Override
      @Nullable
      public ChildEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfSpecialNeeds = CursorUtil.getColumnIndexOrThrow(_cursor, "specialNeeds");
          final ChildEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpSpecialNeeds;
            _tmpSpecialNeeds = _cursor.getString(_cursorIndexOfSpecialNeeds);
            _result = new ChildEntity(_tmpId,_tmpTutorEmail,_tmpName,_tmpAge,_tmpSpecialNeeds);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
