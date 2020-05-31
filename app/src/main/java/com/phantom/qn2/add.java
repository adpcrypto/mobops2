package com.phantom.qn2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class add extends AppCompatActivity {
    SQLiteDatabase mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
         mydb= openOrCreateDatabase("DetailsDb",MODE_PRIVATE,null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydb.close();
    }

    public void clickHandler3(View view){

        EditText editName = (EditText)findViewById(R.id.editText1);
        EditText editAge = (EditText)findViewById(R.id.editText2);
        String name1 = editName.getText().toString();
        String age1 = editAge.getText().toString();
        int age2 = Integer.parseInt(age1);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS DetailsTable(Name VARCHAR,Age INT);");
        mydb.execSQL("INSERT INTO DetailsTable VALUES('"+name1+"','"+age2+"');");
        Toast.makeText(this,"You added Name:  "+name1+"  Age: "+age2,Toast.LENGTH_LONG).show();
    }
}
