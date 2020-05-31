package com.phantom.qn2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class view extends AppCompatActivity {

    SQLiteDatabase mydb;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        mydb= openOrCreateDatabase("DetailsDb",MODE_PRIVATE,null);
        Cursor result;
        String tot;
        result = mydb.rawQuery("SELECT * FROM DetailsTable;",null);
        result.moveToFirst();
        textview = findViewById(R.id.textView2);
        String name3 = result.getString(0);
        String age3 = result.getString(1);
        tot=name3+"   "+age3+"\n";
        while (result.moveToNext()) {
            name3 = result.getString(0);
            age3 = result.getString(1);
            tot =tot+name3+"   "+age3+"\n";
        }
        textview.setText(tot);

    }
}
