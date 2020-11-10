package com.example.myapplication2;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

public class CharField extends androidx.appcompat.widget.AppCompatTextView {
    private int col;
    private int row;
    private int[] linkedWordId;
    private boolean correct;

    public CharField(Context context){
        super(context);
        this.linkedWordId = new int[2];
        this.correct = false;
    }

    public CharField(Context context, int fila, int columna){
        super(context);
        this.col = columna;
        this.row = fila;
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

    public int[] getLinkedWordsId(){
        return linkedWordId;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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
}
