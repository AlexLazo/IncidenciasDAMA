package com.hgrimaldi.incidencias;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdministradorActivity extends AppCompatActivity {
    ImageView incidencia,usuario;

    private Button btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdministradorActivity.this);
                builder.setTitle("Mensaje de confirmación").
                        setMessage("Está seguro, desea cerrar sesión?");
                builder.setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });
                usuario = findViewById(R.id.Vuser);
                usuario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent UsuarioActivity = new Intent(AdministradorActivity.this, UsuariosActivity.class);
                        startActivity(UsuarioActivity);
                    }
                });

                incidencia = findViewById(R.id.vinci);


                incidencia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent incidenciaActivity = new Intent(AdministradorActivity.this, IncidenciasAdministrador.class);
                        startActivity(incidenciaActivity);
                    }
                });
    }
    }


