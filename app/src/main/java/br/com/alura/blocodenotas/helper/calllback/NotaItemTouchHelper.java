package br.com.alura.blocodenotas.helper.calllback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.dao.NotasDao;
import br.com.alura.blocodenotas.dialog.DialogBack;
import br.com.alura.blocodenotas.model.Nota;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.ListaNotasAdapter;

public class NotaItemTouchHelper extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;
    private Context context;

    public NotaItemTouchHelper(ListaNotasAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int delete = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int deslizar = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(deslizar, delete);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = viewHolder1.getAdapterPosition();
        return false;
    }

    //TODO necessário encontrar uma forma de excluir com o Swipe
    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        final int posicao = viewHolder.getAdapterPosition();

        new DialogBack(context)
                .setTitle(context.getString(R.string.attention))
                .setMsg(context.getString(R.string.sure_deleted))
                .setOnSimListener((dialog, which) -> {
                    List<Nota> notas = new NotasDao(context).findAll();
                })
                .setOnNaoListener((dialog, which) -> {
                    dialog.dismiss();
                })
                .build().show();
    }
}
