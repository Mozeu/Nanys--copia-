package com.nanys.care.data.local.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nanys.care.data.local.entity.PrivateNoteEntity;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PrivateNoteDao_Impl implements PrivateNoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PrivateNoteEntity> __insertionAdapterOfPrivateNoteEntity;

  public PrivateNoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPrivateNoteEntity = new EntityInsertionAdapter<PrivateNoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `private_notes` (`id`,`caregiverEmail`,`tutorEmail`,`note`,`rating`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PrivateNoteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCaregiverEmail());
        statement.bindString(3, entity.getTutorEmail());
        statement.bindString(4, entity.getNote());
        statement.bindLong(5, entity.getRating());
        statement.bindLong(6, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insert(final PrivateNoteEntity note, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPrivateNoteEntity.insertAndReturnId(note);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PrivateNoteEntity>> observeByCaregiver(final String caregiverEmail) {
    final String _sql = "\n"
            + "        SELECT * FROM private_notes\n"
            + "        WHERE caregiverEmail = ?\n"
            + "        ORDER BY timestamp DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, caregiverEmail);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"private_notes"}, new Callable<List<PrivateNoteEntity>>() {
      @Override
      @NonNull
      public List<PrivateNoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PrivateNoteEntity> _result = new ArrayList<PrivateNoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PrivateNoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final int _tmpRating;
            _tmpRating = _cursor.getInt(_cursorIndexOfRating);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PrivateNoteEntity(_tmpId,_tmpCaregiverEmail,_tmpTutorEmail,_tmpNote,_tmpRating,_tmpTimestamp);
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
  public Flow<List<PrivateNoteEntity>> observeByPair(final String caregiverEmail,
      final String tutorEmail) {
    final String _sql = "\n"
            + "        SELECT * FROM private_notes\n"
            + "        WHERE caregiverEmail = ? AND tutorEmail = ?\n"
            + "        ORDER BY timestamp DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, caregiverEmail);
    _argIndex = 2;
    _statement.bindString(_argIndex, tutorEmail);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"private_notes"}, new Callable<List<PrivateNoteEntity>>() {
      @Override
      @NonNull
      public List<PrivateNoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PrivateNoteEntity> _result = new ArrayList<PrivateNoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PrivateNoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final int _tmpRating;
            _tmpRating = _cursor.getInt(_cursorIndexOfRating);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PrivateNoteEntity(_tmpId,_tmpCaregiverEmail,_tmpTutorEmail,_tmpNote,_tmpRating,_tmpTimestamp);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
