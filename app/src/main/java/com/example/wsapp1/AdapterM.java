package com.example.wsapp1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wsapp1.model.DataModel;
import com.example.wsapp1.model.DataX;

import java.util.ArrayList;
import java.util.List;

public class AdapterM extends RecyclerView.Adapter<AdapterM.GroupViewHolder> {

    private List<DataX> das = new ArrayList<>();
    private DoOnClick doOnClick;

    public void setDas(List<DataX> das) {

        this.das = das;
        notifyItemRangeChanged(0, das.size());
    }

    public void setDoOnClick(DoOnClick doOnClick) {
        this.doOnClick = doOnClick;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        //  holder.title.setText(das.get(position).getModel());
        holder.title.setText(das.get(position).getName());
        holder.vin.setText(das.get(position).getVin());

        try {

            if (das.get(position).getPhotos().size() != 0) {

                holder.img.setVisibility(View.VISIBLE);
                Glide.with(holder.img)
                        .load("http://q-arp.net:3030/" + das.get(position).getPhotos().get(0).getUrl())
                        .into(holder.img);
            } else {
                holder.img.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return das.size();
    }

    public void setSearch(List<DataX> dm) {
        das = new ArrayList<>();
        das.addAll(dm);
        notifyDataSetChanged();

    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title = itemView.findViewById(R.id.rv_title);
        TextView vin = itemView.findViewById(R.id.rv_vin);
        ImageView img = itemView.findViewById(R.id.rv_img);

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            doOnClick.doClick(das.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    interface DoOnClick {
        void doClick(DataX das, int pos);
    }

}
