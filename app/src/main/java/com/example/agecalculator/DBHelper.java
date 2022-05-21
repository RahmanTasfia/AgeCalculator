package com.example.agecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper( Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("Create Table DateDetails (resultTV TEXT PRIMARY KEY )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists DateDetails");

    }

    public Boolean insertuserdata (String resultTV)

    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("resultTV", resultTV);


        long result=DB.insert("DateDetails", null, contentValues );

        if (result == -1)
        {
            return false;
        }
        else {
            return true;
        }

    }

    public Cursor getdata ()
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from DateDetails", null);
        return cursor;

    }
}
