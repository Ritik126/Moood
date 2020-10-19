package com.example.muziq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEmail;
    private EditText edtPass;
    private Button createAccount;
    private TextView forgotPass;
    private Button btnLogIn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(LoginActivity.this,MyAudioPlayer.class);
            startActivity(intent);
            finish();
        }
        getSupportActionBar().hide();
        initUi();
        mAuth = FirebaseAuth.getInstance();
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
    }

    private void initUi()
    {
        edtEmail = findViewById(R.id.f_email);
        edtPass = findViewById(R.id.s_pass);
        createAccount = findViewById(R.id.create_new_account);
        forgotPass = findViewById(R.id.forgot_pass);
        btnLogIn =findViewById(R.id.btn_reset_password);
        btnLogIn.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        createAccount.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        constraintLayout = (ConstraintLayout)findViewById(R.id.layout2);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_reset_password:  progressDialog.show();
                logIn();
                break;
            case R.id.create_new_account: sendToCreateAccount();
                break;
            case R.id.forgot_pass:  sendToResetPassword();
                break;
        }
    }

    private void sendToResetPassword() {
        Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void sendToCreateAccount() {
        Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    }

    private void logIn() {
        if(!emptyField()) {

            String email = edtEmail.getText().toString();
            String pass = edtPass.getText().toString();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this,MyAudioPlayer.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,"Please enter correct credentials.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this,"Email or Password should not be empty", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }
    private boolean emptyField() {
        return edtEmail.getText().toString().equals("") || edtPass.getText().toString().equals("");

    }

}
