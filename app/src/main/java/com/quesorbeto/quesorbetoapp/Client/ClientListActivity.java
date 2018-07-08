package com.quesorbeto.quesorbetoapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.R;
import com.quesorbeto.quesorbetoapp.Utils;

import java.util.ArrayList;
import java.util.List;

import QueSorbetoDataBase.Client;

public class ClientListActivity extends AppCompatActivity {

    //componentes visuales y logicos
    ListView clientList;
    FloatingActionButton addClientFloatingButton;
    List<Client> clientsGreenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        getSupportActionBar().setTitle("Lista Clientes");
        Utils.addBackPress(this);

        clientList = findViewById(R.id.clientList);
        addClientFloatingButton = findViewById(R.id.addClientFloatingButton);

        //cuando da click a alguna opcion de la lista
        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //se redirerige a la pantalla añadir cliente con el id del cliente seleccionado
                Intent mainIntent = new Intent(ClientListActivity.this, ClientAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idClient", clientsGreenDao.get(position).getIdClient());
                //setea las extras con el bundle
                mainIntent.putExtras(bundle);
                startActivity(mainIntent);
            }
        });

        //cuando añade nuevo cliente
        addClientFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se redirerige a la pantalla añadir cliente
                Intent mainIntent = new Intent(ClientListActivity.this, ClientAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idClient", null);
                //setea las extras con el bundle
                mainIntent.putExtras(bundle);
                startActivity(mainIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //recarga la lista cuando muestra la pantalla
        setAdapterListView();
    }

    //llama la lista de clientes, la ingresa al adapter seleccionado y posteriormente al listview
    private void setAdapterListView(){
        DbHelper dbHelper = new DbHelper(this);
        clientsGreenDao = dbHelper.daoSession.getClientDao().queryBuilder().list();

        List<String> listClients = new ArrayList<>();
        for(int x=0; x < clientsGreenDao.size(); x++){
            listClients.add(clientsGreenDao.get(x).getName().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listClients);

        clientList.setAdapter(adapter);
    }

    //asigna boton back a la flecha de back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
