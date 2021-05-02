package com.itfrees.ames.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.itfrees.ames.R;
import com.itfrees.ames.activity.Learn2Activity;
import com.itfrees.ames.bean.VocaInfo;

import java.util.List;

public class LearnAdapter extends PagerAdapter {
    private Learn2Activity mContext = null ;
    private List<VocaInfo> vocaList;

    @Override
    public int getCount() {
        return vocaList.size() == 0 ? 0 : vocaList.size() + 2;
    }

    public int getRealCount(){
        return vocaList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    public LearnAdapter(Learn2Activity context, List<VocaInfo> vocaList) {
        mContext = context;
        this.vocaList = vocaList;
    }

    private int mapPagerPositionToModelPosition(int pagerPosition) {
        if (pagerPosition == 0) {
            return getRealCount() - 1;
        }
        if (pagerPosition == getRealCount() + 1) {
            return 0;
        }
        return pagerPosition - 1;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        final int modelPosition = mapPagerPositionToModelPosition(position);
        View convertView = null;
        VocaInfo item = vocaList.get(modelPosition);
        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_voca_learn, container, false);
            if(position > vocaList.size()){
                position = 1;
            }
            TextView vocaTextView = convertView.findViewById(R.id.voca);
            vocaTextView.setTag(String.valueOf(position));
            vocaTextView.setText(item.getVoca1());
        }
        container.addView(convertView) ;
        return convertView ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
