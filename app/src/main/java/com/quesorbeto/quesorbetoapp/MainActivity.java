package com.quesorbeto.quesorbetoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.quesorbeto.quesorbetoapp.Client.ClientListActivity;
import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.Invoice.InvoiceAddActivity;
import com.quesorbeto.quesorbetoapp.Invoice.InvoiceClientListSelectionActivity;
import com.quesorbeto.quesorbetoapp.Invoice.InvoiceListActivity;
import com.quesorbeto.quesorbetoapp.Product.ProductListActivity;

import java.util.List;

import QueSorbetoDataBase.Client;
import QueSorbetoDataBase.Product;

public class MainActivity extends AppCompatActivity {

    //Utilizamos linearlayotu como boton
    LinearLayout customerButton;
    LinearLayout logoutButton;
    LinearLayout productButton;
    LinearLayout invoiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerButton = findViewById(R.id.customerButton);
        logoutButton = findViewById(R.id.logoutButton);
        productButton = findViewById(R.id.productButton);
        invoiceButton = findViewById(R.id.invoiceButton);

        //todas las clases heredan de view, el cual tiene todos los eventos de un boton, por ejemplo el click listener
        //entonces el layout funciona como boton en otras palabras, y cualquier otro componente puede hacerlo
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redireccionamos a la lista de clientes
                Intent mainIntent = new Intent(MainActivity.this, ClientListActivity.class);
                startActivity(mainIntent);
            }
        });

        //deslogueamos
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });

        //redireccionamos a lista de productos
        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(mainIntent);
            }
        });

        //redireccionamos a lista de facturas
        invoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hace el la instancia de la base de datos
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                //llama la lista de productos
                List<Product> productList = dbHelper.daoSession.getProductDao().queryBuilder().list();
                //llama la lista de clientes
                List<Client> clientList = dbHelper.daoSession.getClientDao().queryBuilder().list();

                //si no hay clientes o productos no podemos ingresar a la lista
                if(productList.size() > 0 && clientList.size() > 0){
                    Intent mainIntent = new Intent(MainActivity.this, InvoiceListActivity.class);
                    startActivity(mainIntent);
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Para realizar una factura, necesitas clientes y productos, verifica que existan.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        //lee los extras de la pagina anterior
        String email = getIntent().getExtras().getString("email");
        String password = getIntent().getExtras().getString("password");

        //llama a la funcion para saber si el usuario y contraseña son iguales
        //a los guardados en preferencias compartidas, si son distitnos manda un msj de
        //usuario y contraseña incorrectos
        if(!isCorrectAdminUser(email, password)){
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            loginIntent.putExtra("incorrect","incorrect");
            startActivity(loginIntent);
        }

        //añade titulo al activity del menu principal
        getSupportActionBar().setTitle("Menú principal");
    }

    //verifica si el usuario y contraseña son correctos
    private boolean isCorrectAdminUser(String emailExtra, String passwordExtra){

        //hace el llamado a shared preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SHARED_PREFERENCE", MODE_PRIVATE);
        String userName = pref.getString("ADMIN_USERNAME", null);
        String password = pref.getString("ADMIN_PASSWORD", null);

        //verifica si el shared preference guardado y el usuario y contraseña ingresados son iguales
        //si son iguales retorna verdadero, si no falso
        if(userName.equals(emailExtra) && password.equals(passwordExtra)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //no permite que se devuelva al login otra vez con la tecla back
        moveTaskToBack(true);
    }
}
