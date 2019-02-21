package br.com.alura.blocodenotas.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dao.LoginDao;
import br.com.alura.blocodenotas.dialog.DialogBack;
import br.com.alura.blocodenotas.model.Login;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.ListaLogsAdapter;

public class ListaLoginActivity extends AppCompatActivity {

    private LoginDao dao;
    private ListaLogsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_login);
        setTitle("Logs de Acesso");
        carregaLogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_log, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.delete_logs) {
            dao = new LoginDao(ListaLoginActivity.this);
            new DialogBack(ListaLoginActivity.this)
                    .setTitle(getString(R.string.attention))
                    .setMsg("Tem certeza que deseja limpar todos os logs ?")
                    .setSim(getString(R.string.yes))
                    .setNao(getString(R.string.no))
                    .setOnNaoListener(((dialog, which) -> dialog.dismiss()))
                    .setOnSimListener(((dialog, which) -> {
                        dao.deleteAll();
                        carregaLogs();
                        Toast.makeText(ListaLoginActivity.this, "Logs excluídos com sucesso!", Toast.LENGTH_SHORT).show();
                    })).build().show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregaLogs() {
        List<Login> logs = new LoginDao(ListaLoginActivity.this).findAll();
        configuraRecyclerView(logs);
    }

    private void configuraRecyclerView(List<Login> logs) {
        RecyclerView listaLogs = findViewById(R.id.rv_logs);
        configuraAdapter(logs, listaLogs);
    }

    private void configuraAdapter(List<Login> logs, RecyclerView listaLogs) {
        adapter = new ListaLogsAdapter(logs, ListaLoginActivity.this);
        listaLogs.setAdapter(adapter);

        adapter.setOnLogClickListener(((login, posicao) -> {
            new DialogBack(ListaLoginActivity.this)
                    .setTitle(getString(R.string.attention))
                    .setMsg("Tem certeza que deseja excluir ?")
                    .setSim(getString(R.string.yes))
                    .setNao(getString(R.string.no))
                    .setOnNaoListener(((dialog, which) -> dialog.dismiss()))
                    .setOnSimListener(((dialog, which) -> {
                        dao = new LoginDao(ListaLoginActivity.this);
                        dao.delete(login.getIdToken());
                        carregaLogs();
                        Toast.makeText(ListaLoginActivity.this, "Log excluído com sucesso!", Toast.LENGTH_SHORT).show();
                    })).build().show();
        }));
    }
}
