package com.example.myapplication2;

public class FontSize {

    public int CELL;
    public int BUTTON;
    public int HINT;
    public int MISC_TEXT;

    public FontSize(int CELL, int BUTTON, int HINT) {
        this.CELL = CELL;
        this.BUTTON = BUTTON;
        this.HINT = HINT;
        this.MISC_TEXT = 11;
    }

    public FontSize() {
        this.CELL = 11;
        this.BUTTON = 11;
        this.HINT = 11;
        this.MISC_TEXT = 11;
    }

    public int getCELL() {
        return CELL;
    }

    public void setCELL(int CELL) {
        this.CELL = CELL;
    }

    public int getBUTTON() {
        return BUTTON;
    }

    public void setBUTTON(int BUTTON) {
        this.BUTTON = BUTTON;
    }

    public int getHINT() {
        return HINT;
    }

    public void setHINT(int HINT) {
        this.HINT = HINT;
    }

    public int getMISC_TEXT() {
        return MISC_TEXT;
    }

    public void setMISC_TEXT(int MISC_TEXT) {
        this.MISC_TEXT = MISC_TEXT;
    }
}
