package Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

import Controller.FirebaseControlador;
import View.EmpresasRegistradas;
import View.ModificacionEmpresas;
import View.RegistroEmpresas;


public class Empresa implements Serializable {
    private int idEmpresa;                   // ID único de la empresa
    private String nombreEmpresa;                 // Nombre de la empresa
    private String direccionEmpresa;              // Dirección de la empresa
    private String numeroTelEmpresa; // Número de teléfono de la empresa
    private String emailEmpresa;                // Email de contacto de la empresa
    private String nitEmpresa;
    private List<String> facturasIDs;     // Lista de IDs de facturas asociadas a esta empresa

    // Constructor vacío (necesario para Firebase)
    public Empresa() {}

    public Empresa(EditText nomEmpresa,EditText niEmpresa, EditText dirEmpresa, EditText numEmpresa, EditText emEmpresa){
        this.idEmpresa = (int) (Math.random() * 10000);
        this.nombreEmpresa = nomEmpresa.getText().toString();
        this.nitEmpresa = niEmpresa.getText().toString();
        this.direccionEmpresa = dirEmpresa.getText().toString();
        this.numeroTelEmpresa = numEmpresa.getText().toString();
        this.emailEmpresa = emEmpresa.getText().toString();

    }


    public boolean validarCamposVacios(EditText... campos) {
        for (EditText campo : campos) {
            String texto = campo.getText().toString().trim();
            if (texto.isEmpty()) {
                campo.setError("No pueden haber campos vacios. ");
                return false;
            }
        }
        return true;
    }
    public void confirmacionEnvio(Context contexto, Empresa empresa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que quieres enviar los datos?");

        // Botón de confirmación
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseControlador db = new FirebaseControlador();
                db.inicializarFirebase(contexto);
                db.enviarDatos("Empresas", empresa.getIdEmpresa(), empresa);

                Toast.makeText(contexto, "Empresa registrada correctamente. ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("resultado", "eliminado");
                if (contexto instanceof RegistroEmpresas) {
                    ((RegistroEmpresas) contexto).setResult(AppCompatActivity.RESULT_OK, intent);
                    ((RegistroEmpresas) contexto).finish();
                }



                // El usuario confirmó, proceder con el envío de datos
            }
        });

        // Botón de cancelación
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario canceló, no hacer nada
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void confirmacionMod(Context contexto, Empresa empresa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que quieres modificar los datos?");

        // Botón de confirmación
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseControlador db = new FirebaseControlador();
                db.inicializarFirebase(contexto);
                db.enviarDatos("Empresas", empresa.getIdEmpresa(), empresa);

                Toast.makeText(contexto, "Empresa modificada correctamente. ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("resultado", "eliminado");
                if (contexto instanceof ModificacionEmpresas) {
                    ((ModificacionEmpresas) contexto).setResult(AppCompatActivity.RESULT_OK, intent);
                    ((ModificacionEmpresas) contexto).finish();
                }



                // El usuario confirmó, proceder con el envío de datos
            }
        });

        // Botón de cancelación
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario canceló, no hacer nada
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void confirmacionEliminacion(Context contexto, Empresa empresa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que quieres eliminar esta empresa de la base de datos?");

        // Botón de confirmación
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseControlador db = new FirebaseControlador();
                db.inicializarFirebase(contexto);
                db.eliminarDatos("Empresas", empresa.getIdEmpresa(), empresa);

                Toast.makeText(contexto, "Empresa eliminada de la base. ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("resultado", "eliminado");
                if (contexto instanceof ModificacionEmpresas) {
                    ((ModificacionEmpresas) contexto).setResult(AppCompatActivity.RESULT_OK, intent);
                    ((ModificacionEmpresas) contexto).finish();
                }



                // El usuario confirmó, proceder con el envío de datos
            }
        });

        // Botón de cancelación
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario canceló, no hacer nada
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDirecEmpresa() {
        return direccionEmpresa;
    }

    public void setDirecEmpresa(String direcEmpresa) {
        this.direccionEmpresa = direcEmpresa;
    }

    public String getNumeroTelefonoEmpres() {
        return numeroTelEmpresa;
    }

    public void setNumeroTelefonoEmpres(String numeroTelefonoEmpres) {
        this.numeroTelEmpresa = numeroTelefonoEmpres;
    }

    public String getEmailEmpresa() {
        return emailEmpresa;
    }

    public void setEmailEmpresa(String emailEmpresa) {
        this.emailEmpresa = emailEmpresa;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }

    public void setNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
    }

    @Override
    public String toString(){
        return nombreEmpresa;
    }
}
