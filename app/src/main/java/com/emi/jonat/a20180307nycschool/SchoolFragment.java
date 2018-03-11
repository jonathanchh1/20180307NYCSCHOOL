package com.emi.jonat.a20180307nycschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jonat on 3/7/2018.
 */

public  class SchoolFragment extends Fragment {

    public static final String TAG = SchoolFragment.class.getSimpleName();
    private View rootView;
    private RecyclerView mRecyclerView;
    public static final String SCHOOL = "school";
    private ProgressBar mProgressbar;
    private ApiInterface apiService;
    private ArrayList<School> mSchool_List;

    //callback interface for detailactivity.
    private SchoolAdapter.Callbacks mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recycler_list, container, false);


        //initializing recyclerview and arraylist here
        mRecyclerView = rootView.findViewById(R.id.recycler_lists);
        mProgressbar = rootView.findViewById(R.id.progress_bar);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSchool_List = new ArrayList<>();

        //ensuring our state is restored after screen orientation, we passed our arraylist;
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(SCHOOL)){
                mSchool_List = savedInstanceState.getParcelableArrayList(SCHOOL);
            }else{
                FetchSchools();
            }
        }

        FetchSchools();
        mCalllback();
        return rootView;
    }


    //Retrofit method to fetch School object Arraylist
    private void FetchSchools() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<School>> call = apiService.getSchoolInformation();
        call.enqueue(new Callback<ArrayList<School>>() {
            @Override
            public void onResponse(Call<ArrayList<School>> call, Response<ArrayList<School>> response) {
                int statusCode = response.code();

                //using Retrofit bool SUC to ensure response is correct before passing objects into Arraylist.
                if(response.isSuccessful()) {
                    mSchool_List = response.body();
                            Log.d(TAG, "response list : " + mSchool_List);
                            for(School school : mSchool_List) {
                                Log.d(TAG, "dbn id : " + school.getDbn());
                            }
                    mRecyclerView.setAdapter(new SchoolAdapter(mSchool_List, R.layout.school_content, getActivity(), mCallback));
                    mProgressbar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<School>> call, Throwable t) {
                //set up an error catcher to make sure we know if network times out or other issues//
                Log.d(TAG, "error message" + t.toString());
                mProgressbar.setVisibility(View.VISIBLE);
            }

        });

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //parceable for restoring instance on screen orientation.
            outState.putParcelableArrayList(SCHOOL, null);

        super.onSaveInstanceState(outState);
    }


    private void mCalllback(){
        //arguments for detail activity passed here with view position//
        mCallback = new SchoolAdapter.Callbacks() {
            @Override
            public void OnClickListener(School mSchool, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.Args, mSchool);
                startActivity(intent);
            }
        };
    }


}
