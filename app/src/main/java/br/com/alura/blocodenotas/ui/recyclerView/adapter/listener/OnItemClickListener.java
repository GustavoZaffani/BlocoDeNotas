package br.com.alura.blocodenotas.ui.recyclerView.adapter.listener;

import br.com.alura.blocodenotas.model.Nota;

public interface OnItemClickListener {

    void onItemClick(Nota nota, int posicao);
}
