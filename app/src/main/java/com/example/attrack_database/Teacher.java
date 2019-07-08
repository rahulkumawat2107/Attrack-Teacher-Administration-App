package com.example.attrack_database;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attrack_database.Pojos.TeacherPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Teacher extends AppCompatActivity {

    EditText t_name,t_number,t_branch,t_degree,sub_id,t_id;
    ImageView profile_image;
    Button add,remove;
    SelectImageHelper selectImageHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("teacher_regData");
        FirebaseStorage storage;
        final StorageReference storageReference;

        getSupportActionBar().hide();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        t_name = findViewById(R.id.teacher_name);
        t_number = findViewById(R.id.teacher_number);
        t_branch = findViewById(R.id.teacher_branch);
        t_degree = findViewById(R.id.teacher_degree);
        t_id = findViewById(R.id.teacher_id);
        sub_id = findViewById(R.id.subject_id);
        add = findViewById(R.id.add_teacher);
        remove = findViewById(R.id.remove_teacher);
        profile_image = findViewById(R.id.icon);

        selectImageHelper = new SelectImageHelper(this, profile_image);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImageHelper.selectImageOption();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = t_name.getText().toString();
                final String number = t_number.getText().toString();
                final String branch = t_branch.getText().toString();
                final String degree = t_degree.getText().toString();
                final String tea_id = t_id.getText().toString();
                final String sub = sub_id.getText().toString();
                final String id = databaseReference.push().getKey();

                Uri uri = selectImageHelper.getURI_FOR_SELECTED_IMAGE();
                if (uri != null) {
                    final StorageReference reference = storageReference.child("/Image/" + tea_id);
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("1234", "Download Url" + uri.toString());

                                    TeacherPojo teacherPojo = new TeacherPojo();
                                    teacherPojo.setBranch(branch);
                                    teacherPojo.setDegree(degree);
                                    teacherPojo.setId(id);
                                    teacherPojo.setNumber(number);
                                    teacherPojo.setT_id(tea_id);
                                    teacherPojo.setName(name);
                                    teacherPojo.setSubject_id(sub);
                                    teacherPojo.setImageURL(uri.toString());

                                    databaseReference.child(tea_id).setValue(teacherPojo);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Teacher.this, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            String tid;
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(t_id.getText().toString())) {
                    tid = t_id.getText().toString();
                    databaseReference.child(tid).setValue(null);
                    Toast.makeText(getApplicationContext(),"teacher removed successfully", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getApplicationContext(),"id cannot be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        selectImageHelper.handleResult(requestCode, resultCode, result);  // call this helper class method
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);   // call this helper class method
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Teacher.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
