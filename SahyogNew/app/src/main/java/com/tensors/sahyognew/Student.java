package com.tensors.sahyognew;


import android.text.Editable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "rollno_col")
    private String rollno;

    @ColumnInfo(name = "name_col")
    private String name;

    @ColumnInfo(name="attendance_col")
    private int attendance;

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Student(String rollno, String name) {
        this.rollno = rollno;
        this.name = name;
        this.attendance =0;
    }
}
