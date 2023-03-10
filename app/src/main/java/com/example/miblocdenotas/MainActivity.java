package com.example.miblocdenotas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class MainActivity extends Activity {
    /* Constantes para lanzar la Activiy EdicionNota */
    final int PETICION_EDIT = 1;
    final int PETICION_INSERT = 2;
    ListView listViewNotas;
    ImageButton imageButtonAnhadir;
    SimpleCursorAdapter sc_adaptador; // Cursor para el ListView
    BDNotas bdNotas; // SQLiteOpenHelper de la base de datos de las notas
    Cursor notasCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewNotas = findViewById(R.id.listViewNotas);
        imageButtonAnhadir = findViewById(R.id.imageButtonAnhadir);
        registerForContextMenu(listViewNotas);


        /* COMPLETAR. Obtener una instancia de BDNotas,
         * usando el método getListadoNotas,
         * crear el SimpleCursorAdapter y asignarlo a variable sc_adaptador y asignárselo al
         * ListView para visualizar las notas existentes en la base de datos
         */

        bdNotas = new BDNotas(MainActivity.this);

        String[] columnas = {"NOTA_CORTADA", "FECHA_CREACION"};
        int[] controles = {R.id.tvTituloNota, R.id.tvFechaNota};

        notasCursor = bdNotas.getListadoNotas();

        sc_adaptador = new SimpleCursorAdapter(this, R.layout.item_lista_notas, notasCursor, columnas, controles);

        listViewNotas.setAdapter(sc_adaptador);

        listViewNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this, EdicionNota.class);
                intent.putExtra("id", cursor.getInt(1));
                intent.putExtra("nota", cursor.getString(2));
                intent.setAction("Editando");
                startActivityForResult(intent, PETICION_EDIT);

                /* COMPLETAR
                 * lanzar la Activity EdicionNota utilizando la constante PETICION_EDIT
                 * los datos que le enviaremos sera el texto de la nota e ID_NOTA
                 */
            }
        });

        imageButtonAnhadir.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EdicionNota.class);
                intent.setAction("Insertando");
                startActivityForResult(intent, PETICION_INSERT);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        String textoNota = data.getStringExtra("nota");

        if (requestCode == PETICION_EDIT) {
            int idNota = data.getIntExtra("id", 0);

            if (!bdNotas.actualizarNota(idNota, textoNota)) {
                Toast.makeText(this, "Error al modificar", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Nota modificada", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == PETICION_INSERT) {
            if (!bdNotas.insertarNota(textoNota)) {
                Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Nota insertada", Toast.LENGTH_SHORT).show();
        }

        sc_adaptador.changeCursor(bdNotas.getListadoNotas());
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listViewNotas) {
            getMenuInflater().inflate(R.menu.menu_contextual_listview, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
		/* Obtenemos el rowid (_id) del cursor con la información
		   de contexto que nos da el item de menú pulsado, que
		   convertimos en información de adaptador
		   AdapterContextMenuInfo
		*/
        if (item.getItemId() == R.id.menu_contextual_listvew_borrar_nota) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Estás seguro que quieres borrar está nota?");

            dialog.setPositiveButton("Sí.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    long rowid = info.id;

                  /*
                    Método borrarNota devuelve true si nota ha borrado correctamente
                    y false si ocurre un error
                  */

                    if (!bdNotas.borrarNota(rowid)) {
                        Toast.makeText(MainActivity.this, "Error al borrar", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(MainActivity.this, "Nota ha borrado", Toast.LENGTH_SHORT).show();
                    sc_adaptador.changeCursor(bdNotas.getListadoNotas());
                }
            });

            dialog.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();


        }

        return super.onContextItemSelected(item);
    }

}
