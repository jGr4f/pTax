package View;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.auditarrm.taxscan.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class PDFViewer extends AppCompatActivity {

    private ImageView pdfImageView;
    private File pdfFile;
    private Button showMenuButton;
    private LinearLayout scannedDataLayout;
    private boolean isMenuVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        pdfImageView = findViewById(R.id.pdfImageView);
        showMenuButton = findViewById(R.id.showMenuButton);
        scannedDataLayout = findViewById(R.id.scannedDataLayout);

        // Obtener la URI del PDF desde el Intent
        String pdfUrl = getIntent().getStringExtra("pdfUri");
        if (pdfUrl == null) {
            Log.e("depuracion", "No PDF URL provided");
            return;
        }
        ArrayList<String> scannedData = getIntent().getStringArrayListExtra("scannedData");
        // Descargar el archivo PDF desde la URL y guardarlo localmente
        pdfFile = new File(getCacheDir(), "example.pdf");
        downloadPdf(pdfUrl, pdfFile);

        // Mostrar la primera página del PDF
        showPdfPage(0);

        showMenuButton.setOnClickListener(view -> {
            if (isMenuVisible) {
                scannedDataLayout.setVisibility(android.view.View.GONE);
                showMenuButton.setText("Mostrar Datos Escaneados");
            } else {
                showMenuButton.setText("Ocultar Datos Escaneados");
                populateScannedData(scannedData);
                scannedDataLayout.setVisibility(View.VISIBLE);
            }
            isMenuVisible = !isMenuVisible;
        });
    }

    private void populateScannedData(ArrayList<String> scannedData) {
        scannedDataLayout.removeAllViews();  // Limpiar el layout

        if (scannedData != null) {
            for (String data : scannedData) {
                TextView textView = new TextView(this);
                textView.setText(data);
                textView.setPadding(16, 8, 16, 8);
                scannedDataLayout.addView(textView);
            }
        }
    }

    //Descarga el pdf para mostrarlo cuando se le de click
    private void downloadPdf(String pdfUrl, File outputFile) {
        Log.d("depuracion", "Attempting to download PDF from: " + pdfUrl);

        new Thread(() -> {
            try (InputStream inputStream = new URL(pdfUrl).openStream();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                runOnUiThread(() -> {
                    Log.d("depuracion", "PDF successfully downloaded to: " + outputFile.getAbsolutePath());
                    showPdfPage(0); // Show the PDF after download
                });
            } catch (IOException e) {
                Log.e("depuracion", "Error downloading PDF", e);
            }
        }).start();
    }

    // Muestra el pdf
    private void showPdfPage(int pageIndex) {
        if (pdfFile.exists()) {
            Log.d("depuracion", "Displaying PDF from: " + pdfFile.getAbsolutePath());
            try (ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)) {
                PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);

                if (pageIndex >= 0 && pageIndex < pdfRenderer.getPageCount()) {
                    PdfRenderer.Page page = pdfRenderer.openPage(pageIndex);

                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    pdfImageView.setImageBitmap(bitmap);

                    page.close();
                } else {
                    Log.e("depuracion", "Invalid page index: " + pageIndex);
                }
                pdfRenderer.close();
            } catch (IOException e) {
                Log.e("depuracion", "Error rendering PDF", e);
            }
        } else {
            Log.e("depuracion", "PDF file does not exist: " + pdfFile.getAbsolutePath());
        }
    }
}