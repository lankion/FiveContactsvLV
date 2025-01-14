package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contact;
import com.example.fivecontacts.main.model.User;
import com.example.fivecontacts.main.utils.WarringAboutPermissions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ListOfContacts_Activity extends AppCompatActivity implements WarringAboutPermissions.NoticeDialogListener, BottomNavigationView.OnNavigationItemSelectedListener {
    ListView listViewContact;
    BottomNavigationView bottomNavigationViewContact;
    User currentUser;
    String numberCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);
        bottomNavigationViewContact = findViewById(R.id.bnv);
        bottomNavigationViewContact.setOnNavigationItemSelectedListener(this);
        bottomNavigationViewContact.setSelectedItemId(R.id.anvLigar);
        listViewContact = findViewById(R.id.listView1);

        //Dados da Intent Anterior
        Intent whoIsCalling = this.getIntent();
        if (whoIsCalling != null) {
            Bundle bundleData = whoIsCalling.getExtras();
            if (bundleData != null) {
                //Recuperando o Usuario
                currentUser = (User) bundleData.getSerializable("USER_MODEL");
                if (currentUser != null) {
                    setTitle("Contatos de Emergência de " + currentUser.getName());
                    //  preencherListView(user); //Montagem do ListView
                    fillListViewImages(currentUser);
                }
            }
        }

    }

    protected void updateListOfContacts(User user) {
        SharedPreferences sharedRecoveryContacts = getSharedPreferences("CONTACT", Activity.MODE_PRIVATE);

        int numberOfContacts = sharedRecoveryContacts.getInt("NUMBER_OF_CONTACTS", 0);
        ArrayList<Contact> previewContact = new ArrayList<Contact>();
        Contact currentContact;

        for (int i = 1; i <= numberOfContacts; i++) {
            String selectionOfContact = sharedRecoveryContacts.getString("CONTACTS" + i, "");
            if (selectionOfContact.compareTo("") != 0) {
                try {
                    ByteArrayInputStream byteArrayInputStreamContacts =
                            new ByteArrayInputStream(selectionOfContact.getBytes(StandardCharsets.ISO_8859_1.name()));
                    ObjectInputStream objectInputStreamContacts = new ObjectInputStream(byteArrayInputStreamContacts);
                    currentContact = (Contact) objectInputStreamContacts.readObject();

                    if (currentContact != null) {
                        previewContact.add(currentContact);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        user.setContacts(previewContact);
    }

    protected void fillListViewImages(User user) {
        final ArrayList<Contact> contactsImage = user.getContacts();
        Collections.sort(contactsImage);
        if (contactsImage != null) {
            String[] contactNames, contactAbrevs;
            contactNames = new String[contactsImage.size()];
            contactAbrevs = new String[contactsImage.size()];
            for (int j = 0; j < contactsImage.size(); j++) {
                contactAbrevs[j] = contactsImage.get(j).getName().substring(0, 1);
                contactNames[j] = contactsImage.get(j).getName();
            }
            ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < contactsImage.size(); i++) {
                Map<String, Object> listItemMap = new HashMap<String, Object>();
                listItemMap.put("IMAGE_ID", R.drawable.ic_action_ligar_list);
                listItemMap.put("CONTACT_IMG", contactNames[i]);
                listItemMap.put("ABREVS", contactAbrevs[i]);
                itemDataList.add(listItemMap);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemDataList, R.layout.list_view_layout_imagem,
                    new String[]{"IMAGE_ID", "CONTACT_IMG", "ABREVS"}, new int[]{R.id.userImage, R.id.userTitle, R.id.userAbrev});

            listViewContact.setAdapter(simpleAdapter);

            listViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (checkPermission(contactsImage.get(i).getNumber())) {

                        Uri uri = Uri.parse(contactsImage.get(i).getNumber());
                        //  Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                        Intent intentCall = new Intent(Intent.ACTION_CALL, uri);
                        startActivity(intentCall);
                    }
                }
            });
        }
    }

    protected void fillListView(User user) {
        final ArrayList<Contact> contacts = user.getContacts();

        if (contacts != null) {
            final String[] nomesSP;
            nomesSP = new String[contacts.size()];
            for (int j = 0; j < contacts.size(); j++) {
                nomesSP[j] = contacts.get(j).getName();
            }

            ArrayAdapter<String> adaptador;
            adaptador = new ArrayAdapter<String>(this, R.layout.list_view_layout, nomesSP);
            listViewContact.setAdapter(adaptador);

            listViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (checkPermission(contacts.get(i).getNumber())) {

                        Uri uri = Uri.parse(contacts.get(i).getNumber());
                        //   Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                        Intent intentCall = new Intent(Intent.ACTION_CALL, uri);
                        startActivity(intentCall);
                    }
                }
            });
        }//fim do IF do tamanho de contatos
    }

    protected boolean checkPermission(String numberCall) {

        this.numberCall = numberCall;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                String menssage = "Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permissão será solicitada";
                String title = "Permissão de acesso a chamadas";
                int code = 1;

                WarringAboutPermissions warringAboutPermissionsFirstTime = new WarringAboutPermissions(menssage, title, code);
                warringAboutPermissionsFirstTime.onAttach((Context) this);
                warringAboutPermissionsFirstTime.show(getSupportFragmentManager(), "FIRST_TIME");

            } else {
                String menssage = "Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permissão será solicitada";
                String title = "Permissão de acesso a chamadas II";
                int code = 1;

                WarringAboutPermissions warringAboutPermissionsSecondTime = new WarringAboutPermissions(menssage, title, code);
                warringAboutPermissionsSecondTime.onAttach((Context) this);
                warringAboutPermissionsSecondTime.show(getSupportFragmentManager(), "SECOND_TIME");
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 2222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "VALEU", Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse(numberCall);
                    //   Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                    Intent intentCall = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(intentCall);

                } else {
                    Toast.makeText(this, "SEU FELA!", Toast.LENGTH_LONG).show();

                    String mensagem = "Seu aplicativo pode ligar diretamente, mas sem permissão não funciona. Se você marcou não perguntar mais, você deve ir na tela de configurações para mudar a instalação ou reinstalar o aplicativo  ";
                    String titulo = "Porque precisamos telefonar?";
                    WarringAboutPermissions mensagemPermisso = new WarringAboutPermissions(mensagem, titulo, 2);
                    mensagemPermisso.onAttach((Context) this);
                    mensagemPermisso.show(getSupportFragmentManager(), "segundavez");
                }
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Checagem de o Item selecionado é o do perfil
        if (item.getItemId() == R.id.anvPerfil) {
            //Abertura da Tela MudarDadosUsario
            Intent intent = new Intent(this, ProfileUser_Activity.class);
            intent.putExtra("usuario", currentUser);
            startActivityForResult(intent, 1111);

        }
        // Checagem de o Item selecionado é o do perfil
        if (item.getItemId() == R.id.anvMudar) {
            //Abertura da Tela Mudar COntatos
            Intent intent = new Intent(this, UpdateContct_Activity.class);
            intent.putExtra("usuario", currentUser);
            startActivityForResult(intent, 1112);

        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Caso seja um Voltar ou Sucesso selecionar o item Ligar

        if (requestCode == 1111) {//Retorno de Mudar Perfil
            bottomNavigationViewContact.setSelectedItemId(R.id.anvLigar);
            currentUser = atualizarUser();
            setTitle("Contatos de Emergência de " + currentUser.getName());
            updateListOfContacts(currentUser);
            // preencherListViewImagens(user);
            fillListView(currentUser); //Montagem do ListView
        }

        if (requestCode == 1112) {//Retorno de Mudar Contatos
            bottomNavigationViewContact.setSelectedItemId(R.id.anvLigar);
            updateListOfContacts(currentUser);
            //preencherListViewImagens(user);
            fillListView(currentUser); //Montagem do ListView
        }


    }

    private User atualizarUser() {
        User user = null;
        SharedPreferences temUser = getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        String loginSalvo = temUser.getString("login", "");
        String senhaSalva = temUser.getString("senha", "");
        String nomeSalvo = temUser.getString("nome", "");
        String emailSalvo = temUser.getString("email", "");
        boolean manterLogado = temUser.getBoolean("manterLogado", false);
        boolean updateTheme = temUser.getBoolean("DARK_THEME", false);

        user = new User(nomeSalvo, loginSalvo, senhaSalva, emailSalvo, manterLogado, updateTheme);
        return user;
    }

    @Override
    public void onDialogPositiveClick(int codigo) {

        if (codigo == 1) {
            String[] permissions = {Manifest.permission.CALL_PHONE};
            requestPermissions(permissions, 2222);

        }


    }


}


