package com.example.bloodbank.Views.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank.Common.Common;
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

import maes.tech.intentanim.CustomIntent;

public class AddHospitalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Spinner spi_area, spi_hospital, spi_shift;

    Button done;

    DatabaseReference hospitals_databaseReference, databaseReference;


    ArrayList<Area> areas_opps = new ArrayList<Area>();
    ArrayList<Hospital> hospitals_opps = new ArrayList<Hospital>();
    ArrayList<Shift> shifts_opps = new ArrayList<Shift>();
    ArrayList<Dates> dates_opps = new ArrayList<Dates>();

    ArrayList<String> areas = new ArrayList<>();
    ArrayList<String> hospitals = new ArrayList<>();
    ArrayList<String> shifts = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();


    Area area;
    Hospital hospital;
    Shift shift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);

        hospitals_databaseReference = FirebaseDatabase.getInstance().getReference(Common.SelectHospitals_category);
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.Hospitals_category);

        initViews();

        getAreas();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void upload() {


//        databaseReference.child(Common.Area_category).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                boolean exists = false;
//
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    Map<String, Object> model = (Map<String, Object>) child.getValue();
//
//                    if (model.get("name").equals(spi_area.getSelectedItem().toString())) {
//                        exists = true;
//                        break;
//                    }
//                }
//
//                if (exists == true) {
//                    Toast.makeText(AddHospitalActivity.this, "area alredy registered ", Toast.LENGTH_SHORT).show();
//
//                } else if (exists == false) {
//
//                    databaseReference.child(Common.Hospital_category).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            boolean hosp_exists = false;
//
//                            for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                Map<String, Object> model = (Map<String, Object>) child.getValue();
//
//                                if (model.get("name").equals(spi_hospital.getSelectedItem().toString())) {
//                                    hosp_exists = true;
//                                    break;
//                                }
//                            }
//                            if (hosp_exists == true) {
//                                Toast.makeText(AddHospitalActivity.this, "hospital alredy registered ", Toast.LENGTH_SHORT).show();
//
//
//                            } else if (hosp_exists == false) {

        area = new Area(
                spi_area.getSelectedItem().toString()
        );

        DatabaseReference reference = databaseReference.child(Common.Area_category).push();
        String x = reference.getKey();
        reference.setValue(area);


        hospital = new Hospital(
                spi_hospital.getSelectedItem().toString(),
                x
        );
        // declare new DatabaseReference to avoid not to remove all data and set new
        DatabaseReference reference2 = databaseReference.child(Common.Hospital_category).push();
        String x2 = reference2.getKey();
        reference2.setValue(hospital);


        shift = new Shift(
                spi_shift.getSelectedItem().toString(),
                x2
        );
        // declare new DatabaseReference to avoid not to remove all data and set new
        DatabaseReference reference3 = databaseReference.child(Common.Time_category).push();
        String x3 = reference.getKey();
        reference3.setValue(shift);


        startActivity(new Intent(AddHospitalActivity.this, AdminActivity.class));
        Toast.makeText(AddHospitalActivity.this, "تم بنجاح", Toast.LENGTH_SHORT).show();


        //                          }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }


    ////////////////////////////////////
    private void getAreas() {

        final ProgressDialog progressDialog = new ProgressDialog(AddHospitalActivity.this);
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


                final ArrayAdapter<String> arrad = new ArrayAdapter<String>(AddHospitalActivity.this, android.R.layout.simple_list_item_1, areas);
                arrad.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                spi_area.setAdapter(arrad);
                spi_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        String item = parent.getItemAtPosition(i).toString();
                        String id = "";
                        for (Area area : areas_opps) {
                            if (area.getName().equals(item))
                                id = area.getId();
                        }
                        getHospitals(id);

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

        final ProgressDialog progressDialog = new ProgressDialog(AddHospitalActivity.this);
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


                ArrayAdapter<String> arrad2 = new ArrayAdapter<String>(AddHospitalActivity.this, android.R.layout.simple_list_item_1, hospitals);
                arrad2.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                spi_hospital.setAdapter(arrad2);
                spi_hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        final ProgressDialog progressDialog = new ProgressDialog(AddHospitalActivity.this);
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


                ArrayAdapter<String> arrad3 = new ArrayAdapter<String>(AddHospitalActivity.this, android.R.layout.simple_list_item_1, shifts);
                arrad3.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                spi_shift.setAdapter(arrad3);
                spi_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
//    private void load(String id) {
//        flag = false;
//
//        ProgressDialog mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setMessage("Work ...");
//        mProgressDialog.show();
//        appointement_databaseReference.child(id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                appointement = dataSnapshot.getValue(Appointement.class);
//
//                //  to get position of item in the spinner
//
//
//                int area_pos = 1;
//                for (String value : areas) {
//                    area_pos = areas.indexOf(appointement.getArea());
//                }
//
//
//                int hospital_pos = 1;
//                for (String value : hospitals) {
//                    hospital_pos = hospitals.indexOf(appointement.getHospital());
//                }
//
//
//                int shift_pos = 1;
//                for (String value : shifts) {
//                    shift_pos = shifts.indexOf(appointement.getShift());
//                }
//
//
//                edtdate.setText(appointement.getDate());
//                spiarea.setSelection(area_pos);
//                spihospital.setSelection(hospital_pos);
//                spishift.setSelection(shift_pos);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        mProgressDialog.dismiss();
//
//
//    }
/////////////////////////////////////////////////////////////////////////////

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        done = (Button) findViewById(R.id.done);
        spi_shift = (Spinner) findViewById(R.id.edt_shift);
        spi_hospital = (Spinner) findViewById(R.id.edt_hospital);
        spi_area = (Spinner) findViewById(R.id.edt_area);

    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddHospitalActivity.this, AdminActivity.class));
    }
}
