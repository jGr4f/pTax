package Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;


import java.io.Serializable;
import java.util.List;

import Controller.FirebaseControlador;


public class Empresa implements Serializable {
    private int idEmpresa;                   // ID único de la empresa
    private String nombreEmpresa;                 // Nombre de la empresa
    private String direcEmpresa;              // Dirección de la empresa
    private String nTelEmpresa;          // Número de teléfono de la empresa
    private String emailEmpresa;                // Email de contacto de la empresa
    private String nitEmpresa;
    private List<String> facturasIDs;     // Lista de IDs de facturas asociadas a esta empresa

    // Constructor vacío (necesario para Firebase)
    public Empresa() {}

    public Empresa(EditText nomEmpresa,EditText niEmpresa, EditText dirEmpresa, EditText numEmpresa, EditText emEmpresa){
        this.idEmpresa = (int) (Math.random() * 10000);
        this.nombreEmpresa = nomEmpresa.getText().toString();
        this.nitEmpresa = niEmpresa.getText().toString();
        this.direcEmpresa = dirEmpresa.getText().toString();
        this.nTelEmpresa = numEmpresa.getText().toString();
        this.emailEmpresa = emEmpresa.getText().toString();

    }

    public void limpiarCampos(EditText... campos){
        for (EditText campo: campos){
            campo.setText("");
        }
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
    public void confirmacionEnvio(Context contexto, Empresa empresa, EditText... campos) {
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
                limpiarCampos(campos);



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
    public void confirmacionMod(Context contexto, Empresa empresa, EditText... campos) {
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
                limpiarCampos(campos);



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
    public void confirmacionEliminacion(Context contexto, Empresa empresa, EditText... campos) {
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
                limpiarCampos(campos);



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




    public String getDireccionEmpresa() {
        return direcEmpresa;
    }



    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }



    public String getNumeroTelEmpresa() {
        return nTelEmpresa;
    }



    public String getEmailEmpresa() {
        return emailEmpresa;
    }





    @Override
    public String toString(){
        return nombreEmpresa;
    }
}
