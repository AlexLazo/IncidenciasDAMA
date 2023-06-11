package com.hgrimaldi.incidencias;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegistrosActivity extends AppCompatActivity implements RecyclerViewInterface {
    private static final String URL_PRODUCTS = "https://damaapirest.000webhostapp.com/incidencia/incidencia.php";
    private List<ItemList> incidenciaList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

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

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDownloadConfirmationDialog();
            }
        });
    }
    private void showDownloadConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Descargar PDF");
        builder.setMessage("Quieres descargar el PDF?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DownloadPdfTask(RegistrosActivity.this).execute();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or handle other actions
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
                        error.printStackTrace();
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
        intent.putExtra("imagenReferencia", incidenciaList.get(position).getImage());
        intent.putExtra("user", incidenciaList.get(position).getuser());
        intent.putExtra("TipoIncidencia", incidenciaList.get(position).getTipo());
        intent.putExtra("EstadoIncidencia", incidenciaList.get(position).getEstadoIncidencia());

        startActivity(intent);
    }

    private static class DownloadPdfTask extends AsyncTask<Void, Void, File> {
        private Context context;

        public DownloadPdfTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(Void... voids) {
            try {
                URL url = new URL("https://damaapirest.000webhostapp.com/incidencia/generate_pdf.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    File pdfFile = new File(context.getExternalFilesDir(null), "incidencia.pdf");
                    FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }

                    fileOutputStream.close();
                    inputStream.close();

                    return pdfFile;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                openPdfFile(file);
            }
        }

        private void openPdfFile(File file) {
            Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
