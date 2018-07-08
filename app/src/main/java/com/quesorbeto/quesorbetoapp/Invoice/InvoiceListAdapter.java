package com.quesorbeto.quesorbetoapp.Invoice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import QueSorbetoDataBase.Invoice;

//Clase que funciona como estilo y logica para cada una de las celdas de la lista de facturas, por eso extiende de base adapter
public class InvoiceListAdapter extends BaseAdapter {

    private Context context;
    private List<Invoice> invoices;

    //clase constuctor
    public InvoiceListAdapter(Context context, List<Invoice> invoices) {
        this.context = context;
        this.invoices = invoices;
    }

    @Override
    public int getCount() {
        return invoices.size();
    }

    @Override
    public Object getItem(int position) {
        return invoices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TwoLineListItem twoLineListItem;

        //utilizamos el layout simple list item 2 para tener un diseño de celdas por defecto
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        //seteamos los valores requeridos a cada uno de los textos
        text1.setText( "Factura número: QSORB-" + invoices.get(position).getInvoiceNumber().toString());
        text2.setText(DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(invoices.get(position).getInvoiceDate()));

        return twoLineListItem;
    }
}
