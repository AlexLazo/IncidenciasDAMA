package com.hgrimaldi.incidencias;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdministradorActivity extends AppCompatActivity {
    ImageView incidencia, usuario, inciRegis;
    private FloatingActionButton fabGenerateReport;
    private static final int PERMISSION_REQUEST_CODE = 123;

    private Button btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AdministradorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AdministradorActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    generatePDFReport();
                }
            }
        });

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

        inciRegis = findViewById(R.id.vinciRegis);
        inciRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IncidenciasRegisActivity = new Intent(AdministradorActivity.this, RegistrosActivity.class);
                startActivity(IncidenciasRegisActivity);
            }
        });
    }

    private void generatePDFReport() {
        // Define the file path and name
        String filePath = getExternalFilesDir(null) + "/incident_report.pdf";

        try {
            // Create a PdfWriter instance to write the PDF document to a file
            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(filePath));

            // Create a PdfDocument instance using the PdfWriter
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            // Create a Document instance to add content to the PDF
            Document document = new Document(pdfDocument);

            // Add content to the document
            document.add(new Paragraph("Incident Report"));

            // Retrieve data from the MySQL database
            String url = "jdbc:mysql://localhost:3306/id20885853_proyecto_moviles";
            String username = "id20885853_api";
            String password = "Proyecto_moviles1";

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM incidencia WHERE estadoIncidencia = 3");

            while (resultSet.next()) {
                int idIncidencia = resultSet.getInt("id_incidencia");
                int idUsuario = resultSet.getInt("id_usuario");
                int idTipoIncidencia = resultSet.getInt("id_tipoIncidencia");
                String descripcion = resultSet.getString("descripcion");
                String fecha = resultSet.getString("fecha");
                String imagenReferencia = resultSet.getString("imagenReferencia");
                int estadoIncidencia = resultSet.getInt("estadoIncidencia");

                // Add incident details to the PDF report
                document.add(new Paragraph("ID: " + idIncidencia));
                document.add(new Paragraph("User ID: " + idUsuario));
                document.add(new Paragraph("Incident Type ID: " + idTipoIncidencia));
                document.add(new Paragraph("Description: " + descripcion));
                document.add(new Paragraph("Date: " + fecha));

                // Add the image to the document
                if (imagenReferencia != null && !imagenReferencia.isEmpty()) {
                    Image image = new Image(ImageDataFactory.create(imagenReferencia));
                    document.add(image);
                }

                document.add(new Paragraph("Incident State: " + estadoIncidencia));
                document.add(new Paragraph("----------------------------------------"));
            }

            // Close the document
            document.close();

            // Display a success message
            showSuccessMessage("PDF Report generated successfully!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdministradorActivity.this);
        builder.setTitle("Success");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            generatePDFReport();
        }
    }
}


