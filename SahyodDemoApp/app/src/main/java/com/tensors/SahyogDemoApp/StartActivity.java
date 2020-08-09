package com.tensors.SahyogDemoApp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    EditText mail,pass;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String tmail;
            tmail = user.getEmail();
            Intent intent = new Intent(StartActivity.this,MainActivity.class);
            intent.putExtra("Email",tmail);
            startActivity(intent);
        }
        mail = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
    }



    public void login(View view) {
        String txt_mail = mail.getText().toString();
        String txt_pass = pass.getText().toString();
        loginUser(txt_mail,txt_pass);
    }
    private void loginUser(final String email, String pass) {
        auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(StartActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(StartActivity.this,MainActivity.class);
                intent2.putExtra("Email",email);
                startActivity(intent2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StartActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}