package br.com.alura.blocodenotas.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.alura.blocodenotas.R;

public class DialogBack extends AlertDialog.Builder {

    private String sim;
    private String nao;

    public DialogBack(Context context) {
        super(context);
        this.setCancelable(false);
        this.setSim("Sim");
        this.setNao("Não");
    }

    public DialogBack setTitle(String title) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_dialog_msg, null);
        ((TextView) header.findViewById(R.id.header_title)).setText(title);
        this.setCustomTitle(header);
        return this;
    }

    public DialogBack setMsg(String msg) {
        this.setMessage(msg);
        return this;
    }

    public DialogBack setSim(String sim) {
        this.sim = sim;
        return this;
    }

    public DialogBack setNao(String nao) {
        this.nao = nao;
        return this;
    }

    public DialogBack setOnSimListener(DialogInterface.OnClickListener onSimListener) {
        this.setPositiveButton(this.sim, onSimListener);
        return this;
    }

    public DialogBack setOnNaoListener(DialogInterface.OnClickListener onNaoListener) {
        this.setNegativeButton(this.nao, onNaoListener);
        return this;
    }

    public Dialog build() {
        return this.create();
    }
}
