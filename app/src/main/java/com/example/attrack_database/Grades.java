package com.example.attrack_database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attrack_database.Pojos.GradePojo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Grades extends AppCompatActivity {


    EditText g_stu_id,g_subject,g_score_ut1,g_score_ut2,g_score_mid1,g_score_mid2;
    Button submit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("gradesData");


        getSupportActionBar().hide();
        g_stu_id = findViewById(R.id.student_id_grades);
        g_subject = findViewById(R.id.subject);
        g_score_ut1 = findViewById(R.id.score_ut1);
        g_score_ut2 = findViewById(R.id.score_ut2);
        g_score_mid1 = findViewById(R.id.score_mid1);
        g_score_mid2 = findViewById(R.id.scoremid2);
        submit  = findViewById(R.id.submit_grades);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stu_id = g_stu_id.getText().toString();
                final String subject = g_subject.getText().toString();
                final String score_ut1 = g_score_ut1.getText().toString();
                final String score_ut2 = g_score_ut2.getText().toString();
                final String score_mid1 = g_score_mid1.getText().toString();
                final String score_mid2 = g_score_mid2.getText().toString();
                final String id = databaseReference.push().getKey();


                GradePojo gradePojo = new GradePojo();
                gradePojo.setId(id);
                gradePojo.setStu_grade_id(stu_id);
                gradePojo.setSubject(subject);
                gradePojo.setScore_mid1(score_mid1);
                gradePojo.setScore_mid2(score_mid2);
                gradePojo.setScore_ut1(score_ut1);
                gradePojo.setScore_ut2(score_ut2);
                databaseReference.child(id).setValue(gradePojo);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Grades.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
