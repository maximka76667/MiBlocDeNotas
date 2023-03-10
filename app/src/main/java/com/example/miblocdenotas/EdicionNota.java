package com.example.miblocdenotas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EdicionNota extends Activity {

    /* Variables para acceder al texto de la nota y botones */
    EditText etNota;
    Button bGrabar;
    Button bCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_nota);
        etNota = findViewById(R.id.etNota);
        bGrabar = findViewById(R.id.bGrabar);
        bCancelar = findViewById(R.id.bCancelar);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String textoNotaOriginal = intent.getStringExtra("nota");
        String action = intent.getAction();

        etNota.setText(textoNotaOriginal);

        TextView tvTitulo = this.findViewById(R.id.tvTituloMainActivity);
        tvTitulo.setText(action);

        bGrabar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentActualizar = new Intent();
                intentActualizar.putExtra("id", id);
                intentActualizar.putExtra("nota", etNota.getText().toString());

                setResult(RESULT_OK, intentActualizar);
                finish();
            }
        });

        bCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
