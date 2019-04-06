package com.example.bloodbank.Views.Donors;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.bloodbank.R;
import com.example.bloodbank.Views.Doctors.StatisticsActivity;
import com.example.bloodbank.Views.SignIn;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

import maes.tech.intentanim.CustomIntent;


public class DonorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors);

        show();
    }

    private void show() {


        com.tapadoo.alerter.Alerter.create(this)
                .setTitle("Blood Bank")
                .setText("شكرا لك علي تبرعك سوف يساهم في انقاذ الكثير")
                .setIcon(R.drawable.ic_face_black_24dp)
                .setBackgroundColorRes(R.color.colorAccent)
                .setDuration(2000)
                .enableSwipeToDismiss() //seems to not work well with OnClickListener
                .enableProgress(true)
                .setProgressColorRes(R.color.colorAccent)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do something when Alerter message was clicked
                    }
                })
                .setOnShowListener(new OnShowAlertListener() {
                    @Override
                    public void onShow() {
                        //do something when Alerter message shows
                    }
                })
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        //do something when Alerter message hides
                    }
                })
                .show();

    }


    public void gobooknew(View view) {
        startActivity(new Intent(DonorsActivity.this, BookNewAppointmentActivity.class));
        CustomIntent.customType(this, "left-to-right");

    }

    public void gocurrentOpp(View view) {
        startActivity(new Intent(DonorsActivity.this, CurrentAppointmentActivity.class));
        CustomIntent.customType(this, "left-to-right");


    }

    public void golastopp(View view) {
        startActivity(new Intent(DonorsActivity.this, LastAppointmentActivity.class));
        CustomIntent.customType(this, "left-to-right");


    }

    public void godonations(View view) {
        startActivity(new Intent(DonorsActivity.this, MyDonationsActivity.class));
        CustomIntent.customType(this, "left-to-right");


    }

    public void goprofile(View view) {
        startActivity(new Intent(DonorsActivity.this, ProfileActivity.class));
        CustomIntent.customType(this, "left-to-right");


    }

    public void gonotifications(View view) {
        startActivity(new Intent(DonorsActivity.this, NotificationsActivity.class));
        CustomIntent.customType(this, "left-to-right");


    }

    public void gostatistics(View view) {
        startActivity(new Intent(DonorsActivity.this, StatisticsActivity.class));
        CustomIntent.customType(this, "left-to-right");


    }

    public void gomyinfo(View view) {
        startActivity(new Intent(DonorsActivity.this, MyInfoActivity.class));
        CustomIntent.customType(this, "left-to-right");


    }

    public void goOut(View view) {
        startActivity(new Intent(DonorsActivity.this, SignIn.class));
        CustomIntent.customType(this, "left-to-right");

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("خروج")
                .setMessage("هل انت متاكد من الخروج!")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }

                })
                .setNegativeButton("لا", null)
                .show();
    }
}
