package com.emi.jonat.a20180307nycschool;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView EmptyState;
    private Button btnRetry;
    private SwipeRefreshLayout swipeContainer;
    private SchoolAdapter mAdapter;

    //callback interface for detailactivity.
    private SchoolAdapter.Callbacks mCallback;

    public SchoolFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recycler_list, container, false);


        //initializing recyclerview and arraylist here
        mRecyclerView = rootView.findViewById(R.id.recycler_lists);
        mProgressbar = rootView.findViewById(R.id.progress_bar);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initializing arrayList
        mSchool_List = new ArrayList<>();

        //views to handle any network connectivity issues;
        btnRetry = (Button) rootView.findViewById(R.id.error_btn_retry);
        EmptyState = (TextView) rootView.findViewById(R.id.empty_states);

        //handling manual refresh of our view and triggering new data loading
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //once the network request has completed successfully
                FetchRefreshData();
                swipeContainer.setRefreshing(false);
            }
        });

        //Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //adding our data on network renew manually.
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(getContext())) {
                    FetchSchools();
                }
            }
        });

        //ensuring our state is restored after screen orientation, we passed our arraylist;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SCHOOL)) {
                mSchool_List = savedInstanceState.getParcelableArrayList(SCHOOL);
            } else {
                if (isNetworkAvailable(getActivity())) {
                    FetchSchools();
                }
            }
        }


        //making sure we have a network before calling our data.
        if (isNetworkAvailable(getActivity())) {
            FetchSchools();

            btnRetry.setVisibility(View.GONE);
            EmptyState.setVisibility(View.GONE);
        } else {
            btnRetry.setVisibility(View.VISIBLE);
            EmptyState.setVisibility(View.VISIBLE);
        }

        mCalllback();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        FetchSchools();
    }

    private void FetchRefreshData() {
        mAdapter.clear();
        //the data has come back, add new items
        mAdapter.addAll(mSchool_List);
        FetchSchools();
        swipeContainer.setRefreshing(false);
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
                if (response.isSuccessful()) {
                    mSchool_List = response.body();
                    Log.d(TAG, "response list : " + mSchool_List);
                    for (School school : mSchool_List) {
                        Log.d(TAG, "dbn id : " + school.getDbn());
                    }
                    mAdapter = new SchoolAdapter(mSchool_List, R.layout.school_content, getActivity(), mCallback);
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressbar.setVisibility(View.GONE);
                    btnRetry.setVisibility(View.GONE);
                    EmptyState.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<ArrayList<School>> call, Throwable t) {
                //set up an error catcher to make sure we know if network times out or other issues//
                Log.d(TAG, "error message" + t.toString());
                mProgressbar.setVisibility(View.VISIBLE);
                btnRetry.setVisibility(View.VISIBLE);
                EmptyState.setVisibility(View.VISIBLE);
            }

        });

    }

    //method for checking our network state.
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network), Toast.LENGTH_SHORT).show();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //parceable for restoring instance on screen orientation.
        outState.putParcelableArrayList(SCHOOL, null);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem menuSearch = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuSearch);
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isNetworkAvailable(getActivity())){
                    //we clear data to add our query
                    mAdapter.getFilter().filter(query);
                }else{
                    EmptyState.setVisibility(View.VISIBLE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //we will do the same on text changed.
                if(isNetworkAvailable(getActivity())){
                    mAdapter.getFilter().filter(newText);
                }else{
                    EmptyState.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
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
