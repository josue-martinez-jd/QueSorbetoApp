package com.quesorbeto.quesorbetoapp.Client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jaredrummler.materialspinner.MaterialSpinner;

import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.R;
import com.quesorbeto.quesorbetoapp.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
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
    MaterialSpinner spinnerCountries;
    ImageButton btnEdit;

    //variables globales
    List<String> arrayCountries = new ArrayList<>();
    String sChosenCountry = null;

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
        spinnerCountries = findViewById(R.id.spinnerCountries);
        btnEdit = findViewById(R.id.btnEdit);

        saveClientButton.setText("Aceptar");

        //ingresa los paises en el spinner
        spinnerCountriesAdd();

        //obtiene el id del cliente seleccionado
        String idClient = getIntent().getExtras().getString("idClient");

        //si selecciono cliente cargue lo necesario en los componentes edittext visuales
        if(idClient != null){
            DbHelper dbHelper = new DbHelper(ClientAddActivity.this);
            client = dbHelper.daoSession.getClientDao().queryBuilder()
                    .where(ClientDao.Properties.IdClient.eq(idClient))
                    .unique();

            clientName.setText(client.getName());
            clientAdress.setText(client.getAddress());
            clientPhone.setText(client.getPhone());

            disableEditableTextFields();

            //set edit button visible and country dropdown invisible
            btnEdit.setVisibility(View.VISIBLE);
            spinnerCountries.setVisibility(View.INVISIBLE);

        }else{
            //si no selecciono cliente cree uno nuevo
            client = new Client();
        }

        //al escoger un pais
        spinnerCountriesListener();

        //al dar boton de salvar
        saveClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verifica campos requeridos
                if(!clientName.getText().toString().equals("")){
                    DbHelper dbHelper = new DbHelper(ClientAddActivity.this);

                    //setea los nuevos valores
                    if(sChosenCountry==null){
                        sChosenCountry = client.getAddress().split(",")[0].toString();
                    }

                    client.setName(clientName.getText().toString());
                    client.setAddress(sChosenCountry + ',' + clientAdress.getText().toString());
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

        //edit button Listener
        btnEditOnClickLister();
    }

    private void spinnerCountriesAdd() {
        serviceTasks service = new serviceTasks();
        service.execute();
    }

    private void spinnerCountriesListener () {
        spinnerCountries.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                //obtiene el pais seleccionado
                sChosenCountry = arrayCountries.get(position);
               // Utils.showMessage(ClientAddActivity.this,sChosenCountry);
            }
        });
    }

    private void disableEditableTextFields () {
        clientName.setFocusable(false);
        clientAdress.setFocusable(false);
        clientPhone.setFocusable(false);
    }

    private void btnEditOnClickLister () {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveClientButton.setText("Guardar");
                btnEdit.setVisibility(View.INVISIBLE);



                String[] currentAddressArray =client.getAddress().split(",");
                String currentAddress="";
                String country=currentAddressArray[0];

                for(int i =1; i<currentAddressArray.length;i++){
                    if(i!=currentAddressArray.length-1){
                        currentAddress= currentAddress+currentAddressArray[i]+",";
                    }else{
                        currentAddress= currentAddress+currentAddressArray[i];
                    }
                }

                clientName.setText(client.getName());
                clientAdress.setText(currentAddress);

                clientName.setFocusableInTouchMode(true);
                clientAdress.setFocusableInTouchMode(true);
                clientPhone.setFocusableInTouchMode(true);
                spinnerCountries.setVisibility(View.VISIBLE);

                //Gets the country from the first value of the Address
                if(client.getAddress().split(",")[0].toString()!=null){
                    country = client.getAddress().split(",")[0].toString();
                }

                //Sets the selected value of the dropdown with the country from the Address
                for (int i = 0; i < arrayCountries.size(); i++) {
                    if(arrayCountries.get(i).toString().contains(country)) {
                        spinnerCountries.setSelectedIndex(i);
                        return;
                    }
                }


            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       onBackPressed();
       return true;
    }

    private class serviceTasks extends AsyncTask < Void, Void, Void > {
        String urlTxt = "http://country.io/names.json";
        String bufferText;
        String finalText;

        protected Void doInBackground ( Void... params) {
            URL url;
            try{
                url = new URL(urlTxt);
                BufferedReader theBufferReader = new BufferedReader( new InputStreamReader(url.openStream()));

                while ((bufferText = theBufferReader.readLine()) != null) {
                    finalText += bufferText;
                }

                theBufferReader.close();

            } catch (MalformedURLException e) {
                Utils.showMessage(ClientAddActivity.this,"Error al abrir el URL!");
                e.printStackTrace();
                Log.d("===>>ERROR: ", e.toString());
            } catch (IOException e) {
                Utils.showMessage(ClientAddActivity.this,"Error al cargar los datos!");
                e.printStackTrace();
                Log.d("===>>ERROR: ", e.toString());
            }
            return null;
        }

        protected void onPostExecute ( Void result) {
            try {
                String prefix = "null";
                if (finalText.startsWith(prefix)){
                    finalText = finalText.substring(prefix.length(), finalText.length());
                }

                JSONObject jsonClient = new JSONObject(finalText);

                //Agregar paises al dropdown
                for (int i = 0; i < jsonClient.length(); i++) {
                    arrayCountries.add(jsonClient.get(jsonClient.names().getString(i)).toString());
                }
                Utils.sortAlphabeticallyArrayList(arrayCountries);

                spinnerCountries.setItems(arrayCountries);
            }
            catch (JSONException e) {
                Utils.showMessage(ClientAddActivity.this,"Error al mostrar los datos! \n" +
                                                                            "Json Response:\n"+ finalText);
                e.printStackTrace();
                Log.d("===>>ERROR: ", e.toString());
            }
            super.onPostExecute(result);
        }

    }
}
