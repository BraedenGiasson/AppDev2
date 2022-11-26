package com.example.invoiceapp_braedengiasson.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "invoice_details_table",
    foreignKeys = {
            @ForeignKey(entity = Invoice.class,
                    parentColumns = "id",
                    childColumns = "invoice_id",
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
    }
)
public class InvoiceDetails {

    //region Fields

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "invoice_id")
    private int invoiceId;

    @NonNull
    @ColumnInfo(name = "product_name")
    private String productName;

    @NonNull
    @ColumnInfo(name = "price_per_unit")
    private double pricePerUnit;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    @NonNull
    @ColumnInfo(name = "line_total")
    private double lineTotal;

    //endregion

    //region Constructors

    /**
     * Instantiates a new instance of the {{@link InvoiceDetails}} class.
     * @param invoiceId The id of the current invoice.
     * @param productName The name of the product.
     * @param pricePerUnit The price amount per unit.
     * @param lineTotal The amount per line in the invoice.
     */
    @Ignore
    public InvoiceDetails(
            int invoiceId,
            @NonNull String productName,
            double pricePerUnit,
            double lineTotal
    ) {
        setInvoiceId(invoiceId);
        setProductName(productName);
        setPricePerUnit(pricePerUnit);
        setLineTotal(lineTotal);
    }

    /**
     * Instantiates a new instance of the {{@link InvoiceDetails}} class.
     * @param invoiceId The id of the current invoice.
     * @param productName The name of the product.
     * @param pricePerUnit The price amount per unit.
     * @param quantity The quantity amount of this product.
     */
    public InvoiceDetails(
            int invoiceId,
            @NonNull String productName,
            double pricePerUnit,
            int quantity
    ) {
        setInvoiceId(invoiceId);
        setProductName(productName);
        setPricePerUnit(pricePerUnit);
        setQuantity(quantity);
        setLineTotal(pricePerUnit * quantity);
    }

    /****** Maybe can delete */

    /*
     * This constructor is annotated using @Ignore, because Room expects only
     * one constructor by default in an entity class.
     */

    /**
     * Instantiates a new instance of the {{@link InvoiceDetails}} class.
     * @param id The invoice details id.
     * @param invoiceId The id of the current invoice.
     * @param productName The name of the product.
     * @param pricePerUnit The price amount per unit.
     * @param lineTotal The amount per line in the invoice.
     */
    @Ignore
    public InvoiceDetails(
            int id,
            int invoiceId,
            @NonNull String productName,
            double pricePerUnit,
            double lineTotal
    ) {
        setId(id);
        setInvoiceId(invoiceId);
        setProductName(productName);
        setPricePerUnit(pricePerUnit);
        setLineTotal(lineTotal);
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    public int getInvoiceId() {
        return this.invoiceId;
    }

    public String getProductName(){
        return this.productName;
    }

    public double getPricePerUnit(){
        return this.pricePerUnit;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getLineTotal(){
        return this.lineTotal;
    }

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setProductName(@NonNull String productName) {
        this.productName = productName;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = Math.floor(pricePerUnit * 100) / 100;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = Math.floor(lineTotal * 100) / 100;
    }

    //endregion
}
