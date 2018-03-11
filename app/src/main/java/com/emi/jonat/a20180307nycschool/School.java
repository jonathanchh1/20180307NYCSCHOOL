package com.emi.jonat.a20180307nycschool;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jonat on 3/7/2018.
 */


public class School  implements Parcelable{
    @SerializedName("dbn")
    private String dbn;
    @SerializedName("school_name")
    private String Title;
    @SerializedName("city")
    private String city;
    @SerializedName("borough")
    private String borough;
    @SerializedName("extracurricular_activities")
    private String ExtraCurri;
    @SerializedName("phone_number")
    private String PhoneNumber;
    @SerializedName("grades2018")
    private String Grades;
    @SerializedName("interest1")
    private String Interest;
    @SerializedName("location")
    private String location;
    @SerializedName("campus_name")
    private String campus;
    @SerializedName("overview_paragraph")
    private String description;
    @SerializedName("advancedplacement_courses")
    private String AdvancePlacement_Courses;
    @SerializedName("website")
    private String webUrl;



    public School() {
        //empty constructor to allow us to access model data for update sych of UI.
    }


    protected School(Parcel in) {
        dbn = in.readString();
        Title = in.readString();
        city = in.readString();
        borough = in.readString();
        ExtraCurri = in.readString();
        PhoneNumber = in.readString();
        Grades = in.readString();
        Interest = in.readString();
        location = in.readString();
        campus = in.readString();
        description = in.readString();
        AdvancePlacement_Courses = in.readString();
        webUrl = in.readString();
    }

    public static final Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };

    public School(Cursor cursor) {
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getCampus() {
        return campus;
    }


    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getInterest() {
        return Interest;
    }

    public String getExtraCurri() {
        return ExtraCurri;
    }

    public String getDescription() {
        return description;
    }

    public String getDbn() {
        return dbn;
    }

    public String getBorough() {
        return borough;
    }

    public String getGrades() {
        return Grades;
    }

    public String getCity() {
        return city;
    }

    public String getTitle() {
        return Title;
    }

    public String getAdvancePlacement_Courses() {
        return AdvancePlacement_Courses;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInterest(String interest) {
        Interest = interest;
    }

    public void setGrades(String grades) {
        Grades = grades;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDbn(String dbn) {
        this.dbn = dbn;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAdvancePlacement_Courses(String advancePlacement_Courses) {
        AdvancePlacement_Courses = advancePlacement_Courses;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setExtraCurri(String extraCurri) {
        ExtraCurri = extraCurri;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dbn);
        dest.writeString(Title);
        dest.writeString(city);
        dest.writeString(borough);
        dest.writeString(ExtraCurri);
        dest.writeString(PhoneNumber);
        dest.writeString(Grades);
        dest.writeString(Interest);
        dest.writeString(location);
        dest.writeString(campus);
        dest.writeString(description);
        dest.writeString(AdvancePlacement_Courses);
        dest.writeString(webUrl);
    }
}
