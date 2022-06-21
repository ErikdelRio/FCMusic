package com.example.fcmusicapp.ui.descargas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fcmusicapp.databinding.FragmentDescargasBinding;

public class DescargasFragment extends Fragment {

    private FragmentDescargasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DescargasViewModel homeViewModel =
                new ViewModelProvider(this).get(DescargasViewModel.class);

        binding = FragmentDescargasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDescargas;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}