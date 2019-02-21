package br.com.alura.blocodenotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.alura.blocodenotas.model.Login;

public class LoginDao {

    private Context ctx;
    private SQLiteDatabase db;

    public LoginDao (Context ctx) {
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
            login.setIdToken(c.getString(c.getColumnIndex("id_token")));
            login.setUsuario(c.getString(c.getColumnIndex("nome_user")));
            login.setHoraLogin(c.getString(c.getColumnIndex("data")));
            logs.add(login);
        }
        return logs;
    }

    private void save(Login login) {
        ContentValues dados = getDados(login);
        try {
            db.insert("login", null, dados);
        } catch (SQLiteException ex) {
            db.update("login", dados, "id_token = ?", new String[]{login.getIdToken()});
        }
    }

    private ContentValues getDados(Login login) {
        ContentValues cv = new ContentValues();
        cv.put("id_token", login.getIdToken());
        cv.put("nome_user", login.getUsuario());
        cv.put("data", login.getHoraLogin());
        return cv;
    }

    public void salvaLogin(String user, String horaLogin) {
        Login login = new Login();
        geraTokenLogin(login);
        login.setUsuario(user);
        login.setHoraLogin(horaLogin);
        this.save(login);
    }

    private void geraTokenLogin (Login login) {
        login.setIdToken(UUID.randomUUID().toString());
    }

    public void delete(String idLogin) {
        db.delete("login", "id_token = ?", new String[] {idLogin});
    }

    public void deleteAll() {
        String clearLogs = "DELETE FROM login";
        db.execSQL(clearLogs);
    }
}
