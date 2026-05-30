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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nanys.care.data.local.entity.TutorProfileEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TutorProfileDao_Impl implements TutorProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TutorProfileEntity> __insertionAdapterOfTutorProfileEntity;

  private final EntityDeletionOrUpdateAdapter<TutorProfileEntity> __updateAdapterOfTutorProfileEntity;

  public TutorProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTutorProfileEntity = new EntityInsertionAdapter<TutorProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tutor_profiles` (`email`,`city`,`state`,`notes`,`preferences`,`photoUri`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TutorProfileEntity entity) {
        statement.bindString(1, entity.getEmail());
        statement.bindString(2, entity.getCity());
        statement.bindString(3, entity.getState());
        statement.bindString(4, entity.getNotes());
        statement.bindString(5, entity.getPreferences());
        statement.bindString(6, entity.getPhotoUri());
      }
    };
    this.__updateAdapterOfTutorProfileEntity = new EntityDeletionOrUpdateAdapter<TutorProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tutor_profiles` SET `email` = ?,`city` = ?,`state` = ?,`notes` = ?,`preferences` = ?,`photoUri` = ? WHERE `email` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TutorProfileEntity entity) {
        statement.bindString(1, entity.getEmail());
        statement.bindString(2, entity.getCity());
        statement.bindString(3, entity.getState());
        statement.bindString(4, entity.getNotes());
        statement.bindString(5, entity.getPreferences());
        statement.bindString(6, entity.getPhotoUri());
        statement.bindString(7, entity.getEmail());
      }
    };
  }

  @Override
  public Object insert(final TutorProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTutorProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final TutorProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTutorProfileEntity.handle(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByEmail(final String email,
      final Continuation<? super TutorProfileEntity> $completion) {
    final String _sql = "SELECT * FROM tutor_profiles WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TutorProfileEntity>() {
      @Override
      @Nullable
      public TutorProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPreferences = CursorUtil.getColumnIndexOrThrow(_cursor, "preferences");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final TutorProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpPreferences;
            _tmpPreferences = _cursor.getString(_cursorIndexOfPreferences);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            _result = new TutorProfileEntity(_tmpEmail,_tmpCity,_tmpState,_tmpNotes,_tmpPreferences,_tmpPhotoUri);
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
  public Flow<TutorProfileEntity> observeByEmail(final String email) {
    final String _sql = "SELECT * FROM tutor_profiles WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tutor_profiles"}, new Callable<TutorProfileEntity>() {
      @Override
      @Nullable
      public TutorProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPreferences = CursorUtil.getColumnIndexOrThrow(_cursor, "preferences");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final TutorProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpPreferences;
            _tmpPreferences = _cursor.getString(_cursorIndexOfPreferences);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            _result = new TutorProfileEntity(_tmpEmail,_tmpCity,_tmpState,_tmpNotes,_tmpPreferences,_tmpPhotoUri);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
