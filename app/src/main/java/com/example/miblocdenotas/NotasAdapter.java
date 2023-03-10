package com.example.miblocdenotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NotasAdapter extends ArrayAdapter<Nota> {
    private final Context context;
    private final int itemLayoutId;
    private final ArrayList<Nota> notas;

    public NotasAdapter(@NonNull Context context, int resource, @NonNull List<Nota> objects) {
        super(context, resource, objects);
        this.context = context;
        this.itemLayoutId = resource;
        this.notas = (ArrayList<Nota>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(this.itemLayoutId, parent, false);

        Nota notaActual = notas.get(position);

        TextView titulo = listItem.findViewById(R.id.tvTituloNota);
        titulo.setText(notaActual.getNota());

        TextView fecha = (TextView) listItem.findViewById(R.id.tvFechaNota);
        fecha.setText(notaActual.getFechaCreacion());

        return listItem;
    }
}
