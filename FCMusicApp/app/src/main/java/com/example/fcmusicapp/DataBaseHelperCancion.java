package com.example.fcmusicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperCancion extends SQLiteOpenHelper {

    private static final String CANCION_TABLE = "CANCION_TABLE";
    private static final String COL_NOMBRE = "NOMBRE";
    private static final String COL_DURACION = "DURACION";
    private static final String COL_FAVORITO = "FAVORITO";
    private static final String COL_ID = "ID";

    public DataBaseHelperCancion(@Nullable Context context) {
        super(context, "cancion.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + CANCION_TABLE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NOMBRE + " TXT, " + COL_DURACION + " INTEGER, " + COL_FAVORITO + " BOOL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(Cancion c){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NOMBRE,c.getNombre());
        cv.put(COL_DURACION,c.getDuracion());
        cv.put(COL_FAVORITO,c.getLike());

        //checamos que no se encuentre ya en la base
        String queryString = "SELECT * FROM " + CANCION_TABLE + " WHERE " + COL_NOMBRE + " LIKE \""+ c.getNombre() + "\"";
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false;
        }
        long insert = db.insert(CANCION_TABLE, null, cv);
        cursor.close();
        db.close();
        return insert == -1? false:true;
    }

    public boolean deleteOne(Cancion c){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT FROM " + CANCION_TABLE + " WHERE " + COL_ID + " = " + c.getId();

        Cursor cursor = db.rawQuery(query,null);
        Boolean val = cursor.moveToFirst();
        cursor.close();
        db.close();
        return val;
    }

    public List<Cancion> getTodas(){
        List <Cancion> l = new ArrayList<>();

        String queryString = "SELECT * FROM " + CANCION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do{
                int cancionID = cursor.getInt(0);
                String cancionNombre = cursor.getString(1);
                int cancionDuracion = cursor.getInt(2);
                boolean cancionLike = cursor.getInt(3) == 1?true:false;

                Cancion c = new Cancion(cancionID,cancionNombre,cancionDuracion,cancionLike);
                l.add(c);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return l;
    }

    public List<Cancion> busquedaCancion(String nombre){
        List<Cancion> l = new ArrayList<>();
        String queryString = "SELECT * FROM " + CANCION_TABLE + " WHERE " + COL_NOMBRE + " LIKE \"%"+ nombre + "%\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do{
                int cancionID = cursor.getInt(0);
                String cancionNombre = cursor.getString(1);
                int cancionDuracion = cursor.getInt(2);
                boolean cancionLike = cursor.getInt(3) == 1?true:false;

                Cancion c = new Cancion(cancionID,cancionNombre,cancionDuracion,cancionLike);
                l.add(c);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return l;
    }

    public Boolean likeSong(String song){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CANCION_TABLE + " WHERE " + COL_NOMBRE + " = \"" + song + "\"";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        boolean fav = cursor.getInt(3) == 1?true:false;
        if(!fav)
            query = "UPDATE " + CANCION_TABLE + " SET " + COL_FAVORITO + " = 1 WHERE " + COL_NOMBRE + " = \"" + song + "\"";
        else
            query = "UPDATE " + CANCION_TABLE + " SET " + COL_FAVORITO + " = 0 WHERE " + COL_NOMBRE + " = \"" + song + "\"";
        db.execSQL(query);
        return fav;
    }

    public List<Cancion> getFavs(){
        List <Cancion> l = new ArrayList<>();

        String queryString = "SELECT * FROM " + CANCION_TABLE + " WHERE " + COL_FAVORITO + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do{
                int cancionID = cursor.getInt(0);
                String cancionNombre = cursor.getString(1);
                int cancionDuracion = cursor.getInt(2);

                Cancion c = new Cancion(cancionID,cancionNombre,cancionDuracion,true);
                l.add(c);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return l;
    }

    public boolean getLike(String song){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CANCION_TABLE + " WHERE " + COL_NOMBRE + " = \"" + song + "\"";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        db.close();
        return cursor.getInt(3) == 1?true:false;
    }
}
