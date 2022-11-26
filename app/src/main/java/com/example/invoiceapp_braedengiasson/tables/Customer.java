package com.example.invoiceapp_braedengiasson.tables;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "customer_table")
public class Customer {

    //region Fields

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @Nullable
    @ColumnInfo(name = "default_address_id")
    private int defaultAddressId;

    //endregion

    //region Constructors

    /**
     * Instantiates a new instance of the {{@link Customer}} class.
     * @param name The customer name.
     * @param defaultAddressId The customer's default address.
     */
    public Customer(
            @NonNull String name,
            @Nullable int defaultAddressId) {
        setName(name);
        setDefaultAddressId(defaultAddressId);
    }

    /**
     * Instantiates a new instance of the {{@link Customer}} class.
     * @param name The customer name.
     */
    @Ignore
    public Customer(@NonNull String name) {
        setName(name);
    }

    /****** Maybe can delete */

    /*
     * This constructor is annotated using @Ignore, because Room expects only
     * one constructor by default in an entity class.
     */

    /**
     * Instantiates a new instance of the {{@link Customer}} class.
     * @param id The customer's id.
     * @param name The customer name.
     * @param defaultAddressId The customer's default address.
     */
    @Ignore
    public Customer(
            int id,
            @NonNull String name,
            int defaultAddressId) {
        setId(id);
        setName(name);
        setDefaultAddressId(defaultAddressId);
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public int getDefaultAddressId(){
        return this.defaultAddressId;
    }

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name){
        this.name = name;
    }

    public void setDefaultAddressId(@Nullable int defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    //endregion
}
