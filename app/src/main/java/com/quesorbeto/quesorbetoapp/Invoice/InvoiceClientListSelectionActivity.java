package com.quesorbeto.quesorbetoapp.Invoice;

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

public class InvoiceClientListSelectionActivity extends AppCompatActivity {

    //list view cientes
    ListView clientList;
    //boton añadir cliente
    FloatingActionButton addClientFloatingButton;
    //lista clientes base de datos
    List<Client> clientsGreenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        //setea titulo
        getSupportActionBar().setTitle("Seleccionar Cliente Factura");
        Utils.addBackPress(this);

        //liga el layout con el controller
        clientList = findViewById(R.id.clientList);
        addClientFloatingButton = findViewById(R.id.addClientFloatingButton);
        addClientFloatingButton.setVisibility(View.GONE);

        //obtiene el id de la factura seleccionada
        final String idInvoice = getIntent().getExtras().getString("idInvoice");

        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //envia id de cliente y factura y muestra pantalla de añadir factura
                Intent mainIntent = new Intent(InvoiceClientListSelectionActivity.this, InvoiceAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idClient", clientsGreenDao.get(position).getIdClient());
                bundle.putSerializable("idInvoice", idInvoice);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
