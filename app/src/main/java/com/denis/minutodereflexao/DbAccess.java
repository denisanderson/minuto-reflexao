package com.denis.minutodereflexao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.denis.minutodereflexao.DbHelper.DATABASE_NAME;
import static com.denis.minutodereflexao.DbHelper.DATABASE_PATH;

public class DbAccess {

    // Constantes para schema da tabela MENSAGEM
    public static final String TABELA_MENSAGEM = "mensagem";
    public static final String COLUNA_ID = BaseColumns._ID;
    public static final String COLUNA_TITULO = "titulo";
    public static final String COLUNA_TEXTO = "texto";
    public static final String COLUNA_AUTOR = "autor";
    public static final String[] PROJECTION_TODAS_COLUNAS = {COLUNA_ID, COLUNA_TITULO, COLUNA_TEXTO, COLUNA_AUTOR};

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
    public void openRead() {
        Log.i(LOG_TAG, "getReadableDatabase()");
        mDatabase = mDbHelper.getReadableDatabase();
    }

    /**
     * Abre uma conexão com o banco de dados
     */
    public void openWrite() {
        Log.i(LOG_TAG, "getWritableDatabase()");
        mDatabase = mDbHelper.getWritableDatabase();
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

        try {
            Log.i(LOG_TAG, "Query Todas as mensagens");
            mCursor = mDatabase.query(TABELA_MENSAGEM, //Nome da tabela
                    PROJECTION_TODAS_COLUNAS, // campos para pesquisa. NULL = *
                    null, // Criterios de pesquisa WHERE
                    null, // argumentos do WHERE
                    null, // Group By
                    null, // Having
                    null); // Order By
        } catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
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

        String strOrderBy = "RANDOM()";
        String strLimit = "1";

        try {
            Log.i(LOG_TAG, "Query Mensagem Aleatoria iniciou");
            mCursor = mDatabase.query(TABELA_MENSAGEM, //Nome da tabela
                    PROJECTION_TODAS_COLUNAS, // campos para pesquisa. NULL = *
                    null, // Criterios de pesquisa WHERE
                    null, // argumentos do WHERE
                    null, // Group By
                    null, // Having
                    strOrderBy, // Order By
                    strLimit); // Limit
        } catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        } finally {
            Log.i(LOG_TAG, "Query Mensagem Aleatoria terminou");
            return mCursor;
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {
        String mPath;
        SQLiteDatabase checkDB = null;
        try {
            mPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READONLY);
            if (checkDB != null) {
                Log.i(LOG_TAG, "Banco de dados EXISTE: " + mPath);
            }
        } catch (SQLiteException e) {
            Log.i(LOG_TAG, "Banco de dados NÃO existe. " + e.getMessage());
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copia Banco de Dados da pasta assets/databases
     */
    public void copiaDatabase(Context context) {
        try {
            //Context mContext = MyApp.getAppContext();
            InputStream mInput = context.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
            Log.i(LOG_TAG, "Cópia do arquivo concluída");
        } catch (IOException e) {
            Log.i(LOG_TAG, "Erro copiando o arquivo de banco de dados: " + e.getCause());
            throw new Error("Ocorreu um erro copiando o arquivo de banco de dados: " + e.getMessage());
        }
    }

}
