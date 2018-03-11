package com.emi.jonat.a20180307nycschool;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by jonat on 3/8/2018.
 */

public class SatAdapter extends RecyclerView.Adapter<SatAdapter.ViewHolder>{

    private String TAG = SatAdapter.class.getSimpleName();
    private ArrayList<Sat> mSatList;


    public SatAdapter(ArrayList<Sat> sats){
        mSatList = sats;
    }


    public void removes(Sat sat){
        int position = mSatList.indexOf(sat);
        if(position > -1){
            mSatList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear(){
        while(getItemCount() > 0){
            removes(getItem(0));
        }
    }


    public Sat getItem(int position){
        return mSatList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sat_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Sat sat = mSatList.get(position);
        holder.mSat = sat;

        if(sat != null) {
                holder.Avg_Writing.setText(sat.getWriting_avg());
                holder.Avg_Reading.setText(sat.getReading_avg());
                holder.Avg_Math.setText(sat.getMath_avg());
                holder.Takers.setText(sat.getNum_takers());

        }

    }

    @Override
    public int getItemCount() {
        return mSatList.size();
    }

    public void add(Sat sats) {
        mSatList.clear();
        mSatList.add(sats);
        notifyDataSetChanged();
    }

    public void setmSatList(ArrayList<Sat> mSatList) {
        this.mSatList = mSatList;
    }

    public ArrayList<Sat> getmSatList() {
        return mSatList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public final View mView;
        public Sat mSat;
        private TextView Takers;
        private TextView Avg_Writing;
        private TextView Avg_Reading;
        private TextView Avg_Math;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            Takers = itemView.findViewById(R.id.takers_title);
            Avg_Writing = itemView.findViewById(R.id.writing);
            Avg_Reading = itemView.findViewById(R.id.reading);
            Avg_Math = itemView.findViewById(R.id.math);

        }
    }
}
