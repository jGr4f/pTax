package View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
            fieldDirec.setText(empresa.getDireccionEmpresa());
            fieldTel.setText(empresa.getNumeroTelEmpresa());
            fieldCor.setText(empresa.getEmailEmpresa());
            lID.setText(String.valueOf(empresa.getIdEmpresa()));
        } else {
            Log.e("DetalleEmpresaActivity", "El objeto Empresa es nulo.");
        }
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Empresa empresaMod = new Empresa(fieldNombre, fieldNIT, fieldDirec, fieldTel, fieldCor);
                empresaMod.setIdEmpresa(empresa.getIdEmpresa());
                empresaMod.confirmacionMod(ModificacionEmpresas.this, empresaMod, fieldNombre, fieldNIT, fieldDirec, fieldTel, fieldCor);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empresa.confirmacionEliminacion(ModificacionEmpresas.this, empresa, fieldNombre, fieldNIT, fieldDirec, fieldTel, fieldCor);
            }
        });




        /*botonRegistrar = findViewById(R.id.botonRegistrar);
        listaEmpresas = findViewById(R.id.listaEmpresas);

        botonRegistrar.setOnClickListener(view -> {
            Intent intent = new Intent(EmpresasRegistradas.this , RegistroEmpresas.class);
            startActivity(intent);
        });
        FirebaseControlador db = new FirebaseControlador();
        db.inicializarFirebase(EmpresasRegistradas.this);
        db.listarDatos("Empresas",EmpresasRegistradas.this, listaEmpresas);

        listaEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                empresaSelec = (Empresa) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(EmpresasRegistradas.this, ModificacionEmpresas.class);
                startActivity(intent);
            }
        });*/

    }

}
