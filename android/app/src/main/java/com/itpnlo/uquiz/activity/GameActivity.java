package com.itpnlo.uquiz.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.adapter.VocaGridAdapter;
import com.itpnlo.uquiz.adapter.VocaGridAdapter2;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    int question;
    int anwser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), "androidVoca.db", null, 1);
        List<VocaInfo> vocaList = new ArrayList<>(localDBManager.getVocaList(getIntent().getIntExtra("vocaGroupId", 0)));

        final List<VocaInfo> tempVocaList = new ArrayList<>();
        for (int i=0; i<5; i++) {
            tempVocaList.add(vocaList.get(i));
        }

        GridView voca1GridView = findViewById(R.id.voca1GridView);
        final VocaGridAdapter voca1GridAdapter = new VocaGridAdapter(this, tempVocaList);
        final VocaGridAdapter2 voca2GridAdapter = new VocaGridAdapter2(this, tempVocaList);
        voca1GridView.setAdapter(voca1GridAdapter);
        voca1GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                question = i + 1;
                if(anwser != 0 && anwser == i + 1) {
                    Log.e("kokoko", anwser + "");
                    tempVocaList.remove(i);
                    voca1GridAdapter.notifyDataSetChanged();
                    voca2GridAdapter.notifyDataSetChanged();
                }
                anwser = 0;

            }
        });

        GridView voca2GridView = findViewById(R.id.voca2GridView);
        voca2GridView.setAdapter(voca2GridAdapter);
        voca2GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                anwser = i + 1;
                if(question != 0 && question == i + 1) {
                    Log.e("kokoko", question + "");
                    tempVocaList.remove(i);
                    voca1GridAdapter.notifyDataSetChanged();
                    voca2GridAdapter.notifyDataSetChanged();
                }
                question = 0;
            }
        });





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
        }
        return super.onOptionsItemSelected(item);
    }
}
