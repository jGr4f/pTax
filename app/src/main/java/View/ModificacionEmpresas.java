package View;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.auditarrm.taxscan.R;

import Model.Empresa;

public class ModificacionEmpresas extends AppCompatActivity {
    Empresa empresa;

    Button btnModificar;
    Button btnEliminar;
    EditText fieldNombre;
    EditText fieldNIT;
    EditText fieldDirec;
    EditText fieldTel;
    EditText fieldCor;
    TextView lID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificacion);

        empresa = (Empresa) getIntent().getSerializableExtra("empresa");

        btnModificar = findViewById(R.id.botonModificar);
        btnEliminar = findViewById(R.id.botonEliminar);
        fieldNombre = findViewById(R.id.cNombre);
        fieldNIT = findViewById(R.id.cNIT);
        fieldDirec = findViewById(R.id.cDir);
        fieldTel = findViewById(R.id.cTel);
        fieldCor = findViewById(R.id.cEmail);
        lID = findViewById(R.id.textoID);

        if (empresa != null) {
            fieldNombre.setText(empresa.getNombreEmpresa());
            fieldNIT.setText(empresa.getNitEmpresa());
            fieldDirec.setText(empresa.getDirecEmpresa());
            fieldTel.setText(empresa.getNumeroTelefonoEmpres());
            fieldCor.setText(empresa.getEmailEmpresa());
            lID.setText(String.valueOf(empresa.getIdEmpresa()));
        } else {
            Log.e("DetalleEmpresaActivity", "El objeto Empresa es nulo.");
        }
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Empresa empresaMod = new Empresa(fieldNombre, fieldNIT, fieldDirec, fieldTel, fieldCor);
                if(empresaMod.validarCamposVacios(fieldNombre, fieldNIT, fieldDirec, fieldTel, fieldCor)){
                    empresaMod.setIdEmpresa(empresa.getIdEmpresa());
                    empresaMod.confirmacionMod(ModificacionEmpresas.this, empresaMod);



                }
                else{
                    Toast.makeText(ModificacionEmpresas.this, "Por favor complete todos los campos. ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empresa.confirmacionEliminacion(ModificacionEmpresas.this, empresa);

            }
        });




    }

}
