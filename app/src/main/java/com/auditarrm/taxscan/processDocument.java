package com.auditarrm.taxscan;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.documentai.v1beta3.Document;
import com.google.cloud.documentai.v1beta3.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1beta3.DocumentProcessorServiceSettings;
import com.google.cloud.documentai.v1beta3.ProcessRequest;
import com.google.cloud.documentai.v1beta3.ProcessResponse;
import com.google.cloud.documentai.v1beta3.RawDocument;
import com.google.protobuf.ByteString;
import com.itextpdf.text.DocumentException;
import com.tom_roush.harmony.awt.AWTColor;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import Controller.ArchivosControlador;
import View.Home;
import View.PDFViewer;

public class processDocument {

    ArchivosControlador archivosControlador;
    private Context context;
    String tipoArchivo;
    private List<String> scannedData = new ArrayList<>();

    public processDocument(Context context, String projectId, String location, String processorId, Uri filePath, Uri imgpath, String tipoArchivo)
            throws IOException, InterruptedException, ExecutionException, TimeoutException, DocumentException {

        this.context = context;
        this.tipoArchivo = tipoArchivo;
        archivosControlador = new ArchivosControlador();

        //Se usa OkHttp para crear un cliente y llamar a la API mediante HTTPS/REST
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                File pdfFile = null;
                //Se cargan las credenciales de la cuenta de servicio mediante un archivo .json
                InputStream credentialsStream = context.getAssets().open("organic-edge-430816-h1-abe528f07447.json");
                GoogleCredentials credentials = GoogleCredentials.
                        fromStream(credentialsStream).createScoped("https://www.googleapis.com/auth/cloud-platform");
                Log.i("depuracion", "credenciales obtenidas");

                // Inicializa el cliente (Llamar al metodo "close" al finalizar)
                DocumentProcessorServiceSettings documentProcessorServiceSettings =
                        DocumentProcessorServiceSettings.newHttpJsonBuilder()
                                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                                .build();

                // Crear el cliente del servicio
                try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(documentProcessorServiceSettings)) {
                    // The full resource name of the processor, e.g.:
                    // projects/project-id/locations/location/processor/processor-id
                    // You must create new processors in the Cloud Console first
                    String name = String.format("projects/%s/locations/%s/processors/%s", projectId, location, processorId);
                    Log.i("depuracion","Cliente creado");
                    scannedData.clear();
                    InputStream fileInputStream = getContentResolverInputStream(filePath);
                    if (fileInputStream == null) {
                        Log.e("depuracion", "No se pudo obtener el InputStream del archivo.");
                        return;
                    }
                    ByteString content = ByteString.readFrom(fileInputStream);
                    RawDocument document = RawDocument.newBuilder().setContent(content).setMimeType("application/pdf").build();

                    // Configuracion de la solicitud de API
                    ProcessRequest request =
                            ProcessRequest.newBuilder().setName(name).setRawDocument(document).build();

                    //Obtener el texto de la factura
                    ProcessResponse result = client.processDocument(request);
                    Document documentResponse = result.getDocument();

                    Log.i("depuracion","Documento Procesado");
                    List<float[]> rectangles = new ArrayList<>();
                    // Obtiene las entidades de la factura
                    for (Document.Entity entity : documentResponse.getEntitiesList()) {
                        // Imprime en un Log los resultados del escaneo y las coordenadas de los campos obtenidos
                        for (int x = 0; x < entity.getPropertiesCount(); x++) {

                            Log.i("factura", entity.getProperties(x).getType() + ": " + entity.getProperties(x).getMentionText()
                                    + "Posicion: " + entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly());

                            scannedData.add(entity.getProperties(x).getType() + ": " + entity.getProperties(x).getMentionText());
                            float x1 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(0).getX();
                            float y1 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(0).getY();
                            float x2 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(1).getX();
                            float y2 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(1).getY();
                            float x3 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(2).getX();
                            float y3 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(2).getY();
                            float x4 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(3).getX();
                            float y4 = entity.getProperties(x).getPageAnchor().getPageRefs(0).getBoundingPoly().getNormalizedVertices(3).getY();
                            float[] rectangle = {x1, y1, x2, y2, x3, y3, x4, y4};
                            rectangles.add(rectangle);
                        }
                    }
                    try {
                        File file = new File(filePath.getPath());
                        String pdfsinextension = file.getName();
                        pdfFile = new File(context.getExternalFilesDir(null),  pdfsinextension.substring(0, pdfsinextension.length() - 4)+".pdf");
                        PDDocument pdDocument = PDDocument.load(new File(file.getAbsolutePath()));
                        PDPage pdPage = pdDocument.getPage(0);
                        PDPageContentStream contentStream = new PDPageContentStream(pdDocument, pdPage, PDPageContentStream.AppendMode.APPEND, true);

                        if (tipoArchivo.equals("PDFArchivos")){
                            for (float[] rect : rectangles) {
                                float x1 = rect[0] * pdPage.getMediaBox().getWidth();
                                float y1 = pdPage.getMediaBox().getHeight() - (rect[1] * pdPage.getMediaBox().getHeight());
                                float x2 = rect[2] * pdPage.getMediaBox().getWidth();
                                float y2 = pdPage.getMediaBox().getHeight() - (rect[3] * pdPage.getMediaBox().getHeight());
                                float x3 = rect[4] * pdPage.getMediaBox().getWidth();
                                float y3 = pdPage.getMediaBox().getHeight() - (rect[5] * pdPage.getMediaBox().getHeight());
                                float x4 = rect[6] * pdPage.getMediaBox().getWidth();
                                float y4 = pdPage.getMediaBox().getHeight() - (rect[7] * pdPage.getMediaBox().getHeight());

                                //Dibuja los rectangulos de los campos seleccionados
                                contentStream.setStrokingColor(AWTColor.RED);
                                contentStream.setLineWidth(2);
                                contentStream.moveTo(x1, y1);
                                contentStream.lineTo(x2, y2);
                                contentStream.lineTo(x3, y3);
                                contentStream.lineTo(x4, y4);
                                contentStream.closeAndStroke();
                            }
                            //Guarda el pdf con los rectangulos dibujados
                            contentStream.close();
                            pdDocument.save(pdfFile);
                            pdDocument.close();
                            Log.i("depuracion", "PDF modificado guardado en: " + pdfFile.getAbsolutePath());
                        } else{
                            for (float[] rect : rectangles) {
                                float x1 = rect[0] * pdPage.getMediaBox().getWidth();
                                float y1 = rect[1] * pdPage.getMediaBox().getHeight();
                                float x2 = rect[2] * pdPage.getMediaBox().getWidth();
                                float y2 = rect[3] * pdPage.getMediaBox().getHeight();
                                float x3 = rect[4] * pdPage.getMediaBox().getWidth();
                                float y3 = rect[5] * pdPage.getMediaBox().getHeight();
                                float x4 = rect[6] * pdPage.getMediaBox().getWidth();
                                float y4 = rect[7] * pdPage.getMediaBox().getHeight();

                                contentStream.setStrokingColor(AWTColor.RED);
                                contentStream.setLineWidth(2);
                                contentStream.moveTo(x1, y1);
                                contentStream.lineTo(x2, y2);
                                contentStream.lineTo(x3, y3);
                                contentStream.lineTo(x4, y4);
                                contentStream.closeAndStroke();
                            }
                            //Guarda el pdf con los rectangulos dibujados
                            contentStream.close();
                            pdDocument.save(pdfFile);
                            pdDocument.close();
                            Log.i("depuracion", "PDF modificado guardado en: " + pdfFile.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        Log.e("depuracion", "Error al modificar el PDF: " + e.getMessage(), e);
                    }
                }

                //Enviar la factura escaneada a la Home para mostrarla al usuario
                Uri pdfEscaneado = Uri.fromFile(pdfFile);
                Intent intentEscanear = new Intent(context, PDFViewer.class);
                Intent intent = new Intent(context, PDFViewer.class);
                intentEscanear.putExtra("pdfUri", pdfEscaneado.toString());
                intent.putStringArrayListExtra("scannedData", new ArrayList<>(scannedData));
                context.startActivity(intentEscanear);

            } catch (IOException e) {
                Log.e("depuracion", "Error al cargar las credenciales: " + e.getMessage(), e);
            }
        });
    }
    // Devuelve el archivo listo para escanear
    private InputStream getContentResolverInputStream(Uri uri) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.openInputStream(uri);
    }
}
