package com.hgrimaldi.incidencias;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModificarIncidenciaAdministrador extends AppCompatActivity {
    ArrayList<EstadoIncidenciaModel> estadoLista = new ArrayList<>();
    EstadoIncidenciaSpinnerAdapter estadoIncidenciaSpinnerAdapter;

    RequestQueue requestQueue;

    String id_estadoincidencia,id_EstadoIncidenciaActual;
    EditText date,descripcion,User;
    TextView tvTipo;
    Spinner tipoIncidencia;
    Spinner tvEstado;


    ImageView img;
    Button registrar;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_incidencia_administrador);

        requestQueue = Volley.newRequestQueue(this);

        int id = getIntent().getIntExtra("id_incidencia", 0);
        String name = getIntent().getStringExtra("descripcion");
        String fecha = getIntent().getStringExtra("fecha");
        String user = getIntent().getStringExtra("user");
        String EstadoIncidencia = getIntent().getStringExtra("EstadoIncidencia");
        String TipoIncidencia = getIntent().getStringExtra("TipoIncidencia");
        String imageUrl = getIntent().getStringExtra("imagenReferencia");

        tipoIncidencia = findViewById(R.id.spTtipoIncidencia);
        descripcion = findViewById(R.id.txtdescripcion);
        date = findViewById(R.id.txtFechaModif);
        User = findViewById(R.id.txtUserModif);
        tvEstado = findViewById(R.id.spEstado);
        //tvTipo = findViewById(R.id.txtipo);
        img = findViewById(R.id.imgFoto);

        descripcion.setText(name);
        date.setText(fecha);
        User.setText(user);
        tvTipo.setText(TipoIncidencia);

        Picasso.get().load(imageUrl).into(img);

        String url1 = "https://damaapirest.000webhostapp.com/incidencia/listarEstado.php";


        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("estadoincidencia");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int idd = Integer.parseInt(jsonObject.getString("id_estadoIncidencia"));
                        String estadoname = jsonObject.optString("EstadoIncidencia");
                        estadoLista.add(new EstadoIncidenciaModel(idd,estadoname));
                    }
                    estadoIncidenciaSpinnerAdapter = new EstadoIncidenciaSpinnerAdapter(ModificarIncidenciaAdministrador.this, estadoLista);
                    tvEstado.setAdapter(estadoIncidenciaSpinnerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest1);
        selectIdEstadoIncidencia();

        registrar = findViewById(R.id.btnResgistrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

    }



    private void selectIdEstadoIncidencia() {
        tvEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EstadoIncidenciaModel estado = (EstadoIncidenciaModel) parent.getItemAtPosition(position);
                id_estadoincidencia = estado.getEstadoIncidencia();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void updateData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Actualizando....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://damaapirest.000webhostapp.com/incidencia/update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(ModificarIncidenciaAdministrador.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),RegistrosActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ModificarIncidenciaAdministrador.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("estadoIncidencia", String.valueOf(id_estadoincidencia));

                return params;
            }
        };
        requestQueue.add(request);
    }
}