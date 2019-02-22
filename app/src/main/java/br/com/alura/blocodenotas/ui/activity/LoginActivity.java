package br.com.alura.blocodenotas.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.Utils.Utils;
import br.com.alura.blocodenotas.dao.LoginDao;
import br.com.alura.blocodenotas.dao.UsuarioDao;
import br.com.alura.blocodenotas.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        login();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void login() {

        Button btnLogin = findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(onClick -> {
            if (validaLogin()) {
                String timeNow = new Utils().formataDataHora(new Date());
                new LoginDao(LoginActivity.this).salvaLogin(this.user.getText().toString(), timeNow);
                Intent ok = new Intent(LoginActivity.this, ListaNotasActivity.class);
                startActivity(ok);
            } else {
                Toast.makeText(LoginActivity.this, "Login Inválido!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validaLogin() {
        user = findViewById(R.id.login_edt_user);
        password = findViewById(R.id.login_edt_password);
        Usuario userCadastro = new UsuarioDao(LoginActivity.this).findOne();
        return user.getText().toString().equals(userCadastro.getUsuario())
                && password.getText().toString().equals(userCadastro.getSenha());
    }
}
