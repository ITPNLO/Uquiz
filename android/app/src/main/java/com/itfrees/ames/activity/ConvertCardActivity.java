package com.itfrees.ames.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itfrees.ames.R;
import com.itfrees.ames.adapter.ConvertCardAdapter;
import com.itfrees.ames.adapter.EditCardAdapter;
import com.itfrees.ames.bean.DragVocaInfo;
import com.itfrees.ames.bean.VocaGroupInfo;
import com.itfrees.ames.bean.VocaInfo;
import com.itfrees.ames.localDB.LocalDBManager;
import com.itfrees.ames.utils.Consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConvertCardActivity extends AppCompatActivity {
    private VocaInfo vocaInfo;
    private RecyclerView mRecyclerView;
    private ConvertCardAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private List<VocaInfo> vocaInfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_card);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_tab_list);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new ConvertCardAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));

        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        vocaInfoList = localDBManager.getVocaList(getIntent().getIntExtra("vocaGroupId", -1));
        List<DragVocaInfo> dragVocaInfoList = new ArrayList<>();
        if (vocaInfoList.size() > 0) {
            vocaInfo = vocaInfoList.get(0);
            DragVocaInfo item1 = new DragVocaInfo(vocaInfo.getVoca1(), 0, 0);
            DragVocaInfo item2 = new DragVocaInfo(vocaInfo.getVoca2(), 1, 0);
            DragVocaInfo item3 = new DragVocaInfo(vocaInfo.getVoca3(), 2, 0);
            DragVocaInfo item4 = new DragVocaInfo(vocaInfo.getVoca4(), 3, 0);
            DragVocaInfo item5 = new DragVocaInfo(vocaInfo.getVoca5(), 4, 0);
            DragVocaInfo item6 = new DragVocaInfo(vocaInfo.getVoca6(), 5, 0);
            DragVocaInfo item7 = new DragVocaInfo(vocaInfo.getVoca7(), 6, 0);
            DragVocaInfo item8 = new DragVocaInfo(vocaInfo.getVoca8(), 7, 0);
            DragVocaInfo item9 = new DragVocaInfo(vocaInfo.getVoca9(), 8, 0);
            DragVocaInfo item10 = new DragVocaInfo(vocaInfo.getVoca10(), 9, 0);
            dragVocaInfoList.add(item1);
            dragVocaInfoList.add(item2);
            dragVocaInfoList.add(item3);
            dragVocaInfoList.add(item4);
            dragVocaInfoList.add(item5);
            dragVocaInfoList.add(item6);
            dragVocaInfoList.add(item7);
            dragVocaInfoList.add(item8);
            dragVocaInfoList.add(item9);
            dragVocaInfoList.add(item10);
        }

        mAdapter.setItems(dragVocaInfoList);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.convertcardmenu, menu);
        setTitle("");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = getIntent();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.convert_card_save:
                intent.putExtra("vocaGroupInfo", setConvertCard(Consts.CONVERT_MODE_SAVE));
                setResult(RESULT_OK, getIntent());
                finish();
                return true;
            case R.id.convert_card_copy:
                intent.putExtra("vocaGroupInfo", setConvertCard(Consts.CONVERT_MODE_COPY));
                setResult(RESULT_OK, getIntent());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public VocaGroupInfo setConvertCard(String mode){
        int vocaGroupId = getIntent().getIntExtra("vocaGroupId", -1);
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        List<DragVocaInfo> afterItems = mAdapter.getItems();
        VocaGroupInfo vocaGroupInfo = localDBManager.getVocaGroup(vocaGroupId);
        if(Consts.CONVERT_MODE_COPY.equals(mode)){
            String copyGroupName = vocaGroupInfo.getGroupName()+"_copy";
            localDBManager.insertVocaGroup("", copyGroupName);
            vocaGroupId = localDBManager.getLastInsertID();
        }
        for(VocaInfo vocaInfo : vocaInfoList){
            String[] vocaStringList = {vocaInfo.getVoca1ForDB(), vocaInfo.getVoca2ForDB(), vocaInfo.getVoca3ForDB(), vocaInfo.getVoca4ForDB(), vocaInfo.getVoca5ForDB(),
                    vocaInfo.getVoca6ForDB(), vocaInfo.getVoca7ForDB(), vocaInfo.getVoca8ForDB(), vocaInfo.getVoca9ForDB(), vocaInfo.getVoca10ForDB()};
            VocaInfo saveVocaInfo = new VocaInfo();
            saveVocaInfo.setGroupId(vocaGroupId);
            saveVocaInfo.setVocaId(vocaInfo.getVocaId());
            saveVocaInfo.setVoca1(vocaStringList[afterItems.get(0).getBeforePosition()]);
            saveVocaInfo.setVoca2(vocaStringList[afterItems.get(1).getBeforePosition()]);
            saveVocaInfo.setVoca3(vocaStringList[afterItems.get(2).getBeforePosition()]);
            saveVocaInfo.setVoca4(vocaStringList[afterItems.get(3).getBeforePosition()]);
            saveVocaInfo.setVoca5(vocaStringList[afterItems.get(4).getBeforePosition()]);
            saveVocaInfo.setVoca6(vocaStringList[afterItems.get(5).getBeforePosition()]);
            saveVocaInfo.setVoca7(vocaStringList[afterItems.get(6).getBeforePosition()]);
            saveVocaInfo.setVoca8(vocaStringList[afterItems.get(7).getBeforePosition()]);
            saveVocaInfo.setVoca9(vocaStringList[afterItems.get(8).getBeforePosition()]);
            saveVocaInfo.setVoca10(vocaStringList[afterItems.get(9).getBeforePosition()]);
            if (Consts.CONVERT_MODE_SAVE.equals(mode)) {
                saveVocaInfo.setSortNum(vocaInfo.getSortNum());
                localDBManager.updateVoca(saveVocaInfo);
            } else {
                localDBManager.insertVoca(saveVocaInfo);
            }
        }
        return localDBManager.getVocaGroup(vocaGroupId);
    }
}
