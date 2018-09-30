package com.denis.minutodereflexao;

<<<<<<< HEAD
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DbHelper extends SQLiteAssetHelper {

    public static final String DATABASE_PATH = "/data/data/com.denis.minutodereflexao/databases/";
    public static final String DATABASE_NAME = "mr.db";
    public static final int DATABASE_VERSION = 1;
    private final static String LOG_TAG = "DbHelper";
    private Context mContext;

    /**
     * Create a helper object to create, open, and/or manage a database.
     *
     * @param context to use to open or create the database
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
=======
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
>>>>>>> versao2
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
<<<<<<< HEAD

=======
        //TODO: Ao atualizar o BD, manter as mensagens favoritas ja existentes.
>>>>>>> versao2
    }
}
