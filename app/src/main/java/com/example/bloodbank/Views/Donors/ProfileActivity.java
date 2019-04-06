package com.example.bloodbank.Views.Donors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Nationality;
import com.example.bloodbank.Model.UserInformation;
import com.example.bloodbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class ProfileActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    UserInformation userInformation;
    private Toolbar toolbar;
    private TextView txtname, txtkind, txtidentity, txtnationality, txtbirthdate, txtbloodtype, txtphone, txtaccount, txtadress, txtpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.UserInformation_category);


        loadData();


    }


    //////////////////////////////////////////////////////////////////////////////////////
    private void loadData() {
        databaseReference.child(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("account") && dataSnapshot.hasChild("adress") && dataSnapshot.hasChild("birth_date") && dataSnapshot.hasChild("blood_type") && dataSnapshot.hasChild("identification") && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("nationality") && dataSnapshot.hasChild("password") && dataSnapshot.hasChild("phonenumber") && dataSnapshot.hasChild("type")) {
                    userInformation = dataSnapshot.getValue(UserInformation.class);


                    txtname.setText(Common.currentUser.getName());
                    txtkind.setText(userInformation.getType());
                    txtidentity.setText(userInformation.getIdentification());
                    txtnationality.setText(userInformation.getNationality());
                    txtbirthdate.setText(userInformation.getBirth_date());
                    txtbloodtype.setText(userInformation.getBlood_type());
                    txtphone.setText(Common.currentUser.getPhone());
                    txtaccount.setText(Common.currentUser.getEmail());
                    txtadress.setText(userInformation.getAdress());
                    txtpassword.setText(Common.currentUser.getPassword());
                } else {
                    txtname.setText(Common.currentUser.getName());
                    txtkind.setText("-----------");
                    txtidentity.setText("-----------");
                    txtnationality.setText("-----------");
                    txtbirthdate.setText("-----------");
                    txtbloodtype.setText("-----------");
                    txtphone.setText(Common.currentUser.getPhone());
                    txtaccount.setText(Common.currentUser.getEmail());
                    txtadress.setText("-----------");
                    txtpassword.setText(Common.currentUser.getPassword());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    public void gochange(View view) {
        startActivity(new Intent(ProfileActivity.this, ChangeProfileActivity.class));
        CustomIntent.customType(this, "left-to-right");

    }

    private void initViews() {

        txtpassword = (TextView) findViewById(R.id.txt_password);
        txtadress = (TextView) findViewById(R.id.txt_adress);
        txtaccount = (TextView) findViewById(R.id.txt_account);
        txtphone = (TextView) findViewById(R.id.txt_phone);
        txtbloodtype = (TextView) findViewById(R.id.txt_bloodtype);
        txtbirthdate = (TextView) findViewById(R.id.txt_birthdate);
        txtnationality = (TextView) findViewById(R.id.txt_nationality);
        txtidentity = (TextView) findViewById(R.id.txt_identity);
        txtkind = (TextView) findViewById(R.id.txt_kind);
        txtname = (TextView) findViewById(R.id.txt_name);
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
        startActivity(new Intent(ProfileActivity.this, DonorsActivity.class));
    }

    public void goDonationfile(View view) {
        startActivity(new Intent(ProfileActivity.this,DonationFile.class));
        CustomIntent.customType(this, "left-to-right");

    }
}
