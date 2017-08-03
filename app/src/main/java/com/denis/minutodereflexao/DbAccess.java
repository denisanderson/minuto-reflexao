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
     * Construtor da classe
     *
     * @param context
     */
    private DbAccess(Context context) {
        Log.i(LOG_TAG, "Executa contrutor DbAccess");
        mDbHelper = new DbHelper(context);
    }

    /**
     * Retorna uma instância de DbAccess.
     *
     * @param context Contexto da chamada da função
     * @return Instância de DbAccess
     */
    public static DbAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DbAccess(context);
        }
        Log.i(LOG_TAG, "getInstance");
        return instance;
    }

    /**
     * Abre uma conexão com o banco de dados
     */
    public void open() {
        Log.i(LOG_TAG, "getReadableDatabase()");
        mDatabase = mDbHelper.getReadableDatabase();
    }

    /**
     * Fecha a conexão com o banco de dados
     */
    public void close() {
        if (mDatabase != null) {
            Log.i(LOG_TAG, "closeDatabase");
            mDatabase.close();
        }
    }

    /**
     * Consulta todos os dados das colunas especificadas em strProjetion
     *
     * @return Um cursor com o resultado
     */
    public Cursor getTodasMensagens() {

        String[] strProjection = {COLUNA_TITULO, COLUNA_TEXTO, COLUNA_AUTOR};
        try {
            Log.i(LOG_TAG, "Query Todas as mensagens");
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
     * Consulta aleatória na tabela. Returna 1 resultado.
     *
     * @return Um cursor com o resultado
     */
    public Cursor getMensagemAleatoria() {

        String[] strProjection = {COLUNA_TITULO, COLUNA_TEXTO, COLUNA_AUTOR};
        String strOrderBy = "RANDOM()";
        String strLimit = "1";

        try {
            Log.i(LOG_TAG, "Query Todas as mensagens");
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
