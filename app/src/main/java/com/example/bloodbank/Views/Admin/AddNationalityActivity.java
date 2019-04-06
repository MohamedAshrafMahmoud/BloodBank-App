package com.example.bloodbank.Views.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Nationality;
import com.example.bloodbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class AddNationalityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    EditText edt_nationality;

    Button done;

    DatabaseReference databaseReference;
    Nationality nationality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nationality);
        initViews();

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.Nationality_category);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void upload() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean exists = false;

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Map<String, Object> model = (Map<String, Object>) child.getValue();

                    if (model.get("name").equals(edt_nationality.getText().toString())) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    Toast.makeText(AddNationalityActivity.this, "nationslity alredy registered ", Toast.LENGTH_SHORT).show();

                } else {
                    nationality = new Nationality(
                            edt_nationality.getText().toString());
//                    // declare new DatabaseReference to avoid not to remove all data and set new
//                    DatabaseReference reference = databaseReference.push();
//                    String x = reference.getKey();
                    databaseReference.push().setValue(nationality);

                    startActivity(new Intent(AddNationalityActivity.this, AdminActivity.class));
                    Toast.makeText(AddNationalityActivity.this, "تم بنجاح", Toast.LENGTH_SHORT).show();


                }

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

        done = (Button) findViewById(R.id.done);
        edt_nationality = (EditText) findViewById(R.id.edt_nationality);


    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddNationalityActivity.this, AdminActivity.class));
    }

}
