package com.tensors.sahyognew;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    public void addStudent(Student student);

    @Update
    public void updateStudent(Student student);

    @Delete
    public void deleteStudent(Student student);

    @Query("SELECT * from students")
    public LiveData<List<Student>> getStudents();

    @Query("DELETE FROM students")
    public void deleteAllStud();


}
