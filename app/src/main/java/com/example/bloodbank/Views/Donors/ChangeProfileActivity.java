package com.example.bloodbank.Views.Donors;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class ChangeProfileActivity extends AppCompatActivity {


    ArrayList<String> kind;
    ArrayList<String> bloodtype;

    int kind_pos, nationality_pos, bloodtype_pos = 1;

    DatabaseReference databaseReference,nationality_refrence;

    UserInformation userInformation;

    DatePickerDialog datePickerDialog;
    private Toolbar toolbar;
    private EditText txtname, txtidentity, txtphone, txtaccount, txtadress, txtpassword, txtdate;
    private Spinner spikind, spinationality, spibloodtype;
    private Button save;

    //for nationalites from firebase
    ArrayList<Nationality> nationalitys_opps = new ArrayList<Nationality>();
    ArrayList<String> nationalitys = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        initViews();
        initArrayLists();

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.UserInformation_category);
        nationality_refrence = FirebaseDatabase.getInstance().getReference(Common.Nationality_category);

        getNationality();


        //get date
        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ChangeProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                txtdate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] current_date = Common.getDate().split("/");
                String[] donors_date = txtdate.getText().toString().split("/");

                if (spikind.getSelectedItem().toString().length() == 0) {
                    Toast.makeText(ChangeProfileActivity.this, "النوع مطلوب", Toast.LENGTH_SHORT).show();
                    spikind.requestFocus();
                } else if (txtidentity.getText().toString().length() == 0) {
                    txtidentity.setError("الهويه مطلوبه");
                    txtidentity.requestFocus();

                } else if (txtname.getText().toString().length() == 0) {
                    txtname.setError("الاسم مطلوب");
                    txtname.requestFocus();

                } else if (txtphone.getText().toString().length() == 0) {
                    txtphone.setError("رقم الجوال مطلوب");
                    txtphone.requestFocus();

                } else if (txtaccount.getText().toString().length() == 0) {
                    txtaccount.setError("البريد الاكتروني مطلوب");
                    txtaccount.requestFocus();

                } else if (spinationality.getSelectedItem().toString().length() == 0) {
                    Toast.makeText(ChangeProfileActivity.this, "الجنسيه مطلوبه", Toast.LENGTH_SHORT).show();
                    spinationality.requestFocus();

                } else if (txtdate.getText().toString().length() == 0) {
                    txtdate.setError("التاريخ مطلوب");
                    txtdate.requestFocus();

                } else if (Integer.parseInt(current_date[0]) < Integer.parseInt(donors_date[0]) && Integer.parseInt(current_date[1]) <= Integer.parseInt(donors_date[1]) && Integer.parseInt(current_date[2]) <= Integer.parseInt(donors_date[2])) {
                    Toast.makeText(ChangeProfileActivity.this, "لا يمكنك اختيار تاريخ في المستقبل ", Toast.LENGTH_SHORT).show();

                } else if (spibloodtype.getSelectedItem().toString().length() == 0) {
                    Toast.makeText(ChangeProfileActivity.this, "الفصيله مطلوبه", Toast.LENGTH_SHORT).show();
                    spibloodtype.requestFocus();

                } else if (txtadress.getText().toString().length() == 0) {
                    txtadress.setError("العنوان مطلوب ");
                    txtadress.requestFocus();

                } else if (txtpassword.getText().toString().length() == 0) {
                    txtpassword.setError("كلمه السر مطلوبه");
                    txtpassword.requestFocus();

                } else if (Common.isConnectToInternet(getBaseContext())) {
                    Upload();
                } else {
                    Toast.makeText(ChangeProfileActivity.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void load() {

        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Work ...");
        mProgressDialog.show();
        databaseReference.child(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("account") && dataSnapshot.hasChild("adress") && dataSnapshot.hasChild("birth_date") && dataSnapshot.hasChild("blood_type") && dataSnapshot.hasChild("identification") && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("nationality") && dataSnapshot.hasChild("password") && dataSnapshot.hasChild("phonenumber") && dataSnapshot.hasChild("type")) {
                    userInformation = dataSnapshot.getValue(UserInformation.class);

                    //  to get position of item in the spinner
                    for (String x : kind) {
                        if (x.contains(userInformation.getType())) {
                            kind_pos = kind.indexOf(x);
                        }
                    }
                    for (String x : nationalitys) {
                        if (x.contains(userInformation.getNationality())) {
                            nationality_pos = nationalitys.indexOf(x);
                        }
                    }
                    for (String x : bloodtype) {
                        if (x.contains(userInformation.getBlood_type())) {
                            bloodtype_pos = bloodtype.indexOf(x);
                        }
                    }

                    txtname.setText(Common.currentUser.getName());
                    spikind.setSelection(kind_pos);
                    spinationality.setSelection(nationality_pos);
                    txtidentity.setText(userInformation.getIdentification());
                    txtdate.setText(userInformation.getBirth_date());
                    spibloodtype.setSelection(bloodtype_pos);
                    txtphone.setText(Common.currentUser.getPhone());
                    txtaccount.setText(Common.currentUser.getEmail());
                    txtadress.setText(userInformation.getAdress());
                    txtpassword.setText(Common.currentUser.getPassword());
                } else {

                    initArrayLists();

                    txtname.setText(Common.currentUser.getName());
                    txtphone.setText(Common.currentUser.getPhone());
                    txtaccount.setText(Common.currentUser.getEmail());
                    txtpassword.setText(Common.currentUser.getPassword());

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProgressDialog.dismiss();


    }

    private void Upload() {

        userInformation = new UserInformation(
                txtname.getText().toString(),
                spikind.getSelectedItem().toString(),
                txtidentity.getText().toString(),
                spinationality.getSelectedItem().toString(),
                txtdate.getText().toString(),
                spibloodtype.getSelectedItem().toString(),
                txtphone.getText().toString(),
                txtaccount.getText().toString(),
                txtadress.getText().toString(),
                txtpassword.getText().toString()
        );

        databaseReference.child(Common.currentUser.getName()).setValue(userInformation);
        startActivity(new Intent(ChangeProfileActivity.this, ProfileActivity.class));
        Toast.makeText(this, "تم بنجاح", Toast.LENGTH_SHORT).show();


    }

    private void initArrayLists() {

        kind = new ArrayList<String>();
        kind.add("Male");
        kind.add("Female");

//        nationality = new ArrayList<String>();
//        nationality.add("Saudi");
//        nationality.add("Egyption");
//        nationality.add("Sudanese");
//        nationality.add("Pakistani");
//        nationality.add("Indian");
//        nationality.add("Bengali");

        bloodtype = new ArrayList<String>();
        bloodtype.add("A+");
        bloodtype.add("A-");
        bloodtype.add("B+");
        bloodtype.add("B-");
        bloodtype.add("O+");
        bloodtype.add("O-");
        bloodtype.add("AB+");
        bloodtype.add("AB-");


        ArrayAdapter<String> arrad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kind);
        arrad.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        spikind.setAdapter(arrad);
        spikind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String item = parent.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        ArrayAdapter<String> arrad2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nationality);
//        arrad2.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
//        spinationality.setAdapter(arrad2);
//        spinationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
//                String item = parent.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        ArrayAdapter<String> arrad3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bloodtype);
        arrad3.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        spibloodtype.setAdapter(arrad3);
        spibloodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String item = parent.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getNationality() {

        final ProgressDialog progressDialog = new ProgressDialog(ChangeProfileActivity.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();

        nationality_refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    progressDialog.dismiss();
                    Nationality nationality = dataSnapshot.getValue(Nationality.class);
                    nationalitys_opps.add(nationality);

                }

                for (Nationality nationality : nationalitys_opps) {
                    nationalitys.add(nationality.getName());
                }

                load();


                final ArrayAdapter<String> arrad = new ArrayAdapter<String>(ChangeProfileActivity.this, android.R.layout.simple_list_item_1, nationalitys);
                arrad.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                spinationality.setAdapter(arrad);
                spinationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        String item = parent.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void initViews() {

        save = (Button) findViewById(R.id.save);
        txtpassword = (EditText) findViewById(R.id.txt_password);
        txtadress = (EditText) findViewById(R.id.txt_adress);
        txtaccount = (EditText) findViewById(R.id.txt_account);
        txtphone = (EditText) findViewById(R.id.txt_phone);
        spibloodtype = (Spinner) findViewById(R.id.txt_bloodtype);
        this.txtdate = (EditText) findViewById(R.id.txt_date);
        this.spinationality = (Spinner) findViewById(R.id.txt_nationality);
        this.txtidentity = (EditText) findViewById(R.id.txt_identity);
        this.spikind = (Spinner) findViewById(R.id.txt_kind);
        this.txtname = (EditText) findViewById(R.id.txt_name);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        startActivity(new Intent(ChangeProfileActivity.this, ProfileActivity.class));
    }

}
