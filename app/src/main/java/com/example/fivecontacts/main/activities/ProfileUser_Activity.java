package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileUser_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    EditText editTextCurrentUser;
    EditText editTextCurrentPassword;
    EditText editTextCurrentName;
    EditText editTextCurrentEmail;
    Switch switchCurrentLogging;
    Switch switchCurrentTheme;
    Button buttonModify;
    BottomNavigationView bottomNavigationViewChoise;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        buttonModify =findViewById(R.id.btCriar);
        bottomNavigationViewChoise =findViewById(R.id.bnv);
        bottomNavigationViewChoise.setOnNavigationItemSelectedListener(this);
        bottomNavigationViewChoise.setSelectedItemId(R.id.anvPerfil);
        editTextCurrentUser =findViewById(R.id.edT_Login2);
        editTextCurrentPassword =findViewById(R.id.edt_Pass2);
        editTextCurrentName =findViewById(R.id.edtNome);
        editTextCurrentEmail =findViewById(R.id.edEmail);
        switchCurrentLogging =findViewById(R.id.swLogado);
        switchCurrentTheme = findViewById(R.id.swTema2);

        Intent WhoIsCalling = this.getIntent();
        if (WhoIsCalling != null) {
            Bundle bundleData = WhoIsCalling.getExtras();
            if (bundleData != null) {
                //Recuperando o Usuario
                currentUser = (User) bundleData.getSerializable("USER_MODEL");
                setTitle("Alterar dados de "+ currentUser.getName());
            }
        }
        if (currentUser != null) {
            editTextCurrentUser.setText(currentUser.getLogin());
            editTextCurrentPassword.setText(currentUser.getPassword());
            editTextCurrentName.setText(currentUser.getName());
            editTextCurrentEmail.setText(currentUser.getEmail());
            switchCurrentLogging.setChecked(currentUser.isStayedConnected());
            switchCurrentTheme.setChecked(currentUser.isDarkTheme());
        }

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.setName(editTextCurrentName.getText().toString());
                currentUser.setLogin(editTextCurrentUser.getText().toString());
                currentUser.setPassword(editTextCurrentPassword.getText().toString());
                currentUser.setEmail(editTextCurrentEmail.getText().toString());
                currentUser.setStayedConnected(switchCurrentLogging.isChecked());
                currentUser.setDarkTheme(switchCurrentTheme.isChecked());
                saveModifications(currentUser);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Checagem de o Item selecionado é a de mudanças de contatos
        if (item.getItemId() == R.id.anvMudar) {
            //Abertura da Tela de Perfil
            Intent intent = new Intent(this, UpdateContct_Activity.class);
            intent.putExtra("USER_MODEL", currentUser);
            startActivity(intent);
        }
        // Checagem de o Item selecionado é Ligar
        if (item.getItemId() == R.id.anvLigar) {
            //Abertura da Tela Mudar COntatos
            Intent intent = new Intent(this, ListOfContacts_Activity.class);
            intent.putExtra("USER_MODEL", currentUser);
            startActivity(intent);
        }
        return true;
    }

    public void saveModifications(User user){
        SharedPreferences sharedSaveUser= getSharedPreferences("USER_MODEL", Activity.MODE_PRIVATE);
        SharedPreferences.Editor SharedWritingData = sharedSaveUser.edit();
        SharedWritingData.putString("NAME",user.getName());
        SharedWritingData.putString("PASSWORD",user.getPassword());
        SharedWritingData.putString("LOGIN",user.getLogin());
        SharedWritingData.putString("EMAIL",user.getEmail());
        SharedWritingData.putBoolean("STAY_CONNECTED",user.isStayedConnected());
        SharedWritingData.putBoolean("DARK_THEME",user.isDarkTheme());
        SharedWritingData.commit();
        Toast.makeText(ProfileUser_Activity.this,"Modificações Salvas",Toast.LENGTH_LONG).show() ;
        finish();
    }
}