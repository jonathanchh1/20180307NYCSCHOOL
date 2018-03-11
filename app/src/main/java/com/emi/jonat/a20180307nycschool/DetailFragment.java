package com.emi.jonat.a20180307nycschool;

import android.app.Activity;
import android.content.Intent;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonat on 3/7/2018.
 */

public class DetailFragment extends Fragment implements FetchSatAsync.Listener{
 private final static String TAG = DetailFragment.class.getSimpleName();
 private School mSchool = new School();
 private String EXTRA_SAT = "sat";
 private View rootView;
 private ArrayList<Sat> satlist;
 private Sat satObject = new Sat();
 private TextView SchoolName;
 private TextView detail_campus;
 private TextView campus;
 private TextView detail_phone_number;
 private TextView phone_number;

 private TextView detail_grades;
 private TextView grades;
 private TextView detail_interest;
 private TextView interest;
 private TextView description;
 private TextView NData;
 private LayoutInflater mLayoutInflater;
 private SatAdapter satAdapter;
 RecyclerView mRecyclerView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        CollapsingToolbarLayout appbarlayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if(appbarlayout != null && activity instanceof  DetailActivity){
            appbarlayout.setTitleEnabled(false);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;

        Bundle arguments = getArguments();
        Intent intent = getActivity().getIntent();

        if(arguments != null || intent != null && intent.hasExtra(DetailActivity.Args)){
            rootView = mLayoutInflater.inflate(R.layout.detail_fragment, container, false);
            if(arguments != null) {
                mSchool = getArguments().getParcelable(DetailActivity.Args);
            }else{
                mSchool = intent.getParcelableExtra(DetailActivity.Args);
            }

            ViewsList(mSchool, rootView);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            satlist = new ArrayList<>();
            mRecyclerView = rootView.findViewById(R.id.mrecyclerview);

            mRecyclerView.setLayoutManager(linearLayoutManager);
            satAdapter = new SatAdapter(satlist);
            mRecyclerView.setAdapter(satAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);


            if(savedInstanceState != null && savedInstanceState.containsKey(EXTRA_SAT)){
                satlist = savedInstanceState.getParcelableArrayList(EXTRA_SAT);

            }else{
                FetchSat(rootView);
            }
        }

        FetchSat(rootView);
        return rootView;
    }

    private void FetchSat(View rootView) {
        FetchSatAsync satAsync = new FetchSatAsync(this);
        satAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSchool.getDbn());
        Log.d(TAG, "detail Fragment dbn : " + " " + satObject.getDbn());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        satlist = satAdapter.getmSatList();
        if(satlist != null || !satlist.isEmpty()){
            outState.putParcelableArrayList(EXTRA_SAT, null);
        }

    }


    private void ViewsList(School school, View view){
        SchoolName = view.findViewById(R.id.detail_title);
        detail_campus = view.findViewById(R.id.detail_campus_textview);
        campus = view.findViewById(R.id.campus);
        detail_phone_number = view.findViewById(R.id.detail_phone_number_textview);
        phone_number = view.findViewById(R.id.phone_number);
        detail_grades = view.findViewById(R.id.detail_grades_textview);
        grades = view.findViewById(R.id.grades);
        detail_interest = view.findViewById(R.id.detail_interest_textview);
        interest = view.findViewById(R.id.interest);
        description = view.findViewById(R.id.description);
        NData = view.findViewById(R.id.empty_state);

        if(school != null){
            String mtitle = school.getTitle();
            String mcampus = school.getCampus();
            String phoneNumber = school.getPhoneNumber();
            String mgrades = school.getGrades();
            String minterest = school.getInterest();
            String mdescription = school.getDescription();
            SchoolName.setText(mtitle);
            detail_campus.setText(getResources().getString(R.string.campus));
            if(mcampus != null) {
                campus.setText(mcampus);
            }else{
                campus.setText(getResources().getString(R.string.unavailable));
            }
            detail_phone_number.setText(getResources().getString(R.string.phone));
            phone_number.setText(phoneNumber);
            detail_grades.setText(getResources().getString(R.string.grades));
            grades.setText(mgrades);
            detail_interest.setText(getResources().getString(R.string.specialize));
            interest.setText(minterest);
            description.setText(mdescription);

        }

    }


    @Override
    public void onFetchFinished(Sat sat) {
        if (sat != null) {
            satAdapter.clear();
            if (sat.getDbn() != null) {
                satAdapter.add(sat);
            } else{
                NData.setVisibility(View.VISIBLE);
            }

        }else {
                Log.d(TAG, getResources().getString(R.string.unable));
            }

    }
}
