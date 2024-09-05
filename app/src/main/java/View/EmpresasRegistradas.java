package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.auditarrm.taxscan.R;

import Controller.FirebaseControlador;
import Model.Empresa;

public class EmpresasRegistradas extends AppCompatActivity {
    Button botonRegistrar;
    ListView listaEmpresas;
    Empresa empresaSelec;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lempresas);

        botonRegistrar = findViewById(R.id.botonRegistrar);
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
                intent.putExtra("empresa", empresaSelec);
                startActivity(intent);
            }
        });

    }
}
