package com.quesorbeto.quesorbetoapp.Invoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.R;
import com.quesorbeto.quesorbetoapp.Utils;

import java.util.ArrayList;
import java.util.List;
import QueSorbetoDataBase.Invoice;

public class InvoiceListActivity extends AppCompatActivity {

    //componentes graficos y logicos
    ListView invoiceList;
    FloatingActionButton addInvoiceFloatingButton;
    List<Invoice> invoicesGreenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        //setea logica de back y titulo
        getSupportActionBar().setTitle("Lista Facturas");
        Utils.addBackPress(this);

        invoiceList = findViewById(R.id.invoiceList);
        addInvoiceFloatingButton = findViewById(R.id.addInvoiceFloatingButton);

        //cuando selecciona una opcion de  la factura
        //redirecciona a añadir factura pero solo mostrando los datos
        invoiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainIntent = new Intent(InvoiceListActivity.this, InvoiceAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idInvoice", invoicesGreenDao.get(position).getIdInvoice());
                //setea las extras con el bundle
                mainIntent.putExtras(bundle);
                startActivity(mainIntent);
            }
        });

        //cuando da click en añadir factura, redirecciona a añadir factura
        addInvoiceFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(InvoiceListActivity.this, InvoiceClientListSelectionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idInvoice", null);
                //setea las extras con el bundle
                mainIntent.putExtras(bundle);
                startActivity(mainIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapterListView();
    }

    //carga los datos de la factura en la lista
    private void setAdapterListView(){
        DbHelper dbHelper = new DbHelper(this);
        invoicesGreenDao = dbHelper.daoSession.getInvoiceDao().queryBuilder().list();

        List<Invoice> listInvoices = new ArrayList<>();
        for(int x=0; x < invoicesGreenDao.size(); x++){
            listInvoices.add(invoicesGreenDao.get(x));
        }
        invoiceList.setAdapter(new InvoiceListAdapter(InvoiceListActivity.this, listInvoices));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
