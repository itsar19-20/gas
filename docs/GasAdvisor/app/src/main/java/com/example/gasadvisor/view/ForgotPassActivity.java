package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;
import com.example.gasadvisor.model.User;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassActivity extends AppCompatActivity {
    EditText email;
    Button btnRispristina;
    GasAdvisorApi gasAdvisorApi;
    UserDBAdapter userDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        email = findViewById(R.id.et_email_layoutForgotPass);
        btnRispristina = findViewById(R.id.btnRipristina_layoutForgotPass);
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        userDBAdapter = new UserDBAdapter(this);
        btnRispristina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    Toast.makeText(ForgotPassActivity.this, "Insirire un Email valido", Toast.LENGTH_SHORT).show();
                } else {
                    Call<User> getNewPass = gasAdvisorApi.userForgotPassword(email.getText().toString());
                    getNewPass.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(ForgotPassActivity.this, "Impossibile raggiungere questo indirizzo mail", Toast.LENGTH_SHORT).show();
                            } else {
                                User user = response.body();
                                userDBAdapter.open();
                                try {
                                    userDBAdapter.updateUserByUsername(user.getUsername(), user.getPassword(),
                                            user.getEmail(), user.getName(), user.getLastName());
                                } catch (Exception e) {

                                }
                                userDBAdapter.close();
                                setResult(1);
                                finish();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(ForgotPassActivity.this, "Impossibile raggiungere i server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
