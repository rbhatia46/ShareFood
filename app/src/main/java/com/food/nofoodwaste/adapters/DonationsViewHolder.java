package com.food.nofoodwaste.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.food.nofoodwaste.R;
import com.food.nofoodwaste.utils.OnItemClickListener;

/**
 * Created by Ramakrishna on 12/08/2015.
 */


public class DonationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtDetails,txtDistance,txtAddress;
    OnItemClickListener clickListener;

    public DonationsViewHolder(View itemView,OnItemClickListener itemClickListener) {
        super(itemView);

        txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
        txtDetails = (TextView) itemView.findViewById(R.id.txt_details);
        txtDistance = (TextView) itemView.findViewById(R.id.txt_distance);

        itemView.setOnClickListener(this);
        clickListener = itemClickListener;

    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(v, getLayoutPosition());
    }
}

