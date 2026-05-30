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
import com.nanys.care.data.local.entity.CaregiverProfileEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class CaregiverProfileDao_Impl implements CaregiverProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CaregiverProfileEntity> __insertionAdapterOfCaregiverProfileEntity;

  private final EntityDeletionOrUpdateAdapter<CaregiverProfileEntity> __updateAdapterOfCaregiverProfileEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateVerified;

  public CaregiverProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCaregiverProfileEntity = new EntityInsertionAdapter<CaregiverProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `caregiver_profiles` (`email`,`photoUri`,`experienceYears`,`certifications`,`availability`,`availabilityStart`,`availabilityEnd`,`availabilityExceptions`,`hourlyRate`,`extraChildRate`,`city`,`state`,`verified`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CaregiverProfileEntity entity) {
        statement.bindString(1, entity.getEmail());
        statement.bindString(2, entity.getPhotoUri());
        statement.bindLong(3, entity.getExperienceYears());
        statement.bindString(4, entity.getCertifications());
        statement.bindString(5, entity.getAvailability());
        statement.bindString(6, entity.getAvailabilityStart());
        statement.bindString(7, entity.getAvailabilityEnd());
        statement.bindString(8, entity.getAvailabilityExceptions());
        statement.bindDouble(9, entity.getHourlyRate());
        statement.bindDouble(10, entity.getExtraChildRate());
        statement.bindString(11, entity.getCity());
        statement.bindString(12, entity.getState());
        final int _tmp = entity.getVerified() ? 1 : 0;
        statement.bindLong(13, _tmp);
      }
    };
    this.__updateAdapterOfCaregiverProfileEntity = new EntityDeletionOrUpdateAdapter<CaregiverProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `caregiver_profiles` SET `email` = ?,`photoUri` = ?,`experienceYears` = ?,`certifications` = ?,`availability` = ?,`availabilityStart` = ?,`availabilityEnd` = ?,`availabilityExceptions` = ?,`hourlyRate` = ?,`extraChildRate` = ?,`city` = ?,`state` = ?,`verified` = ? WHERE `email` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CaregiverProfileEntity entity) {
        statement.bindString(1, entity.getEmail());
        statement.bindString(2, entity.getPhotoUri());
        statement.bindLong(3, entity.getExperienceYears());
        statement.bindString(4, entity.getCertifications());
        statement.bindString(5, entity.getAvailability());
        statement.bindString(6, entity.getAvailabilityStart());
        statement.bindString(7, entity.getAvailabilityEnd());
        statement.bindString(8, entity.getAvailabilityExceptions());
        statement.bindDouble(9, entity.getHourlyRate());
        statement.bindDouble(10, entity.getExtraChildRate());
        statement.bindString(11, entity.getCity());
        statement.bindString(12, entity.getState());
        final int _tmp = entity.getVerified() ? 1 : 0;
        statement.bindLong(13, _tmp);
        statement.bindString(14, entity.getEmail());
      }
    };
    this.__preparedStmtOfUpdateVerified = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE caregiver_profiles SET verified = ? WHERE email = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CaregiverProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCaregiverProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CaregiverProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCaregiverProfileEntity.handle(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateVerified(final String email, final boolean verified,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateVerified.acquire();
        int _argIndex = 1;
        final int _tmp = verified ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, email);
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
          __preparedStmtOfUpdateVerified.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByEmail(final String email,
      final Continuation<? super CaregiverProfileEntity> $completion) {
    final String _sql = "SELECT * FROM caregiver_profiles WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CaregiverProfileEntity>() {
      @Override
      @Nullable
      public CaregiverProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfExperienceYears = CursorUtil.getColumnIndexOrThrow(_cursor, "experienceYears");
          final int _cursorIndexOfCertifications = CursorUtil.getColumnIndexOrThrow(_cursor, "certifications");
          final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
          final int _cursorIndexOfAvailabilityStart = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityStart");
          final int _cursorIndexOfAvailabilityEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityEnd");
          final int _cursorIndexOfAvailabilityExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityExceptions");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfExtraChildRate = CursorUtil.getColumnIndexOrThrow(_cursor, "extraChildRate");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "verified");
          final CaregiverProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            final int _tmpExperienceYears;
            _tmpExperienceYears = _cursor.getInt(_cursorIndexOfExperienceYears);
            final String _tmpCertifications;
            _tmpCertifications = _cursor.getString(_cursorIndexOfCertifications);
            final String _tmpAvailability;
            _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
            final String _tmpAvailabilityStart;
            _tmpAvailabilityStart = _cursor.getString(_cursorIndexOfAvailabilityStart);
            final String _tmpAvailabilityEnd;
            _tmpAvailabilityEnd = _cursor.getString(_cursorIndexOfAvailabilityEnd);
            final String _tmpAvailabilityExceptions;
            _tmpAvailabilityExceptions = _cursor.getString(_cursorIndexOfAvailabilityExceptions);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final double _tmpExtraChildRate;
            _tmpExtraChildRate = _cursor.getDouble(_cursorIndexOfExtraChildRate);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final boolean _tmpVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerified);
            _tmpVerified = _tmp != 0;
            _result = new CaregiverProfileEntity(_tmpEmail,_tmpPhotoUri,_tmpExperienceYears,_tmpCertifications,_tmpAvailability,_tmpAvailabilityStart,_tmpAvailabilityEnd,_tmpAvailabilityExceptions,_tmpHourlyRate,_tmpExtraChildRate,_tmpCity,_tmpState,_tmpVerified);
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
  public Flow<CaregiverProfileEntity> observeByEmail(final String email) {
    final String _sql = "SELECT * FROM caregiver_profiles WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"caregiver_profiles"}, new Callable<CaregiverProfileEntity>() {
      @Override
      @Nullable
      public CaregiverProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfExperienceYears = CursorUtil.getColumnIndexOrThrow(_cursor, "experienceYears");
          final int _cursorIndexOfCertifications = CursorUtil.getColumnIndexOrThrow(_cursor, "certifications");
          final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
          final int _cursorIndexOfAvailabilityStart = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityStart");
          final int _cursorIndexOfAvailabilityEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityEnd");
          final int _cursorIndexOfAvailabilityExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityExceptions");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfExtraChildRate = CursorUtil.getColumnIndexOrThrow(_cursor, "extraChildRate");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "verified");
          final CaregiverProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            final int _tmpExperienceYears;
            _tmpExperienceYears = _cursor.getInt(_cursorIndexOfExperienceYears);
            final String _tmpCertifications;
            _tmpCertifications = _cursor.getString(_cursorIndexOfCertifications);
            final String _tmpAvailability;
            _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
            final String _tmpAvailabilityStart;
            _tmpAvailabilityStart = _cursor.getString(_cursorIndexOfAvailabilityStart);
            final String _tmpAvailabilityEnd;
            _tmpAvailabilityEnd = _cursor.getString(_cursorIndexOfAvailabilityEnd);
            final String _tmpAvailabilityExceptions;
            _tmpAvailabilityExceptions = _cursor.getString(_cursorIndexOfAvailabilityExceptions);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final double _tmpExtraChildRate;
            _tmpExtraChildRate = _cursor.getDouble(_cursorIndexOfExtraChildRate);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final boolean _tmpVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerified);
            _tmpVerified = _tmp != 0;
            _result = new CaregiverProfileEntity(_tmpEmail,_tmpPhotoUri,_tmpExperienceYears,_tmpCertifications,_tmpAvailability,_tmpAvailabilityStart,_tmpAvailabilityEnd,_tmpAvailabilityExceptions,_tmpHourlyRate,_tmpExtraChildRate,_tmpCity,_tmpState,_tmpVerified);
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
  public Flow<List<CaregiverProfileEntity>> observeAll() {
    final String _sql = "SELECT * FROM caregiver_profiles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"caregiver_profiles"}, new Callable<List<CaregiverProfileEntity>>() {
      @Override
      @NonNull
      public List<CaregiverProfileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfExperienceYears = CursorUtil.getColumnIndexOrThrow(_cursor, "experienceYears");
          final int _cursorIndexOfCertifications = CursorUtil.getColumnIndexOrThrow(_cursor, "certifications");
          final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
          final int _cursorIndexOfAvailabilityStart = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityStart");
          final int _cursorIndexOfAvailabilityEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityEnd");
          final int _cursorIndexOfAvailabilityExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityExceptions");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfExtraChildRate = CursorUtil.getColumnIndexOrThrow(_cursor, "extraChildRate");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "verified");
          final List<CaregiverProfileEntity> _result = new ArrayList<CaregiverProfileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CaregiverProfileEntity _item;
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            final int _tmpExperienceYears;
            _tmpExperienceYears = _cursor.getInt(_cursorIndexOfExperienceYears);
            final String _tmpCertifications;
            _tmpCertifications = _cursor.getString(_cursorIndexOfCertifications);
            final String _tmpAvailability;
            _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
            final String _tmpAvailabilityStart;
            _tmpAvailabilityStart = _cursor.getString(_cursorIndexOfAvailabilityStart);
            final String _tmpAvailabilityEnd;
            _tmpAvailabilityEnd = _cursor.getString(_cursorIndexOfAvailabilityEnd);
            final String _tmpAvailabilityExceptions;
            _tmpAvailabilityExceptions = _cursor.getString(_cursorIndexOfAvailabilityExceptions);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final double _tmpExtraChildRate;
            _tmpExtraChildRate = _cursor.getDouble(_cursorIndexOfExtraChildRate);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final boolean _tmpVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerified);
            _tmpVerified = _tmp != 0;
            _item = new CaregiverProfileEntity(_tmpEmail,_tmpPhotoUri,_tmpExperienceYears,_tmpCertifications,_tmpAvailability,_tmpAvailabilityStart,_tmpAvailabilityEnd,_tmpAvailabilityExceptions,_tmpHourlyRate,_tmpExtraChildRate,_tmpCity,_tmpState,_tmpVerified);
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
  public Flow<List<CaregiverProfileEntity>> search(final String city, final String state,
      final double minPrice, final double maxPrice, final int minExperience,
      final String availability) {
    final String _sql = "\n"
            + "        SELECT cp.* FROM caregiver_profiles cp\n"
            + "        INNER JOIN users u ON u.email = cp.email\n"
            + "        WHERE (? = '' OR cp.city = ?)\n"
            + "          AND (? = '' OR cp.state = ?)\n"
            + "          AND cp.hourlyRate >= ?\n"
            + "          AND cp.hourlyRate <= ?\n"
            + "          AND cp.experienceYears >= ?\n"
            + "          AND (\n"
            + "              ? = ''\n"
            + "              OR cp.availability LIKE '%' || ? || '%'\n"
            + "              OR cp.availabilityStart LIKE '%' || ? || '%'\n"
            + "              OR cp.availabilityEnd LIKE '%' || ? || '%'\n"
            + "              OR cp.availabilityExceptions LIKE '%' || ? || '%'\n"
            + "          )\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 12);
    int _argIndex = 1;
    _statement.bindString(_argIndex, city);
    _argIndex = 2;
    _statement.bindString(_argIndex, city);
    _argIndex = 3;
    _statement.bindString(_argIndex, state);
    _argIndex = 4;
    _statement.bindString(_argIndex, state);
    _argIndex = 5;
    _statement.bindDouble(_argIndex, minPrice);
    _argIndex = 6;
    _statement.bindDouble(_argIndex, maxPrice);
    _argIndex = 7;
    _statement.bindLong(_argIndex, minExperience);
    _argIndex = 8;
    _statement.bindString(_argIndex, availability);
    _argIndex = 9;
    _statement.bindString(_argIndex, availability);
    _argIndex = 10;
    _statement.bindString(_argIndex, availability);
    _argIndex = 11;
    _statement.bindString(_argIndex, availability);
    _argIndex = 12;
    _statement.bindString(_argIndex, availability);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"caregiver_profiles",
        "users"}, new Callable<List<CaregiverProfileEntity>>() {
      @Override
      @NonNull
      public List<CaregiverProfileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfExperienceYears = CursorUtil.getColumnIndexOrThrow(_cursor, "experienceYears");
          final int _cursorIndexOfCertifications = CursorUtil.getColumnIndexOrThrow(_cursor, "certifications");
          final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
          final int _cursorIndexOfAvailabilityStart = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityStart");
          final int _cursorIndexOfAvailabilityEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityEnd");
          final int _cursorIndexOfAvailabilityExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "availabilityExceptions");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfExtraChildRate = CursorUtil.getColumnIndexOrThrow(_cursor, "extraChildRate");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "verified");
          final List<CaregiverProfileEntity> _result = new ArrayList<CaregiverProfileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CaregiverProfileEntity _item;
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            final int _tmpExperienceYears;
            _tmpExperienceYears = _cursor.getInt(_cursorIndexOfExperienceYears);
            final String _tmpCertifications;
            _tmpCertifications = _cursor.getString(_cursorIndexOfCertifications);
            final String _tmpAvailability;
            _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
            final String _tmpAvailabilityStart;
            _tmpAvailabilityStart = _cursor.getString(_cursorIndexOfAvailabilityStart);
            final String _tmpAvailabilityEnd;
            _tmpAvailabilityEnd = _cursor.getString(_cursorIndexOfAvailabilityEnd);
            final String _tmpAvailabilityExceptions;
            _tmpAvailabilityExceptions = _cursor.getString(_cursorIndexOfAvailabilityExceptions);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final double _tmpExtraChildRate;
            _tmpExtraChildRate = _cursor.getDouble(_cursorIndexOfExtraChildRate);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final boolean _tmpVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerified);
            _tmpVerified = _tmp != 0;
            _item = new CaregiverProfileEntity(_tmpEmail,_tmpPhotoUri,_tmpExperienceYears,_tmpCertifications,_tmpAvailability,_tmpAvailabilityStart,_tmpAvailabilityEnd,_tmpAvailabilityExceptions,_tmpHourlyRate,_tmpExtraChildRate,_tmpCity,_tmpState,_tmpVerified);
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
  public Flow<Integer> countVerified() {
    final String _sql = "SELECT COUNT(*) FROM caregiver_profiles WHERE verified = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"caregiver_profiles"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
