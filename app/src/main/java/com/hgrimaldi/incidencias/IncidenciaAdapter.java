package com.hgrimaldi.incidencias;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.ProductViewHolder> {
    private  final RecyclerViewInterface recyclerViewInterface;
    private Context mCtx;
    private List<ItemList> incidenciaList;

    private String domaing_image = "https://damaapirest.000webhostapp.com/incidencia/fotos/";

    public IncidenciaAdapter(Context mCtx, List<ItemList> incidenciaList,
                             RecyclerViewInterface recyclerViewInterface) {
        this.mCtx = mCtx;
        this.incidenciaList = incidenciaList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_list_view, null);
        return new ProductViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ItemList incidencia = incidenciaList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(incidencia.getImage())
                .into(holder.imageView);

        holder.textViewDescripcion.setText(incidencia.getDescripcion());
        holder.textViewFecha.setText(incidencia.getFecha());
        holder.textViewTipo.setText(String.valueOf(incidencia.getTipo()));
        holder.textViewEstado.setText(String.valueOf(incidencia.getEstadoIncidencia()));
        holder.textViewUser.setText(String.valueOf(incidencia.getuser()));
    }

    @Override
    public int getItemCount() {
        return incidenciaList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDescripcion, textViewFecha, textViewTipo, textViewEstado, textViewUser;
        ImageView imageView;

        public ProductViewHolder(View itemView,
                                 RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            textViewDescripcion = itemView.findViewById(R.id.tvDescripcion);
            textViewFecha = itemView.findViewById(R.id.tvFecha);
            textViewTipo = itemView.findViewById(R.id.tvTipoIncidencia);
            textViewEstado = itemView.findViewById(R.id.tvEstadoIncidencia);
            textViewUser = itemView.findViewById(R.id.tvUser);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
