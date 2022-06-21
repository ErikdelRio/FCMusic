package com.example.fcmusicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;

import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity {
    private EditText usuario;
    private EditText correo;
    private EditText contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usuario = (EditText) findViewById(R.id.usuario);
        correo = (EditText) findViewById(R.id.correo);
        contrasena = (EditText) findViewById(R.id.contrasenia);

    }

    public void accede(View view) {
        startActivity(new Intent(Registro.this, com.example.fcmusicapp.LogIn.class));
    }

    public void registrar(View view){
        if(usuario.getText().toString().length() == 0||contrasena.getText().toString().length() == 0 || correo.getText().toString().length() == 0){
            Toast.makeText(this,"por favor llene todos los campos",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!correo.getText().toString().endsWith("@ciencias.unam.mx")){
            Toast.makeText(this,"correo no pertenece a la comunidad ciencias",Toast.LENGTH_SHORT).show();
            return;
        }
        DataBaseHelperUsuario db = new DataBaseHelperUsuario(this);
        String securePassword = get_SHA_1_SecurePassword(contrasena.getText().toString(), "salt");
        Usuario usr = new Usuario(usuario.getText().toString(),correo.getText().toString(),securePassword);
        db.addOne(usr);
        startActivity(new Intent(Registro.this, com.example.fcmusicapp.LogIn.class));
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    private static String get_SHA_1_SecurePassword(String passwordToHash,
                                                   String salt) {
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