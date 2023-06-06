package com.hgrimaldi.incidencias;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class ModificarUsuarioActivity extends AppCompatActivity {

    EditText edId,edName,edUser,edPassword;

    Button btnModificar;

    Spinner edRol,edEstado;
    private int position;

    Context context;

    int id_rol,id_RolActual;

    int id_estado,id_EstadoActual;
    ArrayList<RolModel> rolList = new ArrayList<>();
    RolSpinnerAdapter rolSpinnerAdapter;
    ArrayList<EstadoModel> estadoList = new ArrayList<>();
    EstadoSpinnerAdapter estadoSpinnerAdapter;

    RequestQueue requestQueue;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);

        requestQueue = Volley.newRequestQueue(this);

        edId = findViewById(R.id.txtId);
        edName = findViewById(R.id.txtnombre);
        edUser = findViewById(R.id.txtuser);
        edPassword = findViewById(R.id.txtpassword);
        edRol = findViewById(R.id.rol);
        edEstado = findViewById(R.id.estado);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        edId.setText(UsuariosActivity.usuarioArrayList.get(position).getId_usuario());
        edName.setText(UsuariosActivity.usuarioArrayList.get(position).getNombrecompleto());
        edUser.setText(UsuariosActivity.usuarioArrayList.get(position).getUser());
        edPassword.setText(UsuariosActivity.usuarioArrayList.get(position).getPassword());
        id_RolActual =(UsuariosActivity.usuarioArrayList.get(position).getId_rol());
        id_EstadoActual = (UsuariosActivity.usuarioArrayList.get(position).getId_estadoEmpleado());

        String url3 = "http://192.168.0.184/app_incidencias/usuario/listarRol.php";
        String url4 = "http://192.168.0.184/app_incidencias/usuario/listarEstado.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url3, null, new Response.Listener<JSONObject>() {
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
                    rolSpinnerAdapter = new RolSpinnerAdapter(ModificarUsuarioActivity.this, rolList);
                    edRol.setAdapter(rolSpinnerAdapter);
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

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, url4, null, new Response.Listener<JSONObject>() {
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
                    estadoSpinnerAdapter = new EstadoSpinnerAdapter(ModificarUsuarioActivity.this,estadoList);
                    edEstado.setAdapter(estadoSpinnerAdapter);

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
        setSelectIdRol(id_RolActual);
        setSelectIdEstado(id_EstadoActual);


        btnModificar = findViewById(R.id.btnmodificar);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_updateData();
            }
        });
    }

   private void selectIdRol() {
        edRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        edEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void setSelectIdRol(int idRol){
        for (int i = 0; i < edRol.getCount(); i++){
            RolModel item = (RolModel) edRol.getItemAtPosition(i);
            if (item.getId_rol() == idRol){
                edRol.setSelection(i);
            }
        }
    }

    private void setSelectIdEstado(int idEstado){
        for (int i = 0; i < edEstado.getCount(); i++){
            EstadoModel item = (EstadoModel) edEstado.getItemAtPosition(i);
            if (item.getId_estadoEmpleado() == idEstado){
                edEstado.setSelection(i);
            }
        }
    }

    public void btn_updateData() {

        final String name = edName.getText().toString();
        final String user = edUser.getText().toString();
        final String password = edPassword.getText().toString();
        final String id = edId.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Actualizando....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.184/app_incidencias/usuario/update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(ModificarUsuarioActivity.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),UsuariosActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ModificarUsuarioActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();

                params.put("id_usuario",id);
                params.put("nombrecompleto",name);
                params.put("user",user);
                params.put("password",password);
                params.put("id_rol", String.valueOf(id_rol));
                params.put("id_estadoEmpleado", String.valueOf(id_estado));

                return params;
            }
        };
        requestQueue.add(request);
    }
}