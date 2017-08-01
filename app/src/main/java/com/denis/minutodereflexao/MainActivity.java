package com.denis.minutodereflexao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    // Constante para uso nas mensagens de log para facilitar localização
    private final static String LOG_TAG = "MR_Main";

    TextView mTxtTitulo;
    TextView mTxtTexto;
    TextView mTxtAutor;

    MrDbHelper mDbHelper;
    SQLiteDatabase mDatabase;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnSorteia = (FloatingActionButton) findViewById(R.id.fab_sorteia);
        btnSorteia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Nova mensagem", Toast.LENGTH_SHORT).show();
                //sorteiaMensagem();
            }
        });

        // Associa as variaveis aos objetos na tela
        mTxtTitulo = (TextView) findViewById(R.id.txt_titulo);
        mTxtTexto = (TextView) findViewById(R.id.txt_texto);
        mTxtAutor = (TextView) findViewById(R.id.txt_autor);

        mDbHelper = new MrDbHelper(this);
    }

    private void sorteiaMensagem() {
        mDatabase = mDbHelper.getReadableDatabase();

        String[] strProjection = {
                MrDbHelper.COLUNA_TITULO,
                MrDbHelper.COLUNA_TEXTO,
                MrDbHelper.COLUNA_AUTOR};

        String strOrderBy = "RANDOM()";
        String strLimit = "1";

        Log.i(LOG_TAG, "Query: " + MrDbHelper.TABELA_MENSAGEM + ", " + Arrays.toString(strProjection)
                + ", " + strOrderBy +", "+ strLimit);

        try {
            mCursor = mDatabase.query(MrDbHelper.TABELA_MENSAGEM,
                    strProjection,
                    null,
                    null,
                    null,
                    null,
                    strOrderBy,
                    strLimit);
        } finally {
            mDatabase.close();
        }
    }
}
