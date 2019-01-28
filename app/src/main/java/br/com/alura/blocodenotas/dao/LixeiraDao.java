package br.com.alura.blocodenotas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LixeiraDao {

    private Context context;
    private SQLiteDatabase database;

    public LixeiraDao(Context context) {
         this.context = context;
         this.database = DBUtil.getInstance(context).getWritableDatabase();

    }



}
