package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.auditarrm.taxscan.R;

import Model.Empresa;

public class RegistroEmpresas extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regempresas);

        Intent intent1 = getIntent();

        EditText campoNombre = findViewById(R.id.campoNombreEmpresa);
        EditText campoNIT = findViewById(R.id.campoNIT);
        EditText campoDir = findViewById(R.id.campoDireccion);
        EditText campoTel = findViewById(R.id.campoTelefono);
        EditText campoEmail= findViewById(R.id.campoEmail);
        Button btnSubir = findViewById(R.id.botonEnviar);


        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Empresa empresa = new Empresa(campoNombre, campoNIT, campoDir, campoTel, campoEmail);
                if(empresa.validarCamposVacios(campoNombre, campoNIT, campoDir, campoTel, campoEmail)){
                    empresa.confirmacionEnvio(RegistroEmpresas.this, empresa);



                }
                else{
                    Toast.makeText(RegistroEmpresas.this, "Por favor complete todos los campos. ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
