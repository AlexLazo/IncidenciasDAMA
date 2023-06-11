package com.hgrimaldi.incidencias;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IncidenciasAdministrador extends AppCompatActivity {

    ArrayList<TipoIncidenciaModel> tipoList = new ArrayList<>();
    TipoIncidenciaSpinnerAdapter tipoIncidenciaSpinnerAdapter;
    ArrayList<EstadoIncidenciaModel> estadoLista = new ArrayList<>();
    EstadoIncidenciaSpinnerAdapter estadoIncidenciaSpinnerAdapter;

    RequestQueue requestQueue;

    int id_tipoincidencia;

    int id_estadoincidencia;
    Bitmap bitmap ,decode;
    int PICK_IMAGE_REQUEST = 1;

    TextView usuario;
    ImageView imageView;
    EditText date,descripcion;
    Spinner tipoIncidencia,EstadoIncidencia;
    Button tomarfoto,registrar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias_administrador);

        requestQueue = Volley.newRequestQueue(this);

        usuario = findViewById(R.id.txtUserModif);
        descripcion = findViewById(R.id.txtdescripcion);
        imageView = findViewById(R.id.imgFoto);
        date = findViewById(R.id.txtFechaModif);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(IncidenciasAdministrador.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(String.valueOf(year)+"/"+String.valueOf(month+1)+"/0"+String.valueOf(dayOfMonth));
                    }
                },2023,05,01);
                dialog.show();
            }
        });
        tipoIncidencia = findViewById(R.id.spTtipoIncidencia);
        EstadoIncidencia = findViewById(R.id.spEstado);
        String url = "https://damaapirest.000webhostapp.com/incidencia/listarTipo.php";
        String url1 = "https://damaapirest.000webhostapp.com/incidencia/listarEstado.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("tipoincidencia");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id_tipIncidencia"));
                        String tiponame = jsonObject.optString("TipoIncidencia");
                        tipoList.add(new TipoIncidenciaModel(id, tiponame));
                    }
                    tipoIncidenciaSpinnerAdapter = new TipoIncidenciaSpinnerAdapter(IncidenciasAdministrador.this, tipoList);
                    tipoIncidencia.setAdapter(tipoIncidenciaSpinnerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

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
                    estadoIncidenciaSpinnerAdapter = new EstadoIncidenciaSpinnerAdapter(IncidenciasAdministrador.this, estadoLista);
                    EstadoIncidencia.setAdapter(estadoIncidenciaSpinnerAdapter);
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
        selectIdTipoIncidencia();
        tomarfoto = findViewById(R.id.btnFoto2);
        tomarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        registrar = findViewById(R.id.btnResgistrar2);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarIncidencia();
            }
        });

    }

    private void selectIdTipoIncidencia() {
        tipoIncidencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoIncidenciaModel tipo = (TipoIncidenciaModel) parent.getItemAtPosition(position);
                id_tipoincidencia = tipo.getId_tipIncidencia();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void selectIdEstadoIncidencia() {
        EstadoIncidencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EstadoIncidenciaModel estado = (EstadoIncidenciaModel) parent.getItemAtPosition(position);
                id_estadoincidencia = estado.getId_estadoIncidencia();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes = baos.toByteArray();
        String encodeImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodeImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"selecciona imagen"),PICK_IMAGE_REQUEST);
    }

    private void insertarIncidencia() {

        final String descrip = descripcion.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


        if(descrip.isEmpty()){
            Toast.makeText(this, "Ingrese una breve descripcion", Toast.LENGTH_SHORT).show();
            return;
        }


        else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "https://damaapirest.000webhostapp.com/incidencia/insert.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("IncidenciasAdministrador","Respuesta del error " +response);

                            if(response.equalsIgnoreCase("Datos Insertados")){
                                Toast.makeText(IncidenciasAdministrador.this, "Datos Insertados", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(IncidenciasAdministrador.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(IncidenciasAdministrador.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String imagen =getStringImage(bitmap);
                    String usuario="usuario";
                    String fecha = date.getText().toString().trim();
                    SharedPreferences sharedPreferences=getSharedPreferences(usuario, Context.MODE_PRIVATE);
                    String idUsuarioSesion = sharedPreferences.getString("idUsuario", "");
                    Map<String, String> params = new HashMap<String, String>();



                    params.put("id_usuario",idUsuarioSesion);
                    params.put("id_tipoIncidencia", String.valueOf(id_tipoincidencia));
                    params.put("descripcion",descrip);
                    params.put("fecha", fecha);
                    params.put("imagenReferencia",imagen);
                    params.put("estadoIncidencia","3");
                    return params;
                }
            };
            RequestQueue requestQueue1 = Volley.newRequestQueue(IncidenciasAdministrador.this);
            requestQueue1.add(request);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}