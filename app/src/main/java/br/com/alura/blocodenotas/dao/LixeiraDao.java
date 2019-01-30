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

import static br.com.alura.blocodenotas.ui.activity.Constantes.COD_RESTAURA_NOTA;

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
        database.insert(ctx.getString(R.string.table_lixeira), null, dados);
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
            lixeira.setId(c.getString(c.getColumnIndex(ctx.getString(R.string.column_id))));
            lixeira.setTitulo(c.getString(c.getColumnIndex(ctx.getString(R.string.column_titulo))));
            lixeira.setDescricao(c.getString(c.getColumnIndex(ctx.getString(R.string.column_descricao))));
            lixeira.setData(c.getString(c.getColumnIndex(ctx.getString(R.string.column_data))));
            notasExcluidas.add(lixeira);
        }
        return notasExcluidas;
    }

    private ContentValues getDados(Nota nota) {
        ContentValues dados = new ContentValues();
        dados.put(ctx.getString(R.string.column_id), nota.getId());
        dados.put(ctx.getString(R.string.column_titulo), nota.getTitulo());
        dados.put(ctx.getString(R.string.column_descricao), nota.getDescricao());
        dados.put(ctx.getString(R.string.column_data), nota.getData());
        return dados;
    }

    public void delete(Lixeira lixeira) {
        String[] params = {lixeira.getId()};
        database.delete(ctx.getString(R.string.table_lixeira), ctx.getString(R.string.where_id), params);
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
