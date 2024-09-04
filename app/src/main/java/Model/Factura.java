package Model;

import java.util.Map;

public class Factura {

    private String id;                  // ID único de la factura
    private String fecha;                // Fecha de la factura
    private double valorTotal;              // Monto total de la factura
    private String nombreEmpresa;           // ID de la empresa a la que pertenece la factura
    private String imageUrl;            // URL de la imagen escaneada de la factura
    private Map<String, String> datosEscaneados;

    public Factura() {}

    public Factura(String id, String fecha, double valorTotal, String nombreEmpresa, String imagenUrl, Map<String, String> datosEscaneados) {
        this.id = id;
        this.fecha = fecha;
        this.valorTotal = valorTotal;
        this.nombreEmpresa = nombreEmpresa;
        this.imageUrl = imagenUrl;
        this.datosEscaneados = datosEscaneados;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, String> getDatosEscaneados() {
        return datosEscaneados;
    }

    public void setDatosEscaneados(Map<String, String> datosEscaneados) {
        this.datosEscaneados = datosEscaneados;
    }

}
