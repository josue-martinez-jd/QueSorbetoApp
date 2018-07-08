package com.quesorbeto.quesorbetoapp.Client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.R;
import com.quesorbeto.quesorbetoapp.Utils;

import java.util.UUID;

import QueSorbetoDataBase.Client;
import QueSorbetoDataBase.ClientDao;

public class ClientAddActivity extends AppCompatActivity {

    //componentes visuales y logicos
    Button saveClientButton;
    EditText clientAdress;
    EditText clientPhone;
    EditText clientName;
    Client client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add);

        //setea titulo y boton de atras
        getSupportActionBar().setTitle("Cliente");
        Utils.addBackPress(this);

        saveClientButton = findViewById(R.id.saveClientButton);
        clientAdress = findViewById(R.id.clientAdress);
        clientPhone = findViewById(R.id.clientPhone);
        clientName = findViewById(R.id.clientName);

        //obtiene el id del cliente seleccionado
        String idClient = getIntent().getExtras().getString("idClient");

        //si selecciono cliente cargue lo necesario en los componentes edittext visuales
        if(idClient != null){
            DbHelper dbHelper = new DbHelper(ClientAddActivity.this);
            client = dbHelper.daoSession.getClientDao().queryBuilder()
                    .where(ClientDao.Properties.IdClient.eq(idClient))
                    .unique();

            clientName.setText(client.getName().toString());
            clientAdress.setText(client.getAddress().toString());
            clientPhone.setText(client.getPhone().toString());
        }else{
            //si no selecciono cliente cree uno nuevo
            client = new Client();
        }

        //al dar boton de salvar
        saveClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifica campos requeridos
                if(!clientName.getText().toString().equals("")){
                    DbHelper dbHelper = new DbHelper(ClientAddActivity.this);

                    //setea los nuevos valores
                    client.setName(clientName.getText().toString());
                    client.setAddress(clientAdress.getText().toString());
                    client.setPhone(clientPhone.getText().toString());

                    //setea el id de cliente si es un cliente nuevo
                    if(client.getIdClient() == null){
                        client.setIdClient(UUID.randomUUID().toString());
                        dbHelper.daoSession.getClientDao().insert(client);
                    }else{
                        dbHelper.daoSession.getClientDao().update(client);
                    }

                    //muestra un mensaje de exito y devuelve a la pagina anterior
                    Utils.saveSuccess(ClientAddActivity.this);
               }else{
                    Utils.fillRequiredFields(ClientAddActivity.this);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       onBackPressed();
       return true;
    }
}
