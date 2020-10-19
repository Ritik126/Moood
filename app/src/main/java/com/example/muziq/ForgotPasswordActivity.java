package com.example.muziq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEmail;
    private Button btnReset;
    private TextView backToLogin;
    private FirebaseAuth mAuth;
    private boolean check;
    private ProgressDialog progressDialog;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password);
        initUi();
        mAuth = FirebaseAuth.getInstance();
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUi()
    {
        edtEmail = findViewById(R.id.f_email);
        btnReset =findViewById(R.id.btn_reset_password);
        backToLogin = findViewById(R.id.btn_back);
        btnReset.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        constraintLayout = (ConstraintLayout)findViewById(R.id.layout4);
    }

    public void onBack(View view){
        Intent intent =new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_reset_password: progressDialog.show();
                resetPassword();
                break;
        }
    }

    private void resetPassword() {

        String email = edtEmail.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(ForgotPasswordActivity.this,"Email should not be blank", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else{


            if(checkEmail(email))
            {

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPasswordActivity.this,"Password reset link has been sent to your email",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else{
                            Toast.makeText(ForgotPasswordActivity.this,"Server error.Please try again later"+"", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
            else{
                Toast.makeText(ForgotPasswordActivity.this,"Email doesn't exist.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

    }

    public boolean checkEmail(String email)
    {


        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                check = task.isSuccessful();

            }
        });

        return check;
    }
}

