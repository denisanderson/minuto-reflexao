package com.denis.minutodereflexao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Objects;

import static com.denis.minutodereflexao.DbHelper.DATABASE_NAME;
import static com.denis.minutodereflexao.DbHelper.DATABASE_PATH;

public class DbAccess {

    public static final String COLUNA_ID = BaseColumns._ID;
    public static final String COLUNA_TITULO = "titulo";
    public static final String COLUNA_TEXTO = "texto";
    public static final String COLUNA_AUTOR = "autor";
    public static final String COLUNA_FAVORITO = "favorito";
    // Constantes para schema da tabela MENSAGEM
    private static final String TABELA_MENSAGEM = "mensagem";
    private static final String[] PROJECTION_TODAS_COLUNAS = {COLUNA_ID, COLUNA_TITULO, COLUNA_TEXTO, COLUNA_AUTOR, COLUNA_FAVORITO};

    private final static String LOG_TAG = "DbAccess";
    private static DbAccess instance;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;

    private DbAccess(Context context) {
        Log.i(LOG_TAG, "Executa construtor DbAccess");
        mDbHelper = new DbHelper(context);
    }

    /**
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
     * Abre uma conexão de leitura com o banco de dados
     */
    public void openRead() {
        Log.i(LOG_TAG, "getReadableDatabase()");
        mDatabase = mDbHelper.getReadableDatabase();
    }

    /**
     * Abre uma conexão de escrita com o banco de dados
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
        return checkDB != null;
    }

    public Integer atualizaCampoFavorito(int intId, String strValor) {
        // Valores a atualizar
        ContentValues valores = new ContentValues();
        valores.put(COLUNA_FAVORITO, strValor);
        // Qual linha a alterar (cláusula WHERE)
        String strSelection = COLUNA_ID + " = ?";
        String[] strSelectionArgs = {Objects.toString(intId)};
        // Registrar quantidade de linhas atualizadas após a atualização
        int linhasAtualizadas = 0;

        try {
            Log.i(LOG_TAG, "Submetendo update: (" + TABELA_MENSAGEM + ", " + valores + ", " + strSelection + ", " + Objects.toString(intId) + ")");
            linhasAtualizadas = mDatabase.update(TABELA_MENSAGEM, //Nome da tabela
                    valores, //Valores a atualizar
                    strSelection, //WHERE
                    strSelectionArgs); // argumentos do WHERE
        } catch (Exception e) {
            Log.i(LOG_TAG, "Registrou a seguinte exceção:");
            Log.i(LOG_TAG, e.getMessage());
        }
        Log.i(LOG_TAG, "Qtde. de registros atualizados: " + linhasAtualizadas);
        return linhasAtualizadas;
    }

    public Cursor obtemListaMsgsFavoritas() {
        String strSelection = COLUNA_FAVORITO + " = 1";
        String[] strProjection = {COLUNA_ID, COLUNA_TITULO};

        try {
            Log.i(LOG_TAG, "Query Mensagens Favoritas iniciou");
            mCursor = mDatabase.query(TABELA_MENSAGEM, //Nome da tabela
                    strProjection, // Retorna o ID e titulo da mensagem
                    strSelection, // Critérios de pesquisa WHERE
                    null, // argumentos do WHERE
                    null, // Group By
                    null, // Having
                    null, // Order By
                    null); // Limit
        } catch (Exception e) {
            Log.i(LOG_TAG, "Registrou a seguinte exceção:");
            Log.i(LOG_TAG, e.getMessage());
        } finally {
            Log.i(LOG_TAG, "Query Mensagens Favoritas terminou");
            return mCursor;
        }
    }

    /**
     * @return Um cursor com o resultado aleatorio
     */
    public Cursor getMsgAleatoria() {
        String strOrderBy = "RANDOM()";
        String strLimit = "1";
        try {
            Log.i(LOG_TAG, "Query Mensagem Aleatoria iniciou");
            mCursor = mDatabase.query(TABELA_MENSAGEM, //Nome da tabela
                    PROJECTION_TODAS_COLUNAS, // campos para pesquisa. NULL = *
                    null, // Critérios de pesquisa WHERE
                    null, // argumentos do WHERE
                    null, // Group By
                    null, // Having
                    strOrderBy, // Order By
                    strLimit); // Limit
        } catch (Exception e) {
            Log.i(LOG_TAG, "Registrou a seguinte exceção:");
            Log.i(LOG_TAG, e.getMessage());
        } finally {
            Log.i(LOG_TAG, "Query Mensagem Aleatoria terminou");
            return mCursor;
        }
    }

    /**
     * @return Um cursor com uma mensagem cujo ID foi informado
     * como parametro
     */
    public Cursor obtemMsgFavorita(int intId) {
        // Qual mensagem a buscar (cláusula WHERE)
        String strSelection = COLUNA_ID + " = ?";
        String[] strSelectionArgs = {Objects.toString(intId)};
        try {
            Log.i(LOG_TAG, "Query Mensagen Favorita buscando mensagem com ID: " + intId);
            mCursor = mDatabase.query(TABELA_MENSAGEM, //Nome da tabela
                    PROJECTION_TODAS_COLUNAS, // campos para pesquisa. NULL = *
                    strSelection, // Critérios de pesquisa WHERE
                    strSelectionArgs, // argumentos do WHERE
                    null, // Group By
                    null, // Having
                    null, // Order By
                    null); // Limit
        } catch (Exception e) {
            Log.i(LOG_TAG, "Registrou a seguinte exceção:");
            Log.i(LOG_TAG, e.getMessage());
        } finally {
            Log.i(LOG_TAG, "Query Mensagem Favorita terminou");
            return mCursor;
        }
    }

/*
     // Copia Banco de Dados da pasta assets/databases
    public void copiaDatabase(Context context) {
        try {
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
            Log.i(LOG_TAG, "Copia do arquivo concluida");
        } catch (IOException e) {
            Log.i(LOG_TAG, "Erro copiando o arquivo de banco de dados: " + e.getCause());
            throw new Error("Ocorreu um erro copiando o arquivo de banco de dados: " + e.getMessage());
        }
    }
*/

}
