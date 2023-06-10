package com.hgrimaldi.incidencias;

import android.os.Environment;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PdfGenerator {

    public static void generatePDF() {
        // Check if external storage is available
        if (!isExternalStorageAvailable()) {
            // Handle the error or display a toast message
            return;
        }

        String filePath = Environment.getExternalStorageDirectory().getPath() + "/report.pdf";

        try {
            // Create a new document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for writing to the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set font and font size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Write content to the page
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Incident Report");
            contentStream.endText();

            contentStream.close();

            // Save the document
            document.save(filePath);
            document.close();

            // Handle the success or display a toast message
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error or display a toast message
        }
    }

    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
