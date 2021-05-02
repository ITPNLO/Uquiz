package com.itpnlo.uquiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.bean.VocaInfo;

import java.util.List;

public class VocaGridAdapter2 extends ArrayAdapter<VocaInfo> {
    Context mContext;
    LayoutInflater inflater;
    public VocaGridAdapter2(Context context, List<VocaInfo> list){
        super(context, R.layout.row_voca_list_grid, list);
        mContext = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_voca_list_grid, null);
        }
        VocaInfo item = getItem(position);
        TextView table_name = convertView.findViewById(R.id.vocaTextView);
        table_name.setText(String.valueOf(item.getVoca3()));
        return convertView;
    }
}
