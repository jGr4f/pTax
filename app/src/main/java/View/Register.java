package View;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

import Controller.AuthControlador;

public class Register extends AppCompatActivity {

    private AuthControlador authControlador;
    private static final int RC_SIGN_IN = 1;

    EditText etUser, etPassword, etConfirmPassword, etEmail;
    Button Button_register, Button_loginGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        authControlador = new AuthControlador(this);

        etUser = findViewById(R.id.etUsernameR);
        etPassword = findViewById(R.id.etPasswordR);
        etConfirmPassword = findViewById(R.id.etPassword2R);
        etEmail = findViewById(R.id.etEmailR);
        Button_register = findViewById(R.id.Button_registerR);
        Button_loginGoogle = findViewById(R.id.Button_connectGoogle);

        Button_loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authControlador.initGoogle(Register.this);
            }
        });

        Button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                    authControlador.registrarseFirebase(etEmail.getText().toString(), etPassword.getText().toString(), task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = authControlador.getCurrentUser();
                            actualizarSesion(user);
                        } else {
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Register.this, "Contraseñas no son iguales.", Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(Register.this, "Inicio de sesión falló.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void actualizarSesion(FirebaseUser user) {
        if (user != null) {
            irHome();
        }
    }

    private void irHome(){
        Intent intent = new Intent(Register.this, Home.class);
        startActivity(intent);
        finish();
    }

}