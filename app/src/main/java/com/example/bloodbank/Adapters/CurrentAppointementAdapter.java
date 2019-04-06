package com.example.bloodbank.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Common.Common;
import com.example.bloodbank.Interface.ItemClickListner;
import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.R;
import com.example.bloodbank.Views.Donors.BookNewAppointmentActivity;
import com.example.bloodbank.Views.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CurrentAppointementAdapter extends RecyclerView.Adapter<CurrentAppointementAdapter.CurrentAppointementsViewHolder> {

    List<Appointement> listdata;
    Context context;


    public CurrentAppointementAdapter(List<Appointement> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public CurrentAppointementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CurrentAppointementsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_current, parent, false));
    }

    @Override
    public void onBindViewHolder(CurrentAppointementsViewHolder myHolder, final int position) {

        //  final DatabaseReference itemRef = getRef(position);

        //  final String myKey = itemRef.getKey();


        final String id = listdata.get(position).getId();

        myHolder.txt_area.setText(listdata.get(position).getArea());
        myHolder.txt_hospital.setText(listdata.get(position).getHospital());
        myHolder.txt_shift.setText(listdata.get(position).getShift());
        myHolder.txt_date.setText(listdata.get(position).getDate());
        // myHolder.txt_reason.setText(listdata.get(position).getReason());
        // myHolder.txt_condition.setText(listdata.get(position).getCondition());

        myHolder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookNewAppointmentActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertdDialog = new AlertDialog.Builder(context);

                alertdDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference().child(Common.Appointement_category).
                                child(id).removeValue();
                    }
                });

                alertdDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertdDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class CurrentAppointementsViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_area, txt_hospital, txt_shift, txt_date, txt_condition, txt_reason;
        public Button change, delete;


        public CurrentAppointementsViewHolder(View itemView) {
            super(itemView);

            txt_area = (TextView) itemView.findViewById(R.id.txtarea);
            txt_hospital = (TextView) itemView.findViewById(R.id.txthospital);
            txt_shift = (TextView) itemView.findViewById(R.id.txtshift);
            txt_date = (TextView) itemView.findViewById(R.id.txtdate);
            txt_condition = (TextView) itemView.findViewById(R.id.txtcondition);
            txt_reason = (TextView) itemView.findViewById(R.id.txtreason);
            change = (Button) itemView.findViewById(R.id.change);
            delete = (Button) itemView.findViewById(R.id.delete);


        }
    }


}
