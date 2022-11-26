package com.example.invoiceapp_braedengiasson.helpers;

import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;

import java.util.ArrayList;
import java.util.List;

public class DefaultCustomers {
    public static List<Customer> generate(){
        List<Customer> customers = new ArrayList<>();

        customers.add(Customer1());
        customers.add(Customer2());
        customers.add(Customer3());

        return customers;
    }

    private static Customer Customer1(){
        return new Customer("Cody Pol");
    }

    private static Customer Customer2(){
        return new Customer("Brae Yen");
    }

    private static Customer Customer3(){
        return new Customer("Kalle Hall");
    }
}
