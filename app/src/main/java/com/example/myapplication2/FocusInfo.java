package com.example.myapplication2;

public class FocusInfo {

    private int charFocusedId; // Id of the current focused CharField
    private int focusedOrientation; // Orientation of the current focused word
    private int wordFocusedId; // Id of the current focused word

    public FocusInfo(){
        this.charFocusedId = -1;
        this.focusedOrientation = -1;
        this.wordFocusedId = -1;
    }

    public FocusInfo(int charFocusedId,int focusedOrientation,int wordFocusedId){
        this.charFocusedId=charFocusedId;
        this.focusedOrientation=focusedOrientation;
        this.wordFocusedId=wordFocusedId;
    }

    public void setFocusInfo(int charFocusedId,int focusedOrientation,int wordFocusedId){
        this.charFocusedId=charFocusedId;
        this.focusedOrientation=focusedOrientation;
        this.wordFocusedId=wordFocusedId;
    }

    public int getCharFocusedId() {
        return charFocusedId;
    }

    public void setCharFocusedId(int charFocusedId) {
        this.charFocusedId = charFocusedId;
    }

    public int getFocusedOrientation() {
        return focusedOrientation;
    }

    public void setFocusedOrientation(int focusedOrientation) {
        this.focusedOrientation = focusedOrientation;
    }

    public int getWordFocusedId() {
        return wordFocusedId;
    }

    public void setWordFocusedId(int wordFocusedId) {
        this.wordFocusedId = wordFocusedId;
    }
}
