package com.hgrimaldi.incidencias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IncidenciaEmpleado extends AppCompatActivity {
    ArrayList<TipoIncidenciaModel> tipoList = new ArrayList<>();
    TipoIncidenciaSpinnerAdapter tipoIncidenciaSpinnerAdapter;
    ArrayList<EstadoIncidenciaModel> estadoLista = new ArrayList<>();
    EstadoIncidenciaSpinnerAdapter estadoIncidenciaSpinnerAdapter;

    RequestQueue requestQueue;

    int id_tipoincidencia;

    int id_estadoincidencia;

    String currentPhotoPath;

    static final int REQUEST_PERMISSION_CAMERA = 100;
    static final int REQUEST_TAKE_PHOTO = 101;

    TextView usuario;

    EditText date,descripcion;


    Spinner tipoIncidencia,EstadoIncidencia;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_empleado);
        requestQueue = Volley.newRequestQueue(this);

        usuario = findViewById(R.id.txtusuario);
        descripcion = findViewById(R.id.txtdescrip);
        date = findViewById(R.id.txFecha);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(IncidenciaEmpleado.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(String.valueOf(year)+"/"+String.valueOf(month+1)+"/0"+String.valueOf(dayOfMonth));
                    }
                },2023,05,01);
                dialog.show();
            }
        });
        tipoIncidencia = findViewById(R.id.sptipoincidencia);
        EstadoIncidencia = findViewById(R.id.spestadoincidencia);
        String url = "http://192.168.0.184/app_incidencias/incidencia/listarTipo.php";
        String url1 = "http://192.168.0.184/app_incidencias/incidencia/listarEstado.php";
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
                    tipoIncidenciaSpinnerAdapter = new TipoIncidenciaSpinnerAdapter(IncidenciaEmpleado.this, tipoList);
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
                    estadoIncidenciaSpinnerAdapter = new EstadoIncidenciaSpinnerAdapter(IncidenciaEmpleado.this, estadoLista);
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

    private void insertarIncidencia() {

        final String user = usuario.getText().toString().trim();
        final String descrip = descripcion.getText().toString().trim();
        final String fecha = date.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


        if(descrip.isEmpty()){
            Toast.makeText(this, "Ingrese una breve descripcion", Toast.LENGTH_SHORT).show();
            return;
        }


        else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.184/app_incidencias/incidencia/insert.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("IncidenciasAdministrador","Respuesta del error " +response);

                            if(response.equalsIgnoreCase("Datos Insertados")){
                                Toast.makeText(IncidenciaEmpleado.this, "Datos Insertados", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(IncidenciaEmpleado.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(IncidenciaEmpleado.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();


                    params.put("id_usuario",user);
                    params.put("id_tipoIncidencia", String.valueOf(id_tipoincidencia));
                    params.put("descripcion",descrip);
                    params.put("fecha", String.valueOf(date));
                    params.put("imagenReferencia","aj");
                    params.put("estadoIncidencia","3");
                    return params;
                }
            };
            RequestQueue requestQueue1 = Volley.newRequestQueue(IncidenciaEmpleado.this);
            requestQueue1.add(request);

        }

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, "jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return  image;
    }

    private void takePintureFullSize(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;

            try {
                photoFile = createImageFile();

            }catch (Exception e){
                e.getMessage();
            }
            if(photoFile !=null){
                Uri photoUri = FileProvider.getUriForFile(
                        IncidenciaEmpleado.this,
                        "com.hgrimaldi.incidencias",
                        photoFile
                );

                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}