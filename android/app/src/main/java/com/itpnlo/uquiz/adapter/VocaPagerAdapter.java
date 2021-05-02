package com.itpnlo.uquiz.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.activity.LearnActivity;
import com.itpnlo.uquiz.bean.VocaInfo;

import java.util.List;

public class VocaPagerAdapter extends PagerAdapter {
    private LearnActivity mContext = null ;
    private List<VocaInfo> vocaList;
    private TextToSpeech tts;

    public VocaPagerAdapter() {}

    public VocaPagerAdapter(LearnActivity context, List<VocaInfo> vocaList) {
        mContext = context ;
        this.vocaList = vocaList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View convertView = null ;
        tts = new TextToSpeech(mContext, mContext);
        if (mContext != null) {
            VocaInfo item = vocaList.get(position);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_voca_pager, container, false);
            TextView rowCount = convertView.findViewById(R.id.rowCount) ;
            rowCount.setText(position +1 + " / " + vocaList.size()) ;
            TextView voca1 = convertView.findViewById(R.id.voca1) ;
            voca1.setText(item.getVoca1()) ;
            TextView voca2 = convertView.findViewById(R.id.voca2) ;
            voca2.setText(item.getVoca2()) ;
            ImageButton vocaSound = convertView.findViewById(R.id.vocaSound);
            vocaSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tts.speak(vocaList.get(position).getVoca1(), TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            TextView voca3 = convertView.findViewById(R.id.voca3) ;
            voca3.setText(item.getVoca3()) ;
        }
        container.addView(convertView) ;
        return convertView ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return vocaList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
