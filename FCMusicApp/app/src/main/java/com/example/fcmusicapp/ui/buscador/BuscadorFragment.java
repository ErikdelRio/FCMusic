package com.example.fcmusicapp.ui.buscador;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fcmusicapp.LogIn;
import com.example.fcmusicapp.MainActivity;
import com.example.fcmusicapp.MusicPList;
import com.example.fcmusicapp.R;
import com.example.fcmusicapp.databinding.FragmentBuscadorBinding;

import java.util.Locale;

public class BuscadorFragment extends Fragment {

    private FragmentBuscadorBinding binding;
    private EditText busqueda;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BuscadorViewModel slideshowViewModel =
                new ViewModelProvider(this).get(BuscadorViewModel.class);

        binding = FragmentBuscadorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        busqueda = (EditText) root.findViewById(R.id.buscador);
        busqueda.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent intent = new Intent(getActivity(),MusicPList.class);
                    intent.putExtra("busqueda", busqueda.getText().toString().toLowerCase(Locale.ROOT));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}