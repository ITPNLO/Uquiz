package com.itpnlo.uquiz.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.adapter.VocaAdapter;
import com.itpnlo.uquiz.bean.VocaGroupInfo;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;
import com.itpnlo.uquiz.utils.Consts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocalVocaActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    public HashMap<String,Integer> checkedStatus = new HashMap<String,Integer>();;
    public int memoryChkMode;
    public int focusPosition = -1;
    private List<VocaInfo> vocaList;
    private int vocaGroupId;
    private Parcelable scrollPosition;
    private int settingsStatus = 1;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private TextToSpeech tts;
    private Button languageButton;
    private Locale locale;

    /**
     * Vocaリストにcolumn表示の状態の初期値（表示）
     */
    private final int CHECKED_STATUS = 1;
    private final int UNCHECKED_STATUS = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_voca);

        tts = new TextToSpeech(this, this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        prefs = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        editor = prefs.edit();
        vocaGroupId = getIntent().getIntExtra("vocaGroupId", -1);

        if (prefs != null && prefs.getInt(vocaGroupId + "memoryChkMode",-1) > -1) {
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
        } else {
            memoryChkMode = getIntent().getIntExtra("memoryChkMode", Consts.MEMORY_STATUS_NOT);
            setTitle(Consts.MEMORY_STATUS_NOT_STRING);
        }

        CheckBox voca1CheckBox = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox voca2CheckBox = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox OXCheckBox = (CheckBox)findViewById(R.id.checkBoxOX);
        CheckBox speechCheckBox = (CheckBox)findViewById(R.id.checkBoxSpeech);
        CheckBox writeCheckBox = (CheckBox)findViewById(R.id.checkBoxWrite);
        CheckBox randomCheckbox = (CheckBox)findViewById(R.id.randomCheckBox);
        setViewByCheckedStatus(voca1CheckBox, Consts.VOCA1, CHECKED_STATUS);
        setViewByCheckedStatus(voca2CheckBox, Consts.VOCA2, UNCHECKED_STATUS);
        checkedStatus.put(Consts.VOCA3, UNCHECKED_STATUS);
        checkedStatus.put(Consts.VOCA4, UNCHECKED_STATUS);
        setViewByCheckedStatus(OXCheckBox, Consts.CHECK_BY_OX, CHECKED_STATUS);
        setViewByCheckedStatus(speechCheckBox, Consts.CHECK_BY_SPEECH, CHECKED_STATUS);
        setViewByCheckedStatus(writeCheckBox, Consts.CHECK_BY_WRITE, CHECKED_STATUS);
        setViewByCheckedStatus(randomCheckbox, Consts.RANDOM, UNCHECKED_STATUS);
        setLanguageButton();
        getVocaGroupList();
    }

    /**
     * checkedStatus状態保存
     * @param checkbox CheckBox
     * @param checkedStatusString checkedStatus
     */
    public void setViewByCheckedStatus(CheckBox checkbox, String checkedStatusString, int initialStatus){
        if (prefs != null && prefs.getInt(vocaGroupId + "checkedStatus" + checkedStatusString, -1) > -1) {
            checkedStatus.put(checkedStatusString, prefs.getInt(vocaGroupId + "checkedStatus" + checkedStatusString, -1));
            if (prefs.getInt(vocaGroupId + "checkedStatus" + checkedStatusString, -1) == 1
            || prefs.getInt(vocaGroupId + "checkedStatus" + checkedStatusString, -1) == 2) {
                checkbox.setChecked(true);
            } else if (prefs.getInt(vocaGroupId + "checkedStatus" + checkedStatusString, -1) == 0) {
                checkbox.setChecked(false);
            }
        } else {
            checkedStatus.put(checkedStatusString, initialStatus);
            if (initialStatus == 1) {
                checkbox.setChecked(true);
            } else {
                checkbox.setChecked(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vocalistmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.allCard:
                setTitle(Consts.MEMORY_STATUS_ALL_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_ALL;
                break;
            case R.id.notCheckCard:
                setTitle(Consts.MEMORY_STATUS_NOT_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_NOT;
                break;
            case R.id.knownCard:
                setTitle(Consts.MEMORY_STATUS_KNOWN_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_KNOWN;
                break;
            case R.id.unknownCard:
                setTitle(Consts.MEMORY_STATUS_UNKNOWN_STRING);
                memoryChkMode = Consts.MEMORY_STATUS_UNKNOWN;
                break;
            case R.id.listSettings:
                LinearLayout settingsLayout = findViewById(R.id.settingsLayout);
                if (settingsStatus == 1) {
                    settingsLayout.setVisibility(View.GONE);
                    settingsStatus = 0;
                } else {
                    settingsLayout.setVisibility(View.VISIBLE);
                    settingsStatus = 1;
                }
                break;
        }
        getVocaGroupList();
        return super.onOptionsItemSelected(item);
    }

    private void getVocaGroupList() {
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        if (memoryChkMode == Consts.MEMORY_STATUS_ALL) {
            vocaList = localDBManager.getVocaList(vocaGroupId);
        } else {
            vocaList = localDBManager.getVocaListByMemoryStat(vocaGroupId, memoryChkMode);
        }
        TextView rowVocaGroupPercentageTextView = findViewById(R.id.rowVocaGroupPercentage);
        VocaGroupInfo vocaGroup = localDBManager.getVocaGroupList(vocaGroupId).get(0);
        rowVocaGroupPercentageTextView.setText(vocaGroup.getCountVocaO() + " / " + vocaGroup.getCountVocaX() + " / " + vocaGroup.getCountVocaAll());
        ListView vocaListView = findViewById(R.id.vocaListView);
        if(checkedStatus.get(Consts.RANDOM) != null && checkedStatus.get(Consts.RANDOM) == 1){
            Collections.shuffle(vocaList);
        }
        VocaAdapter vocaAdapter = new VocaAdapter(this, vocaList, tts, locale);
        vocaListView.setAdapter(vocaAdapter);
    }

    public void onClickCheckBox(View view) {
        final boolean checked = ((CheckBox) view).isChecked();
        String checkBox = "";
        final int UNCHECK_STATUS = 0;
        final int CHECK_STATUS = 1;
        final int CHECK_MODE2_STATUS = 2;
        switch(view.getId()) {
            case R.id.checkBox1:
                checkBox = Consts.VOCA1;
                break;
            case R.id.checkBox2:
                checkBox = Consts.VOCA2;
                break;
            case R.id.checkBox3:
                checkBox = Consts.VOCA3;
                break;
            case R.id.checkBox4:
                checkBox = Consts.VOCA4;
                break;
            case R.id.checkBoxOX:
                checkBox = Consts.CHECK_BY_OX;
                break;
            case R.id.checkBoxSpeech:
                checkBox = Consts.CHECK_BY_SPEECH;
                break;
            case R.id.checkBoxWrite:
                checkBox = Consts.CHECK_BY_WRITE;
                break;
            case R.id.randomCheckBox:
                checkBox = Consts.RANDOM;
                break;
        }
        if (checkedStatus.get(Consts.VOCA2) == CHECK_STATUS && Consts.VOCA2.equals(checkBox) && !checked) {
            checkedStatus.put(checkBox, CHECK_MODE2_STATUS);
            ((CheckBox) view).setChecked(true);
        } else if (checked) {
            checkedStatus.put(checkBox, CHECK_STATUS);
        } else {
            checkedStatus.put(checkBox, UNCHECK_STATUS);
        }
        ListView vocaListView = findViewById(R.id.vocaListView);
        scrollPosition = vocaListView.onSaveInstanceState();
        if(checkedStatus.get(Consts.RANDOM) == 1){
            Collections.shuffle(vocaList);
        } else {
            LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
            if (memoryChkMode == Consts.MEMORY_STATUS_ALL) {
                vocaList = localDBManager.getVocaList(vocaGroupId);
            } else {
                vocaList = localDBManager.getVocaListByMemoryStat(vocaGroupId, memoryChkMode);
            }
        }
        VocaAdapter vocaAdapter = new VocaAdapter(this, vocaList, tts, locale);
        vocaListView.setAdapter(vocaAdapter);
        if(scrollPosition != null){
            vocaListView.onRestoreInstanceState(scrollPosition);
        }
    }

    public void onClickKnown(View v){
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        int position= (Integer)v.getTag();
        VocaInfo vocaInfo = vocaList.get(position);
        vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_KNOWN);
        localDBManager.updateMemoryStatVoca(vocaInfo);
        TextView memoryResultText = findViewById(R.id.memoryResultText);
        memoryResultText.setText("O/" + vocaInfo.getVoca1() + "/" + vocaInfo.getVoca2());
        memoryResultText.setTextColor(Color.BLUE);
        ListView vocaListView = findViewById(R.id.vocaListView);
        scrollPosition = vocaListView.onSaveInstanceState();
        getVocaGroupList();
        if(scrollPosition != null){
            vocaListView.onRestoreInstanceState(scrollPosition);
        }
    }

    public void onClickUnKnown(View v){
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        int position= (Integer)v.getTag();
        VocaInfo vocaInfo = vocaList.get(position);
        vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_UNKNOWN);
        localDBManager.updateMemoryStatVoca(vocaInfo);
        TextView memoryResultText = findViewById(R.id.memoryResultText);
        memoryResultText.setText("X/" + vocaInfo.getVoca1() + "/" + vocaInfo.getVoca2());
        memoryResultText.setTextColor(Color.RED);
        ListView vocaListView = findViewById(R.id.vocaListView);
        scrollPosition = vocaListView.onSaveInstanceState();
        getVocaGroupList();
        if(scrollPosition != null){
            vocaListView.onRestoreInstanceState(scrollPosition);
        }
    }

    /**
     * 暗記結果を表示
     * @param result　０：未入力　１：known　２：unknown
     * @param vocaInfo
     * @param inputValue
     */
    public void onClickWriteOrSpeech(int result, VocaInfo vocaInfo, String inputValue){
        ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
        TextView memoryResultText = findViewById(R.id.memoryResultText);
        inputValue = inputValue.replace(",", " ");
        if (inputValue.length() > 50) {
            inputValue = inputValue.substring(0, 50);
        }
        if (result == Consts.MEMORY_STATUS_KNOWN) {
            memoryResultText.setText("O/" + vocaInfo.getVoca1() + "/" + vocaInfo.getVoca2() + "/" + inputValue);
            memoryResultText.setTextColor(Color.BLUE);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
        } else if (result == Consts.MEMORY_STATUS_UNKNOWN) {
            memoryResultText.setText("X/" + vocaInfo.getVoca1() + "/" + vocaInfo.getVoca2() + "/" + inputValue);
            memoryResultText.setTextColor(Color.RED);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP2);
        } else {
            memoryResultText.setText("値を入力してください");
            memoryResultText.setTextColor(Color.RED);
        }
        ListView vocaListView = findViewById(R.id.vocaListView);
        scrollPosition = vocaListView.onSaveInstanceState();
        getVocaGroupList();
        if(scrollPosition != null){
            vocaListView.onRestoreInstanceState(scrollPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        editor.putInt(vocaGroupId + "memoryChkMode", memoryChkMode);
        editor.putInt(vocaGroupId + "checkedStatus" + Consts.VOCA1 , checkedStatus.get(Consts.VOCA1));
        editor.putInt(vocaGroupId + "checkedStatus" + Consts.VOCA2 , checkedStatus.get(Consts.VOCA2));
        editor.putInt(vocaGroupId + "checkedStatus" + Consts.CHECK_BY_OX , checkedStatus.get(Consts.CHECK_BY_OX));
        editor.putInt(vocaGroupId + "checkedStatus" + Consts.CHECK_BY_SPEECH , checkedStatus.get(Consts.CHECK_BY_SPEECH));
        editor.putInt(vocaGroupId + "checkedStatus" + Consts.CHECK_BY_WRITE , checkedStatus.get(Consts.CHECK_BY_WRITE));
        editor.putInt(vocaGroupId + "checkedStatus" + Consts.RANDOM, checkedStatus.get(Consts.RANDOM));
        editor.commit();
    }

    @Override
    public void onInit(int status) {
        // テキスト読み上げ声を調節
        if (status == TextToSpeech.SUCCESS) {
            float pitch = 0.8f;
            float rate = 1.0f;
            tts.setPitch(pitch);
            tts.setSpeechRate(rate);
            tts.setLanguage(locale);
        }
    }

    /**
     * 言語ボタンを設定する
     */
    private void setLanguageButton() {
        String lang = prefs.getString(vocaGroupId + Consts.LOCALE_LANGUAGE,"");
        String country = prefs.getString(vocaGroupId + Consts.LOCALE_COUNTRY,"");
        if (lang.isEmpty() || country.isEmpty()) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(lang, country);
        }
        languageButton = findViewById(R.id.languageButton);
        languageButton.setText(locale.getCountry());
        languageButton.setOnClickListener(getLanguageButtonListener());
        tts.setLanguage(locale);
        ListView vocaListView = findViewById(R.id.vocaListView);
        scrollPosition = vocaListView.onSaveInstanceState();
        getVocaGroupList();
        if(scrollPosition != null){
            vocaListView.onRestoreInstanceState(scrollPosition);
        }
    }

    /**
     * 言語ボタンを押下する
     * @return View.OnClickListener
     */
    private  View.OnClickListener getLanguageButtonListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        };
        return listener;
    }

    /**
     *　TTS言語変更ができるリストをアラートで
     *  表示してタップすると言語変更を適用する。
     */
    private void alertDialog() {
        Locale[] locales = Locale.getAvailableLocales();
        final List<Locale> availableLocaleList = new ArrayList<>();
        final List<String> availableLocaleNameList = new ArrayList<>();
        for (Locale locale : locales) {
            int res = tts.isLanguageAvailable(locale);
            if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                availableLocaleList.add(locale);
                availableLocaleNameList.add(locale.getDisplayName());
            }
        }
        CharSequence info[] = availableLocaleNameList.toArray(new CharSequence[availableLocaleNameList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                locale = availableLocaleList.get(which);
                editor = prefs.edit();
                editor.putString(vocaGroupId + Consts.LOCALE_LANGUAGE, locale.getLanguage());
                editor.putString(vocaGroupId + Consts.LOCALE_COUNTRY, locale.getCountry());
                editor.commit();
                setLanguageButton();
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
