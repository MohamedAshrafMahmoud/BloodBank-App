package com.example.bloodbank.Views.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.bloodbank.R;
import com.example.bloodbank.Views.SignIn;

import maes.tech.intentanim.CustomIntent;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }


    public void goaddarea(View view) {
        startActivity(new Intent(AdminActivity.this, AddHospitalActivity.class));
        CustomIntent.customType(this, "left-to-right");

    }


    public void goOut(View view) {
        startActivity(new Intent(AdminActivity.this, SignIn.class));
        CustomIntent.customType(this, "left-to-right");
    }

    public void godonationsrequests(View view) {

        startActivity(new Intent(AdminActivity.this, AddNationalityActivity.class));
        CustomIntent.customType(this, "left-to-right");
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("خروج")
                .setMessage("هل انت متاكد من الخروج!")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
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
