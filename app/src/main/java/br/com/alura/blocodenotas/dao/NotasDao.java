package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Nota;

import static br.com.alura.blocodenotas.constantes.Constantes.COD_RESTAURA_NOTA;
import static br.com.alura.blocodenotas.constantes.Database.*;


public class NotasDao {

    private Context ctx;
    private SQLiteDatabase database;

    public NotasDao(Context context) {
        this.ctx = context;
        this.database = DBUtil.getInstance(context).getWritableDatabase();
    }

    public void save(Nota nota, int option) {
        ContentValues dados;

        if (nota.getId() == null) {
            nota.setId(geraUUID());
            dados = getDados(nota);
            database.insert(TABLE_NOTAS, null, dados);
        } else if (option == COD_RESTAURA_NOTA) {
            dados = getDados(nota);
            database.insert(TABLE_NOTAS, null, dados);
        } else {
            dados = getDados(nota);
            database.update(TABLE_NOTAS, dados, ctx.getString(R.string.where_id), new String[]{nota.getId()});
        }
    }

    private ContentValues getDados(Nota nota) {
        ContentValues dados = new ContentValues();
        dados.put(NOTAS_ID, nota.getId());
        dados.put(NOTAS_TITULO, nota.getTitulo());
        dados.put(NOTAS_DESCRICAO, nota.getDescricao());
        dados.put(NOTAS_DATA, nota.getData());
        return dados;
    }

    public List<Nota> findByFilter(String titulo) {
        String sql = ctx.getString(R.string.find_where_titulo);
        String search = "%" + titulo + "%";
        Cursor c = database.rawQuery(sql, new String[]{search});
        List<Nota> notas = populaNotas(c);
        c.close();
        return notas;
    }

    public List<Nota> findAll() {
        String sql = ctx.getString(R.string.find_all_notas);
        Cursor c = database.rawQuery(sql, null);
        List<Nota> notas = populaNotas(c);
        c.close();
        return notas;
    }

    private List<Nota> populaNotas(Cursor c) {
        List<Nota> notas = new ArrayList<Nota>();
        while (c.moveToNext()) {
            Nota nota = new Nota();
            nota.setId(c.getString(c.getColumnIndex(NOTAS_ID)));
            nota.setTitulo(c.getString(c.getColumnIndex(NOTAS_TITULO)));
            nota.setDescricao(c.getString(c.getColumnIndex(NOTAS_DESCRICAO)));
            nota.setData(c.getString(c.getColumnIndex(NOTAS_DATA)));
            notas.add(nota);
        }
        return notas;
    }

    public void delete(Nota nota) {
        String[] params = {nota.getId()};
        new LixeiraDao(ctx).save(nota);
        database.delete(TABLE_NOTAS, ctx.getString(R.string.where_id), params);
    }

    private String geraUUID() {
        return UUID.randomUUID().toString();
    }

}
