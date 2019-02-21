package br.com.alura.blocodenotas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Login implements Serializable {

    private String idToken;
    private String usuario;
    private String horaLogin;

    public Login(String idToken, String usuario, String horaLogin) {
        this.idToken = idToken;
        this.usuario = usuario;
        this.horaLogin = horaLogin;
    }

    public Login() {
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getHoraLogin() {
        return horaLogin;
    }

    public void setHoraLogin(String horaLogin) {
        this.horaLogin = horaLogin;
    }
}
