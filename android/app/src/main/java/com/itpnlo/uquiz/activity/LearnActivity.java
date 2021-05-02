package com.itpnlo.uquiz.activity;

import android.Manifest;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.adapter.VocaPagerAdapter;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;

import java.util.ArrayList;
import java.util.List;

public class LearnActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        if (checkPermission() == -1 ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            finish();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), "androidVoca.db", null, 1);
        List<VocaInfo> vocaList = new ArrayList<>(localDBManager.getVocaList(getIntent().getIntExtra("vocaGroupId", 0)));

        ViewPager vocaPager = findViewById(R.id.vocaPager);
        VocaPagerAdapter vocaPagerAdapter = new VocaPagerAdapter(this, vocaList);
        vocaPager.setAdapter(vocaPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.learnmenu, menu);
        setTitle("");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * RECORD AUDIO Permission Check.
     *
     * @return 0 is okay permission, -1 is No permission
     */
    private int checkPermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return permission;
    }

    @Override
    public void onInit(int i) {

    }
}
