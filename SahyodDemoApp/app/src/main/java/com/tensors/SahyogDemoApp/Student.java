package com.tensors.SahyogDemoApp;

public class Student {

    String name,rollno,isstudent;
    public Student() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public void setIsstudent(String isstudent) {
        this.isstudent = isstudent;
    }

    public String getName() {
        return name;
    }

    public String getRollno() {
        return rollno;
    }

    public String getIsstudent() {
        return isstudent;
    }
    public Student(String name,String rollno,String isstudent){
        this.name = name;
        this.rollno = rollno;
        this.isstudent = isstudent;
    }
}

