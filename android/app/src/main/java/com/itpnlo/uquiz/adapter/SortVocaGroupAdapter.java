package com.itpnlo.uquiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.activity.ItemTouchHelperListener;
import com.itpnlo.uquiz.activity.SortVocaGroupActivity;
import com.itpnlo.uquiz.bean.VocaGroupInfo;

import java.util.ArrayList;
import java.util.List;

public class SortVocaGroupAdapter extends RecyclerView.Adapter<SortVocaGroupAdapter.ItemViewHolder> implements ItemTouchHelperListener {

    private List<VocaGroupInfo> items = new ArrayList<>();
    public SortVocaGroupAdapter(){}
    private SortVocaGroupActivity activity;


    @NonNull
    @Override
    public SortVocaGroupAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sort_vocagroup_item, parent, false);
        return new SortVocaGroupAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.onBind(items.get(position), position);
        final String vocaGroupName = items.get(position).getGroupName();
        final int vocaGroupId = items.get(position).getGroupId();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onVocaGroupClick(vocaGroupId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<VocaGroupInfo> itemList, SortVocaGroupActivity activity){
        items = itemList;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        VocaGroupInfo item = items.get(from_position);
        items.remove(from_position);
        items.add(to_position,item);
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView vocaGroupName;
        TextView rowVocaGroupPercentageTextView;
        LinearLayout linearLayout;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            vocaGroupName = itemView.findViewById(R.id.rowVocaGroupName);
            rowVocaGroupPercentageTextView = itemView.findViewById(R.id.rowVocaGroupPercentage);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
        public void onBind(VocaGroupInfo item, int position){
            vocaGroupName.setText(item.getGroupName()+" , "+item.getSortNum());
            rowVocaGroupPercentageTextView.setText(item.getCountVocaO() + " / " + item.getCountVocaX() + " / " + item.getCountVocaAll());
        }
    }

    public List<VocaGroupInfo> getItems(){
        return items;
    }
}
