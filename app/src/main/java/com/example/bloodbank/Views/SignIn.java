package com.example.bloodbank.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.User;
import com.example.bloodbank.R;
import com.example.bloodbank.Views.Admin.AdminActivity;
import com.example.bloodbank.Views.Doctors.DoctorsActivity;
import com.example.bloodbank.Views.Donors.DonorsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import maes.tech.intentanim.CustomIntent;

public class SignIn extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    EditText name, password;
    Button btnsignin, btndoctorsignin,btnadminlogin;
    CheckBox rememberme;

    String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Common.User_category);

        name = (EditText) findViewById(R.id.etemail);
        password = (EditText) findViewById(R.id.etpass);
        btnsignin = (Button) findViewById(R.id.login);
        btndoctorsignin = (Button) findViewById(R.id.logindoctor);
        btnadminlogin = (Button) findViewById(R.id.loginadmin);
        rememberme = (CheckBox) findViewById(R.id.checkremember);


        btndoctorsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().length() == 0) {
                    name.setError("name required");
                    name.requestFocus();
                } else if (password.getText().toString().length() == 0) {
                    password.setError("password required");
                    password.requestFocus();

                } else if (Common.isConnectToInternet(getBaseContext())) {

                    flag = "doctor";
                    showDoctorDialog();

                } else {
                    Toast.makeText(SignIn.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnadminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().length() == 0) {
                    name.setError("name required");
                    name.requestFocus();
                } else if (password.getText().toString().length() == 0) {
                    password.setError("password required");
                    password.requestFocus();

                } else if (Common.isConnectToInternet(getBaseContext())) {

                    flag = "admin";
                    showAdminDialog();

                } else {
                    Toast.makeText(SignIn.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().length() == 0) {
                    name.setError("name required");
                    name.requestFocus();
                } else if (password.getText().toString().length() == 0) {
                    password.setError("password required");
                    password.requestFocus();

                } else if (Common.isConnectToInternet(getBaseContext())) {

                    flag = "user";
                    SignUser(name.getText().toString(), password.getText().toString());

                } else {
                    Toast.makeText(SignIn.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    /* //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// */
    private void SignUser(String s, String s1) {

        final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent;
                if (dataSnapshot.child(name.getText().toString()).exists()) {
                    progressDialog.dismiss();

                    User user = dataSnapshot.child(name.getText().toString()).getValue(User.class);
                    user.setName(name.getText().toString());

                    if (user.getPassword().equals(password.getText().toString())) {

                        if (flag == "doctor") {
                            intent = new Intent(SignIn.this, DoctorsActivity.class);
                        } else if (flag == "admin"){
                            intent = new Intent(SignIn.this, AdminActivity.class);
                        }else {
                            intent = new Intent(SignIn.this, DonorsActivity.class);
                        }

                        Common.currentUser = user;

                        //save data when checkbox rememberme
                        if (rememberme.isChecked()) {
                            SharedPreferences.Editor edit = getSharedPreferences("da", Context.MODE_PRIVATE).edit();
                            edit.putString("name", name.getText().toString());
                            edit.putString("password", password.getText().toString());
                            edit.commit();
                        }

                        startActivity(intent);
                        finish();

                        databaseReference.removeEventListener(this);


                    } else {
                        Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignIn.this, "name not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void showDoctorDialog() {

        final AlertDialog.Builder alertdDialog = new AlertDialog.Builder(SignIn.this);
        alertdDialog.setTitle("ادخل الرقم السري للطبيب");

        LayoutInflater inflater = this.getLayoutInflater();
        View view_pwd = inflater.inflate(R.layout.doctorspassword, null);

        final EditText password = (EditText) view_pwd.findViewById(R.id.oldpassword);

        alertdDialog.setView(view_pwd);

        alertdDialog.setPositiveButton("تسجيل", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //check   password
                if (password.getText().toString().equals(Common.doctor_password)) {
                    SignUser(name.getText().toString(), password.getText().toString());
                } else {
                    dialog.dismiss();
                    Toast.makeText(SignIn.this, "رقم سري خاطيء", Toast.LENGTH_SHORT).show();

                }

            }
        });

        alertdDialog.setNegativeButton("الفاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertdDialog.show();


    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void showAdminDialog() {

        final AlertDialog.Builder alertdDialog = new AlertDialog.Builder(SignIn.this);
        alertdDialog.setTitle("ادخل الرقم السري لadmin");

        LayoutInflater inflater = this.getLayoutInflater();
        View view_pwd = inflater.inflate(R.layout.doctorspassword, null);

        final EditText password = (EditText) view_pwd.findViewById(R.id.oldpassword);

        alertdDialog.setView(view_pwd);

        alertdDialog.setPositiveButton("تسجيل", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //check   password
                if (password.getText().toString().equals(Common.admin_password)) {
                    SignUser(name.getText().toString(), password.getText().toString());
                } else {
                    dialog.dismiss();
                    Toast.makeText(SignIn.this, "رقم سري خاطيء", Toast.LENGTH_SHORT).show();

                }

            }
        });

        alertdDialog.setNegativeButton("الفاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertdDialog.show();


    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void goSignUp(View view) {
        startActivity(new Intent(SignIn.this, SignUp.class));
        CustomIntent.customType(this, "left-to-right");

    }
}
