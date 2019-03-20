package br.com.alura.blocodenotas.constantes;

public interface Database {

    String TABLE_NOTAS = "notas",
            NOTAS_ID = "id",
            NOTAS_TITULO = "titulo",
            NOTAS_DESCRICAO = "descricao",
            NOTAS_DATA = "data";

    String TABLE_LIXEIRA = "lixeira",
            LIXEIRA_ID = "id",
            LIXEIRA_TITULO = "titulo",
            LIXEIRA_DESCRICAO = "descricao",
            LIXEIRA_DATA = "data";

    String TABLE_LOGIN = "login",
            LOGIN_IDTOKEN = "id_token",
            LOGIN_NOMEUSER = "nome_user",
            LOGIN_DATA = "data";

    String TABLE_USUARIO = "usuario",
            USUARIO_ID = "id",
            USUARIO_USUARIO = "usuario",
            USUARIO_SENHA = "senha";
}
