package com.hgrimaldi.incidencias;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.ProductViewHolder> {
    private Context mCtx;
    private List<ItemList> incidenciaList;

    private String domaing_image = "http://192.168.0.184:80/app_incidencias/incidencia/fotos/";

    public IncidenciaAdapter(Context mCtx, List<ItemList> incidenciaList) {
        this.mCtx = mCtx;
        this.incidenciaList = incidenciaList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_list_view, null);
        return new ProductViewHolder(view);
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

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewDescripcion = itemView.findViewById(R.id.tvDescripcion);
            textViewFecha = itemView.findViewById(R.id.tvFecha);
            textViewTipo = itemView.findViewById(R.id.tvTipoIncidencia);
            textViewEstado = itemView.findViewById(R.id.tvEstadoIncidencia);
            textViewUser = itemView.findViewById(R.id.tvUser);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
