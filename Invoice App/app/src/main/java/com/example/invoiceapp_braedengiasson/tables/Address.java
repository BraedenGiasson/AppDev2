package com.example.invoiceapp_braedengiasson.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.Arrays;

@Entity(tableName = "address_table",
    foreignKeys = {
            @ForeignKey(entity = Customer.class,
                    parentColumns = "id",
                    childColumns = "customer_id",
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
    }
)
public class Address {

    //region Fields

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "customer_id")
    private int customerId;

    @NonNull
    @ColumnInfo(name = "street")
    private String street;

    @NonNull
    @ColumnInfo(name = "city")
    private String city;

    @NonNull
    @ColumnInfo(name = "province")
    private String province;

    @NonNull
    @ColumnInfo(name = "country")
    private String country;

    //endregion

    //region Constructors

    /**
     * Instantiates a new instance of the {{@link Address}} class.
     * @param customerId The customer's id at the address.
     * @param street The address street name.
     * @param city The address city name.
     * @param province The address province name.
     * @param country The address country name.
     */
    public Address(
            int customerId,
            @NonNull String street,
            @NonNull String city,
            String province,
            @NonNull String country
    ) {
        setCustomerId(customerId);
        setStreet(street);
        setCity(city);
        setProvince(province);
        setCountry(country);
    }

    /****** Maybe can delete */

    /*
     * This constructor is annotated using @Ignore, because Room expects only
     * one constructor by default in an entity class.
     */

    /**
     * Instantiates a new instance of the {{@link Address}} class.
     *  @param id The address id.
     *  @param customerId The customer's id at the address.
     *  @param street The address street name.
     *  @param city The address city name.
     *  @param province The address province name.
     *  @param country The address country name.
     */
    @Ignore
    public Address(
            int id,
            int customerId,
            @NonNull String street,
            @NonNull String city,
            String province,
            @NonNull String country
    ) {
        setId(id);
        setCustomerId(customerId);
        setStreet(street);
        setCity(city);
        setProvince(province);
        setCountry(country);
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public String getStreet(){
        return this.street;
    }

    public String getCity(){
        return this.city;
    }

    public String getProvince(){
        return this.province;
    }

    public String getCountry(){
        return this.country;
    }

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setStreet(@NonNull String street) {
        this.street = street;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public void setProvince(@NonNull String province) {
        this.province = province;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    //endregion

    @NonNull
    @Override
    public String toString() {
        return String.format("%s\n%s, %s, %s\n",
                getStreet(), getCity(), getProvince(), getCountry());
    }
}
