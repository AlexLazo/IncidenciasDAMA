package com.hgrimaldi.incidencias;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsuariosActivity extends AppCompatActivity {

    ListView listView;
    MyAdapter adapter;

    public static ArrayList<Usuario> usuarioArrayList = new ArrayList<>();
    String url = "https://damaapirest.000webhostapp.com/usuario/retrieve.php";
    Usuario usuario;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        listView = findViewById(R.id.myListView);
        adapter = new MyAdapter(this,usuarioArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"Ver datos","Editar datos","Eliminar datos"};
                builder.setTitle(usuarioArrayList.get(position).getNombrecompleto());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                startActivity(new Intent(getApplicationContext(),DetalleUsuarioActivity.class)
                                        .putExtra("position",position));
                                break;
                            case 1:
                                startActivity(new Intent(getApplicationContext(),ModificarUsuarioActivity.class)
                                        .putExtra("position",position));
                                break;
                            case 2:
                                deleteData(usuarioArrayList.get(position).getId_usuario());
                                break;
                        }
                    }
                });


                builder.create().show();


            }
        });
        retrieveData();
    }
    private void deleteData(String id) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.184/app_incidencias/usuario/delete.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            Toast.makeText(UsuariosActivity.this, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(UsuariosActivity.this, "Datos no eliminados", Toast.LENGTH_SHORT).show();
                        }
                        Intent myIntent = new Intent(context,MainActivity.class);
                        context.startActivity(myIntent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UsuariosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("id_usuario", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void retrieveData() {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        usuarioArrayList.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("usuario");

                            if(sucess.equals("1")){


                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id_usuario = object.getString("id_usuario");
                                    String nombrecompleto = object.getString("nombrecompleto");
                                    String user = object.getString("user");
                                    String password = object.getString("password");
                                    String id_rol = object.getString("id_rol");
                                    String estadoEmpleado = object.getString("id_estadoEmpleado");

                                    usuario = new Usuario(id_usuario,nombrecompleto,user,password,id_rol,estadoEmpleado);
                                    usuarioArrayList.add(usuario);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UsuariosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void btn_add_activity(View view) {
        startActivity(new Intent(getApplicationContext(),RegistarUsuarioActivity.class));
}
}