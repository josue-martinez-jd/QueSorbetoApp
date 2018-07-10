package com.quesorbeto.quesorbetoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    //agregamos los componentes de la pantalla
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;

    //metodo que se inicia al llamar el activity (la pantalla)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String fromMain="";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            fromMain=extras.getString("incorrect");
        }

        if(fromMain.contains("incorrect")){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
            builder1.setMessage("Usuario o contraseña incorrecto.");
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


        //ligamos el componente con el layout xml
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        //asigna al boton de login una accion
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verifica si el usuario y contraseña estan correctamente llenos
                if (!emailEditText.getText().toString().equals("")
                        && !passwordEditText.getText().toString().equals("")) {

                    //Envia hacia que intent debe ir
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

                    //manda el usuario y contraseña por medio de bundle
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("email", emailEditText.getText().toString());
                    bundle.putSerializable("password", passwordEditText.getText().toString());

                    //setea las extras con el bundle
                    mainIntent.putExtras(bundle);

                    //inicia el nuevo activity
                    startActivity(mainIntent);
                } else {
                    //si no esta lleno llama un alert con un mensaje de llenar todos los espacios
                    Utils.fillRequiredFields(LoginActivity.this);
                }
            }
        });

        //llama el metodos de shared preferences admin
        addSharedPreferenceAdmin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        emailEditText.setText("");
        passwordEditText.setText("");
    }

    //Añade las preferencias compartidas la primera vez que ingresa al app
    private void addSharedPreferenceAdmin() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SHARED_PREFERENCE", MODE_PRIVATE);

        //verifica si ya existen las preferencias compartidas
        String userName = pref.getString("ADMIN_USERNAME", null);
        String password = pref.getString("ADMIN_PASSWORD", null);

        //si las preferencias compartidas no existen, las ingresa
        //en este caso el usuario sera test y la contraseña test
        if (password == null && userName == null) {
            SharedPreferences.Editor editor = getSharedPreferences("SHARED_PREFERENCE", MODE_PRIVATE).edit();
            editor.putString("ADMIN_USERNAME", "test");
            editor.putString("ADMIN_PASSWORD", "test");
            editor.commit();
        }
    }
}
