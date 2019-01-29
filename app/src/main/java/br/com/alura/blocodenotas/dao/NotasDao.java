package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.alura.blocodenotas.model.Nota;

import static br.com.alura.blocodenotas.ui.activity.Constantes.COD_RESTAURA_NOTA;

public class NotasDao {

    private Context context;
    private SQLiteDatabase database;

    public NotasDao(Context context) {
        this.context = context;
        this.database = DBUtil.getInstance(context).getWritableDatabase();
    }

    public void save(Nota nota, int option) {
        ContentValues dados;

        if (nota.getId() == null) {
            nota.setId(geraUUID());
            dados = getDados(nota);
            database.insert("notas", null, dados);
        } else if (option == COD_RESTAURA_NOTA) {
            dados = getDados(nota);
            database.insert("notas", null, dados);
        } else {
            dados = getDados(nota);
            database.update("notas", dados, "id = ?", new String[]{nota.getId()});
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
        String search = "%" + titulo + "%";
        Cursor c = database.rawQuery(sql, new String[]{search});
        List<Nota> notas = populaNotas(c);
        c.close();
        return notas;
    }

    public List<Nota> findAll() {
        String sql = "SELECT * FROM notas;";
        Cursor c = database.rawQuery(sql, null);
        List<Nota> notas = populaNotas(c);
        c.close();
        return notas;
    }

    private List<Nota> populaNotas(Cursor c) {
        List<Nota> notas = new ArrayList<Nota>();
        while (c.moveToNext()) {
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
        String[] params = {nota.getId()};
        new LixeiraDao(context).save(nota);
        database.delete("notas", "id = ?", params);
    }

    private String geraUUID() {
        return UUID.randomUUID().toString();
    }

}
