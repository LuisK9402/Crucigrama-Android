package com.example.myapplication2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAcces {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAcces instance;
    Cursor c = null;

    private DatabaseAcces(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAcces getInstance(Context context){
        if(instance==null){
            instance = new DatabaseAcces(context);
        }
        return instance;
    }

    public void openDb(){
        this.db = openHelper.getWritableDatabase();
    }

    public void closeDb(){
        if(db!=null){
            this.db.close();
        }
    }

    public String getCwData(int nivel){
        c = db.rawQuery("SELECT cwInfo FROM niveles WHERE nivel = "+nivel, new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
                String datos = c.getString(0);
                buffer.append(""+datos);
        }
        return buffer.toString();
    }
}
