package com.itpnlo.uquiz.bean;

import java.io.Serializable;

public class DragVocaInfo implements Serializable{
    private String voca = "";
    private int beforePosition;
    private int afterPosition;
    public String getVoca() { return voca; }
    public void setVoca(String voca) { this.voca = voca; }
    public int getBeforePosition() { return beforePosition; }
    public void setBeforePosition(int beforePosition) { this.beforePosition = beforePosition; }
    public int getAfterPosition() { return afterPosition; }
    public void setAfterPosition(int afterPosition) { this.afterPosition = afterPosition; }
    public DragVocaInfo(){}
    public DragVocaInfo(String voca, int beforePosition, int afterPosition){
        this.voca = voca;
        this.beforePosition = beforePosition;
        this.afterPosition = afterPosition;
    }
}
