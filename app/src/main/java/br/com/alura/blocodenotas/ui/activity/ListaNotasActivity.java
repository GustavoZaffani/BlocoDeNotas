package br.com.alura.blocodenotas.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dao.NotasDao;
import br.com.alura.blocodenotas.dao.UsuarioDao;
import br.com.alura.blocodenotas.dialog.DialogBack;
import br.com.alura.blocodenotas.dialog.DialogFiltro;
import br.com.alura.blocodenotas.dialog.DialogLoading;
import br.com.alura.blocodenotas.dialog.DialogUsuario;
import br.com.alura.blocodenotas.helper.calllback.NotaItemTouchHelper;
import br.com.alura.blocodenotas.model.Nota;
import br.com.alura.blocodenotas.model.Usuario;
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
    private EditText texto, user, pass;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO_APPBAR);

        goToNewForm();
        goToFilter();
        goToLixeira();
        goToNewUser();
        goToLogs();
    }

    @Override
    protected void onResume() {
        carregaNotas();
        if (loader != null && loader.isShowing()) {
            loader.dismiss();
        }
        super.onResume();
    }

    private void carregaNotas() {
        dao = new NotasDao(this);
        notas = dao.findAll();
        configuraRecyclerView(notas);
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
                        .setTitle(getString(R.string.attention))
                        .setMsg(getString(R.string.select_option))
                        .setSim(getString(R.string.edit))
                        .setNao(getString(R.string.delete))
                        .setOnSimListener((dialog, which) -> {
                            loader = new DialogLoading(ListaNotasActivity.this, getString(R.string.loading)).build();
                            loader.show();
                            Intent goToEdit = new Intent(ListaNotasActivity.this, FormularioActiviy.class);
                            goToEdit.putExtra(CHAVE_NOTA, nota);
                            goToEdit.putExtra(CHAVE_POSICAO, posicao);
                            startActivityForResult(goToEdit, COD_REQ_ALTERA_NOTA);
                        })
                        .setOnNaoListener((dialog, which) -> {
                            dao = new NotasDao(ListaNotasActivity.this);
                            loader = new DialogLoading(ListaNotasActivity.this, getString(R.string.excluindo)).build();
                            loader.show();
                            dao.delete(nota);
                            carregaNotas();
                            loader.dismiss();
                            Toast.makeText(ListaNotasActivity.this, getString(R.string.register_deleted), Toast.LENGTH_SHORT).show();
                        }).build().show();
            }
        });
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

    private void goToLogs() {
        FloatingActionButton openLogs = findViewById(R.id.lista_btn_logs);
        openLogs.setOnClickListener(view -> {
            startActivity(new Intent(ListaNotasActivity.this, ListaLoginActivity.class));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void goToNewUser() {
        FloatingActionButton btnUser = findViewById(R.id.lista_btn_user);
        btnUser.setOnClickListener(view -> {
            new DialogUsuario(ListaNotasActivity.this)
                    .setSalvar(getString(R.string.save))
                    .setCancelar(getString(R.string.cancel))
                    .setOnCancelarListener(((dialog, which) -> dialog.dismiss()))
                    .setOnSalvarListener(((dialog, which) -> {
                        Usuario usuario = getDadosUsuario(dialog);
                        new UsuarioDao(ListaNotasActivity.this).updateUser(usuario);
                        Toast.makeText(ListaNotasActivity.this, "Alterado com Sucesso", Toast.LENGTH_SHORT).show();
                    }))
                    .build().show();
        });
    }

    private Usuario getDadosUsuario(DialogInterface dialog) {
        Usuario usuario = new Usuario();
        user = ((Dialog) dialog).findViewById(R.id.usuario_edt_user);
        pass = ((Dialog) dialog).findViewById(R.id.usuario_edt_pass);
        if (!TextUtils.isEmpty(user.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())) {
            usuario.setUsuario(user.getText().toString());
            usuario.setSenha(pass.getText().toString());
        }
        return usuario;
    }

    private void goToFilter() {
        FloatingActionButton filter = findViewById(R.id.lista_btn_search);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFiltro(ListaNotasActivity.this)
                        .setFiltrar(getString(R.string.filter))
                        .setCancelar(getString(R.string.cancel))
                        .setOnFiltrarListener(((dialog, which) -> {
                            texto = ((Dialog) dialog).findViewById(R.id.dlg_filtro_edt_txt);
                            if (!TextUtils.isEmpty(texto.getText().toString())) {
                                List<Nota> notas = dao.findByFilter(texto.getText().toString());
                                if (validaFiltro(notas)) {
                                    configuraRecyclerView(notas);
                                }
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

    @Override
    public void onBackPressed() {
        new DialogBack(this)
                .setTitle("Sair")
                .setMsg("Deseja finalizar o bloco de notas?")
                .setSim("sim")
                .setNao("não")
                .setOnSimListener((dialog, which) -> finishAffinity())
                .setOnNaoListener(((dialog, which) -> dialog.dismiss())).build().show();
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

    private Boolean validaFiltro(List<Nota> notas) {
        if (notas.size() == 0) {
            new DialogBack(ListaNotasActivity.this)
                    .setTitle(getString(R.string.ops))
                    .setMsg(getString(R.string.not_found))
                    .setSim(getString(R.string.ok))
                    .setOnSimListener(((dialog, which) -> dialog.dismiss()))
                    .build().show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        dao = new NotasDao(this);
        if (ehInsereNota(requestCode, data)) {
            if (resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                dao.save(notaRecebida, 0);
                carregaNotas();
                adapter.adicionaNota();
                Toast.makeText(ListaNotasActivity.this, getString(R.string.saved_sucess), Toast.LENGTH_SHORT).show();
            }
        }
        if (ehAlteraNota(requestCode, data)) {
            if (resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                if (posicaoRecebida > POSICAO_INVALIDA) {
                    dao.save(notaRecebida, 0);
                }
                carregaNotas();
                Toast.makeText(ListaNotasActivity.this, getString(R.string.updated_sucess), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
