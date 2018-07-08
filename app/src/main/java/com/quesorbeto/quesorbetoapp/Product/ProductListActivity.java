package com.quesorbeto.quesorbetoapp.Product;

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
import QueSorbetoDataBase.Product;

public class ProductListActivity extends AppCompatActivity {

    ListView productList;
    FloatingActionButton addProductFloatingButton;
    List<Product> productsGreenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setTitle("Lista Productos");
        Utils.addBackPress(this);

        productList = findViewById(R.id.productList);
        addProductFloatingButton = findViewById(R.id.addProductFloatingButton);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainIntent = new Intent(ProductListActivity.this, ProductAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idProduct", productsGreenDao.get(position).getIdProduct());
                //setea las extras con el bundle
                mainIntent.putExtras(bundle);
                startActivity(mainIntent);
            }
        });

        addProductFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProductListActivity.this, ProductAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idProduct", null);
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

    private void setAdapterListView(){
        DbHelper dbHelper = new DbHelper(this);
        productsGreenDao = dbHelper.daoSession.getProductDao().queryBuilder().list();

        List<String> listProducts = new ArrayList<>();
        for(int x=0; x < productsGreenDao.size(); x++){
            listProducts.add(productsGreenDao.get(x).getName().toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listProducts);

        productList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
