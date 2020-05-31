package com.phantom.qn2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void clickHandler1(View view){
        Intent myIntent = new Intent(getBaseContext(),add.class);
        startActivity(myIntent);
    }

    public void clickHandler2(View view){
        mydb= openOrCreateDatabase("DetailsDb",MODE_PRIVATE,null);
        Cursor result=null;
        try {
            result = mydb.rawQuery("SELECT * FROM DetailsTable;", null);
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this,"Nothing to show", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        if(result==null){
            Toast toast = Toast.makeText(this,"Nothing to show", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            Intent myIntent = new Intent(getBaseContext(), view.class);
            startActivity(myIntent);
        }
    }
}
