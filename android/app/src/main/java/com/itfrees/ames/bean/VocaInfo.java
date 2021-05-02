package com.itfrees.ames.bean;

import com.itfrees.ames.utils.Consts;

import java.io.Serializable;

public class VocaInfo implements Serializable{
    private int groupId;
    private int vocaId;
    private String voca1 = "";
    private String voca2 = "";
    private String voca3 = "";
    private String voca4 = "";
    private String voca5 = "";
    private String voca6 = "";
    private String voca7 = "";
    private String voca8 = "";
    private String voca9 = "";
    private String voca10 = "";
    private String inputText = "";
    private int memoryStat;
    private int sortNum;
    private boolean isVoca2Visible = false;
    public int getGroupId() {
        return groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getVoca1() {
        return voca1.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca1ForDB() {
        return voca1;
    }
    public void setVoca1(String voca1) {
        if (voca1 == null) {
            this.voca1 = "";
        } else {
            voca1 = voca1.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca1 = voca1;
        }
    }
    public String getVoca2() {
        return voca2.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca2ForDB() {
        return voca2;
    }
    public void setVoca2(String voca2) {
        if (voca2 == null) {
            this.voca2 = "";
        } else {
            voca2 = voca2.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca2 = voca2;
        }
    }
    public String getVoca3() {
        return voca3.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca3ForDB() {
        return voca3;
    }
    public void setVoca3(String voca3) {
        if (voca3 == null) {
            this.voca3 = "";
        } else {
            voca3 = voca3.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca3 = voca3;
        }
    }
    public String getVoca4() {
        return voca4.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca4ForDB() {
        return voca4;
    }
    public void setVoca4(String voca4) {
        if (voca4 == null) {
            this.voca4 = "";
        } else {
            voca4 = voca4.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca4 = voca4;
        }
    }
    public String getVoca5() {
        return voca5.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca5ForDB() {
        return voca5;
    }
    public void setVoca5(String voca5) {
        if (voca5 == null) {
            this.voca5 = "";
        } else {
            voca5 = voca5.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca5 = voca5;
        }
    }
    public String getVoca6() {
        return voca6.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca6ForDB() {
        return voca6;
    }
    public void setVoca6(String voca6) {
        if (voca6 == null) {
            this.voca6 = "";
        } else {
            voca6 = voca6.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca6 = voca6;
        }
    }
    public String getVoca7() {
        return voca7.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca7ForDB() {
        return voca7;
    }
    public void setVoca7(String voca7) {
        if (voca7 == null) {
            this.voca7 = "";
        } else {
            voca7 = voca7.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca7 = voca7;
        }
    }
    public String getVoca8() {
        return voca8.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca8ForDB() {
        return voca8;
    }
    public void setVoca8(String voca8) {
        if (voca8 == null) {
            this.voca8 = "";
        } else {
            voca8 = voca8.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca8 = voca8;
        }
    }
    public String getVoca9() {
        return voca9.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca9ForDB() {
        return voca9;
    }
    public void setVoca9(String voca9) {
        if (voca9 == null) {
            this.voca9 = "";
        } else {
            voca9 = voca9.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca9 = voca9;
        }
    }
    public String getVoca10() {
        return voca10.replace(Consts.QUATATION_REPLACE, Consts.QUATATION);
    }
    public String getVoca10ForDB() {
        return voca10;
    }
    public void setVoca10(String voca10) {
        if (voca10 == null) {
            this.voca10 = "";
        } else {
            voca10 = voca10.replace(Consts.QUATATION, Consts.QUATATION_REPLACE);
            this.voca10 = voca10;
        }
    }
    public int getVocaId() { return vocaId; }
    public void setVocaId(int vocaId) { this.vocaId = vocaId; }
    public int getMemoryStat() { return memoryStat; }
    public void setMemoryStat(int memoryStat) { this.memoryStat = memoryStat; }
    public int getSortNum() { return sortNum; }
    public void setSortNum(int sortNum) { this.sortNum = sortNum; }
    public String getInputText() {
        return inputText;
    }
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
    public boolean isVoca2Visible() { return isVoca2Visible; }
    public void setVoca2Visible(boolean voca2Visible) { isVoca2Visible = voca2Visible; }

    public VocaInfo(){}
}
