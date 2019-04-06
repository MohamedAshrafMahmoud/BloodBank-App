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
import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.Model.Area;
import com.example.bloodbank.Model.Dates;
import com.example.bloodbank.Model.Hospital;
import com.example.bloodbank.Model.Shift;
import com.example.bloodbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class BookNewAppointmentActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private EditText edtdate;
    private Spinner spiarea, spihospital, spishift;
    private Button done;
    DatePickerDialog datePickerDialog;


    DatabaseReference hospitals_databaseReference, appointement_databaseReference;

    Appointement appointement;


    ArrayList<Area> areas_opps = new ArrayList<Area>();
    ArrayList<Hospital> hospitals_opps = new ArrayList<Hospital>();
    ArrayList<Shift> shifts_opps = new ArrayList<Shift>();
    ArrayList<Dates> dates_opps = new ArrayList<Dates>();

    ArrayList<String> areas = new ArrayList<>();
    ArrayList<String> hospitals = new ArrayList<>();
    ArrayList<String> shifts = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();

    int area_pos, hospital_pos, shift_pos = 1;


    //for intent of change from current appointement
    String Id = "";
    String id;

    // to check if there is change in data
    boolean flag = true;

    // to check if date is good
    boolean date_flag = false;

    boolean flag_empty = false;

    // intent flag
    boolean intentflag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_appointment);

        Id = getIntent().getStringExtra("id");
        id = Id;

        hospitals_databaseReference = FirebaseDatabase.getInstance().getReference(Common.Hospitals_category);
        appointement_databaseReference = FirebaseDatabase.getInstance().getReference(Common.Appointement_category);

        initViews();

        getAreas();


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
                datePickerDialog = new DatePickerDialog(BookNewAppointmentActivity.this,
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
//                check_date(edtdate.getText().toString());
                uploadData();
            }
        });


    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void uploadData() {

        String[] current_date = Common.getDate().split("/");
        String[] donors_date = edtdate.getText().toString().split("/");

        if (edtdate.getText().toString().length() == 0) {
            edtdate.setError("التاريخ مطلوب");
            edtdate.requestFocus();


        } else if (Integer.parseInt(current_date[0]) > Integer.parseInt(donors_date[0]) && Integer.parseInt(current_date[1]) >= Integer.parseInt(donors_date[1]) && Integer.parseInt(current_date[2]) >= Integer.parseInt(donors_date[2])) {
            Toast.makeText(this, "لا يمكنك حجز يوم انتهي", Toast.LENGTH_SHORT).show();

        } else if (spiarea.getSelectedItem().toString().length() == 0) {
            Toast.makeText(BookNewAppointmentActivity.this, "المنطقه مطلوبه", Toast.LENGTH_SHORT).show();
            spiarea.requestFocus();
        } else if (spihospital.getSelectedItem().toString().length() == 0) {
            Toast.makeText(BookNewAppointmentActivity.this, "المسنشفي مطلوبه", Toast.LENGTH_SHORT).show();
            spihospital.requestFocus();
        } else if (spishift.getSelectedItem().toString().length() == 0) {
            Toast.makeText(BookNewAppointmentActivity.this, "الفتره مطلوبه", Toast.LENGTH_SHORT).show();
            spishift.requestFocus();

        } else if (check_empty_date() == true) {


            appointement = new Appointement(
                    Common.currentUser.getName(),
                    edtdate.getText().toString(),
                    spiarea.getSelectedItem().toString(),
                    spihospital.getSelectedItem().toString(),
                    spishift.getSelectedItem().toString(),
                    "بانتظار الطبيب...",
                    "بانتظار الطبيب...",
                    false
            );

            if (flag == true) {
                appointement_databaseReference.push().setValue(appointement);
                startActivity(new Intent(BookNewAppointmentActivity.this, DonorsActivity.class));
                Toast.makeText(this, "تم بنجاح", Toast.LENGTH_SHORT).show();

            } else {
                // if flag false so changed in data and upload new data
                appointement_databaseReference.child(Id).setValue(appointement);
                startActivity(new Intent(BookNewAppointmentActivity.this, DonorsActivity.class));
                Toast.makeText(this, "تم بنجاح", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (check_date(edtdate.getText().toString()) == false) {

                Toast.makeText(this, "لا يمكنك التبرع الا كل 3 اشهر .. بالرجاء اختيار معاد اخر", Toast.LENGTH_LONG).show();

            } else {
                appointement = new Appointement(
                        Common.currentUser.getName(),
                        edtdate.getText().toString(),
                        spiarea.getSelectedItem().toString(),
                        spihospital.getSelectedItem().toString(),
                        spishift.getSelectedItem().toString(),
                        "بانتظار الطبيب...",
                        "بانتظار الطبيب...",
                        false
                );

                if (flag == true) {
                    appointement_databaseReference.push().setValue(appointement);
                    startActivity(new Intent(BookNewAppointmentActivity.this, DonorsActivity.class));
                    Toast.makeText(this, "تم بنجاح", Toast.LENGTH_SHORT).show();

                } else {
                    // if flag false so changed in data and upload new data
                    appointement_databaseReference.child(id).setValue(appointement);
                    startActivity(new Intent(BookNewAppointmentActivity.this, DonorsActivity.class));
                    Toast.makeText(this, "تم بنجاح", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    ////////////////////////////////////
    private void getAreas() {

        final ProgressDialog progressDialog = new ProgressDialog(BookNewAppointmentActivity.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();

        hospitals_databaseReference.child(Common.Area_category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    progressDialog.dismiss();
                    Area area = dataSnapshot.getValue(Area.class);
                    area.setId(dataSnapshot.getKey());
                    areas_opps.add(area);

                }

                for (Area area : areas_opps) {
                    areas.add(area.getName());
                }


                final ArrayAdapter<String> arrad = new ArrayAdapter<String>(BookNewAppointmentActivity.this, android.R.layout.simple_list_item_1, areas);
                arrad.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                spiarea.setAdapter(arrad);
                spiarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {


                        String item = parent.getItemAtPosition(i).toString();
                        if (intentflag == true) {
                            String id = "";
                            for (Area area : areas_opps) {
                                if (area.getName().equals(item))
                                    id = area.getId();
                            }
                            getHospitals(id);
                        } else {
                            // if intentflag = false  make Id null not to load in loop last data
                            Id = null;

                            String id = "";
                            for (Area area : areas_opps) {
                                if (area.getName().equals(item))
                                    id = area.getId();
                            }
                            getHospitals(id);
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });

    }

    ///////////////////////////////////////////////////////////////////
    private void getHospitals(String id) {

        hospitals.clear();
        hospitals_opps.clear();

        final ProgressDialog progressDialog = new ProgressDialog(BookNewAppointmentActivity.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();


        hospitals_databaseReference.child(Common.Hospital_category).orderByChild("areaId").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    progressDialog.dismiss();
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    hospital.setId(dataSnapshot.getKey());
                    hospitals_opps.add(hospital);

                }

                for (Hospital hospital : hospitals_opps) {
                    hospitals.add(hospital.getName());
                }


                ArrayAdapter<String> arrad2 = new ArrayAdapter<String>(BookNewAppointmentActivity.this, android.R.layout.simple_list_item_1, hospitals);
                arrad2.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                spihospital.setAdapter(arrad2);
                spihospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        String item = parent.getItemAtPosition(i).toString();

                        String id = "";
                        for (Hospital hospital : hospitals_opps) {
                            if (hospital.getName().equals(item))
                                id = hospital.getId();
                        }
                        getShifts(id);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////
    private void getShifts(String id) {

        shifts.clear();
        shifts_opps.clear();

        final ProgressDialog progressDialog = new ProgressDialog(BookNewAppointmentActivity.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();


        hospitals_databaseReference.child(Common.Time_category).orderByChild("hospitalId").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    progressDialog.dismiss();
                    Shift shift = dataSnapshot.getValue(Shift.class);

                    shifts_opps.add(shift);

                }

                for (Shift shift : shifts_opps) {
                    shifts.add(shift.getTime());
                }


                //for intent of change from current appointement
                if (Id != null) {
                    load(Id);

                }


                ArrayAdapter<String> arrad3 = new ArrayAdapter<String>(BookNewAppointmentActivity.this, android.R.layout.simple_list_item_1, shifts);
                arrad3.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                spishift.setAdapter(arrad3);
                spishift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                progressDialog.dismiss();

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////
    private void load(String id) {
        flag = false;

        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Work ...");
        mProgressDialog.show();
        appointement_databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                appointement = dataSnapshot.getValue(Appointement.class);

                //  to get position of item in the spinner
                area_pos = areas.indexOf(appointement.getArea());

                hospital_pos = hospitals.indexOf(appointement.getHospital());

                shift_pos = shifts.indexOf(appointement.getShift());

                edtdate.setText(appointement.getDate());
                spiarea.setSelection(area_pos);
                spihospital.setSelection(hospital_pos);
                spishift.setSelection(shift_pos);

                intentflag = false;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProgressDialog.dismiss();


    }
/////////////////////////////////////////////////////////////////////////////

    public boolean check_date(final String edt_date) {

        appointement_databaseReference.orderByChild("name").equalTo(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Dates date = dataSnapshot.getValue(Dates.class);

                    dates_opps.add(date);


                    for (Dates date1 : dates_opps) {

                        int month_after = 0;
                        int year_after = 0;

                        String[] start_date = date1.getDate().split("/");
                        String end_date = "";
                        String[] current_date = edt_date.split("/");

                        String startdate = "";
                        startdate = Integer.parseInt(start_date[0]) + "/" + Integer.parseInt(start_date[1]) + "/" + Integer.parseInt(start_date[2]);

                        //add three monthes
                        month_after = Integer.parseInt(start_date[1]) + 3;
                        year_after = Integer.parseInt(start_date[2]);

                        //check if over year
                        if (month_after > 12) {
                            year_after += 1;
                            month_after = month_after % 12;
                            end_date = Integer.parseInt(start_date[0]) + "/" + month_after + "/" + year_after;

                        } else {
                            end_date = Integer.parseInt(start_date[0]) + "/" + month_after + "/" + year_after;
                        }


                        //check if between

                        if (Common.checkBetween(startdate, edt_date, end_date) == true) {

                            date_flag = true;
//                            Toast.makeText(BookNewAppointmentActivity.this, "Not", Toast.LENGTH_SHORT).show();

                        } else {
                            date_flag = false;
//                            Toast.makeText(BookNewAppointmentActivity.this, "between", Toast.LENGTH_SHORT).show();
                        }


                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return date_flag;

    }

    public boolean check_empty_date() {

        final ProgressDialog Dialog = new ProgressDialog(BookNewAppointmentActivity.this);
        Dialog.setMessage("wait ...");
        Dialog.show();


        appointement_databaseReference.orderByChild("name").equalTo(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                     flag_empty = true;
                } else {
                     flag_empty = false;
                }

                Dialog.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return flag_empty;

    }

/////////////////////////////////////////////////////////////////////////////

    private void initViews() {
        this.done = (Button) findViewById(R.id.done);
        spishift = (Spinner) findViewById(R.id.spi_shift);
        spihospital = (Spinner) findViewById(R.id.spi_hospital);
        spiarea = (Spinner) findViewById(R.id.spi_area);
        edtdate = (EditText) findViewById(R.id.edt_date);
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
        startActivity(new Intent(BookNewAppointmentActivity.this, DonorsActivity.class));
    }
}
