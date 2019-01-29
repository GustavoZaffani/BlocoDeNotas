package br.com.alura.blocodenotas.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dao.LixeiraDao;
import br.com.alura.blocodenotas.dialog.DialogBack;
import br.com.alura.blocodenotas.model.Lixeira;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.ListaNotasExcAdapter;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.listener.OnItemExClickListener;

public class LixeiraActivity extends AppCompatActivity {

    public static final String TITLE_APPBAR = "Lixeira";
    private ListaNotasExcAdapter adapter;
    private List<Lixeira> notasEx;
    private LixeiraDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lixeira);
        setTitle(TITLE_APPBAR);

        carregaExcluidas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_form_delete) {

            if (validaLixeira()) {
                new DialogBack(this)
                        .setSim("Sim")
                        .setNao("Não")
                        .setTitle("Atenção")
                        .setMsg("Tem certeza que deseja limpar a lixeira?")
                        .setOnSimListener(((dialog, which) -> {
                            if (dao.findAll().size() > 1) {
                                Toast.makeText(LixeiraActivity.this, "Registros excluídos!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LixeiraActivity.this, "Registro excluído!", Toast.LENGTH_SHORT).show();
                            }
                            dao.deleteAll();
                            carregaExcluidas();
                        }))
                        .setOnNaoListener(((dialog, which) -> dialog.dismiss()))
                        .build().show();
            }
        }

        if (validaLixeira()) {
            if (item.getItemId() == R.id.menu_restore_all) {
                new DialogBack(this)
                        .setSim("Sim")
                        .setNao("Não")
                        .setTitle("Atenção")
                        .setMsg("Tem certeza que deseja restaurar todas as notas?")
                        .setOnSimListener(((dialog, which) -> {
                            if (dao.findAll().size() > 1) {
                                Toast.makeText(LixeiraActivity.this, "Notas restauradas!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LixeiraActivity.this, "Nota restaurada!", Toast.LENGTH_SHORT).show();
                            }
                            dao.restaurarTudo();
                            carregaExcluidas();
                        }))
                        .setOnNaoListener(((dialog, which) -> dialog.dismiss()))
                        .build().show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean validaLixeira() {
        if (dao.findAll().size() == 0) {
            new DialogBack(LixeiraActivity.this)
                    .setTitle("Atenção")
                    .setMsg("A lixeira está vazia!")
                    .setSim("OK")
                    .setOnSimListener(((dialog, which) -> dialog.dismiss()))
                    .build().show();
            return false;
        } else {
            return true;
        }
    }

    private void carregaExcluidas() {
        dao = new LixeiraDao(this);
        notasEx = dao.findAll();
        configuraRecyclerView(notasEx);
    }

    private void configuraRecyclerView(List<Lixeira> notas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_excluidas_recyclerView);
        configuraAdapter(notas, listaNotas);
    }

    private void configuraAdapter(List<Lixeira> notas, RecyclerView listaNotas) {
        adapter = new ListaNotasExcAdapter(notas, this);
        listaNotas.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemExClickListener() {
            @Override
            public void onItemExClickListener(Lixeira lixeira, int posicao) {

                new DialogBack(LixeiraActivity.this)
                        .setTitle("Atenção")
                        .setMsg("Selecione uma opção abaixo:")
                        .setSim("Restaurar")
                        .setNao("Excluir")
                        .setOnSimListener(((dialog, which) -> {
                            dao.restaurarNota(lixeira);
                            carregaExcluidas();
                            Toast.makeText(LixeiraActivity.this, "Nota restaurada", Toast.LENGTH_SHORT).show();
                        }))
                        .setOnNaoListener(((dialog, which) -> {
                            dao.delete(lixeira);
                            carregaExcluidas();
                            Toast.makeText(LixeiraActivity.this, "Nota excluída", Toast.LENGTH_SHORT).show();
                        })).build().show();
            }
        });
    }
}

