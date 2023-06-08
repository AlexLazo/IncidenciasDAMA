package com.hgrimaldi.incidencias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistrosActivity extends AppCompatActivity {
    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_PRODUCTS = "http://192.168.0.184:80/app_incidencias/incidencia/incidencia.php";
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
    }

    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject items = array.getJSONObject(i);

                                incidenciaList.add(new ItemList (
                                        items.getString("descripcion"),
                                        items.getString("fecha"),
                                        items.getString("imagenReferencia"),
                                        items.getString("user"),
                                        items.getString("TipoIncidencia"),
                                        items.getString("EstadoIncidencia")
                                ));
                            }

                            IncidenciaAdapter adapter = new IncidenciaAdapter(RegistrosActivity.this, incidenciaList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}