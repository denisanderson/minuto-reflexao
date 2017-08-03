package com.denis.minutodereflexao;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.denis.minutodereflexao.DbAccess;

public class MainActivity extends AppCompatActivity {

    // Constante para uso nas mensagens de log para facilitar localização
    private final static String LOG_TAG = "Main";

    TextView mTxtTitulo;
    TextView mTxtTexto;
    TextView mTxtAutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnSorteia = (FloatingActionButton) findViewById(R.id.fab_sorteia);
        btnSorteia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sorteia nova mensagem", Toast.LENGTH_SHORT).show();
            }
        });

        // Associa as variaveis aos objetos na tela
        mTxtTitulo = (TextView) findViewById(R.id.txt_titulo);
        mTxtTexto = (TextView) findViewById(R.id.txt_texto);
        mTxtAutor = (TextView) findViewById(R.id.txt_autor);

        DbAccess mDbAccess=DbAccess.getInstance(this);
        mDbAccess.open();
        Cursor cursor=mDbAccess.getTodasMensagens();
        mDbAccess.close();

    }

}
