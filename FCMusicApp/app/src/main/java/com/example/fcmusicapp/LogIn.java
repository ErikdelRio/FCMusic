package com.example.fcmusicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {
    private final String EXTRA_USUARIO = "usuario";
    private EditText usuario;
    private EditText contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        usuario = (EditText) findViewById(R.id.correo);
        contrasenia = (EditText) findViewById(R.id.contrasenia);
    }

    public void registrarse(View view) {
        startActivity(new Intent(LogIn.this,Registro.class));
    }

    public void accede(View view) {
        if (usuario.getText().toString().length() != 0 && contrasenia.getText().toString().length() != 0) {
            DataBaseHelperUsuario db = new DataBaseHelperUsuario(this);
            String securePassword = get_SHA_1_SecurePassword(contrasenia.getText().toString(), "salt");
            if (db.credencialesCorrectas(usuario.getText().toString(),securePassword)){
                Intent intent = new Intent(LogIn.this, MainActivity.class);
                intent.putExtra(EXTRA_USUARIO, usuario.getText().toString());
                startActivity(intent);
            }
            else {
                Toast.makeText(this,"credenciales invalidas",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"por favor llene todos los campos",Toast.LENGTH_SHORT).show();
        }
    }

    private static String get_SHA_1_SecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return generatedPassword;
    }
}