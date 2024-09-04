package View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auditarrm.taxscan.R;
import com.auditarrm.taxscan.processDocument;
import com.itextpdf.text.DocumentException;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import Controller.ArchivosControlador;

public class Home extends AppCompatActivity {

    private ArchivosControlador archivosControlador;
    private RecyclerView recyclerView;
    private static final int COD_SEL_IMAGE = 300;
    private processDocument imagenFactura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        archivosControlador = new ArchivosControlador();

        //Busca si hay facturas escaneadas por mostrar al usuario
        Intent pdfEscaneadoCamara = getIntent();
        if (pdfEscaneadoCamara != null && pdfEscaneadoCamara.hasExtra("PDFCamara")) {
            String pdfUriString = pdfEscaneadoCamara.getStringExtra("PDFCamara");
            Uri pdfUri = Uri.parse(pdfUriString);
            String projectid = "organic-edge-430816-h1";
            String processorid = "281532ab18fdd0b3";
            String location = "us";
            try {
                imagenFactura = new processDocument(this, projectid, location, processorid, pdfUri, pdfUri, "PDFCamara");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        } else if (pdfEscaneadoCamara != null && pdfEscaneadoCamara.hasExtra("PDFEscaneado")) {
            String pdfUriString = pdfEscaneadoCamara.getStringExtra("PDFEscaneado");
            Uri PDFsubir = Uri.parse(pdfUriString);
            archivosControlador.subirFactura(PDFsubir);
        }

        //Inicia los elementos xml
        Button camerax = findViewById(R.id.Button_opencam);
        Button añadir_factura = findViewById(R.id.Button_addfact);
        Button ajustes = findViewById(R.id.Button_ajustes);
        recyclerView = findViewById(R.id.Facturas_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Cargar imágenes en el RecyclerView usando HomeController
        archivosControlador.cargarFacturas(recyclerView, this);

        //Boton para abrir la camara
        camerax.setOnClickListener(view -> abrirCamara());

        ajustes.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Ajustes.class);
            startActivity(intent);
        });

        añadir_factura.setOnClickListener(view -> abrirSeleccionFactura());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == COD_SEL_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri image_url = data.getData();
            String projectid = "organic-edge-430816-h1";
            String processorid = "281532ab18fdd0b3";
            String location = "us";
            // Obtener el tipo MIME del archivo seleccionado
            String mimeType = getContentResolver().getType(image_url);
            Log.i("depuracion", "Uri seleccionada: " + image_url.toString());
            Log.i("depuracion", "MIME Type: " + mimeType);

            // Manejo de archivos seleccionados dependiendo del MIME Type
            if (mimeType != null && mimeType.startsWith("image/")) {
                // Procesar imagen y convertirla a PDF
                try (InputStream inputStream = getContentResolver().openInputStream(image_url)) {
                    if (inputStream != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            // Crear un PDF desde la imagen
                            PdfDocument document = new PdfDocument();
                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                            PdfDocument.Page page = document.startPage(pageInfo);
                            Canvas canvas = page.getCanvas();
                            canvas.drawBitmap(bitmap, 0, 0, null);
                            document.finishPage(page);
                            File pdfResultado = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".pdf");
                            try (FileOutputStream fos = new FileOutputStream(pdfResultado)) {
                                document.writeTo(fos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            document.close();

                            // Procesar el PDF generado
                            Uri nuevoPdf = Uri.fromFile(pdfResultado);
                            try {
                                imagenFactura = new processDocument(this, projectid, location, processorid, nuevoPdf, nuevoPdf, "PDFImagen");
                            } catch (IOException | InterruptedException | ExecutionException | TimeoutException | DocumentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (mimeType != null && mimeType.equals("application/pdf")) {
                try (InputStream inputStream = getContentResolver().openInputStream(image_url)) {
                    if (inputStream != null) {
                        PDDocument pdDocument = PDDocument.load(inputStream);
                        File pdfResultado = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".pdf");
                        pdDocument.save(pdfResultado);
                        pdDocument.close();

                        Uri nuevoPdf = Uri.fromFile(pdfResultado);
                        try {
                            imagenFactura = new processDocument(this, projectid, location, processorid, nuevoPdf, nuevoPdf, "PDFArchivos");
                        } catch (IOException | InterruptedException | ExecutionException | TimeoutException | DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Abre el seleccionador de archivos
    private void abrirSeleccionFactura() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, COD_SEL_IMAGE);
    }

    private void abrirCamara() {
        Intent intent = new Intent(Home.this, Camara_view.class);
        startActivity(intent);
    }

}