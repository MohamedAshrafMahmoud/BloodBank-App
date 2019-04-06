package com.example.bloodbank.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodbank.Model.Appointement;
import com.example.bloodbank.R;

import java.util.List;

public class CurrentAppointementsAdapter extends RecyclerView.Adapter<CurrentAppointementsAdapter.CurrentAppointementsViewHolder> {

    List<Appointement> listdata;
    Context context;



    public CurrentAppointementsAdapter(List<Appointement> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public CurrentAppointementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CurrentAppointementsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_current_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(CurrentAppointementsViewHolder myHolder, int position) {

        myHolder.txt_area.setText(listdata.get(position).getArea());
        myHolder.txt_hospital.setText(listdata.get(position).getHospital());
        myHolder.txt_shift.setText(listdata.get(position).getShift());
        myHolder.txt_date.setText(listdata.get(position).getDate());
        myHolder.txt_reason.setText(listdata.get(position).getReason());
        myHolder.txt_condition.setText(listdata.get(position).getCondition());


        //to check from arrow done
        if (listdata.get(position).isDone() == true) {
            myHolder.done.setVisibility(View.VISIBLE);
        } else {
            myHolder.done.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class CurrentAppointementsViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_area, txt_hospital, txt_shift, txt_date, txt_condition, txt_reason;
        public ImageView done;



        public CurrentAppointementsViewHolder(View itemView) {
            super(itemView);

            txt_area = (TextView) itemView.findViewById(R.id.txtarea);
            txt_hospital = (TextView) itemView.findViewById(R.id.txthospital);
            txt_shift = (TextView) itemView.findViewById(R.id.txtshift);
            txt_date = (TextView) itemView.findViewById(R.id.txtdate);
            txt_condition = (TextView) itemView.findViewById(R.id.txtcondition);
            txt_reason = (TextView) itemView.findViewById(R.id.txtreason);
            done = (ImageView) itemView.findViewById(R.id.done);



        }
    }


}
