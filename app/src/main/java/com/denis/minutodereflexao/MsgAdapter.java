package com.denis.minutodereflexao;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class MsgAdapter extends RecyclerView.Adapter<LineHolder> {
    Cursor mCursor;

    public MsgAdapter(Cursor dataset) {
        mCursor = dataset;
    }

    @NonNull
    @Override
    public LineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.line_item_view_favoritas, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineHolder lineHolder, int i) {
        // TODO: Corrigir: Somente o primeiro registro está sendo apresentado na tela
        lineHolder.txtTitulo.setText(mCursor.getString(mCursor.getColumnIndex(DbAccess.COLUNA_TITULO)));
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }
}
