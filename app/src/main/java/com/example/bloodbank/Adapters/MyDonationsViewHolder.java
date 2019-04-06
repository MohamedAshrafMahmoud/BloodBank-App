package com.example.bloodbank.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodbank.R;

public class MyDonationsViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_area,txt_hospital,txt_shift,txt_date,txt_condition,txt_reason;



    public MyDonationsViewHolder(View itemView) {
        super(itemView);

        txt_area = (TextView) itemView.findViewById(R.id.txtarea);
        txt_hospital = (TextView) itemView.findViewById(R.id.txthospital);
        txt_shift = (TextView) itemView.findViewById(R.id.txtshift);
        txt_date = (TextView) itemView.findViewById(R.id.txtdate);
        txt_condition = (TextView) itemView.findViewById(R.id.txtcondition);
        txt_reason = (TextView) itemView.findViewById(R.id.txtreason);


    }
}
