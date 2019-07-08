package com.example.attrack_database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attrack_database.Pojos.SubjectPojo;
import com.example.attrack_database.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Subject extends AppCompatActivity {

    EditText subject,code;
    Button submit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        getSupportActionBar().hide();
        subject = findViewById(R.id.subject_name);
        code = findViewById(R.id.subject_id);
        submit = findViewById(R.id.submit_subject);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("subjectData");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub = subject.getText().toString();
                String id = code.getText().toString();

                SubjectPojo subjectPojo = new SubjectPojo();
                subjectPojo.setSubject_id(id);
                subjectPojo.setSubject_name(sub);
                databaseReference.child(id).setValue(subjectPojo);
                Toast.makeText(Subject.this, "Subject Registered Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Subject.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
