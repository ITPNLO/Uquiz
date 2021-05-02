package com.itpnlo.uquiz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.itpnlo.uquiz.R;
import com.itpnlo.uquiz.bean.VocaGroupInfo;
import com.itpnlo.uquiz.bean.VocaInfo;
import com.itpnlo.uquiz.localDB.LocalDBManager;
import com.itpnlo.uquiz.utils.Consts;

public class EditCardActivity extends AppCompatActivity {

    private VocaInfo vocaInfo;

    private TextView vocaTextView1;
    private TextView vocaTextView2;
    private TextView vocaTextView3;
    private TextView vocaTextView4;
    private TextView vocaTextView5;
    private TextView vocaTextView6;
    private TextView vocaTextView7;
    private TextView vocaTextView8;
    private TextView vocaTextView9;
    private TextView vocaTextView10;

    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        intent = getIntent();
        vocaInfo = (VocaInfo)intent.getSerializableExtra("vocaInfo");

        vocaTextView1 = findViewById(R.id.vocaValue1);
        vocaTextView2 = findViewById(R.id.vocaValue2);
        vocaTextView3 = findViewById(R.id.vocaValue3);
        vocaTextView4 = findViewById(R.id.vocaValue4);
        vocaTextView5 = findViewById(R.id.vocaValue5);
        vocaTextView6 = findViewById(R.id.vocaValue6);
        vocaTextView7 = findViewById(R.id.vocaValue7);
        vocaTextView8 = findViewById(R.id.vocaValue8);
        vocaTextView9 = findViewById(R.id.vocaValue9);
        vocaTextView10 = findViewById(R.id.vocaValue10);
        if(vocaInfo.getVocaId() < 0){
            vocaTextView1.setText("");
            vocaTextView2.setText("");
            vocaTextView3.setText("");
            vocaTextView4.setText("");
            vocaTextView5.setText("");
            vocaTextView6.setText("");
            vocaTextView7.setText("");
            vocaTextView8.setText("");
            vocaTextView9.setText("");
            vocaTextView10.setText("");
        }else {
            vocaTextView1.setText(vocaInfo.getVoca1());
            vocaTextView2.setText(vocaInfo.getVoca2());
            vocaTextView3.setText(vocaInfo.getVoca3());
            vocaTextView4.setText(vocaInfo.getVoca4());
            vocaTextView5.setText(vocaInfo.getVoca5());
            vocaTextView6.setText(vocaInfo.getVoca6());
            vocaTextView7.setText(vocaInfo.getVoca7());
            vocaTextView8.setText(vocaInfo.getVoca8());
            vocaTextView9.setText(vocaInfo.getVoca9());
            vocaTextView10.setText(vocaInfo.getVoca10());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cardeditmenu, menu);
        setTitle("");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.deleteCard:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Do you want to delete it?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                doDelete(intent);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            case R.id.saveCard:
                intent.putExtra("vocaInfo", vocaInfo);
                doApply(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doApply(Intent intent){
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);

        String sVocaValue1 = vocaTextView1.getText().toString();
        String sVocaValue2 = vocaTextView2.getText().toString();
        String sVocaValue3 = vocaTextView3.getText().toString();
        String sVocaValue4 = vocaTextView4.getText().toString();
        String sVocaValue5 = vocaTextView5.getText().toString();
        String sVocaValue6 = vocaTextView6.getText().toString();
        String sVocaValue7 = vocaTextView7.getText().toString();
        String sVocaValue8 = vocaTextView8.getText().toString();
        String sVocaValue9 = vocaTextView9.getText().toString();
        String sVocaValue10 = vocaTextView10.getText().toString();

        if(("").equals(sVocaValue1)){
            Toast.makeText(this,"input vocaValue1", Toast.LENGTH_SHORT).show();
            vocaTextView1.requestFocus();
            return;
        }

        VocaGroupInfo vocaGroupInfo = (VocaGroupInfo) intent.getSerializableExtra("vocaGroupInfo");
        if(vocaGroupInfo.getGroupId() < 0 ){
            localDBManager.insertVocaGroup("",vocaGroupInfo.getGroupName());
            int vocaGroupId = localDBManager.getLastInsertID();
            vocaInfo.setGroupId(vocaGroupId);
        } else {
            vocaInfo.setGroupId(vocaGroupInfo.getGroupId());
        }

        vocaInfo.setVoca1(sVocaValue1);
        vocaInfo.setVoca2(sVocaValue2);
        vocaInfo.setVoca3(sVocaValue3);
        vocaInfo.setVoca4(sVocaValue4);
        vocaInfo.setVoca5(sVocaValue5);
        vocaInfo.setVoca6(sVocaValue6);
        vocaInfo.setVoca7(sVocaValue7);
        vocaInfo.setVoca8(sVocaValue8);
        vocaInfo.setVoca9(sVocaValue9);
        vocaInfo.setVoca10(sVocaValue10);

        if( vocaInfo.getVocaId() < 0 ){
            localDBManager.insertVoca(vocaInfo);
        } else {
            localDBManager.updateVoca(vocaInfo);
        }

        intent.putExtra("vocaGroupInfo", localDBManager.getVocaGroup(vocaInfo.getGroupId()));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void doDelete(Intent intent){
        LocalDBManager localDBManager = new LocalDBManager(getApplicationContext(), Consts.DB_NAME, null, 1);
        if( vocaInfo.getVocaId() < 0 ){
            Toast.makeText(this,"this card can't delete", Toast.LENGTH_SHORT).show();
        } else {
            localDBManager.deleteVoca(vocaInfo.getVocaId());
            intent.putExtra("vocaGroupInfo", localDBManager.getVocaGroup(vocaInfo.getGroupId()));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
