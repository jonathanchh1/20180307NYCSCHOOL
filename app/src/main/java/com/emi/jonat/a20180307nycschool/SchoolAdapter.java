package com.emi.jonat.a20180307nycschool;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonat on 3/7/2018.
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder> {
    private final Callbacks mCallbacks;
    private final School mSchool = new School();
    private ArrayList<School> SchoolItemList;
    private int rowlayout;
    private Activity activity;

    public SchoolAdapter(ArrayList<School> mSchool, int school_content, FragmentActivity activity, Callbacks callbacks) {
        this.SchoolItemList = mSchool;
        this.rowlayout = school_content;
        this.activity = activity;
        this.mCallbacks = callbacks;
    }


    //Updating our UI whenever new data is available, remove old and renew
    public void setData(ArrayList<School> schooldata){
        remove();
        for(School school : schooldata){
            add(school);
        }
    }


    //updating on data removed.
    private void remove(){
        synchronized (mSchool){
            SchoolItemList.clear();
        }
        notifyDataSetChanged();
    }


    //clearing data
    public void clear() {
        while (getItemCount() > 0){
            remove();
        }
    }

//refreshing data.
    public void addAll(ArrayList<School> mSchool_list) {
        for(School Models : mSchool_list){
            add(Models);
        }
    }

    //adding data here
    private void add(School school){
        synchronized (mSchool){
            SchoolItemList.add(school);
        }
        notifyDataSetChanged();
    }
    @Override
    public SchoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //initializing  our view with rowlayout id of our layout in schoolfragment.
        View view = LayoutInflater.from(parent.getContext()).inflate(rowlayout, parent, false);
        return new SchoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SchoolViewHolder holder, int position) {

        //passing objects and getting our view positions.
        final School mSchoolItems = SchoolItemList.get(position);
        holder.items = mSchoolItems;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.OnClickListener(mSchoolItems, holder.getAdapterPosition());
            }
        });

        //ensuring our School method isn't null before passing information to holders.
        if(mSchoolItems != null) {
            holder.schoolname.setText(mSchoolItems.getTitle());
            holder.location.setText(mSchoolItems.getLocation());
            holder.campus.setText(mSchoolItems.getCampus());
            holder.borough.setText(mSchoolItems.getBorough());
        }

        //passing share action with school name and web object//
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSchoolItems != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, mSchoolItems.getTitle());
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mSchoolItems.getWebUrl());
                    activity.startActivity(Intent.createChooser(shareIntent, "sharing options"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return SchoolItemList.size();
    }



    //callback method for detailActivity
    public interface Callbacks{
        void OnClickListener(School mSchool, int position);
    }

    public static class SchoolViewHolder extends RecyclerView.ViewHolder{
        public School items;
        private TextView schoolname;
        private TextView location;
        private ImageButton shareButton;
        private TextView campus;
        private TextView borough;
        View mView;

        public SchoolViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //getting view ids to inflate
            schoolname = itemView.findViewById(R.id.school_name);
            location = itemView.findViewById(R.id.location);
            shareButton =  itemView.findViewById(R.id.share_button);
            campus = itemView.findViewById(R.id.campus);
            borough = itemView.findViewById(R.id.borough);
        }
    }
}
