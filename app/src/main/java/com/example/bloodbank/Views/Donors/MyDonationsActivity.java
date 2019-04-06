package com.example.bloodbank.Views.Donors;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Adapters.CurrentAppointementsAdapter;
import com.example.bloodbank.Adapters.MyDonationsViewHolder;
import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class MyDonationsActivity extends AppCompatActivity {


    DatabaseReference databaseReference;

    RecyclerView recycler;
    TextView emptyView;

    List<Appointement> list;

    CurrentAppointementsAdapter Cadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations);

        initViews();

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.Appointement_category);

        loadCurrentData();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadCurrentData() {

        databaseReference.orderByChild("name").equalTo(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Appointement>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Appointement appointement = dataSnapshot1.getValue(Appointement.class);

                    list.add(appointement);

                }

                if (list.isEmpty()) {
                    recycler.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recycler.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                Cadapter = new CurrentAppointementsAdapter(list, MyDonationsActivity.this);
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
        recycler.setItemAnimator(new DefaultItemAnimator());

        emptyView = (TextView) findViewById(R.id.empty_view);


    }


    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyDonationsActivity.this, DonorsActivity.class));
    }
}
