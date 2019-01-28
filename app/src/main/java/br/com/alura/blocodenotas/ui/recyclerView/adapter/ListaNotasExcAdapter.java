package br.com.alura.blocodenotas.ui.recyclerView.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Lixeira;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.listener.OnItemExClickListener;

public class ListaNotasExcAdapter extends RecyclerView.Adapter<ListaNotasExcAdapter.NotaViewHolder> {

    private List<Lixeira> notasExcluidas;
    private Context context;
    private OnItemExClickListener onItemClickListener;

    public ListaNotasExcAdapter(List<Lixeira> notas, Context context) {
        this.notasExcluidas = notas;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemExClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaNotasExcAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_excluido, viewGroup, false);
        return new NotaViewHolder(viewCriada);
    }

    public void altera(int posicao, Lixeira nota) {
        notasExcluidas.set(posicao, nota);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ListaNotasExcAdapter.NotaViewHolder notaViewHolder, int i) {
        Lixeira nota = notasExcluidas.get(i);
        notaViewHolder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return notasExcluidas.size();
    }

    public class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView data;
        private Lixeira nota;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            data = itemView.findViewById(R.id.item_data);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemExClickListener(nota, getAdapterPosition());
                }
            });
        }

        public void vincula(Lixeira nota) {
            this.nota = nota;
            preencheCampos(nota);
        }

        private void preencheCampos(Lixeira nota) {
            titulo.setText(nota.getTitulo());
            data.setText(nota.getData());
        }
    }

    public void adicionaNota() {
        notifyDataSetChanged();
    }
}
