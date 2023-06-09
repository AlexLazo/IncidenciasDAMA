package com.hgrimaldi.incidencias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DatosRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_registro);

        String name = getIntent().getStringExtra("descripcion");
        String fecha = getIntent().getStringExtra("fecha");
        String user = getIntent().getStringExtra("user");
        String EstadoIncidencia = getIntent().getStringExtra("EstadoIncidencia");
        String TipoIncidencia = getIntent().getStringExtra("TipoIncidencia");
        int image = getIntent().getIntExtra("imagenReferencia", 0);

        TextView tvName = findViewById(R.id.tvDescripcionRegis);
        TextView tvFecha = findViewById(R.id.tvFechaRegis);
        TextView tvUser = findViewById(R.id.tvUserRegis);
        TextView tvEstado = findViewById(R.id.tvEstadoRegis);
        TextView tvTipo = findViewById(R.id.tvTipoRegis);
        ImageView imagen = findViewById(R.id.imgViewRegis);

        tvName.setText(name);
        tvFecha.setText(fecha);
        tvUser.setText(user);
        tvEstado.setText(EstadoIncidencia);
        tvTipo.setText(TipoIncidencia);
        imagen.setImageResource(image);

        Button btnEliminar, btnEditar;

        btnEditar = findViewById(R.id.btnEditarRegis);
        btnEliminar = findViewById(R.id.btnEliminarRegis);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent DatosRegistro = new Intent(DatosRegistro.this, ModificarRegistro.class);
                startActivity(DatosRegistro);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}