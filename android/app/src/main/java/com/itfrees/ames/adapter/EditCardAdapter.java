package com.itfrees.ames.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itfrees.ames.R;
import com.itfrees.ames.activity.EditVocaActivity;
import com.itfrees.ames.activity.ItemTouchHelperListener;
import com.itfrees.ames.bean.VocaInfo;
import com.itfrees.ames.localDB.LocalDBManager;
import com.itfrees.ames.utils.Consts;

import java.util.ArrayList;
import java.util.List;

public class EditCardAdapter extends RecyclerView.Adapter<EditCardAdapter.ItemViewHolder> implements ItemTouchHelperListener {
    private List<VocaInfo> items = new ArrayList<>();
    public EditCardAdapter(){}
    private EditVocaActivity activity;

    @NonNull
    @Override
    public EditCardAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_voca_card, parent, false);
        return new EditCardAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditCardAdapter.ItemViewHolder holder, final int position) {
        holder.onBind(items.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onVocaClick(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<VocaInfo> itemList, EditVocaActivity activity){
        items = itemList;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        VocaInfo item = items.get(from_position);
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
        TextView vocaVaule1;
        LinearLayout linearLayout;
        Button deleteCardButton;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            vocaVaule1 = itemView.findViewById(R.id.vocaValue1);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            deleteCardButton = (Button)  itemView.findViewById(R.id.deleteCardButton);
        }

        public void onBind(final VocaInfo item){
            vocaVaule1.setText(item.getVoca1()+" , "+item.getSortNum());
            deleteCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Do you want to delete it?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    LocalDBManager localDBManager = new LocalDBManager(activity, Consts.DB_NAME, null, 1);
                                    localDBManager.deleteVoca(item.getVocaId());
                                    activity.onClickDelete();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        }
    }

    public List<VocaInfo> getItems(){
        return items;
    }
}
