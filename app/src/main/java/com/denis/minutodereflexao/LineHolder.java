package com.denis.minutodereflexao;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LineHolder extends RecyclerView.ViewHolder {
    public TextView txtTitulo;
    public ImageView imgCheck;

    public LineHolder(@NonNull View itemView) {
        super(itemView);
        txtTitulo = itemView.findViewById(R.id.line_txt_titulo);
        imgCheck = itemView.findViewById(R.id.line_img_favorita);
    }
}
