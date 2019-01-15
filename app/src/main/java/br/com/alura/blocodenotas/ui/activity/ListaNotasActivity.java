package br.com.alura.blocodenotas.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dao.NotasDao;
import br.com.alura.blocodenotas.helper.calllback.NotaItemTouchHelper;
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
    private List<Nota> notas;
    private NotasDao dao = new NotasDao(this);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO_APPBAR);

        carregaNotas();

        goToNewForm();
        goToEditForm(adapter);
    }

    private void carregaNotas() {
        List<Nota> notas = dao.findAll();
        adapter = configuraAdapter(notas);
    }

    @NonNull
    private ListaNotasAdapter configuraAdapter(List<Nota> notas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerView);
        ListaNotasAdapter adapter = new ListaNotasAdapter(notas, ListaNotasActivity.this);
        listaNotas.setAdapter(adapter);
        configuraItemTouchHelper(listaNotas);
        return adapter;
    }

    private void goToEditForm(ListaNotasAdapter adapter) {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                Intent goToEdit = new Intent(ListaNotasActivity.this, FormularioActiviy.class);
                goToEdit.putExtra(CHAVE_NOTA, nota);
                goToEdit.putExtra(CHAVE_POSICAO, posicao);
                startActivityForResult(goToEdit, COD_REQ_ALTERA_NOTA);
            }
        });
    }

    private void goToNewForm() {
        Button newNote = findViewById(R.id.lista_btn_novo);
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

        if(ehInsereNota(requestCode, data)) {
            if(resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                dao.inserir(notaRecebida);
                carregaNotas();
            }
        }
        if(ehAlteraNota(requestCode, data)) {
            if(resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                if(posicaoRecebida > POSICAO_INVALIDA) {
                    dao.save(notaRecebida);
                    //adapter.altera(posicaoRecebida, notaRecebida);
                }
                carregaNotas();
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

    private void configuraItemTouchHelper(RecyclerView lista) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(lista);
    }
}
