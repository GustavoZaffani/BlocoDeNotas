package br.com.alura.blocodenotas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtil extends SQLiteOpenHelper {

    private static DBUtil dbInstance;

    public DBUtil(Context context) {
        super(context, "Notas", null, 1);
    }

    public static DBUtil getInstance(Context context) {
        if(dbInstance == null) {
            dbInstance = new DBUtil(context);
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
