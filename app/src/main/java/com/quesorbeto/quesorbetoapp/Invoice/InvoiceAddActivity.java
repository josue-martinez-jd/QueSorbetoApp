package com.quesorbeto.quesorbetoapp.Invoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.R;
import com.quesorbeto.quesorbetoapp.Utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import QueSorbetoDataBase.Client;
import QueSorbetoDataBase.Invoice;
import QueSorbetoDataBase.InvoiceDao;
import QueSorbetoDataBase.InvoiceDetail;
import QueSorbetoDataBase.InvoiceDetailDao;
import QueSorbetoDataBase.Product;
import QueSorbetoDataBase.ProductDao;

public class InvoiceAddActivity extends AppCompatActivity {

    //componentes graficos
    Button saveInvoiceButton;
    TextView invoiceDateTextView;
    TextView invoiceNumberTextView;
    MaterialSpinner spinnerProduct;
    EditText quantityEditText;
    TextView totalPriceTextView;
    TextView invoiceClientTextView;
    ListView productInvoiceListView;
    FloatingActionButton addProductFloatingButton;
    LinearLayout productsAndQuantityLinearLayout;

    //componentes logicos
    Invoice invoice = null;
    Product product = null;
    Client client = null;
    //Inicializa la variable de precio en 0
    double sumTotalPriceInvoice = 0;

    //listas de componentes logicos
    List<Client> clientList;
    List<Product> productList;
    List<InvoiceDetail> invoiceDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //obtiene el layout de la factura
        setContentView(R.layout.activity_invoice);
        //setea el titulo de facturacion
        getSupportActionBar().setTitle("Facturación");
        //setea la funcionalidad de back press
        Utils.addBackPress(this);

        //Puente de union entre la logica y el layout
        saveInvoiceButton = findViewById(R.id.saveInvoiceButton);
        invoiceDateTextView = findViewById(R.id.invoiceDateTextView);
        invoiceNumberTextView = findViewById(R.id.invoiceNumberTextView);
        spinnerProduct = findViewById(R.id.spinnerProduct);
        quantityEditText = findViewById(R.id.quantityEditText);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        invoiceClientTextView = findViewById(R.id.invoiceClientTextView);
        productInvoiceListView = findViewById(R.id.productInvoiceListView);
        addProductFloatingButton = findViewById(R.id.addProductFloatingButton);
        productsAndQuantityLinearLayout = findViewById(R.id.productsAndQuantityLinearLayout);

        //obtiene los datos del cliente y la factura seleccionada anteriormente
        String idClient = getIntent().getExtras().getString("idClient");
        String idInvoice = getIntent().getExtras().getString("idInvoice");

        //hace el la instancia de la base de datos
        DbHelper dbHelper = new DbHelper(InvoiceAddActivity.this);
        //llama la lista de productos
        productList = dbHelper.daoSession.getProductDao().queryBuilder().list();
        //llama la lista de clientes
        clientList = dbHelper.daoSession.getClientDao().queryBuilder().list();
        //asigna la lista de detalle de factura como vacia
        invoiceDetailList = new ArrayList<>();

        //ingresa los productos a la lista de productos
        List<String> productString = new ArrayList<>();
        for (int x = 0; x < productList.size(); x++) {
            productString.add(productList.get(x).getName());
        }

        //ingresa los productos al spinner
        spinnerProduct.setItems(productString);

        //seleccionamos el valor inicial de los productos
        if (productList.size() > 0) {
            product = productList.get(0);
        }

        //si trae un id de factura haga
        if (idInvoice != null) {

            //OCULTA EL LINEAR LAYOUT DE PRODUCTOS Y CANTIDAD
            productsAndQuantityLinearLayout.setVisibility(View.INVISIBLE);

            //Oculta el boton de guardar
            saveInvoiceButton.setVisibility(View.INVISIBLE);

            //obtiene la factura de la base de datos
            invoice = dbHelper.daoSession.getInvoiceDao().queryBuilder()
                    .where(InvoiceDao.Properties.IdInvoice.eq(idInvoice))
                    .unique();

            //obtiene la fecha de base de datos
            //Se realiza date format para darle formato mas legible al objeto Date
            invoiceDateTextView.setText(DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.MEDIUM).format(invoice.getInvoiceDate()));

            //obtiene el numero de factura final
            invoiceNumberTextView.setText("Factura número: QSORB-" + invoice.getInvoiceNumber().toString());
            //obtiene el precio total
            totalPriceTextView.setText("₡ " + invoice.getInvoiceTotalPrice().toString());

            //llama la lista de clientes
            invoiceDetailList = dbHelper.daoSession.getInvoiceDetailDao().queryBuilder()
                    .where(InvoiceDetailDao.Properties.IdInvoice.eq(idInvoice))
                    .list();

