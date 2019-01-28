package br.com.alura.blocodenotas.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Nota;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.ListaNotasAdapter;

public class LixeiraActivity extends AppCompatActivity {

    public static final String TITLE_APPBAR = "Lixeira";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE_APPBAR);
        setContentView(R.layout.activity_lixeira);
    }
    private void configuraRecyclerView (List<Nota> notas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerView);
        configuraAdapter(notas, listaNotas);
    }

    private void configuraAdapter(List<Nota> notas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(notas, this);
        listaNotas.setAdapter(adapter);
    }

}

