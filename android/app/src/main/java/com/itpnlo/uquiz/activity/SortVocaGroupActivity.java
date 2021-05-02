package com.itpnlo.uquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.adapter.SortVocaGroupAdapter;
import com.itpnlo.uquiz.bean.VocaGroupInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;
import com.itpnlo.uquiz.utils.Consts;

import java.util.List;

public class SortVocaGroupActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SortVocaGroupAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private List<VocaGroupInfo> vocaGroupInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_vocagroup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_tab_list);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new SortVocaGroupAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void onVocaGroupClick(int vocaGroupId){
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        Intent editVocaIntent = new Intent(this, EditVocaActivity.class);
        VocaGroupInfo vocaGroupInfo = localDBManager.getVocaGroup(vocaGroupId);
        editVocaIntent.putExtra("vocaGroupInfo", vocaGroupInfo);
        startActivity(editVocaIntent);
    }

    public void setSortVocaGroupView(){
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        vocaGroupInfoList = localDBManager.getVocaGroupList();
        mAdapter.setItems(vocaGroupInfoList, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortvocagroupmenu, menu);
        setTitle("Edit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.saveSort:
                List<VocaGroupInfo> list = mAdapter.getItems();
                int index = 1;
                for(VocaGroupInfo info : list){
                    info.setSortNum(index);
                    LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
                    localDBManager.updateVocaGroup(info);
                    index ++;
                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        setSortVocaGroupView();
    }
}