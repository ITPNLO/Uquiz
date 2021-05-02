package com.itpnlo.uquiz.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.adapter.VocaGroupAdapter;
import com.itpnlo.uquiz.bean.VocaGroupInfo;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;
import com.itpnlo.uquiz.utils.Consts;
import com.itpnlo.uquiz.utils.VocaExcelReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int EXCEL_PICKER_REQUEST_CODE = 1;
    private List<VocaGroupInfo> vocaGroupList;
    private ListView vocaGroupListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        vocaGroupListView = findViewById(R.id.vocaGroupListView);
        vocaGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long ln) {
                alertDialog(vocaGroupList.get(position).getGroupId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        setTitle("");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mainAddVoca:
                Intent addVocaIntent = new Intent(MainActivity.this, EditVocaActivity.class);
                VocaGroupInfo vocaGroupInfo = new VocaGroupInfo();
                vocaGroupInfo.setGroupId(-1);
                addVocaIntent.putExtra("vocaGroupInfo", vocaGroupInfo);
                startActivity(addVocaIntent);
                return true;
            case R.id.mainEditVoca:
                Intent editVocaIntent = new Intent(MainActivity.this, SortVocaGroupActivity.class);
                startActivity(editVocaIntent);
                return true;
            case R.id.mainImport:
                if (checkPermission() == -1) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    return false;
                }
                Intent intent = new Intent();
                //すべてのファイル
                intent.setType("*/*");
                //エクセルファイルのみ
//                intent.setType("application/vnd.ms-excel");
//                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                //intent.setType("application/vnd.ms-excel|application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                String[] mimeTypes = {"application/vnd.ms-excel" , "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please select file."), EXCEL_PICKER_REQUEST_CODE);
                return true;
            case R.id.mainExport:
                Toast.makeText(this, "export btn", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mainSettings:
                Toast.makeText(this, "setting btn", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == EXCEL_PICKER_REQUEST_CODE && null != data) {
            InputStream is = null;
            VocaExcelReader ver = new VocaExcelReader();
            try {
                is = getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Object[] results = new Object[2];
            try {
                results = ver.getVocaList(is);
            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage("import file is falied.")
                        .setPositiveButton("OK", null )
                        .show();
                return;
            }
            List<VocaInfo> vocaList = (List<VocaInfo>) results[0];
            String[] vocaGroup = (String[]) results[1];
            if (null == vocaGroup[0]) {
                vocaGroup[0] = "No Title";
            }
            if (null == vocaGroup[1]) {
                vocaGroup[1] = "No Language";
            }
            String groupName = vocaGroup[0];
            String language = vocaGroup[1];
            if (1 > vocaList.size()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder
                        .setMessage("Not found Data.")
                        .setPositiveButton("OK", null )
                        .show();
                return;
            }
            LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
            localDBManager.insertVocaGroup(language, groupName, vocaList);
        }
    }

    /**
     * STORAGE Permission Check.
     *
     * @return 0 is okay permission, -1 is No permission
     */
    private int checkPermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission;
    }

    private void getVocaGroupList() {
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        vocaGroupList = localDBManager.getVocaGroupList();
        VocaGroupAdapter vocaGroupAdapter = new VocaGroupAdapter(this, vocaGroupList);
        vocaGroupListView.setAdapter(vocaGroupAdapter);
    }

    private void alertDialog(final int groupId) {
        CharSequence info[] = new CharSequence[] {"Learn", "answer", "multiple", "Game", "List" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch(which)
                {
                    case 0:
                        intent = new Intent(MainActivity.this, Learn2Activity.class);
                        intent.putExtra("vocaGroupId", groupId);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "answer", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "multiple", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, LearnActivity.class);
                        intent.putExtra("vocaGroupId", groupId);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("vocaGroupId", groupId);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, LocalVocaActivity.class);
                        intent.putExtra("vocaGroupId", groupId);
                        startActivity(intent);
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void onResume() {
        super.onResume();
        getVocaGroupList();
    }

}