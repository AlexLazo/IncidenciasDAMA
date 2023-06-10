package com.hgrimaldi.incidencias;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DatosRegistro extends AppCompatActivity {
    private int id;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_registro);
        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id_incidencia", 0);
            String name = intent.getStringExtra("descripcion");
            String fecha = intent.getStringExtra("fecha");
            String user = intent.getStringExtra("user");
            String EstadoIncidencia = intent.getStringExtra("EstadoIncidencia");
            String TipoIncidencia = intent.getStringExtra("TipoIncidencia");
            String imageUrl = intent.getStringExtra("imagenReferencia");

            TextView tvID = findViewById(R.id.tvIdRegis);
            TextView tvName = findViewById(R.id.tvDescripcionRegis);
            TextView tvFecha = findViewById(R.id.tvFechaRegis);
            TextView tvUser = findViewById(R.id.tvUserRegis);
            TextView tvEstado = findViewById(R.id.tvEstadoRegis);
            TextView tvTipo = findViewById(R.id.tvTipoRegis);
            ImageView imagen = findViewById(R.id.imgViewRegis);

            tvID.setText(String.valueOf(id));
            tvName.setText(name);
            tvFecha.setText(fecha);
            tvUser.setText(user);
            tvEstado.setText(EstadoIncidencia);
            tvTipo.setText(TipoIncidencia);

            Picasso.get().load(imageUrl).into(imagen);
        }

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
                deleteDataWithApi(id);
            }
        });
    }

    private void deleteDataWithApi(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación")
                .setMessage("¿Seguro desea borrar estos datos?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performDataDeletion(id);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada
                    }
                })
                .show();
    }

    private void performDataDeletion(int id) {
        String apiUrl = "http://192.168.0.184/app_incidencias/incidencia/delete.php";

        StringRequest deleteRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DatosRegistro.this, "Datos eliminados correctamente", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(DatosRegistro.this, AdministradorActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DatosRegistro.this, "Error eliminando datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_incidencia", String.valueOf(id));
                return params;
            }
        };

        queue.add(deleteRequest);
    }
}