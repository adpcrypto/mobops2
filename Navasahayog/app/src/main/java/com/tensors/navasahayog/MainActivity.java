package com.tensors.navasahayog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    String mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent5 =getIntent();
        mail = intent5.getStringExtra("Email");
        DocumentReference ref = FirebaseFirestore.getInstance().collection("Students").document(mail);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Toast.makeText(MainActivity.this,doc.getData().toString(),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"No data found for current user",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                        intent.putExtra("Email",mail);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void logou(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent2 = new Intent(MainActivity.this,StartActivity.class);
        startActivity(intent2);
    }
}