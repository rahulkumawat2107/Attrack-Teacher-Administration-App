package com.example.attrack_database;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.attrack_database.Pojos.SubjectPojo;

import java.util.ArrayList;
import java.util.List;

public class TeacherLogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String item;
    String message;
    Button take_attendance,logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher_login);
        Bundle bundle1 = getIntent().getExtras();

        getSupportActionBar().hide();

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_class);

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


        take_attendance = findViewById(R.id.button_attendance);
        logout = findViewById(R.id.button_logout);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = parent.getItemAtPosition(position).toString();
        Bundle bundle1 = getIntent().getExtras();
        message = bundle1.getString("subject_id");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void takeAttendanceButton(View v) {
        Bundle basket= new Bundle();
        basket.putString("class_selected", item);
        Log.d("12345", "takeAttendanceButton: "+item);
        basket.putString("subject_id",message);

        Intent intent = new Intent(TeacherLogin.this,AttendanceSubject.class);
        intent.putExtras(basket);
        startActivity(intent);
        finish();
    }

    public void logoutTeacher (View v){
        Intent intent = new Intent(TeacherLogin.this,LoginActivity.class);
        startActivity(intent);
        finish();
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
