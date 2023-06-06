package com.hgrimaldi.incidencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EstadoIncidenciaSpinnerAdapter extends ArrayAdapter<EstadoIncidenciaModel> {

    public EstadoIncidenciaSpinnerAdapter(@NonNull Context context, ArrayList<EstadoIncidenciaModel> listaModel) {
        super(context, 0, listaModel);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView (int position, View convertView,ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinnerlayaout,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.txtNombre);
        EstadoIncidenciaModel estado = getItem(position);
        if(estado != null){
            textView.setText(estado.getEstadoIncidencia());

        }
        return convertView;
    }
}