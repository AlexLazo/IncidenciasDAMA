package com.hgrimaldi.incidencias;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistarUsuarioActivity extends AppCompatActivity {
    EditText txtName,txtUser,txtPassword;
    Spinner txtId_rol,txtId_Estado;

    ArrayList<RolModel> rolList = new ArrayList<>();
    RolSpinnerAdapter rolSpinnerAdapter;
    ArrayList<EstadoModel> estadoList = new ArrayList<>();
    EstadoSpinnerAdapter estadoSpinnerAdapter;

    RequestQueue requestQueue;
    Button btn_insert;

    int id_rol;

    int id_estado;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_usuario);

        requestQueue = Volley.newRequestQueue(this);

        txtName     = findViewById(R.id.txtnombre);
        txtUser    = findViewById(R.id.txtuser);
        txtPassword  = findViewById(R.id.txtpassword);
        txtId_rol  = findViewById(R.id.rol);
        txtId_Estado = findViewById(R.id.estado);
        String url1 = "http://192.168.0.184/app_incidencias/usuario/listarRol.php";
        String url2 = "http://192.168.0.184/app_incidencias/usuario/listarEstado.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("rol");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id_rol"));
                        String rolname = jsonObject.optString("Rol");
                        rolList.add(new RolModel(id, rolname));
                    }
                    rolSpinnerAdapter = new RolSpinnerAdapter(RegistarUsuarioActivity.this, rolList);
                    txtId_rol.setAdapter(rolSpinnerAdapter);
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

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("estadoempleado");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int idd = Integer.parseInt(jsonObject.getString("id_estadoEmpleado"));
                        String estadoname = jsonObject.optString("Estado");
                       estadoList.add(new EstadoModel(idd,estadoname));
                    }
                    estadoSpinnerAdapter = new EstadoSpinnerAdapter(RegistarUsuarioActivity.this,estadoList);
                    txtId_Estado.setAdapter(estadoSpinnerAdapter);
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
        selectIdRol();
        selectIdEstado();

        btn_insert = findViewById(R.id.btnmodificar);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertData();
                Intent myIntent = new Intent(context,MainActivity.class);
                context.startActivity(myIntent);
            }
        });
    }

    private void selectIdRol() {
        txtId_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RolModel rol = (RolModel) parent.getItemAtPosition(position);
                id_rol = rol.getId_rol();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void selectIdEstado() {
        txtId_Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EstadoModel estado = (EstadoModel) parent.getItemAtPosition(position);
                id_estado = estado.getId_estadoEmpleado();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void insertData() {

        final String name = txtName.getText().toString().trim();
        final String user = txtUser.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");

        if(name.isEmpty()){
            Toast.makeText(this, "Ingresar nombre", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(user.isEmpty()){
            Toast.makeText(this, "Ingresar usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.isEmpty()){
            Toast.makeText(this, "Ingresar contraseña", Toast.LENGTH_SHORT).show();
            return;
        }


        else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.184/app_incidencias/usuario/insert.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("RegistrarUsuarioActivity","Respuesta del error " +response);

                            if(response.equalsIgnoreCase("Datos ingresados")){
                                Toast.makeText(RegistarUsuarioActivity.this, "Datos ingresados", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(RegistarUsuarioActivity.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistarUsuarioActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();

                    params.put("nombrecompleto",name);
                    params.put("user",user);
                    params.put("password",password);
                    params.put("id_rol", String.valueOf(id_rol));
                    params.put("id_estadoEmpleado", String.valueOf(id_estado));
                    return params;
                }
            };
            RequestQueue requestQueue1 = Volley.newRequestQueue(RegistarUsuarioActivity.this);
            requestQueue1.add(request);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
}
}