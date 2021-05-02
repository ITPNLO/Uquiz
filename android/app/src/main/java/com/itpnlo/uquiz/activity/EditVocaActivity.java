package com.itpnlo.uquiz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.adapter.EditCardAdapter;
import com.itpnlo.uquiz.bean.VocaGroupInfo;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;
import com.itpnlo.uquiz.utils.Consts;

import java.util.List;

public class EditVocaActivity extends AppCompatActivity {
    private EditText vocaTitle;
    private VocaGroupInfo vocaGroupInfo;
    private List<VocaInfo> vocaInfoList = null;

    private RecyclerView mRecyclerView;
    private EditCardAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_voca);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Intent intent = getIntent();
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);

        vocaGroupInfo = (VocaGroupInfo) intent.getSerializableExtra("vocaGroupInfo");

        vocaTitle = findViewById(R.id.vocaTitleEdit);
        vocaTitle.setText(vocaGroupInfo.getGroupName());
        vocaInfoList = localDBManager.getVocaList(vocaGroupInfo.getGroupId());

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_tab_list);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new EditCardAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter.setItems(vocaInfoList, this);
    }

    public void onVocaClick(VocaInfo vocaInfo){
        vocaGroupInfo.setGroupName(vocaTitle.getText().toString());
        Intent intent = new Intent(getApplicationContext(), EditCardActivity.class);
        intent.putExtra("vocaGroupInfo", vocaGroupInfo);
        intent.putExtra("vocaInfo", vocaInfo);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vocaeditmenu, menu);
        setTitle("");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.convertCard:
                Toast.makeText(this, "convert card", Toast.LENGTH_SHORT).show();
                Intent convertCardintent = new Intent(getApplicationContext(), ConvertCardActivity.class);
                convertCardintent.putExtra("vocaGroupId", vocaGroupInfo.getGroupId());
                startActivityForResult(convertCardintent, 0);
                return true;
            case R.id.addCard:
                if(("").equals(vocaTitle.getText().toString())){
                    Toast.makeText(this, "input title of voca", Toast.LENGTH_SHORT).show();
                    Intent addCardintent = new Intent(getApplicationContext(), EditCardActivity.class);
                    vocaGroupInfo.setGroupName(vocaTitle.getText().toString());
                    VocaInfo vocaInfo = new VocaInfo();
                    vocaInfo.setVocaId(-1);
                    addCardintent.putExtra("vocaGroupInfo", vocaGroupInfo);
                    addCardintent.putExtra("vocaInfo", vocaInfo);
                    startActivityForResult(addCardintent, 0);
                    return true;
                }
                Intent addCardintent = new Intent(getApplicationContext(), EditCardActivity.class);
                vocaGroupInfo.setGroupName(vocaTitle.getText().toString());
                VocaInfo vocaInfo = new VocaInfo();
                vocaInfo.setVocaId(-1);
                addCardintent.putExtra("vocaGroupInfo", vocaGroupInfo);
                addCardintent.putExtra("vocaInfo", vocaInfo);
                startActivityForResult(addCardintent, 0);
                return true;
            case R.id.deleteVoca:
                if(vocaGroupInfo.getGroupId() < 0){
                    Toast.makeText(this, "not exit voca", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Do you want to delete it?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    localDBManager.deleteVocaGroup(vocaGroupInfo.getGroupId());
                                    finish();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return true;
            case R.id.saveVoca:
                if(("").equals(vocaTitle.getText().toString())){
                    Toast.makeText(this, "input title of voca", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(vocaGroupInfo.getGroupId() < 0){
                    localDBManager.insertVocaGroup("", vocaTitle.getText().toString());
                } else {
                    vocaGroupInfo.setLanguage("");
                    vocaGroupInfo.setGroupName(vocaTitle.getText().toString());
                    localDBManager.updateVocaGroup(vocaGroupInfo);
                }
                List<VocaInfo> list = mAdapter.getItems();
                int index = 1;
                for(VocaInfo info : list){
                    info.setSortNum(index);
                    localDBManager.updateVoca(info);
                    index ++;
                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);

        VocaGroupInfo info = (VocaGroupInfo) data.getSerializableExtra("vocaGroupInfo");
        vocaGroupInfo.setGroupId(info.getGroupId());
        vocaGroupInfo.setSortNum(info.getSortNum());
        List<VocaInfo> vocaInfoList = localDBManager.getVocaList(info.getGroupId());

        vocaTitle.setText(info.getGroupName());

        mAdapter.setItems(vocaInfoList, this);
    }

    public void onClickDelete() {
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        vocaInfoList = localDBManager.getVocaList(vocaGroupInfo.getGroupId());
        mAdapter.setItems(vocaInfoList, this);
    }
}
