package com.example.wsapp1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wsapp1.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterM extends RecyclerView.Adapter<AdapterM.GroupViewHolder> {

    private List<DataModel> das = new ArrayList<>();
    private DoOnClick doOnClick;

    public void setDas(List<DataModel> das) {

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
        holder.title.setText(das.get(position).getModel());
    }

    @Override
    public int getItemCount() {
        return das.size();
    }

    public void setSearch(List<DataModel> dm) {
        das = new ArrayList<>();
        das.addAll(dm);
        notifyDataSetChanged();

    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title = itemView.findViewById(R.id.rv_title);

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
        void doClick(DataModel das, int pos);
    }

}
