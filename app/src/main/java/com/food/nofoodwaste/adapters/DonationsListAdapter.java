package com.food.nofoodwaste.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.food.nofoodwaste.R;
import com.food.nofoodwaste.utils.FoodObject;
import com.food.nofoodwaste.utils.OnItemClickListener;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by RamakrishnaAS on 12/08/2015.
 */
public class DonationsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<FoodObject> foodObjects;
    OnItemClickListener onItemClickListener;


    public DonationsListAdapter(Context context,ArrayList<FoodObject> foodObjects) {
        this.foodObjects = foodObjects;
        mContext = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mitemclickListener) {
        this.onItemClickListener = mitemclickListener;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder gridViewHolder = null;
        View itemView = null;
        if (itemView == null) {
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_list, parent, false);
            mContext = parent.getContext();
            gridViewHolder = new DonationsViewHolder(itemView,onItemClickListener);
            itemView.setTag(gridViewHolder);
        } else {
            gridViewHolder = (RecyclerView.ViewHolder) itemView.getTag();
        }
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FoodObject foodObject = foodObjects.get(position);

        String details = foodObject.getFoodtype()+" Kgs, "+foodObject.getQuantity();
        String address = foodObject.getAddress();
        String distance = "";
        if (foodObject.getDistance() == null){
            distance = "8 kms from current location";
            ((DonationsViewHolder) holder).txtDistance.setVisibility(View.GONE);
        }else{
            distance = foodObject.getDistance()+" kms from current location";
            ((DonationsViewHolder) holder).txtDistance.setVisibility(View.VISIBLE);
        }
        //String distance = foodObject.getDistance()+" kms from current location";
        ((DonationsViewHolder) holder).txtDetails.setText(details);
        ((DonationsViewHolder) holder).txtAddress.setText(address);
        ((DonationsViewHolder) holder).txtDistance.setText(distance);
    }

    @Override
    public int getItemCount() {
        return foodObjects.size();
    }
}
