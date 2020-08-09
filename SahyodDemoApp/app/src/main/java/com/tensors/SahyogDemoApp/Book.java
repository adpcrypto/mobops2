package com.tensors.SahyogDemoApp;

public class Book {


    private int bookid;
    public String bookname;
    public String studentname;
    boolean bookavail;




    public void assignid(int id,String rbookname){
        bookid=id;
        bookavail=true;
        bookname=rbookname;
    }
    public int getBookId() {
        return bookid;
    }

    public boolean checkbookavailability() {
        return bookavail;
    }

}

