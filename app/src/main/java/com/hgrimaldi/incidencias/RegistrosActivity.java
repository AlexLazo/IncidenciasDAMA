package com.hgrimaldi.incidencias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class RegistrosActivity extends AppCompatActivity implements RecyclerViewInterface{
    private static final String URL_PRODUCTS = "http://192.168.0.184/app_incidencias/incidencia/incidencia.php";
    List<ItemList> incidenciaList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.rvLista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        incidenciaList = new ArrayList<>();

        loadProducts();
        FloatingActionButton fabAddIncidencia = findViewById(R.id.fabAddIncidencia);
        fabAddIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrosActivity.this, IncidenciasAdministrador.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int id = jsonObject.getInt("id_incidencia");
                                String descripcion = jsonObject.getString("descripcion");
                                String fecha = jsonObject.getString("fecha");
                                String imagenReferencia = jsonObject.getString("imagenReferencia");
                                String user = jsonObject.getString("user");
                                String tipoIncidencia = jsonObject.getString("TipoIncidencia");
                                String estadoIncidencia = jsonObject.getString("EstadoIncidencia");

                                incidenciaList.add(new ItemList(id, descripcion, fecha, imagenReferencia, user, tipoIncidencia, estadoIncidencia));
                            }

                            IncidenciaAdapter adapter = new IncidenciaAdapter(RegistrosActivity.this, incidenciaList, RegistrosActivity.this);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(RegistrosActivity.this, DatosRegistro.class);

        intent.putExtra("id_incidencia", incidenciaList.get(position).getId_incidencia());
        intent.putExtra("descripcion", incidenciaList.get(position).getDescripcion());
        intent.putExtra("fecha", incidenciaList.get(position).getFecha());
        intent.putExtra("user", incidenciaList.get(position).getuser());
        intent.putExtra("TipoIncidencia", incidenciaList.get(position).getTipo());
        intent.putExtra("EstadoIncidencia", incidenciaList.get(position).getEstadoIncidencia());
        intent.putExtra("imagenReferencia", incidenciaList.get(position).getImage());

        startActivity(intent);
    }
}