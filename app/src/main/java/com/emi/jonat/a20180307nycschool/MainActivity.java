package com.emi.jonat.a20180307nycschool;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private  FragmentManager fragmentManager;
  private Toolbar mToolbar;
  private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //here we are going to inflate the toolbar with an aligned app title.
        mToolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        title.setText(mToolbar.getTitle());
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //here we are initializing the fragment for school.
         if(savedInstanceState == null) {
             fragmentManager = getSupportFragmentManager();
             fragmentManager.beginTransaction()
             .add(R.id.school_container, new SchoolFragment())
                     .commit();
         }
    }


}
