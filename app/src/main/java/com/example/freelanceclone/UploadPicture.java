package com.example.freelanceclone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
//TODO outdated tutorials will render this feature incomplete
public class UploadPicture extends AppCompatActivity {
    ImageView img;
    Button browse, upload;

    FirebaseStorage storage;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        upload = findViewById(R.id.bt_upload);
        browse = findViewById(R.id.bt_nothing);
        img = findViewById(R.id.img_profile);
        storage = FirebaseStorage.getInstance();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    //TODO implement this into the Introduction xml with the intent of keeping/getting image uri for profile photos
    private void uploadImage() {
        if(imageUri != null){
            StorageReference reference = storage.getReference().child("images/" + UUID.randomUUID().toString());
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UploadPicture.this,"Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UploadPicture.this,"Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        img.setImageURI(result);
                        imageUri = result;
                    }
                }
            });
}