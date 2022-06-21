package com.example.fcmusicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelperUsuario extends SQLiteOpenHelper {
    private static final String USUARIO_TABLE = "USUARIO_TABLE";
    private static final String COL_ID = "ID";
    private static final String COL_USUARIO = "USUARIO";
    private static final String COL_CORREO = "CORREO";
    private static final String COL_CONTRASENA = "CONTRASENA";

    public DataBaseHelperUsuario(@Nullable Context context) {
        super(context, "usuario.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + USUARIO_TABLE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_USUARIO + " TXT, " + COL_CORREO + " TXT, " + COL_CONTRASENA + " TXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(Usuario u){
        if (existe(u.getUsuario())){
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_USUARIO,u.getUsuario());
        cv.put(COL_CORREO,u.getCorreo());
        cv.put(COL_CONTRASENA,u.getContrasenia());

        long insert = db.insert(USUARIO_TABLE, null, cv);
        db.close();
        return insert == -1? false:true;
    }

    public boolean deleteOne(Usuario u){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT FROM " + USUARIO_TABLE + " WHERE " + COL_ID + " = " + u.getId();

        Cursor cursor = db.rawQuery(query,null);
        Boolean val = cursor.moveToFirst();
        cursor.close();
        db.close();
        return val;
    }

    public boolean existe(String nombre){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USUARIO_TABLE + " WHERE " + COL_USUARIO+ " LIKE \"" + nombre + "\"";
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public boolean credencialesCorrectas(String usr, String psw){
        if(!existe(usr)){
            return false;
        }
        //psw -> hash
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USUARIO_TABLE + " WHERE " + COL_USUARIO+ " LIKE \"" + usr + "\"";
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
            return cursor.getString(3).equals(psw);
        return false;
    }
}
