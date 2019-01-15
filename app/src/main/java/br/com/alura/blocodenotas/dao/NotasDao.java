package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.blocodenotas.model.Nota;
import br.com.alura.blocodenotas.util.DateUtil;

public class NotasDao extends SQLiteOpenHelper {

    public NotasDao(Context context) {
        super(context, "Notas", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE notas (" +
                "id INT PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "data DATE)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //TODO necessário terminar o case1 antes do F10;
        switch (oldVersion){
            case 1:
                String dropTableExistente = "DROP TABLE notas;";
                db.execSQL(dropTableExistente);
                String addTable = "CREATE TABLE notas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "titulo TEXT NOT NULL," +
                        "descricao TEXT NOT NULL," +
                        "data DATE)";
                db.execSQL(addTable);
//            case 1:
//                String colunaPosicao = "ALTER TABLE notas ADD COLUMN posicao INT;";
//                db.execSQL(colunaPosicao);
//                String deleteAntigas = "DELETE FROM notas WHERE posicao IS NULL;";
//                db.execSQL(deleteAntigas);
//                break;
        }
    }

    public void inserir(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getDados(nota);
        db.insert("notas", null, dados);
    }

    public void save(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getDados(nota);
        try{
            db.insertOrThrow("notas", null, dados);
        } catch (SQLiteConstraintException e) {
            db.update("notas", dados, "id = ?", new String[] {nota.getId().toString()});
        }
    }

    private ContentValues getDados(Nota nota) {
        ContentValues dados = new ContentValues();
        dados.put("titulo", nota.getTitulo());
        dados.put("descricao", nota.getDescricao());
        dados.put("data", nota.getData().toString());
        return dados;
    }

    public List<Nota> findAll() {
        String sql = "SELECT * FROM notas;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<Nota> notas = populaNotas(c);
        c.close();
        return notas;
    }

    private List<Nota> populaNotas(Cursor c) {
        List<Nota> notas = new ArrayList<Nota>();
        while(c.moveToNext()) {
            Nota nota = new Nota();
            nota.setId(c.getInt(c.getColumnIndex("id")));
            nota.setTitulo(c.getString(c.getColumnIndex("titulo")));
            nota.setDescricao(c.getString(c.getColumnIndex("descricao")));
            nota.setData(DateUtil.getDateTime(c.getString(c.getColumnIndex("data"))));
            notas.add(nota);
        }
        return notas;
    }

    public void altera(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = getDados(nota);
        String[] params = {nota.getId().toString()};
        db.update("notas", cv, "id = ?", params);
    }

    public void delete(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {nota.getId().toString()};
        db.delete("notas", "id = ?", params);
    }
}
