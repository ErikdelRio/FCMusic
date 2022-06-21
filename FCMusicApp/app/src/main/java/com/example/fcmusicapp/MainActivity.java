package com.example.fcmusicapp;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fcmusicapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView login;
    private final String EXTRA_USUARIO = "usuario";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String usuario = intent.getStringExtra(EXTRA_USUARIO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_descargas, R.id.act_salir, R.id.nav_buscador,R.id.nav_perfil,R.id.act_playlist)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        login = (TextView) headerView.findViewById(R.id.usr);
        login.setText(usuario);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_musica:
                if(MusicPlayer.mediaPlayer!=null) {
                    startActivity(new Intent(MainActivity.this, com.example.fcmusicapp.MusicPlayer.class)
                            .putExtra("actionButton", true)
                            .putExtra("songs",MusicPlayer.mySongs)
                            .putExtra("songname",MusicPlayer.songName)
                            .putExtra("pos",MusicPlayer.position));
                }
                else
                    Toast.makeText(this, "no se esta reproduciendo ninguna cancion", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_favorite:
                if(MusicPlayer.mediaPlayer!=null){
                    DataBaseHelperCancion db = new DataBaseHelperCancion(this);
                    boolean val=db.likeSong(MusicPlayer.songName);
                    if(val){
                        Toast.makeText(this, "eliminada de favoritos", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "agregada a favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(this, "no se esta reproduciendo ninguna cancion", Toast.LENGTH_SHORT).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Log.e("MainActivity default","no se registro accion");
                return super.onOptionsItemSelected(item);
        }
    }

}