package com.example.bloodbank.Views.Donors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.Model.Dates;
import com.example.bloodbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import maes.tech.intentanim.CustomIntent;

public class DonationFile extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtdonationsucess, txtdonationbad, txtcomingdonation;

    ArrayList<Appointement> appointement_opp = new ArrayList<Appointement>();
    ArrayList<Dates> dates_opp = new ArrayList<Dates>();
    ArrayList<String> appointement = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();

    DatabaseReference databaseReference;

    int donation_sucess, donation_bad = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_file);

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.Appointement_category);

        initViews();

        loadData();
        loadDate();

        initViews();

    }
/////////////////////////////////////////////////////////////////////////////////////


    private void loadData() {

        databaseReference.orderByChild("name").equalTo(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Appointement appointement = dataSnapshot.getValue(Appointement.class);

                        appointement_opp.add(appointement);

                    }

                    for (Appointement appointement : appointement_opp) {

                        if (appointement.isDone() == true) {
                            donation_sucess++;
                        } else if (appointement.isDone() == false) {
                            donation_bad++;
                        }
                    }

                    txtdonationsucess.setText(donation_sucess + "");
                    txtdonationbad.setText(donation_bad + "");

                } else {

                    txtdonationsucess.setText(0 + "");
                    txtdonationbad.setText(0 + "");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadDate() {

        databaseReference.orderByChild("name").equalTo(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Dates date = dataSnapshot.getValue(Dates.class);

                        dates_opp.add(date);

                    }

                    for (Dates date1 : dates_opp) {
                        dates.add(date1.getDate());

                    }

                    //get minmum date

                    ArrayList<Date> dateList = new ArrayList<Date>();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    for (String dateString : dates) {
                        try {
                            dateList.add(simpleDateFormat.parse(dateString));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    Date minDate = Collections.min(dateList);

                    txtcomingdonation.setText(simpleDateFormat.format(minDate)+"");


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void initViews() {

        txtcomingdonation = (TextView) findViewById(R.id.txt_coming_donation);
        txtdonationbad = (TextView) findViewById(R.id.txt_donation_bad);
        txtdonationsucess = (TextView) findViewById(R.id.txt_donation_sucess);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DonationFile.this, ProfileActivity.class));
    }


}