            //setea el adapter con la lista actualizada
            productInvoiceListView.setAdapter(
                    new InvoiceProductsDetailAdapter(
                            InvoiceAddActivity.this, invoiceDetailList));
        } else {
            //si no trae id de factura setee valores por defecto
            invoice = new Invoice();
            product = productList.get(0);
            invoiceDateTextView.setText(DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()));
            totalPriceTextView.setText("₡ 0");

            //llama la ultima factura para conocer el numero de factura, y poder simular un autoincrement
            Invoice lastInvoice = dbHelper.daoSession.getInvoiceDao().queryBuilder()
                    .orderDesc(InvoiceDao.Properties.InvoiceNumber)
                    .limit(1)
                    .unique();
            //si hay una factura anterior
            if (lastInvoice != null) {
                //obtenga el numero de factura anterior y sumele 1
                invoiceNumberTextView.setText("Factura número: QSORB-" + String.valueOf(lastInvoice.getInvoiceNumber() + 1));
            } else {
                //si no asigne 1
                invoiceNumberTextView.setText("Factura número: QSORB-1");
            }
        }

        //obtiene los datos del cliente de acuerdo al id seleccionado
        for (int x = 0; x < clientList.size(); x++) {

            if(idClient != null){
                if (clientList.get(x).getIdClient().equals(idClient)) {
                    client = clientList.get(x);
                }
            }else if(invoice != null){
                if (clientList.get(x).getIdClient().equals(invoice.getIdClient())) {
                    client = clientList.get(x);
                }
            }
        }

        //setea el nombre del cliente seleccionado
        invoiceClientTextView.setText(client.getName());

        //al seleccionar un item del spinner
        spinnerProduct.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                //obtiene producto seleccionado
                product = productList.get(position);
            }
        });

        //al ingresar un producto al listView
        addProductFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Limpia la vaiable de suma total de precios
                sumTotalPriceInvoice = 0;

                //el quantity no debe estar vacio
                if (!quantityEditText.getText().toString().equals("")) {

                    //si el producto esta dentro de la lista
                    boolean isInList = false;
                    for (int x = 0; x < invoiceDetailList.size(); x++) {
                        if (product.getIdProduct().equals(invoiceDetailList.get(x).getIdProduct())) {
                            //solamente sume la nueva cantidad
                            invoiceDetailList.get(x).setQuantity(invoiceDetailList.get(x).getQuantity() +
                                    Integer.parseInt(quantityEditText.getText().toString()));
                            isInList = true;
                        }
                    }

                    //si no esta dentro de la lista
                    if (!isInList) {
                        //ingresa el invoice detail dentro de la lista de detalle
                        InvoiceDetail invoiceDetail = new InvoiceDetail();
                        invoiceDetail.setIdProduct(product.getIdProduct());
                        invoiceDetail.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                        invoiceDetailList.add(invoiceDetail);
                    }

                    //setea el adapter con la lista actualizada
                    productInvoiceListView.setAdapter(
                            new InvoiceProductsDetailAdapter(
                                    InvoiceAddActivity.this, invoiceDetailList));


                    for (int x = 0; x < invoiceDetailList.size(); x++) {

                        //Llama al prodcuto seleccionado de la lista
                        DbHelper dbHelper = new DbHelper(InvoiceAddActivity.this);
                        Product product = dbHelper.daoSession.getProductDao().queryBuilder()
                                .where(ProductDao.Properties.IdProduct.eq(invoiceDetailList.get(x).getIdProduct()))
                                .unique();

                        //obtiene el precio del producto y lo suma al precio total
                        sumTotalPriceInvoice = sumTotalPriceInvoice + (product.getPrice() * invoiceDetailList.get(x).getQuantity());
                    }

                    //setea el nuevo precio total
                    totalPriceTextView.setText("₡ " + String.valueOf(sumTotalPriceInvoice));
                }
            }
        });

        //salvar o actualizar la factura
        saveInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invoiceDetailList.size() > 0) {

                    //Instancia el objeto ORM de ayuda para acceso a la base de datos
                    DbHelper dbHelper = new DbHelper(InvoiceAddActivity.this);

                    //Ingresa la nueva suma total de precios de productos
                    invoice.setInvoiceTotalPrice(sumTotalPriceInvoice);

                    //setea el nuevo id
                    invoice.setIdInvoice(UUID.randomUUID().toString());

                    //setea el id del cliente ligado
                    invoice.setIdClient(client.getIdClient());
                    //setea la nueva fecha
                    invoice.setInvoiceDate(new Date());

                    //llama la ultima factura para conocer el numero de factura, y poder simular un autoincrement
                    Invoice lastInvoice = dbHelper.daoSession.getInvoiceDao().queryBuilder()
                            .orderDesc(InvoiceDao.Properties.InvoiceNumber)
                            .limit(1)
                            .unique();
                    //si hay una factura anterior
                    if (lastInvoice != null) {
                        //obtenga el numero de factura anterior y sumele 1
                        invoice.setInvoiceNumber(lastInvoice.getInvoiceNumber() + 1);
                    } else {
                        //si no asigne 1
                        invoice.setInvoiceNumber(1);
                    }

                    try {
                        //inserte el objeto factura a la base de datos
                        dbHelper.daoSession.getInvoiceDao().insert(invoice);

                        //recorre el objeto detalle y lo inserta en la BD
                        for (int x = 0; x < invoiceDetailList.size(); x++) {
                            InvoiceDetail invoiceDetail = new InvoiceDetail();
                            invoiceDetail.setIdInvoice(invoice.getIdInvoice());
                            invoiceDetail.setIdProduct(invoiceDetailList.get(x).getIdProduct());
                            invoiceDetail.setQuantity(invoiceDetailList.get(x).getQuantity());
                            dbHelper.daoSession.getInvoiceDetailDao().insert(invoiceDetail);
                        }

                        //si salio bien muestra mensaje de success y devuelve a la lista
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(InvoiceAddActivity.this);
                        builder1.setMessage("Guardado con éxito.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(InvoiceAddActivity.this, InvoiceListActivity.class));
                                        finish();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    } catch (Exception ex) {

                        //si algo salio mal muestra un mensaje de error
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(InvoiceAddActivity.this);
                        builder1.setMessage("Error guardando datos.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        InvoiceAddActivity.this.onBackPressed();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                } else {
                    //muestra mensaje de llenar todos los campos requeridos
                    //en este caso si no tiene detalle de factura no la guarda
                    Utils.fillRequiredFields(InvoiceAddActivity.this);
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
