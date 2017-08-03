package com.denis.minutodereflexao;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Constante para uso nas mensagens de log para facilitar localização
    private final static String LOG_TAG = "Main";
    TextView mTxtTitulo;
    TextView mTxtTexto;
    TextView mTxtAutor;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnSorteia = (FloatingActionButton) findViewById(R.id.fab_sorteia);
        btnSorteia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sorteiaMensagem();
            }
        });

        // Associa as variaveis aos objetos na tela
        mTxtTitulo = (TextView) findViewById(R.id.txt_titulo);
        mTxtTexto = (TextView) findViewById(R.id.txt_texto);
        mTxtAutor = (TextView) findViewById(R.id.txt_autor);

    }

    private void sorteiaMensagem() {

        DbAccess mDbAccess = DbAccess.getInstance(this);
        Log.i(LOG_TAG, "Conecta no banco");
        mDbAccess.open();

        Log.i(LOG_TAG, "Executa getMensagemAleatoria()");
        mCursor = mDbAccess.getMensagemAleatoria();
        mCursor.moveToFirst();

        Log.i(LOG_TAG,"ID: " + mCursor.getInt(mCursor.getColumnIndex(DbAccess.COLUNA_ID)));

        // Coloca o resultado na tela
        Log.i(LOG_TAG, "Titulo: " + mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TITULO)));
        mTxtTitulo.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TITULO)));

        Log.i(LOG_TAG, "Texto inserido");
        mTxtTexto.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TEXTO)));

        Log.i(LOG_TAG, "Autor: " + mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_AUTOR)));
        mTxtAutor.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_AUTOR)));

        Log.i(LOG_TAG, "Fecha conexão com o banco");
        mDbAccess.close();

    }
}
