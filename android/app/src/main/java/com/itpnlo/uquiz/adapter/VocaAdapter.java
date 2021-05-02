package com.itpnlo.uquiz.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.activity.LocalVocaActivity;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;
import com.itpnlo.uquiz.utils.Consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VocaAdapter extends ArrayAdapter<VocaInfo> {
    LayoutInflater inflater;
    Context mContext;
    LocalVocaActivity activity;
    List<VocaInfo> vocaInfolist;
    private SpeechRecognizer recognizer;
    List<String> beforeAnswers = new ArrayList<String>();
    private TextToSpeech tts;
    private Locale locale;

    public VocaAdapter(Context context, List<VocaInfo> list, TextToSpeech tts, Locale locale) {
        super(context, R.layout.row_voca_list, list);
        vocaInfolist = list;
        mContext = context;
        activity = (LocalVocaActivity)context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tts = tts;
        this.locale = locale;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_voca_list, parent, false);
        }
        if (activity.focusPosition > -1 && activity.focusPosition == position ) {
            EditText writeEditText = convertView.findViewById(R.id.writeEditText);
            writeEditText.setFocusable(true);
            writeEditText.setFocusableInTouchMode(true);
            writeEditText.setEnabled(true);
            writeEditText.requestFocus();
            activity.focusPosition = -1;
        }
        final VocaInfo item = getItem(position);
        final TextView rowVoca1= convertView.findViewById(R.id.rowVoca1);
        setGravityLeft(rowVoca1, item.getVoca1());
        rowVoca1.setText(item.getVoca1());
        rowVoca1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tts.isSpeaking()){
                    tts.stop();
                } else {
                    tts.speak(item.getVoca1(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        final TextView rowVoca2 = convertView.findViewById(R.id.rowVoca2);
        setGravityLeft(rowVoca2, item.getVoca2());
        rowVoca2.setText(item.getVoca2());
        if(activity.checkedStatus.get(Consts.VOCA2) == 2){
            rowVoca2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Resources res = mContext.getResources();
                    if(item.isVoca2Visible()){
                        view.setBackgroundColor(res.getColor(R.color.voca2Mode2Background));
                        rowVoca2.setTextColor(res.getColor(R.color.voca2Mode2Background));
                        item.setVoca2Visible(false);
                    }else {
                        view.setBackgroundColor(Color.TRANSPARENT);
                        rowVoca2.setTextColor(res.getColor(R.color.textColor));
                        item.setVoca2Visible(true);
                    }
                }
            });
        }
        TextView rowVoca3 = convertView.findViewById(R.id.rowVoca3);
        rowVoca3.setText(item.getVoca3());
        TextView rowVoca4 = convertView.findViewById(R.id.rowVoca4);
        rowVoca4.setText(item.getVoca4());
        LinearLayout OXLayout = convertView.findViewById(R.id.OXLayout);
        LinearLayout speechLayout = convertView.findViewById(R.id.speechLayout);
        LinearLayout writeLayout = convertView.findViewById(R.id.writeLayout);
        //gone : 0, invisible : 1
        int SET_GONE = 0;
        int SET_INVISIBLE = 1;
        setView(rowVoca1, activity.checkedStatus.get(Consts.VOCA1), SET_GONE, item);
        setView(rowVoca2, activity.checkedStatus.get(Consts.VOCA2), SET_GONE, item);
        setView(rowVoca3, activity.checkedStatus.get(Consts.VOCA3), SET_GONE, item);
        setView(rowVoca4, activity.checkedStatus.get(Consts.VOCA4), SET_GONE, item);
        setView(OXLayout, activity.checkedStatus.get(Consts.CHECK_BY_OX), SET_INVISIBLE, item);
        setView(speechLayout, activity.checkedStatus.get(Consts.CHECK_BY_SPEECH), SET_INVISIBLE, item);
        setView(writeLayout, activity.checkedStatus.get(Consts.CHECK_BY_WRITE), SET_GONE, item);
        if (activity.checkedStatus.get(Consts.CHECK_BY_OX) == 0
                && activity.checkedStatus.get(Consts.CHECK_BY_SPEECH) == 0
                && activity.checkedStatus.get(Consts.CHECK_BY_WRITE) != 0) {
            LinearLayout oxSpeechLayout = convertView.findViewById(R.id.OXSpeechLayout);
            oxSpeechLayout.setVisibility(View.GONE);
        } else if (activity.checkedStatus.get(Consts.CHECK_BY_OX) == 0
                && activity.checkedStatus.get(Consts.CHECK_BY_SPEECH) == 0
                && activity.checkedStatus.get(Consts.CHECK_BY_WRITE) == 0) {
            LinearLayout memoryCheckLayout = convertView.findViewById(R.id.memoryCheckLayout);
            memoryCheckLayout.setVisibility(View.GONE);
        } else {
            LinearLayout oxSpeechLayout = convertView.findViewById(R.id.OXSpeechLayout);
            oxSpeechLayout.setVisibility(View.VISIBLE);
        }

        Button knownButton = (Button) convertView.findViewById(R.id.knownButton);
        knownButton.setTag(position);
        Button unknownButton = (Button) convertView.findViewById(R.id.unknownButton);
        unknownButton.setTag(position);
        if(activity.memoryChkMode == Consts.MEMORY_STATUS_KNOWN) {
            knownButton.setVisibility(View.INVISIBLE);
            unknownButton.setVisibility(View.VISIBLE);
        } else if(activity.memoryChkMode == Consts.MEMORY_STATUS_UNKNOWN) {
            knownButton.setVisibility(View.VISIBLE);
            unknownButton.setVisibility(View.INVISIBLE);
        } else {
            knownButton.setVisibility(View.VISIBLE);
            unknownButton.setVisibility(View.VISIBLE);
        }
        Button speechButton = (Button) convertView.findViewById(R.id.speechButton);
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission() == -1 ) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
                    return;
                }
                startSpeechRecognize(position);
            }
        });
        //editTextのrow別入力した値を設定
        final EditText writeEditText = convertView.findViewById(R.id.writeEditText);
        writeEditText.setTag(position);
        writeEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction ( ) == KeyEvent.ACTION_DOWN) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                        setWriteCheck(writeEditText, rowVoca2, position);
                        return true;
                    }
                }
                return false;
            }
        });
        Button writeButton = (Button) convertView.findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWriteCheck(writeEditText, rowVoca2, position);
            }
        });


        writeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((Integer)writeEditText.getTag() != position) {
                    return;
                }
                if (charSequence.toString().contains("\n")){
                    setWriteCheck(writeEditText, rowVoca2, position);
                    return;
                }
                if (charSequence != null) {
                    item.setInputText(charSequence.toString());
                } else {
                    item.setInputText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        writeEditText.setText(item.getInputText());
        return convertView;
    }

    //改行を確認
    public boolean checkNewLine(String value){
        if(value.contains("\n")){
            return true;
        }
        return false;
    }

    //改行がある場合左側表示を処理
    public void setGravityLeft(TextView view, String value){
        if(checkNewLine(value)){
            view.setGravity(Gravity.LEFT);
            view.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    /**
     * 暗記確認（enterキーとwriteボタン）後の処理
     * @param writeEditText
     * @param rowVoca2
     * @param position
     */
    public void setWriteCheck(EditText writeEditText, TextView rowVoca2, int position){
        int result = Consts.MEMORY_STATUS_UNKNOWN;
        if("".equals(writeEditText.getText().toString())){
            activity.onClickWriteOrSpeech(0,null,"");
            return;
        }
        LocalDBManager localDBManager = new LocalDBManager(activity.getApplicationContext(), Consts.DB_NAME, null, 1);
        VocaInfo vocaInfo = vocaInfolist.get(position);
        if (writeEditText.getText().toString().equals(rowVoca2.getText().toString())) {
            vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_KNOWN);
            result = Consts.MEMORY_STATUS_KNOWN;
        } else {
            vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_UNKNOWN);
        }
        localDBManager.updateMemoryStatVoca(vocaInfo);
        if (Consts.MEMORY_STATUS_ALL == activity.memoryChkMode || result == activity.memoryChkMode){
            activity.focusPosition = position + 1;
        } else {
            activity.focusPosition = position;
        }
        activity.onClickWriteOrSpeech(result, vocaInfo, writeEditText.getText().toString());
    }

    //設定されたcheckBoxによってリストのカラム活性・非活性処理
    public void setView(View view, int value, int goneInvisibleStatus, VocaInfo vocaInfo) {
        if (value == 0 && goneInvisibleStatus == 0) {
            view.setVisibility(View.GONE);
        } else if (value == 0 && goneInvisibleStatus == 1) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            if(value == 2){
                Resources res = mContext.getResources();
                if (vocaInfo.isVoca2Visible()) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    ((TextView)view).setTextColor(res.getColor(R.color.textColor));
                } else {
                    view.setBackgroundColor(res.getColor(R.color.voca2Mode2Background));
                    ((TextView)view).setTextColor(res.getColor(R.color.voca2Mode2Background));
                }
            }
        }
    }

    private void startSpeechRecognize(final int position) {
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer.cancel();
            recognizer.destroy();
        }
        // インテント作成
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale.toString());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
        recognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        recognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                //Log.e("koko", "*****onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                //Log.e("koko", "*****onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float v) {
                //Log.e("koko", "*****onRmsChanged");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                //Log.e("koko", "*****onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                //Log.e("koko", "*****onEndOfSpeech");
            }

            @Override
            public void onError(int i) {
                Log.e("koko", "*****onError " + i);
                activity.onClickWriteOrSpeech(0,null,"");
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                String msg = "";
                ArrayList<String> ret = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                for (int i = 0; i < ret.size(); i++) {
                    msg += ret.get(i) + ", ";
                }

                Log.e("koko", "*****onPartialResults:" + msg);

                if (checkAnswer(ret, position)) {
                    LocalDBManager localDBManager = new LocalDBManager(activity.getApplicationContext(), Consts.DB_NAME, null, 1);
                    VocaInfo vocaInfo = vocaInfolist.get(position);
                    vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_KNOWN);
                    localDBManager.updateMemoryStatVoca(vocaInfo);
                    activity.onClickWriteOrSpeech(Consts.MEMORY_STATUS_KNOWN, vocaInfolist.get(position), msg);
                    recognizer.destroy();
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                String msg = "";
                ArrayList<String> ret = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                for (int i = 0; i < ret.size(); i++) {
                    msg += ret.get(i) + ", ";
                }

                Log.e("koko", "*****onResults:" + msg);

                LocalDBManager localDBManager = new LocalDBManager(activity.getApplicationContext(), Consts.DB_NAME, null, 1);
                VocaInfo vocaInfo = vocaInfolist.get(position);
                int result = Consts.MEMORY_STATUS_UNKNOWN;
                if (checkAnswer(ret, position)) {
                    vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_KNOWN);
                    result = Consts.MEMORY_STATUS_KNOWN;
                } else {
                    vocaInfo.setMemoryStat(Consts.MEMORY_STATUS_UNKNOWN);
                }
                localDBManager.updateMemoryStatVoca(vocaInfo);
                activity.onClickWriteOrSpeech(result, vocaInfolist.get(position), msg);
            }
            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.e("koko", "*****onEvent");
            }
        });
        recognizer.startListening(intent);
    }

    private boolean checkAnswer(List<String> answers, int position) {
        if (answers.isEmpty()) {
            return false;
        }
        List<String> toCheckAnswers = new ArrayList<>();
        int answersSize = answers.size();
        for (int i = answersSize; i > 0; i--) {
            String tmpAnswer = answers.get(i - 1);
            boolean isExistSame = false;
            for (String tmpBeforeAnswer : beforeAnswers) {
                if (tmpAnswer.equals(tmpBeforeAnswer)) {
                    isExistSame = true;
                }
                tmpAnswer = tmpAnswer.replace(tmpBeforeAnswer, "");
            }
            if (!isExistSame && !tmpAnswer.isEmpty()) {
                toCheckAnswers.add(tmpAnswer);
            }
        }
        int size = answers.size();
        if (size > toCheckAnswers.size()) {
            size = toCheckAnswers.size();
        }

        for (int i = 0; i < size; i++) {
            String answer = toCheckAnswers.get(i);
            beforeAnswers.add(answer);
            if (answer.equals(vocaInfolist.get(position).getVoca1())
                    || answer.equals(vocaInfolist.get(position).getVoca2())) {
                Log.e("koko", "*****checkAnswer true");
                return true;
            }
        }
        if (!toCheckAnswers.isEmpty()) {
            Log.e("koko1", toCheckAnswers.get(0));
        }
        Log.e("koko", "*****checkAnswer false");
        return false;
    }

    /**
     * RECORD AUDIO Permission Check.
     *
     * @return 0 is okay permission, -1 is No permission
     */
    private int checkPermission() {
        int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);
        return permission;
    }
}
