package com.example.attrack_database;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attrack_database.Pojos.EventPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EventsHolidays extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;
    private  Uri mImageUri;
    EditText t_name, t_description;
    ImageView event_image;
    Button submit;
    SelectImageHelper selectImageHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_holidays);

        getSupportActionBar().hide();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("event_Data");
        FirebaseStorage storage;
        final StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        t_name = findViewById(R.id.event_name);
        t_description = findViewById(R.id.description);
        submit = findViewById(R.id.submit_event);
        event_image = findViewById(R.id.image_event);

        selectImageHelper = new SelectImageHelper(this, event_image);

        event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = t_name.getText().toString();
                final String desc = t_description.getText().toString();
                final String id = databaseReference.push().getKey();

                if (mImageUri != null) {
                    final StorageReference reference = storageReference.child("/Image/" + name);
                    reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("1234", "Download Url" + uri.toString());

                                    EventPojo eventPojo = new EventPojo();
                                    eventPojo.setName(name);
                                    eventPojo.setDescription(desc);
                                    eventPojo.setId(id);

                                    eventPojo.setImage_url(uri.toString());

                                    databaseReference.child(id).setValue(eventPojo);
                                    Toast.makeText(EventsHolidays.this, "Event Created Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EventsHolidays.this, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    Toast.makeText(EventsHolidays.this, "No File Selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        selectImageHelper.handleResult(requestCode, resultCode, result);  // call this helper class method
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && result != null && result.getData() != null){
            mImageUri = result.getData();
            Picasso.with(this).load(mImageUri).into(event_image);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);   // call this helper class method
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventsHolidays.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
