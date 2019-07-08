package com.example.attrack_database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attrack_database.Pojos.SubjectPojo;

import java.util.ArrayList;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private ArrayList<SubjectPojo> values;
    private Context context;

    public SubjectAdapter(Context context, ArrayList<SubjectPojo> myDataset) {
        values = myDataset;
        this.context = context;
    }

    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_attendance, parent, false);
        SubjectAdapter.ViewHolder vh = new SubjectAdapter.ViewHolder(v);
        return vh;
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
        final SubjectPojo myPojo = values.get(position);

        holder.title.setText(myPojo.getSubject_name());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TakeAttendance.class);
                intent.putExtra("subject_selected", myPojo.getSubject_name());
                intent.putExtra("subject_id", myPojo.getSubject_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public LinearLayout linearLayout ;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.subject_name_adapter);
            linearLayout = v.findViewById(R.id.linearLayout_subject);

        }
    }

}
