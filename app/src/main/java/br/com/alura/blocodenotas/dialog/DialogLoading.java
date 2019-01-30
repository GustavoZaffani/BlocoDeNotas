package br.com.alura.blocodenotas.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import br.com.alura.blocodenotas.R;

public class DialogLoading extends AlertDialog.Builder implements Serializable {

    public DialogLoading(Context context, String msg) {
        super(context);
        this.setCancelable(false);

        View e = LayoutInflater.from(context).inflate(R.layout.dialog_aguarde, null);
        ((TextView) e.findViewById(R.id.txt_header)).setText(msg);
        this.setCustomTitle(e);
    }

    public Dialog build() {
        return this.create();
    }
}
