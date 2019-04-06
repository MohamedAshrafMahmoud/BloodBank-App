package com.example.bloodbank.Views.Donors;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Area;
import com.example.bloodbank.Model.Nationality;
import com.example.bloodbank.Model.UserInformation;
import com.example.bloodbank.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class MyInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spikind, spinationality, spibloodtype;
    EditText edtidentity, edtdate, edtpassword, edtadress, edtconfirmpassword;
    DatePickerDialog datePickerDialog;
    Button done;

    ArrayList<String> kind;
    ArrayList<String> nationality;
    ArrayList<String> bloodtype;
    ArrayList<Nationality> nationalitys_opps = new ArrayList<Nationality>();
    ArrayList<String> nationalitys = new ArrayList<>();


    DatabaseReference databaseReference,nationality_refrence;

    UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.UserInformation_category);
        nationality_refrence = FirebaseDatabase.getInstance().getReference(Common.Nationality_category);

        initViews();
        initArrayLists();
        getNationality();




        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //get date
        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MyInfoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtdate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] current_date = Common.getDate().split("/");
                String[] donors_date = edtdate.getText().toString().split("/");

                if (spikind.getSelectedItem().toString().length() == 0) {
                    Toast.makeText(MyInfoActivity.this, "النوع مطلوب", Toast.LENGTH_SHORT).show();
                    spikind.requestFocus();
                } else if (edtidentity.getText().toString().length() == 0) {
                    edtidentity.setError("الهويه مطلوبه");
                    edtidentity.requestFocus();

                } else if (spinationality.getSelectedItem().toString().length() == 0) {
                    Toast.makeText(MyInfoActivity.this, "الجنسيه مطلوبه", Toast.LENGTH_SHORT).show();
                    spinationality.requestFocus();

                } else if (edtdate.getText().toString().length() == 0) {
                    edtdate.setError("التاريخ مطلوب");
                    edtdate.requestFocus();

                } else if (Integer.parseInt(current_date[0]) < Integer.parseInt(donors_date[0]) && Integer.parseInt(current_date[1]) <= Integer.parseInt(donors_date[1]) && Integer.parseInt(current_date[2]) <= Integer.parseInt(donors_date[2])) {
                    Toast.makeText(MyInfoActivity.this, "لا يمكنك اختيار تاريخ في المستقبل ", Toast.LENGTH_SHORT).show();

                } else if (spibloodtype.getSelectedItem().toString().length() == 0) {
                    Toast.makeText(MyInfoActivity.this, "الفصيله مطلوبه", Toast.LENGTH_SHORT).show();
                    spibloodtype.requestFocus();

                } else if (edtadress.getText().toString().length() == 0) {
                    edtadress.setError("العنوان مطلوب ");
                    edtadress.requestFocus();

                } else if (edtpassword.getText().toString().length() == 0) {
                    edtpassword.setError("كلمه السر مطلوبه");
                    edtpassword.requestFocus();

                } else if (edtconfirmpassword.getText().toString().length() == 0) {
                    edtconfirmpassword.setError("كلمه السر مطلوبه");
                    edtconfirmpassword.requestFocus();

                } else if (Common.isConnectToInternet(getBaseContext())) {
                    Upload();
                } else {
                    Toast.makeText(MyInfoActivity.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getNationality() {

        final ProgressDialog progressDialog = new ProgressDialog(MyInfoActivity.this);
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


                final ArrayAdapter<String> arrad = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_list_item_1, nationalitys);
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



    private void Upload() {

        if (!Common.currentUser.getPassword().equals(edtpassword.getText().toString())) {
            edtpassword.setError("كلمه السر غير صحيحه");
            edtpassword.requestFocus();
        } else if (!edtpassword.getText().toString().equals(edtconfirmpassword.getText().toString())) {
            edtconfirmpassword.setError("كلمه السر غير صحيحه");
            edtconfirmpassword.requestFocus();
        } else {
            userInformation = new UserInformation(
                    Common.currentUser.getName(),
                    spikind.getSelectedItem().toString(),
                    edtidentity.getText().toString(),
                    spinationality.getSelectedItem().toString(),
                    edtdate.getText().toString(),
                    spibloodtype.getSelectedItem().toString(),
                    Common.currentUser.getPhone(),
                    Common.currentUser.getEmail(),
                    edtadress.getText().toString(),
                    edtpassword.getText().toString()
            );

            databaseReference.child(Common.currentUser.getName()).setValue(userInformation);
            startActivity(new Intent(MyInfoActivity.this, DonorsActivity.class));
            Toast.makeText(this, "تم بنجاح", Toast.LENGTH_SHORT).show();
        }


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

    private void initViews() {
        edtconfirmpassword = (EditText) findViewById(R.id.edt_confirmpassword);
        edtpassword = (EditText) findViewById(R.id.edt_password);
        edtadress = (EditText) findViewById(R.id.edt_adress);
        spibloodtype = (Spinner) findViewById(R.id.spi_bloodtype);
        edtdate = (EditText) findViewById(R.id.edt_date);
        spinationality = (Spinner) findViewById(R.id.spi_nationality);
        edtidentity = (EditText) findViewById(R.id.edt_identity);
        spikind = (Spinner) findViewById(R.id.spi_kind);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        done = (Button) findViewById(R.id.done);

    }


    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyInfoActivity.this, DonorsActivity.class));
    }
}
