package br.com.alura.blocodenotas.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dialog.DialogBack;
import br.com.alura.blocodenotas.dialog.DialogLoading;
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
    private Nota notaRecebida;

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
            notaRecebida = (Nota) infRecebido.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = infRecebido.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            titulo.setText(notaRecebida.getTitulo());
            descricao.setText(notaRecebida.getDescricao());
        } else {
            notaRecebida = new Nota();
        }
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.form_editTitulo);
        descricao = findViewById(R.id.form_editDesc);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salva_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_form_save) {

            Nota notaRetornada = criaNota();

            if(validaCampos()) {
                new DialogLoading(this, "Salvando...").build().show();
                if(notaRecebida.getId() != null) {
                    notaRetornada.setId(notaRecebida.getId());
                }
                retornaNota(notaRetornada);
                finish();
            } else {
                new DialogBack(this)
                        .setTitle("Atenção")
                        .setMsg("Necessário preencher todos os campos")
                        .setSim("OK")
                        .setOnSimListener(((dialog, which) -> dialog.dismiss()))
                        .build().show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
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
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss"));
        return new Nota(titulo.getText().toString(), descricao.getText().toString(), data);
    }

    private Boolean validaCampos() {
        if(TextUtils.isEmpty(titulo.getText().toString())) {
            titulo.requestFocus();
            return false;
        } else if(TextUtils.isEmpty(descricao.getText().toString())) {
            titulo.requestFocus();
            return false;
        } else return true;
    }
}