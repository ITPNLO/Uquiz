package com.itpnlo.uquiz.localDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.itpnlo.uquiz.bean.VocaGroupInfo;
import com.itpnlo.uquiz.bean.VocaInfo;

import java.util.ArrayList;
import java.util.List;

public class LocalDBManager extends SQLiteOpenHelper {

    public LocalDBManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE voca(id INTEGER , vocaId INTEGER PRIMARY KEY AUTOINCREMENT, voca1 TEXT, voca2 TEXT, voca3 TEXT, voca4 TEXT," +
                " voca5 TEXT, voca6 TEXT, voca7 TEXT, voca8 TEXT, voca9 TEXT, voca10 TEXT, memoryStat INTEGER, sortNum INTEGER DEFAULT 1000000);");
        db.execSQL("CREATE TABLE vocaGroup(id INTEGER PRIMARY KEY AUTOINCREMENT, language TEXT, groupName TEXT, sortNum INTEGER DEFAULT 10000);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    /**
     * 単語のカードをinsertするメソッド
     * @param vocaInfo vocaInfo
     */
    public void insertVoca(VocaInfo vocaInfo) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "insert into voca(id, vocaId, voca1, voca2, voca3, voca4, voca5, voca6, voca7, voca8, voca9, voca10, memoryStat) " +
                "values(" + vocaInfo.getGroupId() + ", " + null + ", '" + vocaInfo.getVoca1ForDB()
                + "', '" + vocaInfo.getVoca2ForDB() + "', '" + vocaInfo.getVoca3ForDB() + "', '" + vocaInfo.getVoca4ForDB() + "', '"
                + vocaInfo.getVoca5ForDB() + "', '" + vocaInfo.getVoca6ForDB() + "', '" + vocaInfo.getVoca7ForDB() + "', '"
                + vocaInfo.getVoca8ForDB() + "', '" + vocaInfo.getVoca9ForDB() + "', '" + vocaInfo.getVoca10ForDB() + "', " + 0 + ");";
        db.execSQL(query);
        db.close();
    }

