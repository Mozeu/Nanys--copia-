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
import com.nanys.care.data.local.entity.BookingEntity;
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
public final class BookingDao_Impl implements BookingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BookingEntity> __insertionAdapterOfBookingEntity;

  private final EntityDeletionOrUpdateAdapter<BookingEntity> __updateAdapterOfBookingEntity;

  public BookingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookingEntity = new EntityInsertionAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bookings` (`id`,`tutorEmail`,`caregiverEmail`,`date`,`timeSlot`,`durationHours`,`location`,`childId`,`childIds`,`additionalNotes`,`totalPrice`,`status`,`colorHex`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTutorEmail());
        statement.bindString(3, entity.getCaregiverEmail());
        statement.bindString(4, entity.getDate());
        statement.bindString(5, entity.getTimeSlot());
        statement.bindLong(6, entity.getDurationHours());
        statement.bindString(7, entity.getLocation());
        if (entity.getChildId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getChildId());
        }
        statement.bindString(9, entity.getChildIds());
        statement.bindString(10, entity.getAdditionalNotes());
        statement.bindDouble(11, entity.getTotalPrice());
        statement.bindString(12, entity.getStatus());
        statement.bindString(13, entity.getColorHex());
        statement.bindLong(14, entity.getCreatedAt());
      }
    };
    this.__updateAdapterOfBookingEntity = new EntityDeletionOrUpdateAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `bookings` SET `id` = ?,`tutorEmail` = ?,`caregiverEmail` = ?,`date` = ?,`timeSlot` = ?,`durationHours` = ?,`location` = ?,`childId` = ?,`childIds` = ?,`additionalNotes` = ?,`totalPrice` = ?,`status` = ?,`colorHex` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTutorEmail());
        statement.bindString(3, entity.getCaregiverEmail());
        statement.bindString(4, entity.getDate());
        statement.bindString(5, entity.getTimeSlot());
        statement.bindLong(6, entity.getDurationHours());
        statement.bindString(7, entity.getLocation());
        if (entity.getChildId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getChildId());
        }
        statement.bindString(9, entity.getChildIds());
        statement.bindString(10, entity.getAdditionalNotes());
        statement.bindDouble(11, entity.getTotalPrice());
        statement.bindString(12, entity.getStatus());
        statement.bindString(13, entity.getColorHex());
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final BookingEntity booking, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBookingEntity.insertAndReturnId(booking);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BookingEntity booking, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBookingEntity.handle(booking);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super BookingEntity> $completion) {
    final String _sql = "SELECT * FROM bookings WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BookingEntity>() {
      @Override
      @Nullable
      public BookingEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTimeSlot = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlot");
          final int _cursorIndexOfDurationHours = CursorUtil.getColumnIndexOrThrow(_cursor, "durationHours");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfChildId = CursorUtil.getColumnIndexOrThrow(_cursor, "childId");
          final int _cursorIndexOfChildIds = CursorUtil.getColumnIndexOrThrow(_cursor, "childIds");
          final int _cursorIndexOfAdditionalNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "additionalNotes");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final BookingEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTimeSlot;
            _tmpTimeSlot = _cursor.getString(_cursorIndexOfTimeSlot);
            final int _tmpDurationHours;
            _tmpDurationHours = _cursor.getInt(_cursorIndexOfDurationHours);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final Long _tmpChildId;
            if (_cursor.isNull(_cursorIndexOfChildId)) {
              _tmpChildId = null;
            } else {
              _tmpChildId = _cursor.getLong(_cursorIndexOfChildId);
            }
            final String _tmpChildIds;
            _tmpChildIds = _cursor.getString(_cursorIndexOfChildIds);
            final String _tmpAdditionalNotes;
            _tmpAdditionalNotes = _cursor.getString(_cursorIndexOfAdditionalNotes);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new BookingEntity(_tmpId,_tmpTutorEmail,_tmpCaregiverEmail,_tmpDate,_tmpTimeSlot,_tmpDurationHours,_tmpLocation,_tmpChildId,_tmpChildIds,_tmpAdditionalNotes,_tmpTotalPrice,_tmpStatus,_tmpColorHex,_tmpCreatedAt);
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
  public Flow<List<BookingEntity>> observeByCaregiver(final String email) {
    final String _sql = "SELECT * FROM bookings WHERE caregiverEmail = ? ORDER BY date ASC, timeSlot ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTimeSlot = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlot");
          final int _cursorIndexOfDurationHours = CursorUtil.getColumnIndexOrThrow(_cursor, "durationHours");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfChildId = CursorUtil.getColumnIndexOrThrow(_cursor, "childId");
          final int _cursorIndexOfChildIds = CursorUtil.getColumnIndexOrThrow(_cursor, "childIds");
          final int _cursorIndexOfAdditionalNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "additionalNotes");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTimeSlot;
            _tmpTimeSlot = _cursor.getString(_cursorIndexOfTimeSlot);
            final int _tmpDurationHours;
            _tmpDurationHours = _cursor.getInt(_cursorIndexOfDurationHours);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final Long _tmpChildId;
            if (_cursor.isNull(_cursorIndexOfChildId)) {
              _tmpChildId = null;
            } else {
              _tmpChildId = _cursor.getLong(_cursorIndexOfChildId);
            }
            final String _tmpChildIds;
            _tmpChildIds = _cursor.getString(_cursorIndexOfChildIds);
            final String _tmpAdditionalNotes;
            _tmpAdditionalNotes = _cursor.getString(_cursorIndexOfAdditionalNotes);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new BookingEntity(_tmpId,_tmpTutorEmail,_tmpCaregiverEmail,_tmpDate,_tmpTimeSlot,_tmpDurationHours,_tmpLocation,_tmpChildId,_tmpChildIds,_tmpAdditionalNotes,_tmpTotalPrice,_tmpStatus,_tmpColorHex,_tmpCreatedAt);
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
  public Flow<List<BookingEntity>> observeByTutor(final String email) {
    final String _sql = "SELECT * FROM bookings WHERE tutorEmail = ? ORDER BY date ASC, timeSlot ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTimeSlot = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlot");
          final int _cursorIndexOfDurationHours = CursorUtil.getColumnIndexOrThrow(_cursor, "durationHours");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfChildId = CursorUtil.getColumnIndexOrThrow(_cursor, "childId");
          final int _cursorIndexOfChildIds = CursorUtil.getColumnIndexOrThrow(_cursor, "childIds");
          final int _cursorIndexOfAdditionalNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "additionalNotes");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTimeSlot;
            _tmpTimeSlot = _cursor.getString(_cursorIndexOfTimeSlot);
            final int _tmpDurationHours;
            _tmpDurationHours = _cursor.getInt(_cursorIndexOfDurationHours);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final Long _tmpChildId;
            if (_cursor.isNull(_cursorIndexOfChildId)) {
              _tmpChildId = null;
            } else {
              _tmpChildId = _cursor.getLong(_cursorIndexOfChildId);
            }
            final String _tmpChildIds;
            _tmpChildIds = _cursor.getString(_cursorIndexOfChildIds);
            final String _tmpAdditionalNotes;
            _tmpAdditionalNotes = _cursor.getString(_cursorIndexOfAdditionalNotes);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new BookingEntity(_tmpId,_tmpTutorEmail,_tmpCaregiverEmail,_tmpDate,_tmpTimeSlot,_tmpDurationHours,_tmpLocation,_tmpChildId,_tmpChildIds,_tmpAdditionalNotes,_tmpTotalPrice,_tmpStatus,_tmpColorHex,_tmpCreatedAt);
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
  public Flow<List<BookingEntity>> observeByCaregiverAndStatus(final String email,
      final String status) {
    final String _sql = "SELECT * FROM bookings WHERE caregiverEmail = ? AND status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    _argIndex = 2;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTimeSlot = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlot");
          final int _cursorIndexOfDurationHours = CursorUtil.getColumnIndexOrThrow(_cursor, "durationHours");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfChildId = CursorUtil.getColumnIndexOrThrow(_cursor, "childId");
          final int _cursorIndexOfChildIds = CursorUtil.getColumnIndexOrThrow(_cursor, "childIds");
          final int _cursorIndexOfAdditionalNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "additionalNotes");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTimeSlot;
            _tmpTimeSlot = _cursor.getString(_cursorIndexOfTimeSlot);
            final int _tmpDurationHours;
            _tmpDurationHours = _cursor.getInt(_cursorIndexOfDurationHours);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final Long _tmpChildId;
            if (_cursor.isNull(_cursorIndexOfChildId)) {
              _tmpChildId = null;
            } else {
              _tmpChildId = _cursor.getLong(_cursorIndexOfChildId);
            }
            final String _tmpChildIds;
            _tmpChildIds = _cursor.getString(_cursorIndexOfChildIds);
            final String _tmpAdditionalNotes;
            _tmpAdditionalNotes = _cursor.getString(_cursorIndexOfAdditionalNotes);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new BookingEntity(_tmpId,_tmpTutorEmail,_tmpCaregiverEmail,_tmpDate,_tmpTimeSlot,_tmpDurationHours,_tmpLocation,_tmpChildId,_tmpChildIds,_tmpAdditionalNotes,_tmpTotalPrice,_tmpStatus,_tmpColorHex,_tmpCreatedAt);
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
  public Flow<List<BookingEntity>> observeByTutorAndStatus(final String email,
      final String status) {
    final String _sql = "SELECT * FROM bookings WHERE tutorEmail = ? AND status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    _argIndex = 2;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTimeSlot = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlot");
          final int _cursorIndexOfDurationHours = CursorUtil.getColumnIndexOrThrow(_cursor, "durationHours");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfChildId = CursorUtil.getColumnIndexOrThrow(_cursor, "childId");
          final int _cursorIndexOfChildIds = CursorUtil.getColumnIndexOrThrow(_cursor, "childIds");
          final int _cursorIndexOfAdditionalNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "additionalNotes");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTimeSlot;
            _tmpTimeSlot = _cursor.getString(_cursorIndexOfTimeSlot);
            final int _tmpDurationHours;
            _tmpDurationHours = _cursor.getInt(_cursorIndexOfDurationHours);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final Long _tmpChildId;
            if (_cursor.isNull(_cursorIndexOfChildId)) {
              _tmpChildId = null;
            } else {
              _tmpChildId = _cursor.getLong(_cursorIndexOfChildId);
            }
            final String _tmpChildIds;
            _tmpChildIds = _cursor.getString(_cursorIndexOfChildIds);
            final String _tmpAdditionalNotes;
            _tmpAdditionalNotes = _cursor.getString(_cursorIndexOfAdditionalNotes);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new BookingEntity(_tmpId,_tmpTutorEmail,_tmpCaregiverEmail,_tmpDate,_tmpTimeSlot,_tmpDurationHours,_tmpLocation,_tmpChildId,_tmpChildIds,_tmpAdditionalNotes,_tmpTotalPrice,_tmpStatus,_tmpColorHex,_tmpCreatedAt);
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
  public Flow<Integer> countPendingForCaregiver(final String email) {
    final String _sql = "SELECT COUNT(*) FROM bookings WHERE caregiverEmail = ? AND status = 'pending'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<Integer>() {
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

  @Override
  public Flow<Integer> countTodayAccepted(final String email, final String today) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM bookings\n"
            + "        WHERE (caregiverEmail = ? OR tutorEmail = ?)\n"
            + "          AND status = 'accepted'\n"
            + "          AND date = ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    _argIndex = 2;
    _statement.bindString(_argIndex, email);
    _argIndex = 3;
    _statement.bindString(_argIndex, today);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<Integer>() {
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

  @Override
  public Flow<List<BookingEntity>> observeAllAccepted() {
    final String _sql = "SELECT * FROM bookings WHERE status = 'accepted'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTimeSlot = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlot");
          final int _cursorIndexOfDurationHours = CursorUtil.getColumnIndexOrThrow(_cursor, "durationHours");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfChildId = CursorUtil.getColumnIndexOrThrow(_cursor, "childId");
          final int _cursorIndexOfChildIds = CursorUtil.getColumnIndexOrThrow(_cursor, "childIds");
          final int _cursorIndexOfAdditionalNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "additionalNotes");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTimeSlot;
            _tmpTimeSlot = _cursor.getString(_cursorIndexOfTimeSlot);
            final int _tmpDurationHours;
            _tmpDurationHours = _cursor.getInt(_cursorIndexOfDurationHours);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final Long _tmpChildId;
            if (_cursor.isNull(_cursorIndexOfChildId)) {
              _tmpChildId = null;
            } else {
              _tmpChildId = _cursor.getLong(_cursorIndexOfChildId);
            }
            final String _tmpChildIds;
            _tmpChildIds = _cursor.getString(_cursorIndexOfChildIds);
            final String _tmpAdditionalNotes;
            _tmpAdditionalNotes = _cursor.getString(_cursorIndexOfAdditionalNotes);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new BookingEntity(_tmpId,_tmpTutorEmail,_tmpCaregiverEmail,_tmpDate,_tmpTimeSlot,_tmpDurationHours,_tmpLocation,_tmpChildId,_tmpChildIds,_tmpAdditionalNotes,_tmpTotalPrice,_tmpStatus,_tmpColorHex,_tmpCreatedAt);
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
  public Flow<List<BookingEntity>> observeAll() {
    final String _sql = "SELECT * FROM bookings";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTutorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "tutorEmail");
          final int _cursorIndexOfCaregiverEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "caregiverEmail");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTimeSlot = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlot");
          final int _cursorIndexOfDurationHours = CursorUtil.getColumnIndexOrThrow(_cursor, "durationHours");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfChildId = CursorUtil.getColumnIndexOrThrow(_cursor, "childId");
          final int _cursorIndexOfChildIds = CursorUtil.getColumnIndexOrThrow(_cursor, "childIds");
          final int _cursorIndexOfAdditionalNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "additionalNotes");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTutorEmail;
            _tmpTutorEmail = _cursor.getString(_cursorIndexOfTutorEmail);
            final String _tmpCaregiverEmail;
            _tmpCaregiverEmail = _cursor.getString(_cursorIndexOfCaregiverEmail);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTimeSlot;
            _tmpTimeSlot = _cursor.getString(_cursorIndexOfTimeSlot);
            final int _tmpDurationHours;
            _tmpDurationHours = _cursor.getInt(_cursorIndexOfDurationHours);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final Long _tmpChildId;
            if (_cursor.isNull(_cursorIndexOfChildId)) {
              _tmpChildId = null;
            } else {
              _tmpChildId = _cursor.getLong(_cursorIndexOfChildId);
            }
            final String _tmpChildIds;
            _tmpChildIds = _cursor.getString(_cursorIndexOfChildIds);
            final String _tmpAdditionalNotes;
            _tmpAdditionalNotes = _cursor.getString(_cursorIndexOfAdditionalNotes);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new BookingEntity(_tmpId,_tmpTutorEmail,_tmpCaregiverEmail,_tmpDate,_tmpTimeSlot,_tmpDurationHours,_tmpLocation,_tmpChildId,_tmpChildIds,_tmpAdditionalNotes,_tmpTotalPrice,_tmpStatus,_tmpColorHex,_tmpCreatedAt);
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
