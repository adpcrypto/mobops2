package com.tensors.navasahayog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    EditText name,age,dist,state;
    String txt_name,txt_age,txt_dist,txt_state,mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name  = findViewById(R.id.editTextTextPersonName);
        age   =  findViewById(R.id.editTextTextPersonName2);
        dist  = findViewById(R.id.editTextTextPersonName3);
        state = findViewById(R.id.editTextTextPersonName4);

        Intent intent5 =getIntent();
        mail = intent5.getStringExtra("Email");
    }

    public void complete(View view) {

        txt_name = name.getText().toString();
        txt_age  = age.getText().toString();
        txt_dist = dist.getText().toString();
        txt_state= state.getText().toString();
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        Map<String , Object> details = new HashMap<>();
        details.put("Name",txt_name);
        details.put("Age",txt_age);
        details.put("District",txt_dist);
        details.put("State",txt_state);

        db.collection("Students").document(mail).set(details).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                       Toast.makeText(DetailsActivity.this,"Registration Complete", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(DetailsActivity.this,MainActivity.class);
                        startActivity(intent2);
                   }
               }
           });

    }
}