package com.example.bloodbank.Views.Doctors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.Model.UserInformation;
import com.example.bloodbank.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class StatisticsActivity extends AppCompatActivity {

    BarChart barChart;
    TextView male, female, txtdonationsucess, txtdonationbad, txttodaydonations;
    ArrayList<UserInformation> informations_opp = new ArrayList<UserInformation>();
    ArrayList<Appointement> appointement_opp = new ArrayList<Appointement>();

    ArrayList<String> informations = new ArrayList<>();

    int male_n, female_n, success, bad, today_donations = 0;

    DatabaseReference info_databaseReference, appo_databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        info_databaseReference = FirebaseDatabase.getInstance().getReference(Common.UserInformation_category);
        appo_databaseReference = FirebaseDatabase.getInstance().getReference(Common.Appointement_category);

        initViews();

        loadAppointementData();
        loadInformationData();

        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void loadInformationData() {

        info_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);

                        informations_opp.add(userInformation);

                    }

                    for (UserInformation userInformation : informations_opp) {

                        if (userInformation.getType().equals("Male")) {
                            male_n++;
                        } else if (userInformation.getType().equals("Female")) {
                            female_n++;
                        }
                    }


                    male.setText(male_n + "");
                    female.setText(female_n + "");
                    setData();


                } else {

                    male.setText(0 + "");
                    female.setText(0 + "");
                    setData();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadAppointementData() {

        appo_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Appointement appointement = dataSnapshot.getValue(Appointement.class);

                        appointement_opp.add(appointement);

///////////////////////////
                        String[] current_date = Common.getDate().split("/");
                        String[] donors_date = appointement.getDate().split("/");

                        if (Integer.parseInt(current_date[0]) == Integer.parseInt(donors_date[0]) && Integer.parseInt(current_date[1]) == Integer.parseInt(donors_date[1]) && Integer.parseInt(current_date[2]) == Integer.parseInt(donors_date[2])) {
                            today_donations++;

                        }
                        txttodaydonations.setText(today_donations + "");


                    }
///////////////////////////
                    for (Appointement appointement : appointement_opp) {

                        if (appointement.isDone() == true) {
                            success++;
                        } else if (appointement.isDone() == false) {
                            bad++;
                        }
                    }
                    txtdonationsucess.setText(success + "");
                    txtdonationbad.setText(bad + "");


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


    private void setData() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(1, male_n)); //male
        entries.add(new BarEntry(2, female_n));  //female
        entries.add(new BarEntry(3, success));  //female
        entries.add(new BarEntry(4, bad));  //female
        entries.add(new BarEntry(5, today_donations));  //female

        BarDataSet set = new BarDataSet(entries, "Dates Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);


        BarData data = new BarData(set);


        barChart.setData(data);
        barChart.invalidate();
        barChart.animateY(5000);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        barChart = (BarChart) findViewById(R.id.chart);
        male = (TextView) findViewById(R.id.txtmale);
        female = (TextView) findViewById(R.id.txtfemale);
        txttodaydonations = (TextView) findViewById(R.id.txt_today_donations);
        txtdonationbad = (TextView) findViewById(R.id.txt_donation_bad);
        txtdonationsucess = (TextView) findViewById(R.id.txt_donation_sucess);

    }


    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StatisticsActivity.this, DoctorsActivity.class));
    }
}
