package com.example.freelanceclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button login, register, admin, guest;
    EditText provided_email, provided_pass;
    TextView tv_forgot_pass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);
        admin = findViewById(R.id.bt_admin);
        guest = findViewById(R.id.bt_guest);
        tv_forgot_pass = findViewById(R.id.tv_forgot);
        provided_email = findViewById(R.id.provided_email);
        provided_pass = findViewById(R.id.provided_pass);
        progressDialog = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Register(); }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { LogAuth(); }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { AdminAuth();}
        });
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { GuestAuth(); }
        });

        tv_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive reset link.");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //YES
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
                                Toast.makeText(getApplicationContext(),"Reset Link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Error, Reset Link is not sent" +e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //NO
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Reached the end of going back using back button",Toast.LENGTH_SHORT).show();
    }
    public void Register(){
        //to be changed later on
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    //login authorize, requires firebase auth
    private void LogAuth(){
        //trim() to make sure it turns to string: advisable and just followed
        String email = provided_email.getText().toString();
        String pass = provided_pass.getText().toString();

        if(!email.matches(emailPattern)) {
            provided_email.setError("Enter Correct Email");
        }
        else if(pass.isEmpty() || pass.length()<6) {
            provided_pass.setError("Enter a password no less than 6 letters long");
        }
        else{
            progressDialog.setMessage("Please wait while login is processing...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //tell authentication to sign in with email and password
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete( Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this,"Login", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void AdminAuth(){
        String email = "thekidofnerds@gmail.com";
        String pass = "tester123";
        progressDialog.setMessage("Please wait while login is processing...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //tell authentication to sign in with email and password
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(MainActivity.this,"Login", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void GuestAuth(){
        String email = "hanseubertdelacruz@gmail.com";
        String pass = "tester345";
        progressDialog.setMessage("Please wait while login is processing...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //tell authentication to sign in with email and password
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(MainActivity.this,"Login", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToNextActivity() {
        //To be change later on
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}