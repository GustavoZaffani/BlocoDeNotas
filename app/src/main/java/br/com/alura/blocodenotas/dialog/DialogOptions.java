package br.com.alura.blocodenotas.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.alura.blocodenotas.R;

public class DialogOptions extends AlertDialog.Builder {

    private String editar;
    private String excluir;

    @SuppressLint("NewApi")
    public DialogOptions(Context context) {
        super(context);
        this.setView(R.layout.dialog_options);
        this.setCancelable(true);
        this.setEditar("Editar");
        this.setExcluir("Excluir");
    }

    public DialogOptions setEditar(String editar) {
        this.editar = editar;
        return this;
    }

    public DialogOptions setExcluir(String excluir) {
        this.excluir = excluir;
        return this;
    }

    public DialogOptions setOnEditarListener(DialogInterface.OnClickListener onEditarListener) {
        this.setPositiveButton(this.editar, onEditarListener);
        return this;
    }

    public DialogOptions setOnExcluirListener(DialogInterface.OnClickListener onExcluirListener) {
        this.setNegativeButton(this.excluir, onExcluirListener);
        return this;
    }

    public Dialog build() {
        return this.create();
    }
}
