package com.example.bloodbank.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.R;
import com.example.bloodbank.Views.Doctors.DonitionsRequestActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DoctorApprovalAdapter extends RecyclerView.Adapter<DoctorApprovalAdapter.DoctorApprovalViewHolder> {

    List<Appointement> listdata;
    Context context;
    ArrayList<String> kind;
    int kind_pos = 1;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.Appointement_category);


    public DoctorApprovalAdapter(List<Appointement> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public DoctorApprovalAdapter.DoctorApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new DoctorApprovalAdapter.DoctorApprovalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_doctor_approval, parent, false));
    }

    @Override
    public void onBindViewHolder(final DoctorApprovalAdapter.DoctorApprovalViewHolder viewHolder, final int position) {

        viewHolder.txt_name.setText(listdata.get(position).getName());
        viewHolder.txt_area.setText(listdata.get(position).getArea());
        viewHolder.txt_hospital.setText(listdata.get(position).getHospital());
        viewHolder.txt_shift.setText(listdata.get(position).getShift());
        viewHolder.txt_date.setText(listdata.get(position).getDate());
        viewHolder.spiconditio.setSelection(kind_pos);

        // hide when done
        if (listdata.get(position).isDone() == true) {
            viewHolder.card.setVisibility(View.GONE);
            // not to keep blank space after hide
            viewHolder.card.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

        }


//        //to check from arrow done
//        if (listdata.get(position).isDone() == true) {
//            viewHolder.done.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.done.setVisibility(View.GONE);
//        }


        kind = new ArrayList<String>();
        kind.add("نجح الطلب");
        kind.add("رفض الطلب");
        kind.add("تاجيل الطلب");
        ArrayAdapter<String> arrad = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, kind);
        arrad.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        viewHolder.spiconditio.setAdapter(arrad);
        viewHolder.spiconditio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String item = parent.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHolder.edt_reason.getText().toString().length() == 0) {
                    viewHolder.edt_reason.setError("السبب مطلوب");
                    viewHolder.edt_reason.requestFocus();

                } else {
                    Appointement appoint = new Appointement(
                            listdata.get(position).getName(),
                            listdata.get(position).getDate(),
                            listdata.get(position).getArea(),
                            listdata.get(position).getHospital(),
                            listdata.get(position).getShift(),
                            viewHolder.spiconditio.getSelectedItem().toString(),
                            viewHolder.edt_reason.getText().toString(),
                            true
                    );

                    databaseReference.child(listdata.get(position).getId()).setValue(appoint);
                    Toast.makeText(context, "  تم  ", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DoctorApprovalViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_area, txt_hospital, txt_shift, txt_date, txt_name;
        public Spinner spiconditio;
        public EditText edt_reason;
        public ImageView done;
        public Button save;
        public CardView card;

        public DoctorApprovalViewHolder(View itemView) {
            super(itemView);

            txt_name = (TextView) itemView.findViewById(R.id.txtname);
            txt_area = (TextView) itemView.findViewById(R.id.txtarea);
            txt_hospital = (TextView) itemView.findViewById(R.id.txthospital);
            txt_shift = (TextView) itemView.findViewById(R.id.txtshift);
            txt_date = (TextView) itemView.findViewById(R.id.txtdate);
            spiconditio = (Spinner) itemView.findViewById(R.id.spicondition);
            edt_reason = (EditText) itemView.findViewById(R.id.edt_reason);
            done = (ImageView) itemView.findViewById(R.id.done);
            save = (Button) itemView.findViewById(R.id.save);
            card = (CardView) itemView.findViewById(R.id.card);


        }
    }

}
