package com.denis.minutodereflexao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbAccess {

    // Constantes para schema da tabela MENSAGEM
    public static final String TABELA_MENSAGEM = "mensagem";
    public static final String COLUNA_TITULO = "titulo";
    public static final String COLUNA_TEXTO = "texto";
    public static final String COLUNA_AUTOR = "autor";
    private final static String LOG_TAG = "DbAccess";
    private static DbAccess instance;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;

    /**
     * Private constructor to aboid object creation from outside classes.
     * @param context
     */
    private DbAccess(Context context) {
        Log.i(LOG_TAG,"Executa contrutor DbAccess");
        mDbHelper = new DbHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DbAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DbAccess(context);
        }
        Log.i(LOG_TAG,"getInstance");
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        Log.i(LOG_TAG,"getReadableDatabase()");
        mDatabase = mDbHelper.getReadableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (mDatabase != null) {
            Log.i(LOG_TAG,"closeDatabase");
            mDatabase.close();
        }
    }

    /**
     * Read all data from the table.
     * @return a cursor
     */
    public Cursor getTodasMensagens(){

        String[] strProjection = {COLUNA_TITULO,COLUNA_TEXTO,COLUNA_AUTOR};
        try {
            Log.i(LOG_TAG,"Query Todas as mensagens");
            mCursor = mDatabase.query(TABELA_MENSAGEM, //Nome da tabela
                    strProjection, // campos para pesquisa. NULL = *
                    null, // Criterios de pesquisa WHERE
                    null, // argumentos do WHERE
                    null, // Group By
                    null, // Having
                    null); // Order By
        } finally {
            return mCursor;
        }
    }

    /**
     * Read all data from the table.
     * @return a cursor
     */
    public Cursor getMensagemAleatoria(){

        String[] strProjection = {COLUNA_TITULO,COLUNA_TEXTO,COLUNA_AUTOR};
        String strOrderBy = "RANDOM()";
        String strLimit="1";

        try {
            Log.i(LOG_TAG,"Query Todas as mensagens");
            mCursor = mDatabase.query(TABELA_MENSAGEM, //Nome da tabela
                    strProjection, // campos para pesquisa. NULL = *
                    null, // Criterios de pesquisa WHERE
                    null, // argumentos do WHERE
                    null, // Group By
                    null, // Having
                    strOrderBy, // Order By
                    strLimit); // Limit
        } finally {
            return mCursor;
        }
    }

}
