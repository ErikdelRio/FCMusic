package com.example.fcmusicapp;

import android.Manifest;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MusicPList extends AppCompatActivity {
    ListView listView;
    ArrayList<File> mySongs;
    String busqueda;
    List<Cancion> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_p_list);
        listView=findViewById(R.id.listViewSong);
        if (getIntent().hasExtra("busqueda")){
            busqueda = getIntent().getStringExtra("busqueda");
        }
        runtimePermission();
    }

    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        DataBaseHelperCancion dbc = new DataBaseHelperCancion(this);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(findSong(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3")) {
                        Cancion c = new Cancion();
                        mmr.setDataSource(singleFile.getPath());
                        long duration = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        duration = duration / 1000;
                        c.setNombre(singleFile.getName().replace(".mp3", ""));
                        c.setDuracion((int) duration);
                        arrayList.add(singleFile);
                        dbc.addOne(c);
                    }
                }
            }
        }
        if(busqueda != null){
            ArrayList<File> aux = new ArrayList<>();
            for (File f: arrayList){
                if (f.getName().toLowerCase(Locale.ROOT).contains(busqueda)){
                    aux.add(f);
                }
            }
            arrayList = aux;
        }
        return arrayList;
    }

    public void displaySongs(){
        mySongs=findSong(Environment.getExternalStorageDirectory());
        DataBaseHelperCancion bbdC = new DataBaseHelperCancion(this);
        if(busqueda == null)
            songs = bbdC.getTodas();
        else{
            songs = bbdC.busquedaCancion(busqueda);
        }

        customAdapter customAdapter=new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view,int i,long l){
                Cancion c=(Cancion) listView.getItemAtPosition(i);
                startActivity(new Intent(getApplicationContext(),MusicPlayer.class)
                        .putExtra("songs",mySongs)
                        .putExtra("songname",c.getNombre())
                        .putExtra("pos",i));
            }
        });

    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Object getItem(int i) {
            return songs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return songs.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView=getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textSong= myView.findViewById(R.id.txtSongName);
            textSong.setSelected(true);
            textSong.setText(songs.get(i).getNombre());
            return myView;
        }
    }

}