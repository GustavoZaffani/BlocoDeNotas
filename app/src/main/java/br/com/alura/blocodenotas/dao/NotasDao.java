package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import br.com.alura.blocodenotas.model.Nota;

public class NotasDao extends SQLiteOpenHelper {

    public NotasDao(Context context) {
        super(context, "Notas", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE notas (" +
                "id CHAR(36) PRIMARY KEY ," +
                "titulo TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "data TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion){

        }
    }

    public void save(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados;

        if(nota.getId() == null) {
            nota.setId(geraUUID());
            dados = getDados(nota);
            db.insert("notas", null, dados);
        } else {
            dados = getDados(nota);
            db.update("notas", dados, "id = ?", new String[] {nota.getId()});
        }
    }

    private ContentValues getDados(Nota nota) {
        ContentValues dados = new ContentValues();
        dados.put("id", nota.getId());
        dados.put("titulo", nota.getTitulo());
        dados.put("descricao", nota.getDescricao());
        dados.put("data", nota.getData());
        return dados;
    }

    public List<Nota> findByFilter(String titulo) {
        String sql = "SELECT * FROM notas WHERE titulo LIKE ?";
        SQLiteDatabase db = getReadableDatabase();
        String search = "%" + titulo + "%";
        Log.i("aqui está", search);
        Cursor c = db.rawQuery(sql, new String[] {search});

        List<Nota> notas = populaNotas(c);
        c.close();
        return notas;
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
            nota.setId(c.getString(c.getColumnIndex("id")));
            nota.setTitulo(c.getString(c.getColumnIndex("titulo")));
            nota.setDescricao(c.getString(c.getColumnIndex("descricao")));
            nota.setData(c.getString(c.getColumnIndex("data")));
            notas.add(nota);
        }
        return notas;
    }

    public void delete(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        //Log.i("olha aqui o id ", nota.getId());
        String[] params = {nota.getId()};
        db.delete("notas", "id = ?", params);
    }

    public void invertePosicao(int posicaoInicial, int posicaoFinal) {
        List<Nota> notas = new ArrayList<>();
        notas = findAll();
        Collections.swap(notas, posicaoInicial, posicaoFinal);
    }

    private String geraUUID() {
        return UUID.randomUUID().toString();
    }
}
