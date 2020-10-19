package com.example.muziq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener  {

    private EditText edtUsername;
    private EditText edtPass;
    private EditText edtEmail;
    private Button btCreateAccount;
    private TextView backToLogin;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        initUi();
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
        edtUsername =findViewById(R.id.user);
        edtEmail = findViewById(R.id.f_email);
        edtPass = findViewById(R.id.s_pass);
        btCreateAccount = findViewById(R.id.btn_reset_password);
        backToLogin = findViewById(R.id.back_to_login);
        btCreateAccount.setOnClickListener(this);
        backToLogin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account...");
        progressDialog.setCanceledOnTouchOutside(false);
        constraintLayout = (ConstraintLayout)findViewById(R.id.layout3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_reset_password: progressDialog.show();
                createNewAccount();

                break;
            case R.id.back_to_login:  sendBackToLogIn();
                break;
        }

    }

    private void sendBackToLogIn() {
        Intent intent =new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    private void storeUSerData(HashMap userData)
    {
        database.getReference().child("users").child(mAuth.getCurrentUser().getUid()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(SignupActivity.this,task.getException()+"", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        });
    }
    private void createNewAccount() {

        if(! emptyField()) {

            String email = edtEmail.getText().toString();
            String password = edtPass.getText().toString();
            String userName = edtUsername.getText().toString();
            final HashMap<String, String> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("userName", userName);
            userData.put("date",getCurrentDate());
            userData.put("status","unpaid");
            userData.put("endDate",todayPlus7());
            userData.put("image","na");

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        storeUSerData(userData);
                        Toast.makeText(SignupActivity.this, "Account created", Toast.LENGTH_LONG).show();
                        mAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                    Toast.makeText(SignupActivity.this,"Please verify email.", Toast.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        Toast.makeText(SignupActivity.this,"Please verify the format of every credentials ad try again.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(SignupActivity.this,"Any field should not remain empty", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }

    private boolean emptyField()
    {
        return edtEmail.getText().toString().equals("") || edtPass.getText().toString().equals("")||edtUsername.getText().toString().equals("");
    }
    public String getCurrentDate() {

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();

        return df.format(date);
    }
    public String todayPlus7()
    {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date toDate = cal.getTime();
        //  String fromDate = df.format(toDate);
        return df.format(toDate);
    }

    public void getDateDifference(Date d1,Date d2)
    {
        //  Date d2 = new Date();
        //  Date d1 = new Date(604800000l);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        //  Date d3 = df.parse("08/06/20 20:03:15");
        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        int diffInDays = (int) diff / (1000 * 60 * 60 * 24);
        long diffHours = diff / (60 * 60 * 1000);

    }

}
