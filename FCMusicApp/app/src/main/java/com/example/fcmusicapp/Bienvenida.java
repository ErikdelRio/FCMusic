package com.example.fcmusicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Bienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bienvenida);
    }

    public void accede(View view) {
        startActivity(new Intent(Bienvenida.this,LogIn.class));
    }

    public void registrarse(View view) {
        startActivity(new Intent(Bienvenida.this,Registro.class));
    }
}