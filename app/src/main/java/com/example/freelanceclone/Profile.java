package com.example.freelanceclone;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.UUID;

/*
TODO implement circle imageview or imageview upload image and replace default, allow editing data except status quo and email(for now).
 */
public class Profile extends AppCompatActivity {
    TextView tv_full, tv_email, tv_contact, tv_status, tv_sex;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    String userId = fAuth.getCurrentUser().getUid();
    String status, profileId;
    DocumentReference documentReference = fStore.document("User Data/"+userId);
    //Testing storage reference dec 15 9:08AM
    StorageReference imageRef;

    Button bt_user_post, bt_user_service;
    ImageView imageView;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Textview
        tv_full = findViewById(R.id.tv_full);
        tv_email = findViewById(R.id.tv_email);
        tv_contact = findViewById(R.id.tv_contact);
        tv_status = findViewById(R.id.tv_status);
        tv_sex = findViewById(R.id.tv_sex);

        //Button
        bt_user_post = findViewById(R.id.bt_user_post);
        bt_user_service = findViewById(R.id.bt_user_service);

        //Imageview
        imageView = findViewById(R.id.iv_profile);

        //Storage
        storage = FirebaseStorage.getInstance();
        //TODO change the iamge name and move it's scope when document reference is done retrieving strings

        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { String msg = "Enter new contact number for update"; ChangeData("Contact No",msg); }
        });
        tv_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { String msg = "Enter new contact number for update"; ChangeData("Name",msg); }
        });

        bt_user_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.matches("Employer")){
                    startActivity(new Intent(Profile.this, EmployerPostList.class));
                }else{ Unauthorized("Employee"); }
            }
        });
        bt_user_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.matches("Employee")){
                    //Toast.makeText(Profile.this, "Ur an employee!", Toast.LENGTH_SHORT).show();
                }else{
                    Unauthorized("Employer");
                }
            }
        });

        //Fixed by: Naming convention of document name value
        // must not have non-alphabetical characters but space might be ok
        //DocumentReference call
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String pro_name = documentSnapshot.getString("Name");
                            String pro_email = documentSnapshot.getString("User Email");
                            String pro_contact = documentSnapshot.getString("Contact No");
                            String pro_status = documentSnapshot.getString("Status"); //later
                            String pro_sex = documentSnapshot.getString("Sex");
                            //String pro_profile = documentSnapshot.getString("Profile Image");
                            //Boolean pro_status = documentSnapshot.getBoolean("Registration");

                            tv_full.setText(pro_name);
                            tv_email.setText(pro_email);
                            tv_contact.setText(pro_contact);
                            tv_status.setText(pro_status);
                            tv_sex.setText(pro_sex);
                            status = pro_status;
                            //profileId = pro_profile;

                        } else {
                            Toast.makeText(Profile.this, "Document does not exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Document does not exists", Toast.LENGTH_SHORT).show();
                    }
                });

        //StorageReference call and file init
        imageRef =  storage.getReference().child("profile_pic/"+userId);
        try {
            final File localFile = File.createTempFile(userId,"");
            imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Profile.this, "Going", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(Profile.this, "Image Document does not exists", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void Unauthorized(String status) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("You cannot access this button because you are an "+status);
        alert.setTitle(status+" Exception");
        alert.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    //TODO update data with provided value for respective key
    private void ChangeData(String key, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg);
        alert.setTitle("Update Your "+key);
        final EditText edittext = new EditText(Profile.this);
        alert.setView(edittext);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String YouEditTextValue = edittext.getText().toString();
                documentReference.update(key,YouEditTextValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Successfully updated data for "+msg+" Go Back to Profile",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        alert.setNegativeButton("Don't Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}