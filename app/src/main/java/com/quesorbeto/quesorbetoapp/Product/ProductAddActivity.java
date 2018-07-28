package com.quesorbeto.quesorbetoapp.Product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.R;
import com.quesorbeto.quesorbetoapp.Utils;

import java.util.UUID;

import QueSorbetoDataBase.Product;
import QueSorbetoDataBase.ProductDao;

public class ProductAddActivity extends AppCompatActivity {

    Button saveProductButton;
    EditText productName;
    EditText productCode;
    EditText productPrice;
    Product product = null;
    ImageButton btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        getSupportActionBar().setTitle("Producto");
        Utils.addBackPress(this);

        saveProductButton = findViewById(R.id.saveProductButton);
        productPrice = findViewById(R.id.productPrice);
        productCode = findViewById(R.id.productCode);
        productName = findViewById(R.id.productName);
        btnEdit = findViewById(R.id.btnEdit);

        String idProduct = getIntent().getExtras().getString("idProduct");

        if(idProduct != null){
            DbHelper dbHelper = new DbHelper(ProductAddActivity.this);
            product = dbHelper.daoSession.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.IdProduct.eq(idProduct))
                    .unique();

            productName.setText(product.getName());
            productCode.setText(product.getCode());
            productPrice.setText(String.valueOf(product.getPrice()));

            disableEditableTextFields();

            //set edit button visible
            btnEdit.setVisibility(View.VISIBLE);

        }else{
            product = new Product();
        }

        saveProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!productCode.getText().toString().equals("") &&
                        !productPrice.getText().toString().equals("")){
                    DbHelper dbHelper = new DbHelper(ProductAddActivity.this);

                    product.setName(productName.getText().toString()+" ₡"+productPrice.getText().toString());
                    product.setCode(productCode.getText().toString());
                    product.setPrice(Double.parseDouble(productPrice.getText().toString()));

                    if(product.getIdProduct() == null){
                        product.setIdProduct(UUID.randomUUID().toString());
                        dbHelper.daoSession.getProductDao().insert(product);
                    }else{
                        dbHelper.daoSession.getProductDao().update(product);
                    }

                    Utils.saveSuccess(ProductAddActivity.this);
                }else{
                    Utils.fillRequiredFields(ProductAddActivity.this);
                }
            }
        });

        //edit button Listener
        btnEditOnClickLister();
    }

    private void disableEditableTextFields () {
        productName.setFocusable(false);
        productCode.setFocusable(false);
        productPrice.setFocusable(false);
    }

    private void btnEditOnClickLister () {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productName.setFocusableInTouchMode(true);
                productCode.setFocusableInTouchMode(true);
                productPrice.setFocusableInTouchMode(true);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
