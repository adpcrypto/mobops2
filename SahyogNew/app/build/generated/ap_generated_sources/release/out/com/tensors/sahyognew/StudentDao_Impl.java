package com.tensors.sahyognew;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StudentDao_Impl implements StudentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Student> __insertionAdapterOfStudent;

  private final EntityDeletionOrUpdateAdapter<Student> __deletionAdapterOfStudent;

  private final EntityDeletionOrUpdateAdapter<Student> __updateAdapterOfStudent;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllStud;

  public StudentDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudent = new EntityInsertionAdapter<Student>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `students` (`ID`,`rollno_col`,`name_col`,`attendance_col`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Student value) {
        stmt.bindLong(1, value.getID());
        if (value.getRollno() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRollno());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        stmt.bindLong(4, value.getAttendance());
      }
    };
    this.__deletionAdapterOfStudent = new EntityDeletionOrUpdateAdapter<Student>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `students` WHERE `ID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Student value) {
        stmt.bindLong(1, value.getID());
      }
    };
    this.__updateAdapterOfStudent = new EntityDeletionOrUpdateAdapter<Student>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `students` SET `ID` = ?,`rollno_col` = ?,`name_col` = ?,`attendance_col` = ? WHERE `ID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Student value) {
        stmt.bindLong(1, value.getID());
        if (value.getRollno() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRollno());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        stmt.bindLong(4, value.getAttendance());
        stmt.bindLong(5, value.getID());
      }
    };
    this.__preparedStmtOfDeleteAllStud = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM students";
        return _query;
      }
    };
  }

  @Override
  public void addStudent(final Student student) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStudent.insert(student);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteStudent(final Student student) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfStudent.handle(student);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateStudent(final Student student) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfStudent.handle(student);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllStud() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllStud.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllStud.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Student>> getStudents() {
    final String _sql = "SELECT * from students";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"students"}, false, new Callable<List<Student>>() {
      @Override
      public List<Student> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfID = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
          final int _cursorIndexOfRollno = CursorUtil.getColumnIndexOrThrow(_cursor, "rollno_col");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name_col");
          final int _cursorIndexOfAttendance = CursorUtil.getColumnIndexOrThrow(_cursor, "attendance_col");
          final List<Student> _result = new ArrayList<Student>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Student _item;
            final String _tmpRollno;
            _tmpRollno = _cursor.getString(_cursorIndexOfRollno);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item = new Student(_tmpRollno,_tmpName);
            final int _tmpID;
            _tmpID = _cursor.getInt(_cursorIndexOfID);
            _item.setID(_tmpID);
            final int _tmpAttendance;
            _tmpAttendance = _cursor.getInt(_cursorIndexOfAttendance);
            _item.setAttendance(_tmpAttendance);
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
}
