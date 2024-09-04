package View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auditarrm.taxscan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import Controller.AuthControlador;

public class Login extends AppCompatActivity {

    private AuthControlador authControlador;
    Button iniciarsesionGoogle_button, iniciarsesionFirebase_button, registrarse_button;
    EditText usuario_et, contraseña_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Iniciar objetos xml
        usuario_et = findViewById(R.id.Usuario_edittext);
        contraseña_et = findViewById(R.id.Contraseña_edittext);
        iniciarsesionGoogle_button = findViewById(R.id.conectarGoogle_button);
        iniciarsesionFirebase_button = findViewById(R.id.IniciarSesion_button);
        registrarse_button = findViewById(R.id.Registro_button);

        // Iniciar AuthController
        authControlador = new AuthControlador(this);

        // Botón para ir a la view de registro
        registrarse_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        // Botón para iniciar sesión con usuario y contraseña
        iniciarsesionFirebase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usuario_et.getText().toString().equals("") || contraseña_et.getText().toString().equals("") ){
                    Toast.makeText(Login.this, "Completa los campos de usuario y contraseña.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    authControlador.iniciarSesFirebase(usuario_et.getText().toString(), contraseña_et.getText().toString(), new AuthControlador.AuthCallback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            actualizarSesion(user);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(Login.this, "Inicio de sesión falló.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Botón para iniciar sesión con Google
        iniciarsesionGoogle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authControlador.initGoogle(Login.this);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AuthControlador.RC_SIGN_IN) {
            authControlador.handleGoogleSignInResult(data, new AuthControlador.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    actualizarSesion(user);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(Login.this, "Inicio de sesión falló.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Si el usuario ya inició sesión, lo envía a la home
    private void actualizarSesion(FirebaseUser user) {
        if (user != null) {
            irHome();
        }
    }

    // Método para ir a la home
    private void irHome(){
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        Log.i("depuracion", "El usuario inició sesión correctamente");
        finish();
    }
}