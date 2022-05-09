package com.example.freelanceclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    Button bt_reg, bt_hasAccount;
    EditText et_reg_email, et_reg_pass, et_reg_confirm;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        bt_reg = findViewById(R.id.bt_reg);
        bt_hasAccount = findViewById(R.id.bt_hasAccount);
        et_reg_email = findViewById(R.id.et_reg_email);
        et_reg_pass = findViewById(R.id.et_reg_pass);
        et_reg_confirm = findViewById(R.id.et_reg_confirm);
        progressDialog = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        bt_hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Auth();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back to the Login Screen",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    private void Auth(){
        //trim() to make sure it turns to string: advisable and just followed
        String email = et_reg_email.getText().toString().trim();
        String pass = et_reg_pass.getText().toString().trim();
        String confirm = et_reg_confirm.getText().toString().trim();

        if(!email.matches(emailPattern)) {
            et_reg_email.setError("Enter Correct Email");
        }
        else if(pass.isEmpty() || pass.length()<6) {
            et_reg_pass.setError("Enter a password no less than 6 letters long");
        }
        else if(!pass.equals(confirm)) {
            et_reg_confirm.setError("Password does not match both fields");
        }
        else{
            progressDialog.setMessage("Please wait while registration is processing...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //create user with provided email and password, will handle all error(email registered, incorrect password)
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete( Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this,"Registration Complete", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, Introduction.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}