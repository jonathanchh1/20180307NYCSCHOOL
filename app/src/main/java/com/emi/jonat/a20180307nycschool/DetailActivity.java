package com.emi.jonat.a20180307nycschool;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    public static final String Args = "arguments";
    //initializing DetailFragment here--//
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //actionbar enabled for home back button
      ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //if our savedinstance is null our fragment will be passed//
         if(savedInstanceState == null){
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container, new DetailFragment())
                        .commit();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //implementing home button
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
