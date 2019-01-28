package br.com.alura.blocodenotas.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dao.NotasDao;
import br.com.alura.blocodenotas.dialog.DialogBack;
import br.com.alura.blocodenotas.dialog.DialogFiltro;
import br.com.alura.blocodenotas.dialog.DialogLoading;
import br.com.alura.blocodenotas.helper.calllback.NotaItemTouchHelper;
import br.com.alura.blocodenotas.model.Lixeira;
import br.com.alura.blocodenotas.model.Nota;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.ListaNotasAdapter;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.listener.OnItemClickListener;

import static br.com.alura.blocodenotas.ui.activity.Constantes.CHAVE_NOTA;
import static br.com.alura.blocodenotas.ui.activity.Constantes.CHAVE_POSICAO;
import static br.com.alura.blocodenotas.ui.activity.Constantes.COD_REQ_ALTERA_NOTA;
import static br.com.alura.blocodenotas.ui.activity.Constantes.COD_REQ_INSERE_NOTA;
import static br.com.alura.blocodenotas.ui.activity.Constantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Bloco de Notas";
    private ListaNotasAdapter adapter;
    private NotasDao dao;
    private List<Nota> notas;
    private Dialog loader;
    private EditText texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO_APPBAR);

        carregaNotas();
        goToNewForm();
        goToFilter();
        goToLixeira();
    }

    private void goToLixeira() {
        FloatingActionButton openLixeira = findViewById(R.id.lista_btn_lixeira);
        openLixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLixeira = new Intent(ListaNotasActivity.this, LixeiraActivity.class);
                startActivity(goLixeira);
            }
        });
    }

    @Override
    protected void onResume() {
        if(loader != null && loader.isShowing()) {
            loader.dismiss();
        }
        super.onResume();
    }

    private void carregaNotas() {
        dao = new NotasDao(this);
        notas = dao.findAll();
        configuraRecyclerView(notas);
        dao.close();
    }

    private void configuraRecyclerView(List<Nota> notas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerView);
        configuraAdapter(notas, listaNotas);
        //configuraItemTouchHelper(listaNotas);
    }

    private void configuraAdapter(List<Nota> notas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(notas, this);
        listaNotas.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                new DialogBack(ListaNotasActivity.this)
                        .setTitle("Atenção")
                        .setMsg("Selecione uma opção abaixo: ")
                        .setSim("Editar")
                        .setNao("Excluir")
                        .setOnSimListener((dialog, which) -> {
                                loader = new DialogLoading(ListaNotasActivity.this, "Carregando...").build();
                                loader.show();
                                Intent goToEdit = new Intent(ListaNotasActivity.this, FormularioActiviy.class);
                                goToEdit.putExtra(CHAVE_NOTA, nota);
                                goToEdit.putExtra(CHAVE_POSICAO, posicao);
                                startActivityForResult(goToEdit, COD_REQ_ALTERA_NOTA);
                        })
                        .setOnNaoListener((dialog, which) -> {
                                dao = new NotasDao(ListaNotasActivity.this);
                                loader = new DialogLoading(ListaNotasActivity.this, "Excluindo...").build();
                                loader.show();
                                dao.delete(nota);
                                carregaNotas();
                                loader.dismiss();
                                Toast.makeText(ListaNotasActivity.this, "Registro excluído!", Toast.LENGTH_SHORT).show();
                                Intent teste = new Intent(ListaNotasActivity.this, LixeiraActivity.class);
                                startActivity(teste);
                        }).build().show();
            }
        });
    }

    private void goToFilter() {
        FloatingActionButton filter = findViewById(R.id.lista_btn_search);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFiltro(ListaNotasActivity.this)
                        .setFiltrar("FILTRAR")
                        .setCancelar("CANCELAR")
                        .setOnFiltrarListener(((dialog, which) -> {
                            texto = ((Dialog) dialog).findViewById(R.id.dlg_filtro_edt_txt);
                            if(!TextUtils.isEmpty(texto.getText().toString())) {
                                List<Nota> notas = dao.findByFilter(texto.getText().toString());
                                configuraRecyclerView(notas);
                            } else {
                                carregaNotas();
                            }
                        }))
                        .setOnCancelarListener(((dialog, which) -> {
                            dialog.dismiss();
                        }))
                        .build().show();
            }
        });
    }

    private void goToNewForm() {
        FloatingActionButton newNote = findViewById(R.id.lista_btn_novo);
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFormulario = new Intent(ListaNotasActivity.this, FormularioActiviy.class);
                startActivityForResult(toFormulario, COD_REQ_INSERE_NOTA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        dao = new NotasDao(this);
        if(ehInsereNota(requestCode, data)) {
            if(resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                dao.save(notaRecebida);
                carregaNotas();
                adapter.adicionaNota();
                Toast.makeText(ListaNotasActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
        if(ehAlteraNota(requestCode, data)) {
            if(resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                if(posicaoRecebida > POSICAO_INVALIDA) {
                    dao.save(notaRecebida);
                }
                carregaNotas();
                Toast.makeText(ListaNotasActivity.this, "Alterado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        dao.close();
    }

    private boolean ehAlteraNota(int requestCode, Intent data) {
        return requestCode == COD_REQ_ALTERA_NOTA && data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehInsereNota(int requestCode, Intent data) {
        return requestCode == COD_REQ_INSERE_NOTA && data != null && data.hasExtra(CHAVE_NOTA);
    }

    //TODO a implementação dessa animação será feita posteriormente
    private void configuraItemTouchHelper(RecyclerView lista) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelper(adapter, this));
        itemTouchHelper.attachToRecyclerView(lista);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
