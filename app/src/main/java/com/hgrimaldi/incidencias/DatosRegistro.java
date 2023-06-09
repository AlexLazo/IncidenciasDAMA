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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_registro);

        int id = getIntent().getIntExtra("id_incidencia", 0);
        String name = getIntent().getStringExtra("descripcion");
        String fecha = getIntent().getStringExtra("fecha");
        String user = getIntent().getStringExtra("user");
        String EstadoIncidencia = getIntent().getStringExtra("EstadoIncidencia");
        String TipoIncidencia = getIntent().getStringExtra("TipoIncidencia");
        String imageUrl = getIntent().getStringExtra("imagenReferencia");

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

        Picasso.get().load(imageUrl).into(imagen);

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
                deleteDataWithApi();
            }
        });
    }

    private void deleteDataWithApi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación")
                .setMessage("¿Seguro desea borrar estos datos?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performDataDeletion();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
    }

    private void performDataDeletion() {
        String apiUrl = "http://restapi-dama.000.pe/incidencia/delete.php";
        int id = getIntent().getIntExtra("id_incidencia", 0);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, apiUrl,
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Pass the id_incidencia parameter in the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("id_incidencia", String.valueOf(id));
                return headers;
            }
        };

        queue.add(deleteRequest);
    }
}