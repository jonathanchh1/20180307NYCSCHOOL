package com.emi.jonat.a20180307nycschool;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonat on 3/7/2018.
 */

public class DetailFragment extends Fragment implements FetchSatAsync.Listener {
    private final static String TAG = DetailFragment.class.getSimpleName();
    private School mSchool = new School();
    private String EXTRA_SAT = "sat";
    private MapView mapView;
    private View rootView;
    private ArrayList<Sat> satlist;
    private Sat satObject = new Sat();
    private TextView SchoolName;
    private TextView detail_campus;
    private TextView campus;
    private TextView detail_phone_number;
    private TextView phone_number;
    private final int ZOOM = 15;
    private TextView Extra;
    private TextView detail_grades;
    private TextView grades;
    private TextView detail_interest;
    private TextView interest;
    private TextView description;
    private TextView bus;
    private TextView NData;
    private Bundle mBundle;
    private LayoutInflater mLayoutInflater;
    private SatAdapter satAdapter;
    RecyclerView mRecyclerView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        //inflating our appbar here and enabling features.
        CollapsingToolbarLayout appbarlayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appbarlayout != null && activity instanceof DetailActivity) {
            appbarlayout.setTitleEnabled(false);
        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding Bundle for maps
        mBundle = savedInstanceState;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mLayoutInflater = inflater;

        //getting bundle information if available or intent with pojo
        Bundle arguments = getArguments();
        Intent intent = getActivity().getIntent();

        if (arguments != null || intent != null && intent.hasExtra(DetailActivity.Args)) {
            rootView = mLayoutInflater.inflate(R.layout.detail_fragment, container, false);
            if (arguments != null) {
                mSchool = getArguments().getParcelable(DetailActivity.Args);
            } else {
                mSchool = intent.getParcelableExtra(DetailActivity.Args);
            }

            //initializing all our views here.
            ViewsList(mSchool, rootView);


            mapView = (MapView) rootView.findViewById(R.id.mapview);
            mapView.onCreate(mBundle);
            double lat = Double.parseDouble(mSchool.getLat());
            double lg = Double.parseDouble(mSchool.getLongitude());
            setMap(lat, lg);
            Log.d(TAG, "school types : " + " " + lat + " " + lg);
            //setting up recyclerview with linearlayout
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            satlist = new ArrayList<>();
            mRecyclerView = rootView.findViewById(R.id.mrecyclerview);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            satAdapter = new SatAdapter(satlist);
            mRecyclerView.setAdapter(satAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);


            //restoring our state here if screen is rotated and passing data.
            if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_SAT)) {
                satlist = savedInstanceState.getParcelableArrayList(EXTRA_SAT);

            } else {
                FetchSat(rootView);
            }
        }

        LoadData(mSchool);
        FetchSat(rootView);
        return rootView;
    }

    //initializing asych with our parameter id, passing School Pojo dbn id//
    private void FetchSat(View rootView) {
        FetchSatAsync satAsync = new FetchSatAsync(this);
        satAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSchool.getDbn());
        Log.d(TAG, "detail Fragment dbn : " + " " + satObject.getDbn());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        satlist = satAdapter.getmSatList();
        //adding data for our state instance here
        if (satlist != null || !satlist.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_SAT, null);
        }

    }


    //our view Method, getting all ids of our views.
    private void ViewsList(School school, View view) {
        SchoolName = view.findViewById(R.id.detail_title);
        detail_campus = view.findViewById(R.id.detail_campus_textview);
        campus = view.findViewById(R.id.campus);
        detail_phone_number = view.findViewById(R.id.detail_phone_number_textview);
        phone_number = view.findViewById(R.id.phone_number);
        Extra = view.findViewById(R.id.extracu);
        bus = view.findViewById(R.id.bus);
        detail_grades = view.findViewById(R.id.detail_grades_textview);
        grades = view.findViewById(R.id.grades);
        detail_interest = view.findViewById(R.id.detail_interest_textview);
        interest = view.findViewById(R.id.interest);
        description = view.findViewById(R.id.description);
        NData = view.findViewById(R.id.empty_state);
        LoadData(school);
    }

    ///loading our detail into views;
    private void LoadData(School school) {

        //ensuring our data is not null before passing values to views
        if (school != null) {
            String mtitle = school.getTitle();
            String mcampus = school.getCampus();
            String phoneNumber = school.getPhoneNumber();
            String mgrades = school.getGrades();
            String minterest = school.getInterest();
            String mdescription = school.getDescription();
            String mExtra = school.getExtraCurri();
            String Bus = school.getBus();

            if (mtitle != null) {
                SchoolName.setText(mtitle);
            } else {
                SchoolName.setText(getResources().getString(R.string.unavailable));
            }

            if (mcampus != null) {
                campus.setText(mcampus);
            } else {
                campus.setText(getResources().getString(R.string.unavailable));
            }

            if (phoneNumber != null) {
                phone_number.setText(phoneNumber);
            } else {
                phone_number.setText(getResources().getString(R.string.unavailable));
            }

            if (mgrades != null) {
                grades.setText(mgrades);
            } else {
                grades.setText(getResources().getString(R.string.unavailable));
            }


            if (minterest != null) {
                interest.setText(minterest);
            } else {
                interest.setText(getResources().getString(R.string.unavailable));
            }

            if (mdescription != null) {
                description.setText(mdescription);

            } else {
                description.setText(getResources().getString(R.string.unavailable));
            }

            if (mExtra != null) {
                Extra.setText(mExtra);
            } else {
                Extra.setText(getResources().getString(R.string.unavailable));
            }

            if (Bus != null) {
                bus.setText(Bus);
            } else {
                bus.setText(getResources().getString(R.string.unavailable));
            }
        }

        detail_campus.setText(getResources().getString(R.string.campus));
        detail_phone_number.setText(getResources().getString(R.string.phone));
        detail_grades.setText(getResources().getString(R.string.grades));
        detail_interest.setText(getResources().getString(R.string.specialize));
    }

    @Override
    public void onFetchFinished(Sat sat) {
        if (sat != null) {
            satAdapter.clear();
            //making sure our dbn is not null before passing  objects to adapter
            if (sat.getDbn() != null) {
                satAdapter.add(sat);
            } else {
                //dbn is null or no data is found, we pass our empty state to indicate unavailability//
                NData.setVisibility(View.VISIBLE);
            }

        } else {

            //if async fails, we will be notified by passing an error message.
            Log.d(TAG, getResources().getString(R.string.unable));
        }

    }

    public void setMap(final double latitude, final double longitude){
        mapView.onCreate(mBundle);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                final GoogleMap map = googleMap;

                MapsInitializer.initialize(getContext());
                //change map type as your requirements
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng marker = new LatLng(latitude, longitude);
                //move the camera default animation
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, ZOOM));

                map.setMinZoomPreference(10);
                //add radius
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(marker);
                circleOptions.radius(300);
                circleOptions.fillColor(getResources().getColor(R.color.light_green));
                circleOptions.strokeColor(Color.BLUE);
                circleOptions.strokeWidth(4);
                map.addCircle(circleOptions);

                //add a default marker in the position
              Marker m =  map.addMarker(new MarkerOptions().position(marker).title(mSchool.getTitle())
                      .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
              m.showInfoWindow();
            }
        });
    }

    }

