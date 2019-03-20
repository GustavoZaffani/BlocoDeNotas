package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Usuario;

import static br.com.alura.blocodenotas.constantes.Database.*;

public class UsuarioDao {

    private Context ctx;
    private SQLiteDatabase database;

    public UsuarioDao(Context ctx) {
        this.ctx = ctx;
        this.database = DBUtil.getInstance(ctx).getWritableDatabase();
    }

    public void updateUser(Usuario usuario) {
        ContentValues dados;
        if (usuario.getId() == null) {
            usuario.setId(1);
        }
        dados = getDados(usuario);
        database.update(TABLE_USUARIO, dados, ctx.getString(R.string.where_id), new String[]{usuario.getId().toString()});
    }

    private ContentValues getDados(Usuario usuario) {
        ContentValues cv = new ContentValues();
        cv.put(USUARIO_ID, usuario.getId());
        cv.put(USUARIO_USUARIO, usuario.getUsuario());
        cv.put(USUARIO_SENHA, usuario.getSenha());
        return cv;
    }

    public Usuario findOne() {
        String sql = "SELECT * FROM usuario";
        Cursor c = database.rawQuery(sql, null);
        Usuario usuario = dadosBD(c);
        c.close();
        return usuario;
    }

    private Usuario dadosBD(Cursor c) {
        Usuario usuario = new Usuario();
        while (c.moveToNext()) {
            usuario.setId(c.getInt(c.getColumnIndex(USUARIO_ID)));
            usuario.setUsuario(c.getString(c.getColumnIndex(USUARIO_USUARIO)));
            usuario.setSenha(c.getString(c.getColumnIndex(USUARIO_SENHA)));
        }
        return usuario;
    }
}