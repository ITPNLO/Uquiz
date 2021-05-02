package com.itpnlo.uquiz.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.adapter.LearnAdapter;
import com.itpnlo.uquiz.bean.VocaGroupInfo;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;
import com.itpnlo.uquiz.utils.Consts;

import java.util.ArrayList;
import java.util.List;

public class Learn2Activity extends AppCompatActivity {
    public int memoryChkMode;
    private List<VocaInfo> vocaList;
    private int vocaGroupId;
    private GestureDetector gestureDetector;
    private ViewPager vocaPager;
    private LearnAdapter learnAdapter;
    private int index = 0;
    private TextView rowCountTextView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        prefs = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        editor = prefs.edit();

        rowCountTextView = findViewById(R.id.rowCount);
        vocaGroupId = getIntent().getIntExtra("vocaGroupId", -1);
        if(prefs != null && prefs.getInt(vocaGroupId + "memoryChkMode",-1) > -1){
            memoryChkMode = getIntent().getIntExtra("memoryChkMode", prefs.getInt(vocaGroupId + "memoryChkMode",-1));
            switch (memoryChkMode) {
                case Consts.MEMORY_STATUS_ALL:
                    setTitle(Consts.MEMORY_STATUS_ALL_STRING);
                    break;
                case Consts.MEMORY_STATUS_KNOWN:
                    setTitle(Consts.MEMORY_STATUS_KNOWN_STRING);
                    break;
                case Consts.MEMORY_STATUS_UNKNOWN:
                    setTitle(Consts.MEMORY_STATUS_UNKNOWN_STRING);
                    break;
                case Consts.MEMORY_STATUS_NOT:
                    setTitle(Consts.MEMORY_STATUS_NOT_STRING);
                    break;
            }
        }else{
            memoryChkMode = getIntent().getIntExtra("memoryChkMode", Consts.MEMORY_STATUS_NOT);
            setTitle(Consts.MEMORY_STATUS_NOT_STRING);
        }
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        if (memoryChkMode == Consts.MEMORY_STATUS_ALL) {
            vocaList = localDBManager.getVocaList(vocaGroupId);
        } else {
            vocaList = localDBManager.getVocaListByMemoryStat(vocaGroupId, memoryChkMode);
        }

