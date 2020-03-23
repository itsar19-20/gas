package com.example.gasadvisor.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gasadvisor.R;

public class AdvancedSearchFragment extends Fragment {
    private Button btnPiuVicino;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_advanced, container, false);
    btnPiuVicino = v.findViewById(R.id.btn_piuVicino_avanzFrag);
    btnPiuVicino.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().setResult(0);
            getActivity().finish();
        }
    });


    return v;
    }
}
