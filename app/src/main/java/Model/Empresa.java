package Model;

import java.util.List;

public class Empresa {
    private String idEmpresa;                   // ID único de la empresa
    private String nombreEmpresa;                 // Nombre de la empresa
    private String direccionEmpresa;              // Dirección de la empresa
    private String numeroTelEmpresa;          // Número de teléfono de la empresa
    private String emailEmpresa;                // Email de contacto de la empresa
    private List<String> facturasIDs;     // Lista de IDs de facturas asociadas a esta empresa

    // Constructor vacío (necesario para Firebase)
    public Empresa() {}

    public Empresa(String idEmpresa, String nombreEmpresa, String direccionEmpresa, String numeroTelEmpresa, String emailEmpresa, List<String> facturasIDs) {
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.direccionEmpresa = direccionEmpresa;
        this.numeroTelEmpresa = numeroTelEmpresa;
        this.emailEmpresa = emailEmpresa;
        this.facturasIDs = facturasIDs;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public String getNumeroTelEmpresa() {
        return numeroTelEmpresa;
    }

    public void setNumeroTelEmpresa(String numeroTelEmpresa) {
        this.numeroTelEmpresa = numeroTelEmpresa;
    }

    public String getEmailEmpresa() {
        return emailEmpresa;
    }

    public void setEmailEmpresa(String emailEmpresa) {
        this.emailEmpresa = emailEmpresa;
    }

    public List<String> getFacturasIDs() {
        return facturasIDs;
    }

    public void setFacturasIDs(List<String> facturasIDs) {
        this.facturasIDs = facturasIDs;
    }
}
