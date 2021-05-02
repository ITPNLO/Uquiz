package com.itpnlo.uquiz.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.bean.VocaGroupInfo;
import java.util.List;

public class VocaGroupAdapter extends ArrayAdapter<VocaGroupInfo> {
    LayoutInflater inflater;
    Context mContext;

    public VocaGroupAdapter(Context context, List<VocaGroupInfo> list) {
        super(context, R.layout.row_voca_group_list, list);
        mContext = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_voca_group_list, null);
        }
        VocaGroupInfo item = getItem(position);
        TextView rowVocaGroupNameTextView = convertView.findViewById(R.id.rowVocaGroupName);
        TextView rowVocaGroupPercentageTextView = convertView.findViewById(R.id.rowVocaGroupPercentage);
        rowVocaGroupNameTextView.setText(item.getGroupName());
        rowVocaGroupPercentageTextView.setText(item.getCountVocaO() + " / " + item.getCountVocaX() + " / " + item.getCountVocaAll());
        return convertView;
    }
}
