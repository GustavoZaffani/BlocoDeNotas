package br.com.alura.blocodenotas.ui.recyclerView.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.alura.blocodenotas.R;
import br.com.alura.blocodenotas.model.Login;
import br.com.alura.blocodenotas.ui.recyclerView.adapter.listener.OnLogClickListener;

public class ListaLogsAdapter extends RecyclerView.Adapter<ListaLogsAdapter.LogViewHolder> {

    private List<Login> logs;
    private Context ctx;
    private OnLogClickListener onLogClickListener;

    public ListaLogsAdapter(List<Login> logins, Context context) {
        this.logs = logins;
        this.ctx = context;
    }

    public void setOnLogClickListener(OnLogClickListener onLogClickListener) {
        this.onLogClickListener = onLogClickListener;
    }

    @NonNull
    @Override
    public ListaLogsAdapter.LogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(ctx)
                .inflate(R.layout.logs_login, viewGroup, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder logViewHolder, int i) {
        Login login = logs.get(i);
        logViewHolder.vincula(login);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public void adicionaLog() {
        notifyDataSetChanged();
    }

    public class LogViewHolder extends RecyclerView.ViewHolder {

        private TextView user;
        private TextView token;
        private TextView data;

        private Login login;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.log_user);
            token = itemView.findViewById(R.id.log_token);
            data = itemView.findViewById(R.id.log_data);

            itemView.setOnClickListener(view -> {
                onLogClickListener.onLogClick(login, getAdapterPosition());
            });
        }

        public void vincula(Login login) {
            this.login = login;
            preencheCampos(login);
        }

        private void preencheCampos(Login login) {
            user.setText(login.getUsuario());
            token.setText(login.getIdToken());
            data.setText(login.getHoraLogin());
        }
    }
}
