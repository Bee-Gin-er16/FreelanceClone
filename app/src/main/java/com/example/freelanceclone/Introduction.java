package com.example.freelanceclone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Introduction extends AppCompatActivity {
    EditText et_firstname, et_lastname, et_contact;
    Button bt_full_reg;
    Spinner spinner, gender;
    TextView tv_datepick;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri imageUri;
    String userId = fAuth.getUid();

    HashMap<String, Object> user_credentials = new HashMap<>();
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        //EditText
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_contact = findViewById(R.id.et_contact);

        //Buttons
        bt_full_reg = findViewById(R.id.bt_full_reg);

        //TextView
        tv_datepick= findViewById(R.id.tv_datepick);

        //CircleView

        //Spinner instance and setups
        spinner = findViewById(R.id.status_spinner);
        gender = findViewById(R.id.gender_spinner);

        //Spinner Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.economic_status, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);

        //Set drop down
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set Adapter
        spinner.setAdapter(adapter);
        gender.setAdapter(genderAdapter);

        //on item selected listener
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Your sex alignment is "+choice, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "You are currently an "+choice, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //calendar
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //REMINDER: DON'T ALWAYS USE GetApplicationContext(), alternative/recommended: Class_name.this
        tv_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Introduction.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                tv_datepick.setText(date);
            }
        };

        //Button Register Onclick
        bt_full_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = tv_datepick.getText().toString().trim();
                if(et_firstname.getText().toString().isEmpty() || et_lastname.getText().toString().isEmpty()
                        || et_contact.getText().toString().isEmpty() || date.isEmpty()){
                    Toast.makeText(getApplicationContext(), "You must fill ALL EMPTY FIELDS & upload a profile picture", Toast.LENGTH_SHORT).show();
                }else{
                    /*
                    TODO
                     * add kv {Employed : false , Job access : null } default values in preparation for acceptance from employer
                     * implement image upload or set a default also gender spinner
                     (disregard LGBT work standards first, gender available male/female)
                    */

                    String full_name = et_firstname.getText().toString().trim()+" "+et_lastname.getText().toString().trim();
                    String contact = et_contact.getText().toString().trim();
                    String status = spinner.getSelectedItem().toString();
                    String sex = gender.getSelectedItem().toString();
                    user_credentials.put("Complete Registration", true);
                    user_credentials.put("Status", status);
                    user_credentials.put("User Email",fAuth.getCurrentUser().getEmail());
                    user_credentials.put("Name", full_name);
                    user_credentials.put("Contact No", contact);
                    user_credentials.put("Birthday", date);
                    user_credentials.put("Sex", sex); //new check if it's in the database
                    //user_credentials.put("Profile Image", imageUri.toString());
                    user_credentials.put("UID",userId);
                    DocumentReference doc_ref = db.collection("User Data").document(userId);
                    doc_ref.set(user_credentials).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Success ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), InitialUpload.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //TODO wait since it could be encoded image (string) but results do show the images showing in storage firebase
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        imageUri = result; //setImageUri to the result
                    }
                }
            });

    //TODO Update as of 10:58 AM Dec:15, try making the uid as a child folder. Resolution: undeclared Firebase storage instance
    //Sends to database
    private void upload() {
        if(imageUri != null){
            /**
             * the profile picture(imageUri) will change into the id name of user profile
             * */
            StorageReference reference = storage.getReference().child("profile_pic/"+userId+"/");
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Introduction.this,"Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Introduction.this,"Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(Introduction.this,"Failed to get URI", Toast.LENGTH_SHORT).show();
        }
    }
    
    //Back button actions
    @Override
    public void onBackPressed(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Introduction.this);
        alert.setTitle("Cannot Go Back Exception");
        alert.setMessage("This is one of the crucial steps of obtaining your credentials\n Advisable not to go back");
        alert.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

}