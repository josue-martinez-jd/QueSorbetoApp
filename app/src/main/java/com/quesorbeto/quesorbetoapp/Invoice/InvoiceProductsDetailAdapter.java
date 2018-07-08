package com.quesorbeto.quesorbetoapp.Invoice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quesorbeto.quesorbetoapp.DBGenerator.DbHelper;
import com.quesorbeto.quesorbetoapp.R;

import java.util.List;
import QueSorbetoDataBase.InvoiceDetail;
import QueSorbetoDataBase.Product;
import QueSorbetoDataBase.ProductDao;
//Clase que funciona como estilo y logica para cada una de las celdas de la lista de detalle de facturas,
// por eso extiende de base adapter
public class InvoiceProductsDetailAdapter extends BaseAdapter {

    private Context context;
    private List<InvoiceDetail> invoiceDetails;

    public InvoiceProductsDetailAdapter(Context context, List<InvoiceDetail> invoiceDetails) {

        this.context = context;
        this.invoiceDetails = invoiceDetails;
    }

    @Override
    public int getCount() {
        return invoiceDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return invoiceDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        //obtenemos nuestro detalle de factura por cada una de las celdas
        //(esto funciona como un for)
        InvoiceDetail invoiceDetail = (InvoiceDetail)getItem(position);

        //llamamos nuestro row layout, puede verse en la carpeta de layouts
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.row_product_invoice_layout, null);
        }else{
            view = convertView;
        }

        //obtenemos el producto de acuerdo al detalle de factura
        DbHelper dbHelper = new DbHelper(context);
        Product product = dbHelper.daoSession.getProductDao().queryBuilder()
                .where(ProductDao.Properties.IdProduct.eq(invoiceDetail.getIdProduct()))
                .unique();

        TextView textView =  view.findViewById(R.id.textView);
        TextView textViewSecond =  view.findViewById(R.id.textViewSecond);
        TextView textViewThird =  view.findViewById(R.id.textViewThird);

        //asignamos lo necesario a nuestros textviews, para darle una razon mas logica al cliente final
        textView.setText(product.getName());
        textViewSecond.setText("Cantidad: " + invoiceDetail.getQuantity());
        textViewThird.setText( "₡ " + String.valueOf(product.getPrice() * invoiceDetail.getQuantity()));

        return view;
    }

}
