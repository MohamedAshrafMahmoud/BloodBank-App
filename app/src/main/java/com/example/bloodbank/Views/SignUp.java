package com.example.bloodbank.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.User;
import com.example.bloodbank.R;

import com.example.bloodbank.Views.Donors.DonorsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import maes.tech.intentanim.CustomIntent;

public class SignUp extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;


    TextView name, phone, email, password, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = (TextView) findViewById(R.id.edtname);
        phone = (TextView) findViewById(R.id.edtphone);
        email = (TextView) findViewById(R.id.edtemail);
        password = (TextView) findViewById(R.id.edtpass);
        signUp = (TextView) findViewById(R.id.signUp);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Common.User_category);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectToInternet(getBaseContext())) {


                    if (name.getText().toString().length() == 0) {
                        name.setError("name not entered");
                        name.requestFocus();
                    } else if (phone.getText().toString().length() == 0) {
                        phone.setError("phone not entered");
                        phone.requestFocus();

                    } else if (email.getText().toString().length() == 0) {
                        email.setError("email not entered");
                        email.requestFocus();
                    }
                    // check email validation
                    else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        email.setError("wrong email format");
                        email.requestFocus();
                    } else if (TextUtils.isEmpty(password.getText().toString())) {
                        password.setError("Password not entered");
                        password.requestFocus();
                    } else {


                        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                        progressDialog.setMessage("please wait ....");
                        progressDialog.show();


                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(name.getText().toString()).exists()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUp.this, "name alredy registered ", Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    User user = new User(phone.getText().toString(), email.getText().toString(), password.getText().toString());
                                    databaseReference.child(name.getText().toString()).setValue(user);
                                    user.setName(name.getText().toString());
                                    Common.currentUser = user;
                                    Toast.makeText(SignUp.this, "SignUp sucessfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUp.this, DonorsActivity.class));
                                    finish();

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }

            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
}


