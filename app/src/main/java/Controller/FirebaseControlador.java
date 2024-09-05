package Controller;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import Model.Empresa;

public class FirebaseControlador {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<Empresa> listIdEmp = new ArrayList<Empresa>();
    ArrayAdapter<Empresa> arrayAdapter;

    public void inicializarFirebase(Context context){
        FirebaseApp.initializeApp(context);
        this.mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();
        currentUser = mAuth.getCurrentUser();

    }
    public void enviarDatos(String principal,int idEm, Empresa empresa){
        databaseReference.child(principal).child(currentUser.getUid()).child(String.valueOf(idEm)).setValue(empresa);
    }
    public void eliminarDatos(String principal,int idEm, Empresa empresa){
        databaseReference.child(principal).child(currentUser.getUid()).child(String.valueOf(idEm)).removeValue();
    }


    public void listarDatos(String principal,Context contexto, ListView lista){
        DatabaseReference refEspecifica = FirebaseDatabase.getInstance().getReference().child(principal).child(currentUser.getUid());

        refEspecifica.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listIdEmp.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){

                    Empresa empresa = objSnapshot.getValue(Empresa.class);

                    if (empresa != null) {
                        listIdEmp.add(empresa); // Agregar cada empresa a la lista
                    }

                }
                if (arrayAdapter == null) {
                    arrayAdapter = new ArrayAdapter<>(contexto, android.R.layout.simple_list_item_1, listIdEmp);
                    lista.setAdapter(arrayAdapter);
                } else {
                    arrayAdapter.notifyDataSetChanged(); // Notificar cambios en los datos
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Error al leer los datos.", error.toException());
            }
        });
    }

}
