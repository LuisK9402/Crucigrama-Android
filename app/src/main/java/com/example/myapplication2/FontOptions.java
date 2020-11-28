package com.example.myapplication2;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;

public class FontOptions {

    public int CELL;
    public int BUTTON;
    public int HINT;
    public int MISC_TEXT;
    float density;


    public int CELLdp;
    public int BUTTONdp;
    public int HINTdp;
    public int MISC_TEXTdp;

    public Typeface FONT_STYLE;

    public static final int SMALL_FONT = 0;
    public static final int MEDIUM_FONT= 1;
    public static final int LARGE_FONT =2;
    public static final int SERIF_FONT = 0;
    public static final int DISLEXY_FONT = 1;

    public FontOptions(int CELL, int BUTTON, int HINT, String FONT_STYLE, Context context) {
        this.CELL = CELL;
        this.BUTTON = BUTTON;
        this.HINT = HINT;
        this.MISC_TEXT = 11;
        this.FONT_STYLE = font_id(FONT_STYLE, context);
        this.density  = context.getResources().getDisplayMetrics().density;
        this.BUTTONdp = (int) (BUTTON*density);
        this.CELLdp = (int) (CELL*density);
        this.MISC_TEXTdp = (int) (11*density);
    }

    public FontOptions() {
        this.CELL = 11;
        this.BUTTON = 11;
        this.HINT = 11;
        this.MISC_TEXT = 11;
    }

    private Typeface font_id(String font, Context context){
        if(font.equals("serif")){
            return Typeface.createFromAsset(context.getAssets(), "fonts/open_serif_book.ttf");
        }
        if(font.equals("openDyslexic")){
            return Typeface.createFromAsset(context.getAssets(), "fonts/open_dyslexic_regular.otf");
        }
        return Typeface.createFromAsset(context.getAssets(), "fonts/open_serif_book.ttf");
    }

    public int getCELL() {
        return CELL;
    }

    public void setCELL(int CELL) {
        this.CELL = CELL;
        this.CELLdp = (int) (CELL*density);

    }

    public int getBUTTON() {
        return BUTTON;
    }

    public void setBUTTON(int BUTTON) {
        this.BUTTON = BUTTON;
        this.BUTTONdp = (int) (BUTTON*density);

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
        this.MISC_TEXT = (int) (11*density);
    }

    public Typeface getFONT_STYLE() {
        return FONT_STYLE;
    }

    public void setFONT_STYLE(String FONT_STYLE, Context context) {
        this.FONT_STYLE = font_id(FONT_STYLE, context);
    }
}
