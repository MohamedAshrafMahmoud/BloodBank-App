package com.example.bloodbank.Views.Doctors;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Adapters.DoctorApprovalAdapter;
import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class DonitionsRequestActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    RecyclerView recycler;
    TextView emptyView;

    List<Appointement> list;

    DoctorApprovalAdapter Cadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donitions_request);

        initViews();

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.Appointement_category);


        loadCurrentData();

    }


    //////////////////////////////////////////////////////////////////////////////////
    private void loadCurrentData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Appointement>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Appointement appointement = dataSnapshot1.getValue(Appointement.class);

                    appointement.setId(dataSnapshot1.getKey());

                    String[] current_date = Common.getDate().split("/");
                    String[] donors_date = appointement.getDate().split("/");

                    if (Integer.parseInt(current_date[0]) == Integer.parseInt(donors_date[0]) && Integer.parseInt(current_date[1]) == Integer.parseInt(donors_date[1]) && Integer.parseInt(current_date[2]) == Integer.parseInt(donors_date[2])) {

                        list.add(appointement);
                    }

                }

                if (list.isEmpty()) {
                    recycler.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recycler.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                Cadapter = new DoctorApprovalAdapter(list, DonitionsRequestActivity.this);
                recycler.setAdapter(Cadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
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

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        emptyView = (TextView) findViewById(R.id.empty_view);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DonitionsRequestActivity.this, DoctorsActivity.class));
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
}
