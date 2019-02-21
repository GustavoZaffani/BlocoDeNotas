package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.alura.blocodenotas.model.Usuario;

public class UsuarioDao {

    private Context ctx;
    private SQLiteDatabase database;

    public UsuarioDao (Context ctx) {
        this.ctx = ctx;
        this.database = DBUtil.getInstance(ctx).getWritableDatabase();
    }

    public void updateUser (Usuario usuario) {
        ContentValues dados;
        if (usuario.getId() == null) {
            usuario.setId(1);
        }
        dados = getDados(usuario);
        database.update("usuario", dados, "id = ?", new String[] {usuario.getId().toString()});
    }

    private ContentValues getDados(Usuario usuario) {
        ContentValues cv = new ContentValues();
        cv.put("id", usuario.getId());
        cv.put("usuario", usuario.getUsuario());
        cv.put("senha", usuario.getSenha());
        return cv;
    }

    public Usuario findOne() {
        String sql = "SELECT * FROM usuario";
        Cursor c = database.rawQuery(sql, null);
        Usuario usuario = dadosBD(c);
        c.close();
        return usuario;
    }

    private Usuario dadosBD (Cursor c) {
        Usuario usuario = new Usuario();
        while(c.moveToNext()) {
            usuario.setId(c.getInt(c.getColumnIndex("id")));
            usuario.setUsuario(c.getString(c.getColumnIndex("usuario")));
            usuario.setSenha(c.getString(c.getColumnIndex("senha")));
        }
        return usuario;
    }
}
