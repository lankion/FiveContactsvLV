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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contact;
import com.example.fivecontacts.main.model.User;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CheckLogin extends AppCompatActivity {

    boolean firstTimeUser = true;
    boolean firstTimePassword = true;
    EditText editTextUser;
    EditText editTextPassword;
    Button buttonLogin;
    Button buttonNew;
    TextView TextViewForgottenPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checagem_login);

        if (constructorUserWithoutLogin()) {
            User userWithoutLogin = constructorUser();
            fillListOfContacts(userWithoutLogin);
            Intent intent = new Intent(CheckLogin.this, ListOfContacts_Activity.class);
            intent.putExtra("USER_MODEL", userWithoutLogin);
            startActivity(intent);
            finish();

        } else {
            buttonLogin = findViewById(R.id.btLogar);
            buttonNew = findViewById(R.id.btNovo);
            editTextUser = findViewById(R.id.edT_Login);
            editTextPassword = findViewById(R.id.edt_Pass);

            //Colocando Underline (Vamos usar esse campo mais na frente com o FireBase)
            //TextViewForgottenPassword = findViewById(R.id.tvEsqueceuSenha);
            //TextViewForgottenPassword.setPaintFlags(TextViewForgottenPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            //Evento de limpar Componente
            editTextUser.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (firstTimeUser) {
                        editTextUser.setText("");
                        firstTimeUser = false;
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

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Ao clicar deve-se:
                    //1- Checar se existe um SharedPreferences
                    //2- Comparar login e senha salvos
                    //3- Se tudo der certo, resgatar lista de contatos
                    //4- Abrir a Atividade lista de Contatos passando como parametro o objeto User e seus 5 Contatos

                    SharedPreferences SharedCheckCurrentUser = getSharedPreferences("USER_MODEL", Activity.MODE_PRIVATE);
                    String savedLogin = SharedCheckCurrentUser.getString("LOGIN", "");
                    String savedPassword = SharedCheckCurrentUser.getString("PASSWORD", "");

                    if ((savedLogin != null) && (savedPassword != null)) {
                        //Recuperando da tela
                        String currentPassword = editTextPassword.getText().toString();
                        String currentLogin = editTextUser.getText().toString();

                        //Comparando
                        if ((savedLogin.compareTo(currentLogin) == 0) && (savedPassword.compareTo(currentPassword) == 0)) {
                            User currentUser = constructorUser();
                            fillListOfContacts(currentUser);
                            //Abrindo a Lista de Contatos
                            Intent intent = new Intent(CheckLogin.this, ListOfContacts_Activity.class);
                            intent.putExtra("USER_MODEL", currentUser);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CheckLogin.this, "Login e Senha Incorretos", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CheckLogin.this, "Login e Senha nulos", Toast.LENGTH_LONG).show();
                    }
                }
            });

            //Novo Usuário
            buttonNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CheckLogin.this, NewUser_Activity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private User constructorUser() {
        User currentUser = null;
        SharedPreferences SharedCurrentUser = getSharedPreferences("USER_MODEL", Activity.MODE_PRIVATE);
        String savedLogin = SharedCurrentUser.getString("LOGIN", "");
        String savedPassword = SharedCurrentUser.getString("PASSWORD", "");
        String savedName = SharedCurrentUser.getString("NAME", "");
        String savedEmail = SharedCurrentUser.getString("EMAIL", "");
        boolean stayConnected = SharedCurrentUser.getBoolean("STAY_CONNECTED", false);
        boolean darkTheme = SharedCurrentUser.getBoolean("DARK_THEME", false);

        currentUser = new User(savedName, savedLogin, savedPassword, savedEmail, stayConnected, darkTheme);
        return currentUser;
    }


    private boolean constructorUserWithoutLogin() {
        SharedPreferences SharedUserWithoutLogin = getSharedPreferences("USER_MODEL", Activity.MODE_PRIVATE);
        boolean stayConnected = SharedUserWithoutLogin.getBoolean("STAY_CONNECTED", false);
        return stayConnected;
    }

    protected void fillListOfContacts(User user) {
        SharedPreferences SharedRecoverContacts = getSharedPreferences("CONTACT", Activity.MODE_PRIVATE);

        int numberOfContacts = SharedRecoverContacts.getInt("NUMBER_OF_CONTACTS", 0);
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        Contact currentContacts;

        for (int i = 1; i <= numberOfContacts; i++) {
            String objSel = SharedRecoverContacts.getString("CONTACTS" + i, "");
            if (objSel.compareTo("") != 0) {
                try {
                    ByteArrayInputStream byteArrayInputStreamContact =
                            new ByteArrayInputStream(objSel.getBytes(StandardCharsets.ISO_8859_1.name()));
                    ObjectInputStream objectInputStreamContact = new ObjectInputStream(byteArrayInputStreamContact);
                    currentContacts = (Contact) objectInputStreamContact.readObject();

                    if (currentContacts != null) {
                        contacts.add(currentContacts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        user.setContacts(contacts);
    }

}