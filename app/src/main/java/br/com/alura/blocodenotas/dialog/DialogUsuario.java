package br.com.alura.blocodenotas.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import br.com.alura.blocodenotas.R;

public class DialogUsuario extends AlertDialog.Builder {

    private String salvar;
    private String cancelar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DialogUsuario(@NonNull Context context) {
        super(context);
        this.setCancelable(true);
        this.setSalvar("SALVAR");
        this.setCancelar("CANCELAR");
        this.setView(R.layout.dialog_usuario);
    }

    public DialogUsuario setCancelar(String cancelar) {
        this.cancelar = cancelar;
        return this;
    }

    public DialogUsuario setSalvar(String salvar) {
        this.salvar = salvar;
        return this;
    }

    public DialogUsuario setOnSalvarListener (DialogInterface.OnClickListener onSalvarListener) {
        this.setPositiveButton(this.salvar, onSalvarListener);
        return this;
    }

    public DialogUsuario setOnCancelarListener (DialogInterface.OnClickListener onCancelarListener) {
        this.setNegativeButton(this.cancelar, onCancelarListener);
        return this;
    }

    public Dialog build() {
        return this.create();
    }

}
