package com.hgrimaldi.incidencias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetalleUsuarioActivity extends AppCompatActivity {

    TextView tvid,tvname,tvuser,tvpassword,tvidrol,tvidestadoempleado;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_usuario);

        //Inicializando vistas
        tvid = findViewById(R.id.txtid);
        tvname = findViewById(R.id.txtname);
        tvuser = findViewById(R.id.txuser);
        tvpassword = findViewById(R.id.txpassword);
        tvidrol = findViewById(R.id.txtrol);
        tvidestadoempleado = findViewById(R.id.txtidestado);

        Intent intent =getIntent();
        position = intent.getExtras().getInt("position");

        tvid.setText("ID: "+UsuariosActivity.usuarioArrayList.get(position).getId_usuario());
        tvname.setText("Nombre: "+UsuariosActivity.usuarioArrayList.get(position).getNombrecompleto());
        tvuser.setText("User: "+UsuariosActivity.usuarioArrayList.get(position).getUser());
        tvpassword.setText("Password: "+UsuariosActivity.usuarioArrayList.get(position).getPassword());
        tvidrol.setText("Rol: "+UsuariosActivity.usuarioArrayList.get(position).getId_rol());
        tvidestadoempleado.setText("Estado: "+UsuariosActivity.usuarioArrayList.get(position).getId_estadoEmpleado());


    }
}