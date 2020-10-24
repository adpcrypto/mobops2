package com.tensors.sahyognew.Login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tensors.sahyognew.MainView.MainActivity;
import com.tensors.sahyognew.R;
import com.tensors.sahyognew.databinding.ActivityLoginBinding;

public class StartActivity extends AppCompatActivity {

    FirebaseAuth User;
    ActivityLoginBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String ID;
            ID = user.getEmail();
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("Email",ID);
            startActivity(intent);
            finish();
        }
        User = FirebaseAuth.getInstance();
    }



    public void login(View view) {

        String UserID = mBinding.username.getText().toString();
        String Password = mBinding.password.getText().toString();
        if (!(UserID.equals("") || Password.equals("") )){
            mBinding.loading.setVisibility(View.VISIBLE);
            mBinding.login.setEnabled(false);
            mBinding.login.setText(R.string.login_wait);


            loginUser(UserID,Password);
        }else{
            Toast.makeText(StartActivity.this,"Please Enter an Email/Password",Toast.LENGTH_SHORT).show();
        }

    }
    private void loginUser(final String email, String pass) {
        //User.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           // @Override
           // public void onSuccess(AuthResult authResult) {

                Intent intent2 = new Intent(StartActivity.this,MainActivity.class);
                intent2.putExtra("Email",email);
                startActivity(intent2);
                Toast.makeText(StartActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                mBinding.loading.setVisibility(View.GONE);
                finish();
           // }
       // }).addOnFailureListener(new OnFailureListener() {
            //@Override
           // public void onFailure(@NonNull Exception e) {
            //    Intent intent2 = new Intent(StartActivity.this,MainActivity.class);
            //    intent2.putExtra("Email",email);
            //    startActivity(intent2);
            //    Toast.makeText(StartActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
            //    mBinding.loading.setVisibility(View.GONE);
            //    finish();
            //}
       // });
    }
}