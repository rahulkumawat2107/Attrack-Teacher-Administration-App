package com.example.attrack_database;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.attrack_database.Pojos.TeacherPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText username,password;
    DatabaseReference reference;
    ProgressDialog mDialog;
    String userid,pass;
    String item;
    int flag=0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        username =  (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.editText2);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> categories = new ArrayList<String>();
        categories.add("Admin");
        categories.add("Teacher");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = parent.getItemAtPosition(position).toString();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void onButtonClick(View v) {


        userid = username.getText().toString();
        pass = password.getText().toString();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please Wait..." + userid);
        mDialog.setTitle("Loading");
        mDialog.show();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("teacher_regData");

        if (item == "Admin") {
            if(userid.equals("admin") && pass.equals("adminpass")){
                mDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                mDialog.dismiss();
                Toast.makeText(this, "Invalid AdminID and Password", Toast.LENGTH_SHORT).show();
            }

        }
        if(item == "Teacher"){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                String userid1 = username.getText().toString();
                String pass1 = password.getText().toString();
                String subject;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        TeacherPojo teacherPojo = dataSnapshot1.getValue(TeacherPojo.class);
                        String t_name = teacherPojo.getName();
                        String tpassword=teacherPojo.getT_id();
                        if(t_name.equals(userid1) && tpassword.equals(pass1)){
                            flag=1;
                            subject = teacherPojo.getSubject_id();
                        }
                    }
                    if(flag==1){
                        Bundle basket= new Bundle();
                        basket.putString("subject_id", subject);

                        mDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, TeacherLogin.class);
                        intent.putExtras(basket);
                        startActivity(intent);
                        finish();
                    }else{
                        mDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid TeacherID and Password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });

        }
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
