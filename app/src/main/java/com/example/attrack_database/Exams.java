package com.example.attrack_database;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attrack_database.Pojos.ExamPojo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Exams extends AppCompatActivity implements View.OnClickListener {

    EditText e_name,e_start_date,e_end_date;
    Button btn_submit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private int mdate,myear,mmonth,mhour,mminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("examData");

        getSupportActionBar().hide();
        e_end_date = findViewById(R.id.exam_end_date);
        e_name = findViewById(R.id.exam_name);
        e_start_date = findViewById(R.id.exam_start_date);
        btn_submit = findViewById(R.id.exam_submit);

        e_start_date.setOnClickListener(this);
        e_end_date.setOnClickListener(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = e_name.getText().toString();
                String start_date = e_start_date.getText().toString();
                String end_date = e_end_date.getText().toString();
                String id = databaseReference.push().getKey();

                ExamPojo examPojo = new ExamPojo();
                examPojo.setEnd_date(end_date);
                examPojo.setStart_date(start_date);
                examPojo.setName(name);
                examPojo.setId(id);
                databaseReference.child(name).setValue(examPojo);

                Toast.makeText(Exams.this, "Exam Scheduled Successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Exams.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(final View v) {
        final Calendar calendar = Calendar.getInstance();
        myear = calendar.get(Calendar.YEAR);
        mmonth = calendar.get(Calendar.MONTH);
        mdate = calendar.get(Calendar.DATE);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(v == e_start_date){
                    e_start_date.setText(dayOfMonth + "-" + (month +1) + "-" + year);
                }
                else if (v == e_end_date)
                    e_end_date.setText(dayOfMonth + "-" + (month +1) + "-" + year);
            }
        },myear,mmonth,mdate);
        datePickerDialog.show();
    }
}
