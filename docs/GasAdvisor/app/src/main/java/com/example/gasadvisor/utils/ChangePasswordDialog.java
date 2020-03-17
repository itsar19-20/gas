package com.example.gasadvisor.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gasadvisor.R;

public class ChangePasswordDialog extends AppCompatDialogFragment {
    private EditText etUsername, etNewPassword, etPassword;
    private PasswordDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_change_password, null);
        builder.setView(view).setTitle("Inserisci i dati per procedere")
                .setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Salva", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = etUsername.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String password = etPassword.getText().toString();
                listener.PasswordDialogClick(username, newPassword , password);
            }
        });
        etUsername = view.findViewById(R.id.et_username_dialogChangePass);
        etPassword = view.findViewById(R.id.et_password_dialogChangePass);
        etNewPassword = view.findViewById(R.id.et_newPassword_dialogChangePass);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PasswordDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "classe deve implementare UsernameDialogListener");
        }
    }

    public interface PasswordDialogListener {
        void PasswordDialogClick(String username, String newPassword, String password);
    }
}
