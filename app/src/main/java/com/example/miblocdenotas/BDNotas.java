package com.example.miblocdenotas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BDNotas extends SQLiteOpenHelper {
    final static String FICH_BD = "notas.sqlite";
    final static int VERSION = 2;
    final static String TABLE_NAME = "TABLA_NOTAS";

    SQLiteDatabase db = null;
    Context contexto;

    public BDNotas(@Nullable Context context) {
        super(context, FICH_BD, null, VERSION);
        contexto = context;
    }

    /* Metodo para obtener un String con la fecha y hora actuales
     * en formato correcto de base de datos: anho-mes-dia hora:minuto:segundos*/
    public static String getStringFechaHoraActual() {
        GregorianCalendar calendarioHoy = new GregorianCalendar();
        Date fechaHoraActual = calendarioHoy.getTime();

        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formateador.format(fechaHoraActual);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql;
        sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                " ID_NOTA INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                " NOTA TEXT NOT NULL," +
                " FECHA_CREACION DATETIME NOT NULL )";

        sqLiteDatabase.execSQL(sql);
        onUpgrade(sqLiteDatabase, 1, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void abreBD() {
        if (db == null) {
            db = this.getReadableDatabase();
        }
    }

    public void cierraBD() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    /* Metodo para obtener las notas de la base de datos */
    public Cursor getListadoNotas() {
        abreBD();
        Cursor c = db.rawQuery("SELECT rowid as _id, ID_NOTA, NOTA, CASE WHEN length(NOTA)<15 THEN NOTA ELSE (substr(NOTA,1,15)  || '...') END AS NOTA_CORTADA, FECHA_CREACION, strftime('%d/%m/%Y %H:%M:%S',FECHA_CREACION) AS FECHA_CREACION_FORMATEADA FROM TABLA_NOTAS order by ID_NOTA DESC", null);
        /*  Las columnas que obtenemos son:
         *  _id : Identificadador de fila para el SimpleCursorAdapter
         *  ID_NOTA: Valor clave entero
         *  NOTA: El texto de la nota
         *  NOTA_CORTADA: Los primeros 15 caracteres de la nota
         *  FECHA_CREACION: Fecha en que se creo la nota
         *  FECHA_CREACION_FORMATEADA: FECHA_CREACION formateada en dia/mes/anho hora:minuto:segundos
         */
        return c;

    }

    public boolean insertarNota(String nota) {
        abreBD();

        ContentValues values = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String actualDate = sdf.format(timestamp);

        values.put("NOTA", nota);
        values.put("FECHA_CREACION", actualDate);

        long rowID = db.insert(TABLE_NAME, null, values);

        return rowID != -1;
    }

    public boolean actualizarNota(int idNota, String nota) {
        abreBD();

        if (idNota == 0) {
            return false;
        }

        ContentValues newValues = new ContentValues();
        newValues.put("NOTA", nota);

        String[] conditionParams = {idNota + ""};

        int numFilas = db.update(TABLE_NAME, newValues, "ID_NOTA=?",
                conditionParams);

        return numFilas > 0;
    }

    public boolean borrarNota(long rowId) {
        String[] conditionParams = {rowId + ""};

        int numFilasBorradas = db.delete(TABLE_NAME, "rowid=?",
                conditionParams);

        return numFilasBorradas > 0;
    }
}
