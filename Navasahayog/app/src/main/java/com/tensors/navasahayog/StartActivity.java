package com.tensors.navasahayog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    public void register(View view) {
        String txt_mail = mail.getText().toString();
        String txt_pass = pass.getText().toString();
        if((TextUtils.isEmpty(txt_mail))|| (TextUtils.isEmpty(txt_pass))){
            Toast.makeText(StartActivity.this,"Empty username or password",Toast.LENGTH_SHORT).show();
        }else if(txt_pass.length() <6){
            Toast.makeText(StartActivity.this,"Password is smaller than 6 digits",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(StartActivity.this,"Please wait while user is being created",Toast.LENGTH_SHORT).show();
            registerUser(txt_mail,txt_pass);
        }
    }
    private void registerUser(final String mail, String pass) {
        auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(StartActivity.this,"User being created...",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(StartActivity.this,DetailsActivity.class);
                    intent2.putExtra("Email",mail);
                    startActivity(intent2);
                }else{
                    Toast.makeText(StartActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        });
    }
}