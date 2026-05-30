package com.nanys.care.data.local.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NanysDatabase_Impl extends NanysDatabase {
  private volatile UserDao _userDao;

  private volatile CaregiverProfileDao _caregiverProfileDao;

  private volatile TutorProfileDao _tutorProfileDao;

  private volatile ChildDao _childDao;

  private volatile BookingDao _bookingDao;

  private volatile ReviewDao _reviewDao;

  private volatile PrivateNoteDao _privateNoteDao;

  private volatile MessageDao _messageDao;

  private volatile CatalogDao _catalogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`email` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `role` TEXT NOT NULL, `fullName` TEXT NOT NULL, `phone` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`email`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `caregiver_profiles` (`email` TEXT NOT NULL, `photoUri` TEXT NOT NULL, `experienceYears` INTEGER NOT NULL, `certifications` TEXT NOT NULL, `availability` TEXT NOT NULL, `availabilityStart` TEXT NOT NULL, `availabilityEnd` TEXT NOT NULL, `availabilityExceptions` TEXT NOT NULL, `hourlyRate` REAL NOT NULL, `extraChildRate` REAL NOT NULL, `city` TEXT NOT NULL, `state` TEXT NOT NULL, `verified` INTEGER NOT NULL, PRIMARY KEY(`email`), FOREIGN KEY(`email`) REFERENCES `users`(`email`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_caregiver_profiles_city` ON `caregiver_profiles` (`city`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_caregiver_profiles_state` ON `caregiver_profiles` (`state`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_caregiver_profiles_hourlyRate` ON `caregiver_profiles` (`hourlyRate`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tutor_profiles` (`email` TEXT NOT NULL, `city` TEXT NOT NULL, `state` TEXT NOT NULL, `notes` TEXT NOT NULL, `preferences` TEXT NOT NULL, `photoUri` TEXT NOT NULL, PRIMARY KEY(`email`), FOREIGN KEY(`email`) REFERENCES `users`(`email`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `children` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tutorEmail` TEXT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL, `specialNeeds` TEXT NOT NULL, FOREIGN KEY(`tutorEmail`) REFERENCES `tutor_profiles`(`email`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_children_tutorEmail` ON `children` (`tutorEmail`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bookings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tutorEmail` TEXT NOT NULL, `caregiverEmail` TEXT NOT NULL, `date` TEXT NOT NULL, `timeSlot` TEXT NOT NULL, `durationHours` INTEGER NOT NULL, `location` TEXT NOT NULL, `childId` INTEGER, `childIds` TEXT NOT NULL DEFAULT '', `additionalNotes` TEXT NOT NULL, `totalPrice` REAL NOT NULL, `status` TEXT NOT NULL, `colorHex` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookings_caregiverEmail` ON `bookings` (`caregiverEmail`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookings_tutorEmail` ON `bookings` (`tutorEmail`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookings_date` ON `bookings` (`date`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookings_status` ON `bookings` (`status`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reviews` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fromTutorEmail` TEXT NOT NULL, `toCaregiverEmail` TEXT NOT NULL, `bookingId` INTEGER NOT NULL, `rating` INTEGER NOT NULL, `comment` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reviews_toCaregiverEmail` ON `reviews` (`toCaregiverEmail`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reviews_bookingId` ON `reviews` (`bookingId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `private_notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `caregiverEmail` TEXT NOT NULL, `tutorEmail` TEXT NOT NULL, `note` TEXT NOT NULL, `rating` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_private_notes_caregiverEmail` ON `private_notes` (`caregiverEmail`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_private_notes_tutorEmail` ON `private_notes` (`tutorEmail`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `senderEmail` TEXT NOT NULL, `receiverEmail` TEXT NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `flagged` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_senderEmail` ON `messages` (`senderEmail`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_receiverEmail` ON `messages` (`receiverEmail`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_timestamp` ON `messages` (`timestamp`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `catalog_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category` TEXT NOT NULL, `name` TEXT NOT NULL, `value` TEXT NOT NULL, `extra` TEXT NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_catalog_items_category` ON `catalog_items` (`category`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '264c62fac184ca3bde082c20ef3bd5aa')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `caregiver_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `tutor_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `children`");
        db.execSQL("DROP TABLE IF EXISTS `bookings`");
        db.execSQL("DROP TABLE IF EXISTS `reviews`");
        db.execSQL("DROP TABLE IF EXISTS `private_notes`");
        db.execSQL("DROP TABLE IF EXISTS `messages`");
        db.execSQL("DROP TABLE IF EXISTS `catalog_items`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fullName", new TableInfo.Column("fullName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.nanys.care.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsCaregiverProfiles = new HashMap<String, TableInfo.Column>(13);
        _columnsCaregiverProfiles.put("email", new TableInfo.Column("email", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("photoUri", new TableInfo.Column("photoUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("experienceYears", new TableInfo.Column("experienceYears", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("certifications", new TableInfo.Column("certifications", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("availability", new TableInfo.Column("availability", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("availabilityStart", new TableInfo.Column("availabilityStart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("availabilityEnd", new TableInfo.Column("availabilityEnd", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("availabilityExceptions", new TableInfo.Column("availabilityExceptions", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("hourlyRate", new TableInfo.Column("hourlyRate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("extraChildRate", new TableInfo.Column("extraChildRate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregiverProfiles.put("verified", new TableInfo.Column("verified", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCaregiverProfiles = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCaregiverProfiles.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("email"), Arrays.asList("email")));
        final HashSet<TableInfo.Index> _indicesCaregiverProfiles = new HashSet<TableInfo.Index>(3);
        _indicesCaregiverProfiles.add(new TableInfo.Index("index_caregiver_profiles_city", false, Arrays.asList("city"), Arrays.asList("ASC")));
        _indicesCaregiverProfiles.add(new TableInfo.Index("index_caregiver_profiles_state", false, Arrays.asList("state"), Arrays.asList("ASC")));
        _indicesCaregiverProfiles.add(new TableInfo.Index("index_caregiver_profiles_hourlyRate", false, Arrays.asList("hourlyRate"), Arrays.asList("ASC")));
        final TableInfo _infoCaregiverProfiles = new TableInfo("caregiver_profiles", _columnsCaregiverProfiles, _foreignKeysCaregiverProfiles, _indicesCaregiverProfiles);
        final TableInfo _existingCaregiverProfiles = TableInfo.read(db, "caregiver_profiles");
        if (!_infoCaregiverProfiles.equals(_existingCaregiverProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "caregiver_profiles(com.nanys.care.data.local.entity.CaregiverProfileEntity).\n"
                  + " Expected:\n" + _infoCaregiverProfiles + "\n"
                  + " Found:\n" + _existingCaregiverProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsTutorProfiles = new HashMap<String, TableInfo.Column>(6);
        _columnsTutorProfiles.put("email", new TableInfo.Column("email", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTutorProfiles.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTutorProfiles.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTutorProfiles.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTutorProfiles.put("preferences", new TableInfo.Column("preferences", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTutorProfiles.put("photoUri", new TableInfo.Column("photoUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTutorProfiles = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTutorProfiles.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("email"), Arrays.asList("email")));
        final HashSet<TableInfo.Index> _indicesTutorProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTutorProfiles = new TableInfo("tutor_profiles", _columnsTutorProfiles, _foreignKeysTutorProfiles, _indicesTutorProfiles);
        final TableInfo _existingTutorProfiles = TableInfo.read(db, "tutor_profiles");
        if (!_infoTutorProfiles.equals(_existingTutorProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "tutor_profiles(com.nanys.care.data.local.entity.TutorProfileEntity).\n"
                  + " Expected:\n" + _infoTutorProfiles + "\n"
                  + " Found:\n" + _existingTutorProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsChildren = new HashMap<String, TableInfo.Column>(5);
        _columnsChildren.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChildren.put("tutorEmail", new TableInfo.Column("tutorEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChildren.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChildren.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChildren.put("specialNeeds", new TableInfo.Column("specialNeeds", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChildren = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChildren.add(new TableInfo.ForeignKey("tutor_profiles", "CASCADE", "NO ACTION", Arrays.asList("tutorEmail"), Arrays.asList("email")));
        final HashSet<TableInfo.Index> _indicesChildren = new HashSet<TableInfo.Index>(1);
        _indicesChildren.add(new TableInfo.Index("index_children_tutorEmail", false, Arrays.asList("tutorEmail"), Arrays.asList("ASC")));
        final TableInfo _infoChildren = new TableInfo("children", _columnsChildren, _foreignKeysChildren, _indicesChildren);
        final TableInfo _existingChildren = TableInfo.read(db, "children");
        if (!_infoChildren.equals(_existingChildren)) {
          return new RoomOpenHelper.ValidationResult(false, "children(com.nanys.care.data.local.entity.ChildEntity).\n"
                  + " Expected:\n" + _infoChildren + "\n"
                  + " Found:\n" + _existingChildren);
        }
        final HashMap<String, TableInfo.Column> _columnsBookings = new HashMap<String, TableInfo.Column>(14);
        _columnsBookings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("tutorEmail", new TableInfo.Column("tutorEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("caregiverEmail", new TableInfo.Column("caregiverEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("timeSlot", new TableInfo.Column("timeSlot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("durationHours", new TableInfo.Column("durationHours", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("childId", new TableInfo.Column("childId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("childIds", new TableInfo.Column("childIds", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("additionalNotes", new TableInfo.Column("additionalNotes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("totalPrice", new TableInfo.Column("totalPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("colorHex", new TableInfo.Column("colorHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBookings = new HashSet<TableInfo.Index>(4);
        _indicesBookings.add(new TableInfo.Index("index_bookings_caregiverEmail", false, Arrays.asList("caregiverEmail"), Arrays.asList("ASC")));
        _indicesBookings.add(new TableInfo.Index("index_bookings_tutorEmail", false, Arrays.asList("tutorEmail"), Arrays.asList("ASC")));
        _indicesBookings.add(new TableInfo.Index("index_bookings_date", false, Arrays.asList("date"), Arrays.asList("ASC")));
        _indicesBookings.add(new TableInfo.Index("index_bookings_status", false, Arrays.asList("status"), Arrays.asList("ASC")));
        final TableInfo _infoBookings = new TableInfo("bookings", _columnsBookings, _foreignKeysBookings, _indicesBookings);
        final TableInfo _existingBookings = TableInfo.read(db, "bookings");
        if (!_infoBookings.equals(_existingBookings)) {
          return new RoomOpenHelper.ValidationResult(false, "bookings(com.nanys.care.data.local.entity.BookingEntity).\n"
                  + " Expected:\n" + _infoBookings + "\n"
                  + " Found:\n" + _existingBookings);
        }
        final HashMap<String, TableInfo.Column> _columnsReviews = new HashMap<String, TableInfo.Column>(7);
        _columnsReviews.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("fromTutorEmail", new TableInfo.Column("fromTutorEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("toCaregiverEmail", new TableInfo.Column("toCaregiverEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("bookingId", new TableInfo.Column("bookingId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("rating", new TableInfo.Column("rating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("comment", new TableInfo.Column("comment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReviews = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReviews = new HashSet<TableInfo.Index>(2);
        _indicesReviews.add(new TableInfo.Index("index_reviews_toCaregiverEmail", false, Arrays.asList("toCaregiverEmail"), Arrays.asList("ASC")));
        _indicesReviews.add(new TableInfo.Index("index_reviews_bookingId", false, Arrays.asList("bookingId"), Arrays.asList("ASC")));
        final TableInfo _infoReviews = new TableInfo("reviews", _columnsReviews, _foreignKeysReviews, _indicesReviews);
        final TableInfo _existingReviews = TableInfo.read(db, "reviews");
        if (!_infoReviews.equals(_existingReviews)) {
          return new RoomOpenHelper.ValidationResult(false, "reviews(com.nanys.care.data.local.entity.ReviewEntity).\n"
                  + " Expected:\n" + _infoReviews + "\n"
                  + " Found:\n" + _existingReviews);
        }
        final HashMap<String, TableInfo.Column> _columnsPrivateNotes = new HashMap<String, TableInfo.Column>(6);
        _columnsPrivateNotes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrivateNotes.put("caregiverEmail", new TableInfo.Column("caregiverEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrivateNotes.put("tutorEmail", new TableInfo.Column("tutorEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrivateNotes.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrivateNotes.put("rating", new TableInfo.Column("rating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrivateNotes.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrivateNotes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrivateNotes = new HashSet<TableInfo.Index>(2);
        _indicesPrivateNotes.add(new TableInfo.Index("index_private_notes_caregiverEmail", false, Arrays.asList("caregiverEmail"), Arrays.asList("ASC")));
        _indicesPrivateNotes.add(new TableInfo.Index("index_private_notes_tutorEmail", false, Arrays.asList("tutorEmail"), Arrays.asList("ASC")));
        final TableInfo _infoPrivateNotes = new TableInfo("private_notes", _columnsPrivateNotes, _foreignKeysPrivateNotes, _indicesPrivateNotes);
        final TableInfo _existingPrivateNotes = TableInfo.read(db, "private_notes");
        if (!_infoPrivateNotes.equals(_existingPrivateNotes)) {
          return new RoomOpenHelper.ValidationResult(false, "private_notes(com.nanys.care.data.local.entity.PrivateNoteEntity).\n"
                  + " Expected:\n" + _infoPrivateNotes + "\n"
                  + " Found:\n" + _existingPrivateNotes);
        }
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(6);
        _columnsMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderEmail", new TableInfo.Column("senderEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("receiverEmail", new TableInfo.Column("receiverEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("flagged", new TableInfo.Column("flagged", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(3);
        _indicesMessages.add(new TableInfo.Index("index_messages_senderEmail", false, Arrays.asList("senderEmail"), Arrays.asList("ASC")));
        _indicesMessages.add(new TableInfo.Index("index_messages_receiverEmail", false, Arrays.asList("receiverEmail"), Arrays.asList("ASC")));
        _indicesMessages.add(new TableInfo.Index("index_messages_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(db, "messages");
        if (!_infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(com.nanys.care.data.local.entity.MessageEntity).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsCatalogItems = new HashMap<String, TableInfo.Column>(5);
        _columnsCatalogItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCatalogItems.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCatalogItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCatalogItems.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCatalogItems.put("extra", new TableInfo.Column("extra", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCatalogItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCatalogItems = new HashSet<TableInfo.Index>(1);
        _indicesCatalogItems.add(new TableInfo.Index("index_catalog_items_category", false, Arrays.asList("category"), Arrays.asList("ASC")));
        final TableInfo _infoCatalogItems = new TableInfo("catalog_items", _columnsCatalogItems, _foreignKeysCatalogItems, _indicesCatalogItems);
        final TableInfo _existingCatalogItems = TableInfo.read(db, "catalog_items");
        if (!_infoCatalogItems.equals(_existingCatalogItems)) {
          return new RoomOpenHelper.ValidationResult(false, "catalog_items(com.nanys.care.data.local.entity.CatalogItemEntity).\n"
                  + " Expected:\n" + _infoCatalogItems + "\n"
                  + " Found:\n" + _existingCatalogItems);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "264c62fac184ca3bde082c20ef3bd5aa", "3443154f403c63cdcd58a7bd2180af7b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","caregiver_profiles","tutor_profiles","children","bookings","reviews","private_notes","messages","catalog_items");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `caregiver_profiles`");
      _db.execSQL("DELETE FROM `tutor_profiles`");
      _db.execSQL("DELETE FROM `children`");
      _db.execSQL("DELETE FROM `bookings`");
      _db.execSQL("DELETE FROM `reviews`");
      _db.execSQL("DELETE FROM `private_notes`");
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `catalog_items`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CaregiverProfileDao.class, CaregiverProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TutorProfileDao.class, TutorProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChildDao.class, ChildDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BookingDao.class, BookingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReviewDao.class, ReviewDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PrivateNoteDao.class, PrivateNoteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MessageDao.class, MessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CatalogDao.class, CatalogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public CaregiverProfileDao caregiverProfileDao() {
    if (_caregiverProfileDao != null) {
      return _caregiverProfileDao;
    } else {
      synchronized(this) {
        if(_caregiverProfileDao == null) {
          _caregiverProfileDao = new CaregiverProfileDao_Impl(this);
        }
        return _caregiverProfileDao;
      }
    }
  }

  @Override
  public TutorProfileDao tutorProfileDao() {
    if (_tutorProfileDao != null) {
      return _tutorProfileDao;
    } else {
      synchronized(this) {
        if(_tutorProfileDao == null) {
          _tutorProfileDao = new TutorProfileDao_Impl(this);
        }
        return _tutorProfileDao;
      }
    }
  }

  @Override
  public ChildDao childDao() {
    if (_childDao != null) {
      return _childDao;
    } else {
      synchronized(this) {
        if(_childDao == null) {
          _childDao = new ChildDao_Impl(this);
        }
        return _childDao;
      }
    }
  }

  @Override
  public BookingDao bookingDao() {
    if (_bookingDao != null) {
      return _bookingDao;
    } else {
      synchronized(this) {
        if(_bookingDao == null) {
          _bookingDao = new BookingDao_Impl(this);
        }
        return _bookingDao;
      }
    }
  }

  @Override
  public ReviewDao reviewDao() {
    if (_reviewDao != null) {
      return _reviewDao;
    } else {
      synchronized(this) {
        if(_reviewDao == null) {
          _reviewDao = new ReviewDao_Impl(this);
        }
        return _reviewDao;
      }
    }
  }

  @Override
  public PrivateNoteDao privateNoteDao() {
    if (_privateNoteDao != null) {
      return _privateNoteDao;
    } else {
      synchronized(this) {
        if(_privateNoteDao == null) {
          _privateNoteDao = new PrivateNoteDao_Impl(this);
        }
        return _privateNoteDao;
      }
    }
  }

  @Override
  public MessageDao messageDao() {
    if (_messageDao != null) {
      return _messageDao;
    } else {
      synchronized(this) {
        if(_messageDao == null) {
          _messageDao = new MessageDao_Impl(this);
        }
        return _messageDao;
      }
    }
  }

  @Override
  public CatalogDao catalogDao() {
    if (_catalogDao != null) {
      return _catalogDao;
    } else {
      synchronized(this) {
        if(_catalogDao == null) {
          _catalogDao = new CatalogDao_Impl(this);
        }
        return _catalogDao;
      }
    }
  }
}
