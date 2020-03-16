package com.example.gasadvisor.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gasadvisor.R;

public class ChangeUsernameDialog extends AppCompatDialogFragment {
    private EditText etUsername, etNewUsername, etPassword;
    private UsernameDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_change_username, null);
        builder.setView(view).setTitle("Inserisci i dati per procedere")
                .setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Salva", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = etUsername.getText().toString();
                String newUsername = etNewUsername.getText().toString();
                String password = etPassword.getText().toString();
                listener.UsernameDialogClick(username, newUsername , password);
            }
        });
        etUsername = view.findViewById(R.id.et_username_profileDialog);
        etNewUsername = view.findViewById(R.id.et_newUsername_profileDialog);
        etPassword = view.findViewById(R.id.et_password_profileDialog);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UsernameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "classe deve implementare UsernameDialogListener");
        }
    }

    public interface UsernameDialogListener {
        void UsernameDialogClick(String username, String newUsername, String password);
    }
}
