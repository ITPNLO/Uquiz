package com.itfrees.ames.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itfrees.ames.R;
import com.itfrees.ames.activity.ItemTouchHelperListener;
import com.itfrees.ames.bean.DragVocaInfo;

import java.util.ArrayList;
import java.util.List;

public class ConvertCardAdapter extends RecyclerView.Adapter<ConvertCardAdapter.ItemViewHolder> implements ItemTouchHelperListener{

    private List<DragVocaInfo> items = new ArrayList<>();

    public ConvertCardAdapter(){}

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.convert_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.onBind(items.get(position),position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<DragVocaInfo> itemList){
        items = itemList;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        DragVocaInfo item = items.get(from_position);
        items.remove(from_position);
        items.add(to_position,item);
        item.setAfterPosition(to_position);
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tabName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tabName = itemView.findViewById(R.id.voca);
        }
        public void onBind(DragVocaInfo item,int position){
            tabName.setText(item.getVoca());
            item.setAfterPosition(position);
        }
    }

    public List<DragVocaInfo> getItems(){
        return items;
    }
}
