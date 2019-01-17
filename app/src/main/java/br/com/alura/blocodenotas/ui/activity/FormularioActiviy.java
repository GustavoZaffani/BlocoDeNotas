package br.com.alura.blocodenotas.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Date;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dialog.DialogBack;
import br.com.alura.blocodenotas.model.Nota;

import static br.com.alura.blocodenotas.ui.activity.Constantes.CHAVE_NOTA;
import static br.com.alura.blocodenotas.ui.activity.Constantes.CHAVE_POSICAO;
import static br.com.alura.blocodenotas.ui.activity.Constantes.POSICAO_INVALIDA;

public class FormularioActiviy extends AppCompatActivity {

    public static final String TITLE_APPBAR_INSERT = "Nova nota";
    public static final String TITLE_APPBAR_EDIT = "Altera nota";
    private EditText titulo = null;
    private EditText descricao = null;
    private int posicaoRecebida = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_activiy);

        setTitle(TITLE_APPBAR_INSERT);
        preencheForm();
    }

    private void preencheForm() {
        inicializaCampos();
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

    @Override
    public void onBackPressed() {
        if(titulo != null && descricao != null) {
            Log.i("titulo", titulo.toString());
            Log.i("descricao", descricao.toString());
        }

        new DialogBack(FormularioActiviy.this)
                .setTitle("Deseja realmente voltar?")
                .setMsg("Os dados adicionados serão perdidos")
                .setOnNaoListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setOnSimListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).build().show();
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