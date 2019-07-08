package com.example.attrack_database;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.attrack_database.Pojos.StudentPojo;
import com.example.attrack_database.Pojos.TeacherPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Student extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView profile_image;
    EditText name,branch,year,student_id,number,roll_no,email,password,stu_class;
    Button add_stu,remove_stu;
    String item;
    SelectImageHelper selectImageHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("studentData");
        final FirebaseStorage storage;
        final StorageReference storageReference;
        getSupportActionBar().hide();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        Spinner spinner2 = (Spinner) findViewById(R.id.sclass);

        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("CS-A");
        categories.add("CS-B");
        categories.add("CS-C");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(dataAdapter);


        name = findViewById(R.id.student_name);
        branch = findViewById(R.id.student_branch);
        year = findViewById(R.id.student_year);
        student_id = findViewById(R.id.student_id);
        number = findViewById(R.id.student_number);
        roll_no = findViewById(R.id.student_roll_no);
        profile_image = findViewById(R.id.image_student);
        email = findViewById(R.id.student_email);
        add_stu = findViewById(R.id.add);
        remove_stu = findViewById(R.id.remove);
        password = findViewById(R.id.student_password);

        selectImageHelper = new SelectImageHelper(this, profile_image);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImageHelper.selectImageOption();

            }
        });

        add_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s_name = name.getText().toString();
                final String s_id = student_id.getText().toString();
                final String s_branch = branch.getText().toString();
                final String s_year = year.getText().toString();
                final String s_rollno = roll_no.getText().toString();
                final String s_number = number.getText().toString();
                final String s_email = email.getText().toString();
                final String s_password = password.getText().toString();
                final String id = databaseReference.push().getKey();
                Uri uri = selectImageHelper.getURI_FOR_SELECTED_IMAGE();
                if (uri != null) {
                    final StorageReference reference = storageReference.child("/Image/" + s_id);
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("1234", "Download Url" + uri.toString());

                                    StudentPojo studentPojo = new StudentPojo();
                                    studentPojo.setBranch(s_branch);
                                    studentPojo.setS_class(item);
                                    studentPojo.setNumber(s_number);
                                    studentPojo.setYear(s_year);
                                    studentPojo.setRollno(s_rollno);
                                    studentPojo.setS_id(s_id);
                                    studentPojo.setId(id);
                                    studentPojo.setName(s_name);
                                    studentPojo.setEmail(s_email);
                                    studentPojo.setPassword(s_password);
                                    studentPojo.setImageURL(uri.toString());

                                    databaseReference.child(s_id).setValue(studentPojo);

                                    Toast.makeText(getApplicationContext(),"student added successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Student.this, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        remove_stu.setOnClickListener(new View.OnClickListener() {
            String sid;
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(student_id.getText().toString())) {
                    sid = student_id.getText().toString();
                    databaseReference.child(sid).setValue(null);
                    Toast.makeText(getApplicationContext(),"student removed successfully", Toast.LENGTH_LONG).show();

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
        Intent intent = new Intent(Student.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}