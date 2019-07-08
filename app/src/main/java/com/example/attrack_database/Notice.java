package com.example.attrack_database;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attrack_database.Pojos.NoticePojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class Notice extends AppCompatActivity {

    Button btn_select,btn_upload;
    EditText title;
    Uri pdfUri;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        btn_select = findViewById(R.id.select_file);
        btn_upload = findViewById(R.id.upload_file);
        title = findViewById(R.id.notice_title);

        getSupportActionBar().hide();

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Notice.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectpdf();
                }
                else{
                    ActivityCompat.requestPermissions(Notice.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri != null)
                    uploadFile(pdfUri);
                else
                    Toast.makeText(Notice.this, "Select a File", Toast.LENGTH_SHORT).show();
                        
                }
            });
        }

    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String filename = System.currentTimeMillis()+".pdf";
        final String filename1 = System.currentTimeMillis()+"";
        final String title_notice = title.getText().toString();
        StorageReference storageReference = storage.getReference();
        storageReference.child("Pdf").child(filename).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                NoticePojo noticePojo = new NoticePojo();
                noticePojo.setTitle(title_notice);
                noticePojo.setId(filename1);
                noticePojo.setPdfUrl(url);

                databaseReference = firebaseDatabase.getReference("noticeData");
                databaseReference.child(filename1).setValue(noticePojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Notice.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(Notice.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Notice.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentprogress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentprogress);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectpdf();
        }
        else {
            Toast.makeText(Notice.this, "Please Provide Permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectpdf() {

        Intent intent = new Intent();
        intent.setType("notice/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 86 && resultCode == RESULT_OK && data!=null){
            pdfUri = data.getData();
        }
        else{
            Toast.makeText(this, "Please Select a File", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Notice.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
