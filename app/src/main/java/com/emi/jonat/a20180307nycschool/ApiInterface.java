package com.emi.jonat.a20180307nycschool;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jonat on 3/7/2018.
 */

public interface ApiInterface {
    @GET("97mf-9njv.json")
    Call<ArrayList<School>> getSchoolInformation();

    @GET("734v-jeq5.json")
    Call<ArrayList<Sat>> getSchoolSAT();




}
