package com.example.fcmusicapp.ui.perfil;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fcmusicapp.Cancion;
import com.example.fcmusicapp.DataBaseHelperCancion;
import com.example.fcmusicapp.MainActivity;
import com.example.fcmusicapp.MusicPList;
import com.example.fcmusicapp.MusicPlayer;
import com.example.fcmusicapp.R;
import com.example.fcmusicapp.databinding.FragmentPerfilBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private List<Cancion> favs;
    private List<String> nombres;
    private ArrayList<File> mySongs;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel galleryViewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPerfil;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        DataBaseHelperCancion db = new DataBaseHelperCancion(getContext());
        this.favs = db.getFavs();
        db.close();
        List<String> aux = new ArrayList<>();
        for(Cancion c: favs){
            aux.add(c.getNombre());
        }
        this.nombres=aux;
        mySongs=findSong(Environment.getExternalStorageDirectory());
        ListView listView = (ListView) root.findViewById(R.id.lista_fav);
        customAdapter customAdapter=new customAdapter();
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view,int i,long l){
                Cancion c=(Cancion) listView.getItemAtPosition(i);
                startActivity(new Intent(getContext(), MusicPlayer.class)
                        .putExtra("songs",mySongs)
                        .putExtra("songname",c.getNombre())
                        .putExtra("pos",i));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class customAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favs.size();
        }

        @Override
        public Object getItem(int i) {
            return favs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return favs.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView=getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textSong= myView.findViewById(R.id.txtSongName);
            textSong.setSelected(true);
            textSong.setText(favs.get(i).getNombre());
            return myView;
        }
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(findSong(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3"))
                        if (nombres.contains(singleFile.getName().replace(".mp3","")))
                            arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }
}