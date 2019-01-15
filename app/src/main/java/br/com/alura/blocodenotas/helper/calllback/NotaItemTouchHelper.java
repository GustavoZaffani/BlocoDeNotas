package br.com.alura.blocodenotas.helper.calllback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import br.com.alura.blocodenotas.dao.NotasDao;
import br.com.alura.blocodenotas.ui.activity.ListaNotasActivity;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.ListaNotasAdapter;

public class NotaItemTouchHelper extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;
    private Context context;

    public NotaItemTouchHelper(ListaNotasAdapter adapter) {
        this.adapter = adapter;
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
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int posicao = viewHolder.getAdapterPosition();
        Log.i("posicao que swiped ", String.valueOf(posicao));
    }

    private void removeNota(int posicao) {

    }
}
