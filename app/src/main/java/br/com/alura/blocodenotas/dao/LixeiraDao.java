package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.blocodenotas.model.Lixeira;
import br.com.alura.blocodenotas.model.Nota;

import static br.com.alura.blocodenotas.ui.activity.Constantes.COD_RESTAURA_NOTA;

public class LixeiraDao {

    private Context context;
    private SQLiteDatabase database;

    public LixeiraDao(Context context) {
         this.context = context;
         this.database = DBUtil.getInstance(context).getWritableDatabase();
    }

    public void save (Nota nota) {
        ContentValues dados;
        dados = getDados(nota);
        database.insert("lixeira", null, dados);
    }

    public List<Lixeira> findAll() {
        String sql = "SELECT * FROM lixeira;";
        Cursor c = database.rawQuery(sql, null);
        List<Lixeira> notasExcluidas = populaLixeira(c);
        return notasExcluidas;
    }

    private List<Lixeira> populaLixeira(Cursor c) {
        List<Lixeira> notasExcluidas = new ArrayList<Lixeira>();
        while(c.moveToNext()) {
            Lixeira lixeira = new Lixeira();
            lixeira.setId(c.getString(c.getColumnIndex("id")));
            lixeira.setTitulo(c.getString(c.getColumnIndex("titulo")));
            lixeira.setDescricao(c.getString(c.getColumnIndex("descricao")));
            lixeira.setData(c.getString(c.getColumnIndex("data")));
            notasExcluidas.add(lixeira);
        }
        return notasExcluidas;
    }

    private ContentValues getDados(Nota nota) {
        ContentValues dados = new ContentValues();
        dados.put("id", nota.getId());
        dados.put("titulo", nota.getTitulo());
        dados.put("descricao", nota.getDescricao());
        dados.put("data", nota.getData());
        return dados;
    }

    public void delete (Lixeira lixeira) {
        String[] params = {lixeira.getId()};
        database.delete("lixeira", "id = ?", params);
    }

    public void restaurarNota (Lixeira lixeira) {
        Nota notaRetornada = transformationToNota(lixeira);
        new NotasDao(context).save(notaRetornada, COD_RESTAURA_NOTA);
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
        String sql = "DELETE FROM lixeira;";
        database.execSQL(sql);
    }
}
