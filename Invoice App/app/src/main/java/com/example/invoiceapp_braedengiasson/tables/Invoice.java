package com.example.invoiceapp_braedengiasson.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoice_table",
    foreignKeys = {
            @ForeignKey(entity = Customer.class,
                    parentColumns = "id",
                    childColumns = "customer_id",
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
    }
)
public class Invoice {

    //region Fields

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "customer_id")
    private int customerId;

    @NonNull
    @ColumnInfo(name = "delivery_address")
    private String deliveryAddress;

    @NonNull
    @ColumnInfo(name = "total")
    private double total;

    @NonNull
    @ColumnInfo(name = "date_of_issue")
    private String dateOfIssue;

    //endregion

    //region Constructors

    /**
     * Instantiates a new instance of the {{@link Invoice}} class.
     * @param customerId The customer's id for the invoice.
     * @param deliveryAddress The delivery address for the invoice.
     * @param total The total of the invoice.
     */
    public Invoice(
            int customerId,
            @NonNull String deliveryAddress,
            double total,
            @NonNull String dateOfIssue
    ) {
        setCustomerId(customerId);
        setDeliveryAddress(deliveryAddress);
        setTotal(total);
        setDateOfIssue(dateOfIssue);
    }

    /*
     * This constructor is annotated using @Ignore, because Room expects only
     * one constructor by default in an entity class.
     */

    /**
     * Instantiates a new instance of the {{@link Invoice}} class.
     * @param id The invoice id.
     * @param customerId The customer's id for the invoice.
     * @param deliveryAddress The delivery address for the invoice.
     * @param total The total of the invoice.
     */
    @Ignore
    public Invoice(
            int id,
            int customerId,
            @NonNull String deliveryAddress,
            double total
    ) {
        setId(id);
        setCustomerId(customerId);
        setDeliveryAddress(deliveryAddress);
        setTotal(total);
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public String getDeliveryAddress(){
        return this.deliveryAddress;
    }

    public double getTotal(){
        return this.total;
    }

    public String getDateOfIssue(){
        return this.dateOfIssue;
    }

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setDeliveryAddress(@NonNull String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setTotal(double total) {
        this.total = Math.floor(total * 100) / 100;
    }

    public void setDateOfIssue(@NonNull String dateOfIssue){
        this.dateOfIssue = dateOfIssue;
    }

    //endregion
}