    /**
     * 単語のカードをupdateするメソッド
     * @param vocaInfo vocaInfo
     */
    public void updateVoca(VocaInfo vocaInfo) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "update voca set id=" + vocaInfo.getGroupId() + ", voca1='" + vocaInfo.getVoca1ForDB() + "', voca2='" + vocaInfo.getVoca2ForDB()
                + "', voca3='" + vocaInfo.getVoca3ForDB() + "', voca4='" + vocaInfo.getVoca4ForDB() + "', voca5='" + vocaInfo.getVoca5ForDB()
                + "', voca6='" + vocaInfo.getVoca6ForDB() + "', voca7='" + vocaInfo.getVoca7ForDB() + "', voca8='" + vocaInfo.getVoca8ForDB()
                + "', voca9='" + vocaInfo.getVoca9ForDB() + "', voca10='" + vocaInfo.getVoca10ForDB() + "', sortNum=" + vocaInfo.getSortNum()
                + " where vocaId='" + vocaInfo.getVocaId() + "';";
        db.execSQL(query);
        db.close();
    }

    /**
     * 単語のカードをdeleteするメソッド
     * @param vocaId vocaId
     */
    public void deleteVoca(int vocaId) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "delete from voca where vocaId='" + vocaId + "';";
        db.execSQL(query);
        db.close();
    }

    /**
     * VOCAGROUPとVOCAをdeleteするメソッド
     * @param id vocaGroupId
     */
    public void deleteVocaGroup(int id) {
        deleteGroup(id);
        deleteAllVoca(id);
    }

    public void deleteGroup(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "delete from vocaGroup where id='" + id + "';";
        db.execSQL(query);
        db.close();
    }

    public void deleteAllVoca(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "delete from voca where id='" + id + "';";
        db.execSQL(query);
        db.close();
    }

    /**
     * 単語のカードの暗記状態をupdateするメソッド
     * @param vocaInfo vocaInfo
     */
    public void updateMemoryStatVoca(VocaInfo vocaInfo) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "update voca set memoryStat=" + vocaInfo.getMemoryStat() + " where vocaId='"+ vocaInfo.getVocaId() +"';";
        db.execSQL(query);
        db.close();
    }

    public VocaGroupInfo getVocaGroup(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM vocaGroup WHERE id=" + id + ";", null);
        VocaGroupInfo vocaGroup = new VocaGroupInfo();
        while (cursor.moveToNext()) {
            vocaGroup.setGroupId(id);
            vocaGroup.setLanguage(cursor.getString(1));
            vocaGroup.setGroupName(cursor.getString(2));
            vocaGroup.setSortNum(cursor.getInt(3));
        }
        return vocaGroup;
    }

    /**
     * 単語をinsertするメソッド
     * @param id vocaGroupId
     * @param vocaList vocaList
     */
    public void insertVoca(int id, List<VocaInfo> vocaList) {
        SQLiteDatabase db = getWritableDatabase();
        for (VocaInfo vocaInfo : vocaList) {
            String query = "insert into voca(id, vocaId, voca1, voca2, voca3, voca4, voca5, voca6, voca7, voca8, voca9, voca10, memoryStat) "
                    + "values(" + id + ", " + null + ", '" + vocaInfo.getVoca1ForDB() + "', '" + vocaInfo.getVoca2ForDB()
                    + "', '" + vocaInfo.getVoca3ForDB() + "', '" + vocaInfo.getVoca4ForDB() + "', '" + vocaInfo.getVoca5ForDB()
                    + "', '" + vocaInfo.getVoca6ForDB() + "', '" + vocaInfo.getVoca7ForDB() + "', '" + vocaInfo.getVoca8ForDB()
                    + "', '" + vocaInfo.getVoca9ForDB() + "', '" + vocaInfo.getVoca10ForDB() + "', " + 0 + ");";
            db.execSQL(query);
        }
        db.close();
    }

    /**
     * 単語帳の一覧
     * @param id vocaGroupId
     * @return
     */
    public List<VocaInfo> getVocaList(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM voca WHERE id=" + id + " order by sortNum;", null);
        List<VocaInfo> vocaList = new ArrayList<>();
        while (cursor.moveToNext()) {
            VocaInfo vocaInfo = new VocaInfo();
            vocaInfo.setGroupId(cursor.getInt(0));
            vocaInfo.setVocaId(cursor.getInt(1));
            vocaInfo.setVoca1(cursor.getString(2));
            vocaInfo.setVoca2(cursor.getString(3));
            vocaInfo.setVoca3(cursor.getString(4));
            vocaInfo.setVoca4(cursor.getString(5));
            vocaInfo.setVoca5(cursor.getString(6));
            vocaInfo.setVoca6(cursor.getString(7));
            vocaInfo.setVoca7(cursor.getString(8));
            vocaInfo.setVoca8(cursor.getString(9));
            vocaInfo.setVoca9(cursor.getString(10));
            vocaInfo.setVoca10(cursor.getString(11));
            vocaInfo.setMemoryStat(cursor.getInt(12));
            vocaInfo.setSortNum(cursor.getInt(13));
            vocaList.add(vocaInfo);
        }
        return vocaList;
    }

    /**
     * 単語帳の一覧
     * @param id vocaGroupId
     * @param memoryStat memoryStat
     * @return
     */
    public List<VocaInfo> getVocaListByMemoryStat(int id, int memoryStat) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM voca WHERE id=" + id + " and memoryStat=" + memoryStat + " order by sortNum;", null);
        List<VocaInfo> vocaList = new ArrayList<>();
        while (cursor.moveToNext()) {
            VocaInfo vocaInfo = new VocaInfo();
            vocaInfo.setGroupId(cursor.getInt(0));
            vocaInfo.setVocaId(cursor.getInt(1));
            vocaInfo.setVoca1(cursor.getString(2));
            vocaInfo.setVoca2(cursor.getString(3));
            vocaInfo.setVoca3(cursor.getString(4));
            vocaInfo.setVoca4(cursor.getString(5));
            vocaInfo.setVoca5(cursor.getString(6));
            vocaInfo.setVoca6(cursor.getString(7));
            vocaInfo.setVoca7(cursor.getString(8));
            vocaInfo.setVoca8(cursor.getString(9));
            vocaInfo.setVoca9(cursor.getString(10));
            vocaInfo.setVoca10(cursor.getString(11));
            vocaInfo.setMemoryStat(cursor.getInt(12));
            vocaInfo.setMemoryStat(cursor.getInt(13));
            vocaList.add(vocaInfo);
        }
        return vocaList;
    }

    /**
     * vocaGroupInsert
     * @param language
     * @param groupName
     */
    public void insertVocaGroup(String language, String groupName) {
        //String query = "insert into vocaGroup values(null, '" + language + "', '" + groupName + "');";
        String query = "insert into vocaGroup(id, language, groupName) values(null, null, '" + groupName + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     *
     * @param vocaGroupInfo
     */
    public void updateVocaGroup(VocaGroupInfo vocaGroupInfo) {
        String query = "update vocaGroup set language=null, groupName='" + vocaGroupInfo.getGroupName()
                + "', sortNum=" + vocaGroupInfo.getSortNum() + " where id="+ vocaGroupInfo.getGroupId() +";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void insertVocaGroup(String language, String groupName, List<VocaInfo> vocaList) {
        insertVocaGroup(language,groupName);
        insertVoca(getLastInsertID(), vocaList);
    }

    /**
     * vocaGroupListと暗記パーセントを取得
     * @return
     */
    public List<VocaGroupInfo> getVocaGroupList() {
        List<VocaGroupInfo> vocaGroupList = getVocaGroupList(-1);
        return vocaGroupList;
    }

    /**
     * vocaGroupInfoと暗記パーセントを取得
     * @param vocaGroupId
     * @return
     */
    public List<VocaGroupInfo> getVocaGroupList(int vocaGroupId) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT vocaGroup.id,vocaGroup.language,vocaGroup.groupName,vocaGroup.sortNum" +
                ",(SELECT count(voca.memoryStat) FROM voca where voca.memoryStat=1 and voca.id=vocaGroup.id) as countO" +
                ",(SELECT count(voca.memoryStat) FROM voca where voca.memoryStat=2 and voca.id=vocaGroup.id) as countX" +
                ",(SELECT count(memoryStat) FROM voca where voca.id=vocaGroup.id) as countAll " +
                "from vocaGroup " +
                "LEFT JOIN voca " +
                "on vocaGroup.id = voca.id ";
        if(vocaGroupId > -1){
            sql += "WHERE vocaGroup.id = " + vocaGroupId;
        }
        sql += " GROUP by vocaGroup.id order by vocaGroup.sortNum";
        Cursor cursor = db.rawQuery(sql, null);
        List<VocaGroupInfo> vocaGroupList = new ArrayList<>();
        while (cursor.moveToNext()) {
            VocaGroupInfo vocaGroupInfo = new VocaGroupInfo();
            vocaGroupInfo.setGroupId(cursor.getInt(0));
            vocaGroupInfo.setLanguage(cursor.getString(1));
            vocaGroupInfo.setGroupName(cursor.getString(2));
            vocaGroupInfo.setSortNum(cursor.getInt(3));
            vocaGroupInfo.setCountVocaO(cursor.getInt(4));
            vocaGroupInfo.setCountVocaX(cursor.getInt(5));
            vocaGroupInfo.setCountVocaAll(cursor.getInt(6));
            vocaGroupList.add(vocaGroupInfo);
        }
        return vocaGroupList;
    }

    /**
     * vocaGroupListの最後ID
     * @return
     */
    public int getLastInsertID() {
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM vocaGroup;", null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(0);
        }
        return id;
    }
}
