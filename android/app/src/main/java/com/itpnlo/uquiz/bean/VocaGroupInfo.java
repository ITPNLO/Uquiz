package com.itpnlo.uquiz.bean;

import java.io.Serializable;

public class VocaGroupInfo implements Serializable {
    private int groupId;
    private String language;
    private String groupName;
    private int countVocaO;
    private int countVocaX;
    private int countVocaAll;
    private int sortNum;
    public int getGroupId() {
        return groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public int getCountVocaO() { return countVocaO; }
    public void setCountVocaO(int countVocaO) { this.countVocaO = countVocaO; }
    public int getCountVocaX() { return countVocaX; }
    public void setCountVocaX(int countVocaX) { this.countVocaX = countVocaX; }
    public int getCountVocaAll() { return countVocaAll; }
    public void setCountVocaAll(int countVocaAll) { this.countVocaAll = countVocaAll; }
    public int getSortNum() { return sortNum; }
    public void setSortNum(int sortNum) { this.sortNum = sortNum; }
}
