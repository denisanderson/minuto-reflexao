package com.denis.minutodereflexao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_FILE = "MRPrefs";
    private static final String LOG_TAG = "MainActivity";
    TextView mTxtTitulo;
    TextView mTxtTexto;
    TextView mTxtAutor;
    Cursor mCursor;
    int intIdAnterior;
    private ShareActionProvider mShareActionProvider;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    boolean mFavChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Monta o menu de navegação
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        // Inicializa variavel auxiliar para evitar repetição da mensagem
        intIdAnterior = 0;

        // Eecuta rotina para verificar se o banco de dados do app
        // precisa ser atualizado no aparelho
        verificaDbUpdate();

    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Cria Listener para o evento
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (TextUtils.isEmpty(mTxtTexto.getText().toString())) {
                    Toast toast = Toast.makeText(MainActivity.this, R.string.toast_sem_msg, Toast.LENGTH_SHORT);
                    LinearLayout layout = (LinearLayout) toast.getView();
                    if (layout.getChildCount() > 0) {
                        TextView tv = (TextView) layout.getChildAt(0);
                        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return false;
                }
                Intent sendIntent = new Intent();
                setShareIntent(sendIntent);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, mTxtTitulo.getText());
                sendIntent.putExtra(Intent.EXTRA_TEXT, mTxtTexto.getText());
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                return true;
            }
        });
        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            // Botão Favorito é clicado
            case R.id.menu_favorito:
                if (mFavChecked) {
                    Toast toast = Toast.makeText(MainActivity.this, "CHECKED -> UNCHECKED", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    item.setIcon(R.drawable.ic_favorite_unchecked_white_24dp);
                    mFavChecked = false;
                }else {
                    if (TextUtils.isEmpty(mTxtTexto.getText().toString())) {
                        Toast toast = Toast.makeText(MainActivity.this, R.string.toast_sem_msg, Toast.LENGTH_SHORT);
                        LinearLayout layout = (LinearLayout) toast.getView();
                        if (layout.getChildCount() > 0) {
                            TextView tv = (TextView) layout.getChildAt(0);
                            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        }
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return false;
                    }else {
                        Toast toast = Toast.makeText(MainActivity.this, "UNCHECKED -> CHECKED", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        item.setIcon(R.drawable.ic_favorite_checked_white_24dp);
                        mFavChecked=true;}
                }
                return true;
        }

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    /**
     * Verifica se a versão do banco de dados informado no App
     * é o mesmo registrado no SharedPreferences no aparelho
     * Se diferente, apaga o DB local para permitir cópia da nova versão
     */
    private void verificaDbUpdate() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        int dbVersion = sharedPref.getInt(getString(R.string.shared_prefs_db_version), 0); // Retorna 0 se o valor não existir
        if (dbVersion != DbHelper.DATABASE_VERSION) {
            Log.i(LOG_TAG, "PREFS. " + dbVersion + " DATABASE_VERSION " + DbHelper.DATABASE_VERSION);
            apagaDatabase();
            atualizaSharedPrefs(sharedPref, DbHelper.DATABASE_VERSION);
        }
    }

    /**
     * Atualiza SharedPreferences com o valor da versão atual do banco de dados
     *
     * @param sharedPref      Objeto sharedPreferences aberto para leitura
     * @param databaseVersion Versão do banco de dados para escrever na Preferencia
     */
    private void atualizaSharedPrefs(SharedPreferences sharedPref, int databaseVersion) {
        SharedPreferences.Editor mEditor = sharedPref.edit();
        mEditor.putInt(getString(R.string.shared_prefs_db_version), databaseVersion);
        mEditor.commit();
    }

    /**
     * Se o arquivo de banco de dados existir, o apaga.
     */
    private void apagaDatabase() {
        DbAccess mDbAccess = DbAccess.getInstance(this);
        if (mDbAccess.checkDataBase()) {
            Log.i(LOG_TAG, "DB encontrado. Apagando arquivo.");
            try {
                this.deleteDatabase(DbHelper.DATABASE_NAME);
            } catch (SQLException e) {
                Log.i(LOG_TAG, e.getMessage());
            }
        } else {
            Log.i(LOG_TAG, "Arquivo de banco de dados não encontrado");
        }
    }

    private void sorteiaMensagem() {
        DbAccess mDbAccess = DbAccess.getInstance(this);
        mDbAccess.openRead();

        Log.i(LOG_TAG, "Executa getMensagemAleatoria()");
        mCursor = mDbAccess.getMensagemAleatoria();

        mCursor.moveToFirst();
        Log.i(LOG_TAG, "ID Anterior " + intIdAnterior + " ID Atual " + mCursor.getInt(mCursor.getColumnIndex(DbAccess.COLUNA_ID)));
        int intIdAtual = mCursor.getInt(mCursor.getColumnIndex(DbAccess.COLUNA_ID));
        if (intIdAtual == intIdAnterior) {
            Log.i(LOG_TAG, "Re-executa getMensagemAleatoria()");
            mCursor = mDbAccess.getMensagemAleatoria();
            mCursor.moveToFirst();
        }

        // Coloca o resultado na tela
        mTxtTitulo.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TITULO)));
        mTxtTexto.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TEXTO)));
        mTxtAutor.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_AUTOR)));
        intIdAnterior = intIdAtual;

        mDbAccess.close();
    }
}
