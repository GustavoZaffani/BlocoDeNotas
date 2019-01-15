package br.com.alura.blocodenotas.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.time.LocalDate;
import java.util.Date;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Nota;

import static br.com.alura.blocodenotas.ui.activity.Constantes.CHAVE_NOTA;
import static br.com.alura.blocodenotas.ui.activity.Constantes.CHAVE_POSICAO;
import static br.com.alura.blocodenotas.ui.activity.Constantes.POSICAO_INVALIDA;

public class FormularioActiviy extends AppCompatActivity {

    public static final String TITLE_APPBAR_INSERT = "Nova nota";
    public static final String TITLE_APPBAR_EDIT = "Altera nota";
    private EditText titulo;
    private EditText descricao;
    private int posicaoRecebida = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_activiy);

        setTitle(TITLE_APPBAR_INSERT);
        inicializaCampos();
        preencheForm();
    }

    private void preencheForm() {
        Intent infRecebido = getIntent();
        if(infRecebido.hasExtra(CHAVE_NOTA)) {
            setTitle(TITLE_APPBAR_EDIT);
            Nota notaRecebida = (Nota) infRecebido.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = infRecebido.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            titulo.setText(notaRecebida.getTitulo());
            descricao.setText(notaRecebida.getDescricao());
        }
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.form_editTitulo);
        descricao = findViewById(R.id.form_editDesc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salva_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_form_save) {
            Nota novaNota = criaNota();
            retornaNota(novaNota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent returnInsert = new Intent();
        returnInsert.putExtra(CHAVE_NOTA, nota);
        returnInsert.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, returnInsert);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    private Nota criaNota() {
        Date dataAtual = new Date(2018,11,11);
        return new Nota(titulo.getText().toString(), descricao.getText().toString(), dataAtual);
    }
}






