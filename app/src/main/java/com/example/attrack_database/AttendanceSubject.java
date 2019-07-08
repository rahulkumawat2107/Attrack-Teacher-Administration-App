package com.example.attrack_database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attrack_database.Pojos.Attendance_sheet;
import com.example.attrack_database.Pojos.SubjectPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendanceSubject extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<SubjectPojo> arrayList = new ArrayList<>();
    FirebaseDatabase database;
    String subject,item;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_subject);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("subjectData");

        getSupportActionBar().hide();
        Bundle bundle1 = getIntent().getExtras();
        item = bundle1.getString("class_selected");
        subject = bundle1.getString("subject_id");
        Log.d("1234", "attendance "+subject);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_subject);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getDataFromFirebase();


    }

    public void getDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SubjectPojo subjectPojo = dataSnapshot1.getValue(SubjectPojo.class);
                    if(subjectPojo.getSubject_id().equals(subject)){
                        arrayList.add(subjectPojo);
                    }
                }
                SubjectAdapter subjectAdapter = new SubjectAdapter(AttendanceSubject.this, arrayList);
                recyclerView.setAdapter(subjectAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AttendanceSubject.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Bundle basket= new Bundle();
        basket.putString("subject_id", subject);
        Log.d("abcd", "attendance subject "+subject);
        Intent intent = new Intent(AttendanceSubject.this,TeacherLogin.class);
        intent.putExtras(basket);
        startActivity(intent);
        finish();
    }
}