package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Login;

import static br.com.alura.blocodenotas.constantes.Database.*;

public class LoginDao {

    private Context ctx;
    private SQLiteDatabase db;

    public LoginDao(Context ctx) {
        this.ctx = ctx;
        this.db = DBUtil.getInstance(ctx).getWritableDatabase();
    }

    public List<Login> findAll() {
        String sql = "SELECT * FROM login;";
        Cursor c = db.rawQuery(sql, null);
        List<Login> logsLogin = populaLogs(c);
        c.close();
        return logsLogin;
    }

    private List<Login> populaLogs(Cursor c) {
        List<Login> logs = new ArrayList<>();
        while (c.moveToNext()) {
            Login login = new Login();
            login.setIdToken(c.getString(c.getColumnIndex(LOGIN_IDTOKEN)));
            login.setUsuario(c.getString(c.getColumnIndex(LOGIN_NOMEUSER)));
            login.setHoraLogin(c.getString(c.getColumnIndex(LOGIN_DATA)));
            logs.add(login);
        }
        return logs;
    }

    private void save(Login login) {
        ContentValues dados = getDados(login);
        try {
            db.insert(TABLE_LOGIN, null, dados);
        } catch (SQLiteException ex) {
            db.update(TABLE_LOGIN, dados, ctx.getString(R.string.where_id_login), new String[]{login.getIdToken()});
        }
    }

    private ContentValues getDados(Login login) {
        ContentValues cv = new ContentValues();
        cv.put(LOGIN_IDTOKEN, login.getIdToken());
        cv.put(LOGIN_NOMEUSER, login.getUsuario());
        cv.put(LOGIN_DATA, login.getHoraLogin());
        return cv;
    }

    public void salvaLogin(String user, String horaLogin) {
        Login login = new Login();
        geraTokenLogin(login);
        login.setUsuario(user);
        login.setHoraLogin(horaLogin);
        this.save(login);
    }

    private void geraTokenLogin(Login login) {
        login.setIdToken(UUID.randomUUID().toString());
    }

    public void delete(String idLogin) {
        db.delete(TABLE_LOGIN, ctx.getString(R.string.where_id_login), new String[]{idLogin});
    }

    public void deleteAll() {
        String clearLogs = "DELETE FROM login";
        db.execSQL(clearLogs);
    }
}
