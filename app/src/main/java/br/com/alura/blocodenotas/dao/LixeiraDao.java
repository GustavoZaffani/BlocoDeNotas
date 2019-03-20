package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Lixeira;
import br.com.alura.blocodenotas.model.Nota;

import static br.com.alura.blocodenotas.constantes.Constantes.COD_RESTAURA_NOTA;
import static br.com.alura.blocodenotas.constantes.Database.*;

public class LixeiraDao {

    private Context ctx;
    private SQLiteDatabase database;

    public LixeiraDao(Context context) {
        this.ctx = context;
        this.database = DBUtil.getInstance(context).getWritableDatabase();
    }

    public void save(Nota nota) {
        ContentValues dados;
        dados = getDados(nota);
        database.insert(TABLE_LIXEIRA, null, dados);
    }

    public List<Lixeira> findAll() {
        String sql = ctx.getString(R.string.find_all_lixeira);
        Cursor c = database.rawQuery(sql, null);
        List<Lixeira> notasExcluidas = populaLixeira(c);
        return notasExcluidas;
    }

    private List<Lixeira> populaLixeira(Cursor c) {
        List<Lixeira> notasExcluidas = new ArrayList<Lixeira>();
        while (c.moveToNext()) {
            Lixeira lixeira = new Lixeira();
            lixeira.setId(c.getString(c.getColumnIndex(LIXEIRA_ID)));
            lixeira.setTitulo(c.getString(c.getColumnIndex(LIXEIRA_TITULO)));
            lixeira.setDescricao(c.getString(c.getColumnIndex(LIXEIRA_DESCRICAO)));
            lixeira.setData(c.getString(c.getColumnIndex(LIXEIRA_DATA)));
            notasExcluidas.add(lixeira);
        }
        return notasExcluidas;
    }

    private ContentValues getDados(Nota nota) {
        ContentValues dados = new ContentValues();
        dados.put(NOTAS_ID, nota.getId());
        dados.put(NOTAS_TITULO, nota.getTitulo());
        dados.put(NOTAS_DESCRICAO, nota.getDescricao());
        dados.put(NOTAS_DATA, nota.getData());
        return dados;
    }

    public void delete(Lixeira lixeira) {
        String[] params = {lixeira.getId()};
        database.delete(TABLE_LIXEIRA, ctx.getString(R.string.where_id), params);
    }

    public void restaurarNota(Lixeira lixeira) {
        Nota notaRetornada = transformationToNota(lixeira);
        new NotasDao(ctx).save(notaRetornada, COD_RESTAURA_NOTA);
        delete(lixeira);
    }

    public Nota transformationToNota(Lixeira lixeira) {
        Nota notaRestaurada = new Nota();
        notaRestaurada.setId(lixeira.getId());
        notaRestaurada.setData(lixeira.getData());
        notaRestaurada.setDescricao(lixeira.getDescricao());
        notaRestaurada.setTitulo(lixeira.getTitulo());
        return notaRestaurada;
    }

    public void deleteAll() {
        String sql = ctx.getString(R.string.delete_all_lixeira);
        database.execSQL(sql);
    }

    public void restaurarTudo() {
        List<Lixeira> notas = findAll();
        for (int i = 0; i < notas.size(); i++) {
            restaurarNota(notas.get(i));
        }
    }
}
