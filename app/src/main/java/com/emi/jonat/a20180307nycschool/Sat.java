package com.emi.jonat.a20180307nycschool;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jonat on 3/8/2018.
 */

public class Sat implements Parcelable {
    @SerializedName("dbn")
    private String dbn;
    @SerializedName("num_of_sat_test_takers")
    private String num_takers;
    @SerializedName("sat_math_avg_score")
    private String math_avg;
    @SerializedName("sat_writing_avg_score")
    private String writing_avg;
    @SerializedName("sat_critical_reading_avg_score")
    private String reading_avg;
    @SerializedName("school_name")
    private String school_title;


    protected Sat(Parcel in) {
        dbn = in.readString();
        num_takers = in.readString();
        math_avg = in.readString();
        writing_avg = in.readString();
        reading_avg = in.readString();
        school_title = in.readString();
    }

    public Sat() {

    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dbn);
        dest.writeString(num_takers);
        dest.writeString(math_avg);
        dest.writeString(writing_avg);
        dest.writeString(reading_avg);
        dest.writeString(school_title);
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sat> CREATOR = new Creator<Sat>() {
        @Override
        public Sat createFromParcel(Parcel in) {
            return new Sat(in);
        }

        @Override
        public Sat[] newArray(int size) {
            return new Sat[size];
        }
    };

    public void setMath_avg(String math_avg) {
        this.math_avg = math_avg;
    }




    public void setSchool_title(String school_title) {
        this.school_title = school_title;
    }

    public void setWriting_avg(String writing_avg) {
        this.writing_avg = writing_avg;
    }

    public void setNum_takers(String num_takers) {
        this.num_takers = num_takers;
    }

    public void setReading_avg(String reading_avg) {
        this.reading_avg = reading_avg;
    }

    public String getWriting_avg() {
        return writing_avg;
    }

    public String getSchool_title() {
        return school_title;
    }

    public String getReading_avg() {
        return reading_avg;
    }

    public String getNum_takers() {
        return num_takers;
    }

    public String getDbn() {
        return dbn;
    }

    public String getMath_avg() {
        return math_avg;
    }

    public void setDbn(String dbn) {
        this.dbn = dbn;
    }
}
