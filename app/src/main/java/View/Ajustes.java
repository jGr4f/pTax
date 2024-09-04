package View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.auditarrm.taxscan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Ajustes extends AppCompatActivity {

    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    public StorageReference storageRef;
    Button logout;
    ImageView photo;
    TextView nombre;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);

        // Inicia las variables necesarias para usar Firebase
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Inicia los elementos de xml
        home = findViewById(R.id.Button_home);
        logout = findViewById(R.id.logout_button);
        photo = findViewById(R.id.profile_image); // Asumiendo que este es el ID correcto del ImageView en tu XML
        nombre = findViewById(R.id.username_text); // Asumiendo que este es el ID correcto del TextView en tu XML

        // Botón para ir a la home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ajustes.this, Home.class);
                startActivity(intent);
            }
        });

        // Mostrar nombre y foto del usuario actual
        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            Uri photoUrl = currentUser.getPhotoUrl();

            if (name != null) {
                nombre.setText(name);
            }

            if (photoUrl != null) {
                photo.setImageURI(photoUrl);
            }
        }

        // Botón para cerrar sesión
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Ajustes.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Ajustar insets para Edge to Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}