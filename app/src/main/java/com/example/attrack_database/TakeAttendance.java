package com.example.attrack_database;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attrack_database.Pojos.AttendancePojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TakeAttendance extends AppCompatActivity {
    String teacher_id;
    String subjectid;
    String subject_selected;
    String item1;
    ArrayList<String> selectedItems;
    ArrayList<String> nonselectedItems;


    ArrayList Userlist = new ArrayList<>();
    ArrayList Usernames = new ArrayList<>();


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        // ArrayList Userlist;
        selectedItems = new ArrayList<String>();

        getSupportActionBar().hide();

        TextView classname = (TextView) findViewById(R.id.textView);
        classname.setText("IT-X");

        Bundle bundle1 = getIntent().getExtras();
        subject_selected = bundle1.getString("subject_selected");
        item1 = bundle1.getString("class_selected");
        classname.setText(subject_selected);
        Log.d("12345", "onCreate: "+item1);
        DatabaseReference dbuser = ref.child("studentData");
        dbuser.orderByChild("s_class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(dsp.child("s_id").getValue().toString()); //add result into array list
                    Usernames.add(dsp.child("name").getValue().toString());


                }
                OnStart(Userlist);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void OnStart(ArrayList<String> userlist) {
        super.onStart();
        nonselectedItems = userlist;
        //create an instance of ListView
        ListView chl = (ListView) findViewById(R.id.checkable_list);
        //set multiple selection mode
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //supply data itmes to ListView
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.checkable_list_layout, R.id.txt_title, userlist);
        chl.setAdapter(aa);
        //set OnItemClickListener
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    selectedItems.add(selectedItem); //add selected item to the list of selected items

            }

        });
    }

    public void showSelectedItem(View view){
        String selItems = "";
        ref = FirebaseDatabase.getInstance().getReference();

        dbAttendance = ref.child("attendance").child(date);

        AttendancePojo attendancePojo = new AttendancePojo();
        attendancePojo.setDate(date);
        attendancePojo.setSubject_attendance(subject_selected);

        for (String item : selectedItems) {
            Toast.makeText(this, "Attendance created Successfully", Toast.LENGTH_SHORT).show();
            nonselectedItems.remove(item);
            attendancePojo.setS_id(item);
            Log.d("abcd", "showSelectedItem: "+item);
            dbAttendance.child(subject_selected).child(item).setValue("P");
            if (selItems == "")
                selItems = item;
            else
                selItems += "/" + item;
        }
        for (String item : nonselectedItems) {
            attendancePojo.setS_id(item);
            Toast.makeText(this, "Attendance created Successfully", Toast.LENGTH_SHORT).show();
            dbAttendance.child(subject_selected).child(item).setValue("A");
            //Toast.makeText(this, "absentees:" + nonselectedItems, Toast.LENGTH_LONG).show();

        }


        Bundle bundle1 = getIntent().getExtras();
        subjectid = bundle1.getString("subject_id");
        Bundle basket= new Bundle();
        basket.putString("subject_id", subjectid);
        Intent intent = new Intent(TakeAttendance.this,AttendanceSubject.class);
        intent.putExtras(basket);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        Bundle bundle1 = getIntent().getExtras();
        subjectid = bundle1.getString("subject_id");
        Bundle basket= new Bundle();
        basket.putString("subject_id", subjectid);
        Log.d("abcd", "showSelectedItem: "+subjectid+" "+subject_selected);
        Intent intent = new Intent(TakeAttendance.this,AttendanceSubject.class);
        intent.putExtras(basket);
        startActivity(intent);
        finish();
    }
}
