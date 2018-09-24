package com.denis.minutodereflexao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DbHelper extends SQLiteAssetHelper {

    public static final int DATABASE_VERSION = 2;
    @SuppressLint("SdCardPath")
    static final String DATABASE_PATH = "/data/data/com.denis.minutodereflexao/databases/";
    static final String DATABASE_NAME = "mr.db";
    private final static String LOG_TAG = "DbHelper";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Ao atualizar o BD, manter as mensagens favoritas ja existentes.
    }
}
