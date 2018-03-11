package com.emi.jonat.a20180307nycschool;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jonat on 3/10/2018.
 */

public class FetchSatAsync extends AsyncTask<String, Void, Sat> {
    public static final String TAG = FetchSatAsync.class.getSimpleName();
    private final Listener mListener;
    private  Sat model = new Sat();
    public FetchSatAsync(Listener listener){
        this.mListener = listener;
    }


    @Override
    protected Sat doInBackground(String... params) {
        if(params == null){
            return  null;
        }

        String id = params[0];
        ApiInterface services = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Sat>> call = services.getSchoolSAT();
        try{
            Response<ArrayList<Sat>> response = call.execute();
            if(response.isSuccessful()){
                ArrayList<Sat> satslist = response.body();
                for(Sat satObj : satslist) {
                    if(satObj.getDbn().equals(id)) {
                        model = satObj;
                    }
                }

            Log.d(TAG, "model dbn Id : " +  " " + model.getDbn());
                }

                return model;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Sat sat) {
        if(sat != null){
            mListener.onFetchFinished(sat);
        }else{
            mListener.onFetchFinished(null);
        }
    }

    public interface Listener{
        void onFetchFinished(Sat sat);
    }
}
