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
        if (dbInstance == null) {
            dbInstance = new DBUtil(context);
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableNotas = "CREATE TABLE notas (" +
                "id CHAR(36) PRIMARY KEY ," +
                "titulo TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "data TEXT)";
        db.execSQL(tableNotas);

        String tableLixeira = "CREATE TABLE lixeira (" +
                "id CHAR(36) PRIMARY KEY ," +
                "titulo TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "data TEXT)";
        db.execSQL(tableLixeira);

        String tableLogin = "CREATE TABLE login (" +
                "id_token CHAR (36) PRIMARY KEY," +
                "nome_user TEXT," +
                "data TEXT)";
        db.execSQL(tableLogin);

        String tableUsuario = "CREATE TABLE usuario (" +
                "id INTEGER PRIMARY KEY," +
                "usuario TEXT NOT NULL," +
                "senha TEXT NOT NULL)";
        db.execSQL(tableUsuario);

        String userDefault = "INSERT INTO usuario (" +
                "id, usuario, senha) VALUES (" +
                "1, 'admin', '123')";
        db.execSQL(userDefault);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {

        }
    }
}
