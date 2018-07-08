package com.quesorbeto.quesorbetoapp.DBGenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class DataBaseGenerator
{
    public static void main(String[] args) throws Exception {
        try {
            //set the scheme and name database
            Schema schema = new Schema(1, "QueSorbetoDataBase") {
            };
            schema.enableKeepSectionsByDefault();
            CreateEntity(schema);

            //generate database in this path
            new DaoGenerator().generateAll(schema, "app/src/main/java");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void CreateEntity(Schema schema) {
        try {
            //Es necesario siempre mantener un id autoincrement
            //para mayor manejabilidad utilizo un id aparte, un GUID

            //crear la tabla cliente
            Entity Client = schema.addEntity("Client");
            Client.addIdProperty().autoincrement();
            Client.addStringProperty("idClient").notNull();
            Client.addStringProperty("name").notNull();
            Client.addStringProperty("phone");
            Client.addStringProperty("address");

            //crear la tabla producto
            Entity Product = schema.addEntity("Product");
            Product.addIdProperty().autoincrement();
            Product.addStringProperty("idProduct").notNull();
            Product.addStringProperty("name");
            Product.addStringProperty("code").notNull();
            Product.addDoubleProperty("Price").notNull();

            //crear la tabla factura
            Entity Invoice = schema.addEntity("Invoice");
            Invoice.addIdProperty().autoincrement();
            Invoice.addStringProperty("idClient").notNull();
            Invoice.addStringProperty("idInvoice").notNull();
            Invoice.addIntProperty("invoiceNumber");
            Invoice.addDateProperty("invoiceDate");
            Invoice.addDoubleProperty("invoiceTotalPrice");

            //Crear tabla detalle de factura
            Entity InvoiceDetail = schema.addEntity("InvoiceDetail");
            InvoiceDetail.addIdProperty().autoincrement();
            InvoiceDetail.addStringProperty("idInvoice").notNull();
            InvoiceDetail.addIntProperty("quantity").notNull();
            InvoiceDetail.addStringProperty("idProduct").notNull();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
