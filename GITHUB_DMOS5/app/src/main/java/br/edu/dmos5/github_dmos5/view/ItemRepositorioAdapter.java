package br.edu.dmos5.github_dmos5.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.dmos5.github_dmos5.R;
import br.edu.dmos5.github_dmos5.model.Repositorio;

public class ItemRepositorioAdapter extends RecyclerView.Adapter<ItemRepositorioAdapter.RepositorioViewHolder> {

    private List<Repositorio> repositorios;
    private static RecyclerItemClickListener clickListener;

    public ItemRepositorioAdapter(@NonNull List<Repositorio> repositorios){
        this.repositorios = repositorios;
    }

    @NonNull
    @Override
    public ItemRepositorioAdapter.RepositorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        RepositorioViewHolder contatoViewHolder = new RepositorioViewHolder(view);
        return contatoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRepositorioAdapter.RepositorioViewHolder holder, int position) {
        holder.textViewRepositoCodigo.setText(repositorios.get(position).getId().toString());
        holder.textViewRepositoNome.setText(repositorios.get(position).getName());
        holder.textViewRepositoUrl.setText(repositorios.get(position).getHtml_url());
    }

    @Override
    public int getItemCount() {
        return repositorios.size();
    }

    public void setClickListener(RecyclerItemClickListener clickListener) {
        ItemRepositorioAdapter.clickListener = clickListener;
    }

    public class RepositorioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewRepositoCodigo;
        public TextView textViewRepositoNome;
        public TextView textViewRepositoUrl;

        public RepositorioViewHolder(@NonNull View itemView){
            super(itemView);
            textViewRepositoCodigo = itemView.findViewById(R.id.textview_reposito_codigo);
            textViewRepositoNome = itemView.findViewById(R.id.textview_reposito_nome);
            textViewRepositoUrl = itemView.findViewById(R.id.textview_reposito_url);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }
}