        gestureDetector = new GestureDetector(new VerticalGesture());
        findViewById(R.id.vocaPager).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    return true;
                }
                return false;
            }
        });

        vocaPager = (ViewPager) findViewById(R.id.vocaPager);
        learnAdapter = new LearnAdapter(this, vocaList);
        vocaPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int jumpPosition = -1;

            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {
                // We do nothing here.
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    // prepare to jump to the last page
                    jumpPosition = learnAdapter.getRealCount();
                } else if (position == learnAdapter.getRealCount() + 1) {
                    //prepare to jump to the first page
                    jumpPosition = 1;
                } else {
                }
                index = 0;
                if (position == vocaList.size() + 1) {
                    position = 1;
                } else if (position == 0) {
                    position = vocaList.size();
                }
                rowCountTextView.setText(position + " / " + vocaList.size());
                // swipeでカードを変更する時、値を初期化
                TextView vocaTextView = (TextView)vocaPager.findViewWithTag(String.valueOf(position));
                if (vocaTextView!=null) {
                    VocaInfo info = vocaList.get(position - 1);
                    vocaTextView.setText(info.getVoca1());
                }
                // 暗記の状態を表示
                LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
                TextView rowVocaGroupPercentageTextView = findViewById(R.id.rowVocaGroupPercentage);
                VocaGroupInfo vocaGroup = localDBManager.getVocaGroupList(vocaGroupId).get(0);
                rowVocaGroupPercentageTextView.setText(vocaGroup.getCountVocaO() + " / " + vocaGroup.getCountVocaX() + " / " + vocaGroup.getCountVocaAll());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Let's wait for the animation to complete then do the jump.
                if (jumpPosition >= 0
                        && state == ViewPager.SCROLL_STATE_IDLE) {
                    // Jump without animation so the user is not
                    // aware what happened.
                    vocaPager.setCurrentItem(jumpPosition, false);
                    // Reset jump position.
                    jumpPosition = -1;
                }
            }
        });
        vocaPager.setAdapter(learnAdapter);
        vocaPager.setCurrentItem(1, false);
        if (prefs != null && prefs.getInt(vocaGroupId + "currentPage",-1) > -1) {
            vocaPager.setCurrentItem(prefs.getInt(vocaGroupId + "currentPage",-1),false);
        } else {
            vocaPager.setCurrentItem(1, false);
        }
        Button knownButton = (Button) findViewById(R.id.knownButton);
        Button unknownButton = (Button) findViewById(R.id.unknownButton);
        knownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VocaInfo vocaInfo = vocaList.get(vocaPager.getCurrentItem() - 1);
                vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_KNOWN);
                setVocaList(vocaInfo, vocaPager.getCurrentItem());
            }
        });
        unknownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VocaInfo vocaInfo = vocaList.get(vocaPager.getCurrentItem() - 1);
                vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_UNKNOWN);
                setVocaList(vocaInfo, vocaPager.getCurrentItem());
            }
        });
        setMemoryButton();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        else
            return false;
    }

    public class VerticalGesture extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        //最後のカードindex
        private static final int CARD_LAST_INDEX = 3;
        String[] vocaStringList;

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (vocaList.size() == 0) {
                return true;
            }
            if(vocaPager.getCurrentItem() > vocaList.size()) {
                vocaPager.setCurrentItem(1);
            }
            VocaInfo vocaInfo = vocaList.get(vocaPager.getCurrentItem() - 1);
            vocaStringList = new String[]{vocaInfo.getVoca1(), vocaInfo.getVoca2(), vocaInfo.getVoca3(), vocaInfo.getVoca4(),
                    vocaInfo.getVoca5(), vocaInfo.getVoca6(), vocaInfo.getVoca7(), vocaInfo.getVoca8(), vocaInfo.getVoca9(), vocaInfo.getVoca10()};
            List<String> cards = new ArrayList<String>();
            for(int i=0; i<=CARD_LAST_INDEX; i++) {
                if (!"".equals(vocaStringList[i])) {
                    cards.add(vocaStringList[i]);
                }
            }
            if (index >= cards.size()-1) {
                index = 0;
            } else {
                ++index;
            }
            TextView vocaTextView = (TextView)vocaPager.findViewWithTag(String.valueOf(vocaPager.getCurrentItem()));
            vocaTextView.setText(cards.get(index));
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    int page = vocaPager.getCurrentItem();
                    if (page == vocaList.size()+1) {
                        page = 1;
                    } else if (page == 0) {
                        page = vocaList.size();
                    }
                    VocaInfo vocaInfo = vocaList.get(page-1);
                    if (diffY > 0) {
                        vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_UNKNOWN);
                    } else {
                        vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_KNOWN);
                    }
                    setVocaList(vocaInfo, page);
                    result = true;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    /**
     * Learnページから暗記状態を設定
     * @param vocaInfo
     * @param currentItem
     */
    public void setVocaList(VocaInfo vocaInfo, int currentItem) {
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        localDBManager.updateMemoryStatVoca(vocaInfo);
        if (memoryChkMode == Consts.MEMORY_STATUS_ALL ||
                (memoryChkMode == Consts.MEMORY_STATUS_KNOWN && vocaInfo.getMemoryStat() == Consts.MEMORY_STATUS_KNOWN) ||
                (memoryChkMode == Consts.MEMORY_STATUS_UNKNOWN && vocaInfo.getMemoryStat() == Consts.MEMORY_STATUS_UNKNOWN)) {
            currentItem++;
            if (currentItem > vocaList.size()){
                currentItem = 1;
            }
        } else {
            vocaList.remove(currentItem-1);
        }
        setMemoryButton();
        learnAdapter = new LearnAdapter(this, vocaList);
        vocaPager.setAdapter(learnAdapter);

        vocaPager.setCurrentItem(currentItem, false);
        if (vocaInfo.getMemoryStat() == Consts.MEMORY_STATUS_KNOWN) {
            Toast.makeText(this, "known", Toast.LENGTH_SHORT).show();
        } else if (vocaInfo.getMemoryStat() == Consts.MEMORY_STATUS_UNKNOWN) {
            Toast.makeText(this, "unknown", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 暗記別Learnページ設定
     */
    public void setVocaListByMemoryStatus() {
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        if (memoryChkMode == Consts.MEMORY_STATUS_ALL) {
            vocaList = localDBManager.getVocaList(vocaGroupId);
        } else {
            vocaList = localDBManager.getVocaListByMemoryStat(vocaGroupId, memoryChkMode);
        }
        setMemoryButton();
        learnAdapter = new LearnAdapter(this, vocaList);
        vocaPager.setAdapter(learnAdapter);
        vocaPager.setCurrentItem(1, false);
    }

    /**
     * LearnページのtextView,Buttonのvisibilityを設定
     */
    public void setMemoryButton(){
        Button knownButton = findViewById(R.id.knownButton);
        Button unknownButton = findViewById(R.id.unknownButton);
        if (vocaList.size() == 0) {
            rowCountTextView.setText("not exit");
            knownButton.setVisibility(View.GONE);
            unknownButton.setVisibility(View.GONE);
        } else {
            knownButton.setVisibility(View.VISIBLE);
            unknownButton.setVisibility(View.VISIBLE);
        }
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        TextView rowVocaGroupPercentageTextView = findViewById(R.id.rowVocaGroupPercentage);
        VocaGroupInfo vocaGroup = localDBManager.getVocaGroupList(vocaGroupId).get(0);
        rowVocaGroupPercentageTextView.setText(vocaGroup.getCountVocaO() + " / " + vocaGroup.getCountVocaX() + " / " + vocaGroup.getCountVocaAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.learnmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.allCard:
                Toast.makeText(this, "AllVoca", Toast.LENGTH_SHORT).show();
                setTitle(Consts.MEMORY_STATUS_ALL_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_ALL;
                break;
            case R.id.notCheckCard:
                Toast.makeText(this, "NotCheckVoca", Toast.LENGTH_SHORT).show();
                setTitle(Consts.MEMORY_STATUS_NOT_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_NOT;
                break;
            case R.id.knownCard:
                Toast.makeText(this, "KnownVoca", Toast.LENGTH_SHORT).show();
                setTitle(Consts.MEMORY_STATUS_KNOWN_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_KNOWN;
                break;
            case R.id.unknownCard:
                Toast.makeText(this, "UnknownVoca", Toast.LENGTH_SHORT).show();
                setTitle(Consts.MEMORY_STATUS_UNKNOWN_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_UNKNOWN;
                break;
        }
        setVocaListByMemoryStatus();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        super.onPause();
        editor.putInt(vocaGroupId + "memoryChkMode", memoryChkMode);
        editor.putInt(vocaGroupId + "currentPage", vocaPager.getCurrentItem());
        editor.commit();
    }
}
