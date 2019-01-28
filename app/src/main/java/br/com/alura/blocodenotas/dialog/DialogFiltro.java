package br.com.alura.blocodenotas.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import br.com.alura.blocodenotas.R;

public class DialogFiltro extends AlertDialog.Builder {

    private String cancelar;
    private String filtrar;

    @SuppressLint("NewApi")
    public DialogFiltro(Context context) {
        super(context);
        this.setView(R.layout.dialog_filtro);
        this.setCancelable(true);
        this.setCancelar("CANCELAR");
        this.setFiltrar("FILTRAR");
    }

    public DialogFiltro setCancelar(String cancelar) {
        this.cancelar = cancelar;
        return this;
    }

    public DialogFiltro setFiltrar(String filtrar) {
        this.filtrar = filtrar;
        return this;
    }

    public DialogFiltro setOnCancelarListener(DialogInterface.OnClickListener onClickListener) {
        this.setNegativeButton(this.cancelar, onClickListener);
        return this;
    }

    public DialogFiltro setOnFiltrarListener(DialogInterface.OnClickListener onFiltrarListener) {
        this.setPositiveButton(this.filtrar, onFiltrarListener);
        return this;
    }

    public Dialog build(){
        return this.create();
    }
}
