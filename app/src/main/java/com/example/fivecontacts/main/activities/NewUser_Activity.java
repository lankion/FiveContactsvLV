package com.example.fivecontacts.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.User;

public class NewUser_Activity extends AppCompatActivity {
    boolean firstTimeUser = true;
    boolean firstTimeName = true;
    boolean firstTimeEmail = true;
    boolean firstTimePassword = true;
    EditText editTextUser;
    EditText editTextPassword;
    EditText editTextName;
    EditText editTextEmail;
    Switch switchLogin;
    Switch switchTheme;
    Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);
        buttonCreate = findViewById(R.id.btCriar);
        editTextUser = findViewById(R.id.edT_Login2);
        editTextPassword = findViewById(R.id.edt_Pass2);
        editTextName = findViewById(R.id.edtNome);
        editTextEmail = findViewById(R.id.edEmail);
        switchLogin = findViewById(R.id.swLogado);
        switchTheme = findViewById(R.id.swTema);
        setTitle("Novo Usuário");

        //Evento de limpar Componente
        editTextUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (firstTimeUser) {
                    firstTimeUser = false;
                    editTextUser.setText("");
                }

                return false;
            }
        });
        //Evento de limpar Componente
        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (firstTimePassword) {
                    firstTimePassword = false;
                    editTextPassword.setText("");
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    );
                }
                return false;
            }
        });
        //Evento de limpar Componente - E-mail
        editTextEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (firstTimeEmail) {
                    firstTimeEmail = false;
                    editTextEmail.setText("");
                }

                return false;
            }
        });
        //Evento de limpar Componente - Nome
        editTextName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (firstTimeName) {
                    firstTimeName = false;
                    editTextName.setText("");
                }

                return false;
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName, newLogin, newPassword, newEmail;
                boolean stayConnected;
                newName = editTextName.getText().toString();
                newLogin = editTextUser.getText().toString();
                newPassword = editTextPassword.getText().toString();
                newEmail = editTextEmail.getText().toString();
                stayConnected = switchLogin.isChecked();
                boolean darkTheme = switchTheme.isChecked();

                SharedPreferences sharedNewUser = getSharedPreferences("USER_MODEL", Activity.MODE_PRIVATE);
                SharedPreferences.Editor SharedWritingData = sharedNewUser.edit();
                SharedWritingData.putString("NAME", newName);
                SharedWritingData.putString("PASSWORD", newPassword);
                SharedWritingData.putString("LOGIN", newLogin);
                SharedWritingData.putString("EMAIL", newEmail);
                SharedWritingData.putBoolean("STAY_CONNECTED", stayConnected);
                SharedWritingData.putBoolean("DARK_THEME", darkTheme);
                SharedWritingData.commit();
                User newUser = new User(newName, newLogin, newPassword, newEmail, stayConnected, darkTheme);

                Intent intent = new Intent(NewUser_Activity.this, UpdateContct_Activity.class);
                intent.putExtra("USER_MODEL", newUser);
                startActivity(intent);
                finish();
            }
        });
    }
}