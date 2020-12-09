package com.proyecto.cruzando;

import android.content.Context;

public class CharField extends androidx.appcompat.widget.AppCompatTextView {
    private int col;
    private int row;
    private int[] linkedWordId;
    private boolean correct;
    private boolean isHead;
    private int headIdx;

    public CharField(Context context){
        super(context);
        this.linkedWordId = new int[2];
        this.correct = false;
    }

    public void setLinkedWordId(int wordId){
        if(linkedWordId == null || linkedWordId.length == 0){
            linkedWordId[0] = wordId;
        }
        else {
            linkedWordId[1] = wordId;
        }
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setPos(int fila, int columna){
        this.col = columna;
        this.row = fila;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isHead(){
        return this.isHead;
    }

    public void setHead(Boolean isHead){
        this.isHead = isHead;
    }

    public int getHeadIdx() {
        return headIdx;
    }

    public void setHeadIdx(int headIdx) {
        this.headIdx = headIdx;
    }
}
