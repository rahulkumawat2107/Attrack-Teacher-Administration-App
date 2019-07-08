package com.example.attrack_database;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.attrack_database.Pojos.Attendance_sheet;
import com.example.attrack_database.Pojos.GradePojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_student,btn_teacher,btn_event,btn_grades,btn_exam,btn_attendance,btn_notice,btn_logout,btn_subject;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbrefstudent,dbrefsubject,dbrefattendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        btn_student = findViewById(R.id.student_regis);
        btn_teacher = findViewById(R.id.teacher_regis);
        btn_event = findViewById(R.id.events_update);
        btn_grades = findViewById(R.id.grades);
        btn_exam = findViewById(R.id.exams);
        btn_notice = findViewById(R.id.notice);
        btn_logout = findViewById(R.id.logout);
        btn_subject = findViewById(R.id.subject_regis);


        firebaseDatabase = FirebaseDatabase.getInstance();
        dbrefstudent = firebaseDatabase.getReference("studentData");
        dbrefsubject = firebaseDatabase.getReference("gradesData");
        dbrefattendance = firebaseDatabase.getReference("attendance");

        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Student.class);
                startActivity(intent);
                finish();
            }
        });
        btn_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,Teacher.class);
                startActivity(intent1);
                finish();
            }
        });
        btn_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,Subject.class);
                startActivity(intent);
                finish();
            }
        });
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,EventsHolidays.class);
                startActivity(intent2);
                finish();
            }
        });
        btn_grades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this,Grades.class);
                startActivity(intent3);
                finish();
            }
        });
        btn_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this,Exams.class);
                startActivity(intent4);
                finish();
            }
        });
        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(MainActivity.this,Notice.class);
                startActivity(intent6);
                finish();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
