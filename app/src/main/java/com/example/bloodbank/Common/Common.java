package com.example.bloodbank.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bloodbank.Model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.opencensus.stats.View;

/**
 * Created by mohamed on 4/14/18.
 */

public class Common {

    public static User currentUser;
    public static String doctor_password = "1234";
    public static String admin_password = "admin";
    public static String UserInformation_category = "UserInformation";
    public static String Appointement_category = "Appointements";
    public static String Nationality_category = "Nationality";
    public static String User_category = "User";
    public static String Hospitals_category = "Hospitals";
    public static String SelectHospitals_category = "SelectHospitals";
    public static String Area_category = "Area";
    public static String Hospital_category = "Hospital";
    public static String Time_category = "Time";


    //for checking connection internet
    public static boolean isConnectToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }


    //get date
    public static String getDate_hour() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }   //get date

    // get today date
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }




    // check if selected date is between dates
    public static boolean checkBetween(String startDate, String dateToCheck, String endDate) {
        boolean res = false;
        SimpleDateFormat fmt1 = new SimpleDateFormat("dd/MM/yyyy"); //22-05-2013
        SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy"); //22-05-2013
        try {
            Date requestDate = fmt2.parse(dateToCheck);
            Date fromDate = fmt1.parse(startDate);
            Date toDate = fmt1.parse(endDate);
            res = requestDate.compareTo(fromDate) >= 0 && requestDate.compareTo(toDate) <= 0;
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        return res;
    }

}
