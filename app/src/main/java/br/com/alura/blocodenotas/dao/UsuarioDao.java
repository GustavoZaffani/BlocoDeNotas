package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Usuario;

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
        database.update(ctx.getString(R.string.table_usuario), dados, ctx.getString(R.string.where_id), new String[]{usuario.getId().toString()});
    }

    private ContentValues getDados(Usuario usuario) {
        ContentValues cv = new ContentValues();
        cv.put(ctx.getString(R.string.usuario_id), usuario.getId());
        cv.put(ctx.getString(R.string.usuario_usuario), usuario.getUsuario());
        cv.put(ctx.getString(R.string.usuario_senha), usuario.getSenha());
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
            usuario.setId(c.getInt(c.getColumnIndex(ctx.getString(R.string.usuario_id))));
            usuario.setUsuario(c.getString(c.getColumnIndex(ctx.getString(R.string.usuario_usuario))));
            usuario.setSenha(c.getString(c.getColumnIndex(ctx.getString(R.string.usuario_senha))));
        }
        return usuario;
    }
}
