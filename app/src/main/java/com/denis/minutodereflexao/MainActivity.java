package com.denis.minutodereflexao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Declaracao de variaveis globais
    public static final String PREFS_FILE = "MRPrefs";
    private static final String LOG_TAG = "Main";
    TextView mTxtTitulo;
    TextView mTxtTexto;
    TextView mTxtAutor;
    Cursor mCursor;
    int mIdAnterior;
    boolean mFavChecked = false;
    NavigationView mNavigationView;
    DrawerLayout mDrawer;
    MenuItem mShareItem;
    ActionMenuItemView mFavoritaItem;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sorteiaMensagem();
/*
                // Cria uma barra preta na parte de baixo com uma mensagem
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
            }
        });

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Associa as variaveis aos objetos na tela
        mTxtTitulo = findViewById(R.id.txt_titulo);
        mTxtTexto = findViewById(R.id.txt_texto);
        mTxtAutor = findViewById(R.id.txt_autor);

        // Inicializa variavel auxiliar para evitar repetição da mensagem
        mIdAnterior = 0;

        // Executa rotina para verificar se o banco de dados do app
        // precisa ser atualizado no aparelho
        verificaDbUpdate();

        // Verifica se a tela é aberta a partir da seleção de uma mensagem favorita.
        // Em caso positivo, a intent recebe o ID da mensagem selecionada e
        // coloca o texto da mensagem na tela
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String id = intent.getExtras().getString("msgId");
            getMsgFavorita(id);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Fetch and store ShareActionProvider
        mFavoritaItem = findViewById(R.id.action_favorito);
        mShareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareItem);

        // Cria Listener para o evento
        mShareItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(LOG_TAG, "Clicou Share Listener");
                if (TextUtils.isEmpty(mTxtTexto.getText().toString())) {
                    mostraMsgCentralizada(R.string.toast_sem_msg_share);
                    return false;
                }
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                setShareIntent(sendIntent);
                //sendIntent.setAction(Intent.ACTION_SEND);
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
        switch (item.getItemId()) {

            case R.id.action_favorito:
                Log.i(LOG_TAG, "Clicou Favorito");
                if (mFavChecked) {
                    item.setIcon(R.mipmap.ic_favorite_unchecked_white);
                    mFavChecked = false;
                    atualizaFavorito(mFavChecked);
                    Toast.makeText(MainActivity.this, R.string.toast_msg_del_favorita, Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Clicou no botão para remover dos favoritos");
                } else {
                    if (TextUtils.isEmpty(mTxtTexto.getText().toString())) {
                        mostraMsgCentralizada(R.string.toast_sem_msg_favorita);
                        return false;
                    } else {
                        item.setIcon(R.mipmap.ic_favorite_checked_white);
                        mFavChecked = true;
                        atualizaFavorito(mFavChecked);
                        Toast.makeText(MainActivity.this, R.string.toast_msg_add_favorita, Toast.LENGTH_SHORT).show();
                        Log.i(LOG_TAG, "Clicou no botão para incluir nos favoritos");
                    }
                }
                return true;

            case R.id.action_share:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_inicio:
                Intent inicio = new Intent(this, MainActivity.class);
                startActivity(inicio);
                break;
            case R.id.nav_favorita:
                Intent fav = new Intent(this, FavoritasActivity.class);
                startActivity(fav);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void mostraMsgCentralizada(int mInt) {
        Toast toast = Toast.makeText(MainActivity.this, mInt, Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        if (layout.getChildCount() > 0) {
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void verificaDbUpdate() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        int dbVersion = sharedPref.getInt(getString(R.string.shared_prefs_db_version), 0); // Retorna 0 se o valor não existir

        // TODO: Revisar rotina para não remover favoritos ao atualizar BD
        if (dbVersion != DbHelper.DATABASE_VERSION) {
            Log.i(LOG_TAG, "PREFS. " + dbVersion + " DATABASE_VERSION " + DbHelper.DATABASE_VERSION);
            apagaDatabase();
            atualizaSharedPrefs(sharedPref, DbHelper.DATABASE_VERSION);
        }
    }

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

    private void atualizaSharedPrefs(SharedPreferences sharedPref, int databaseVersion) {
        SharedPreferences.Editor mEditor = sharedPref.edit();
        mEditor.putInt(getString(R.string.shared_prefs_db_version), databaseVersion);
        mEditor.apply();
    }

    @SuppressLint("RestrictedApi")
    private void sorteiaMensagem() {
        DbAccess dbaccess = DbAccess.getInstance(this);
        dbaccess.openRead();

        Log.i(LOG_TAG, "Executa getMsgAleatoria()");
        mCursor = dbaccess.getMsgAleatoria();

        mCursor.moveToFirst();
        Log.i(LOG_TAG, "ID Anterior " + mIdAnterior + " ID Atual " + mCursor.getInt(mCursor.getColumnIndex(DbAccess.COLUNA_ID)));
        int idatual = mCursor.getInt(mCursor.getColumnIndex(DbAccess.COLUNA_ID));
        if (idatual == mIdAnterior) {
            Log.i(LOG_TAG, "Re-executa getMsgAleatoria()");
            mCursor = dbaccess.getMsgAleatoria();
            mCursor.moveToFirst();
        }

        // Coloca o resultado na tela
        mTxtTitulo.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TITULO)));
        mTxtTexto.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TEXTO)));
        mTxtAutor.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_AUTOR)));

        Log.i(LOG_TAG, "Valor coluna Favorito = " + mCursor.getString(mCursor.getColumnIndex((DbAccess.COLUNA_FAVORITO))));

        // Identifica objeto do menu para mudar o botão de favorito, dependendo do valor do favorito no db
        ActionMenuItemView item = findViewById(R.id.action_favorito);
        if (Objects.equals(mCursor.getString(mCursor.getColumnIndex((DbAccess.COLUNA_FAVORITO))), "0")) {
            Log.i(LOG_TAG, "Mensagem não está nos favoritos");
            item.setIcon(getResources().getDrawable(R.mipmap.ic_favorite_unchecked_white));
            mFavChecked = false;
        } else {
            Log.i(LOG_TAG, "Mensagem está nos favoritos");
            item.setIcon(getResources().getDrawable(R.mipmap.ic_favorite_checked_white));
            mFavChecked = true;
        }

        mIdAnterior = idatual;
        dbaccess.close();
    }

    private void atualizaFavorito(Boolean bFavChecked) {
        DbAccess dbaccess = DbAccess.getInstance(this);
        dbaccess.openWrite();

        int resultado;

        if (bFavChecked) {
            resultado = dbaccess.atualizaCampoFavorito(mIdAnterior, "1");
        } else {
            resultado = dbaccess.atualizaCampoFavorito(mIdAnterior, "0");
        }

        Log.i(LOG_TAG, "Registros atualizados no BD: " + resultado);
        Toast.makeText(this, resultado + " registro atualizado", Toast.LENGTH_SHORT).show();

        dbaccess.close();
    }

    @SuppressLint("RestrictedApi")
    private void getMsgFavorita(String id) {
        DbAccess dbaccess = DbAccess.getInstance(this);
        dbaccess.openRead();

        Log.i(LOG_TAG, "Executa getMsgFavorita()");
        Log.i(LOG_TAG, "ID enviado: " + id);
        mCursor = dbaccess.obtemMsgFavorita(id);

        mCursor.moveToFirst();
        // Coloca o resultado na tela
        mTxtTitulo.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TITULO)));
        mTxtTexto.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TEXTO)));
        mTxtAutor.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_AUTOR)));

        mIdAnterior = Integer.parseInt(id);

        // TODO: Ícone de favorito não está ficando preenchido
        //mFavoritaItem.setIcon(getResources().getDrawable(R.mipmap.ic_favorite_checked_white));

        dbaccess.close();
    }

}
