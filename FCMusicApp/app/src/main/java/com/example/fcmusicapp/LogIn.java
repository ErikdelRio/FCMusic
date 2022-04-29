package com.example.fcmusicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void registrarse(View view) {
        startActivity(new Intent(LogIn.this,Registro.class));
    }

    public void accede(View view) {
        startActivity(new Intent(LogIn.this, com.example.fcmusicapp.MusicPList.class));
    }
}