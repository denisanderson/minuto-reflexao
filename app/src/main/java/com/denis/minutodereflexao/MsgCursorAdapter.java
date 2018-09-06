package com.denis.minutodereflexao;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MsgCursorAdapter extends RecyclerView.Adapter<MsgCursorAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    MsgCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.line_item_view_favoritas, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder lineHolder, int posicao) {
        if (!mCursor.moveToPosition(posicao)) {
            return;
        }

        final String titulo = mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TITULO));
        final String id = mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_ID));

        lineHolder.mTituloText.setText(titulo);

        lineHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Colocar aqui código para abrir tela com a mensagem completa
                Toast.makeText(mContext, "ID: " + id + " - " + titulo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

/*
    public void swapCursor(Cursor novoCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = novoCursor;

        if (mCursor != null) {
            notifyDataSetChanged();
        }
    }
*/

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTituloText;
        ImageView mFavoritoImagem;
        RelativeLayout mRelativeLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTituloText = itemView.findViewById(R.id.line_txt_titulo);
            mFavoritoImagem = itemView.findViewById(R.id.line_img_favorita);
            mRelativeLayout = itemView.findViewById(R.id.relativeLayout); // Objeto será usado para criar area clicável para ação de abrir a mensagem
        }
    }
}
