package com.example.invoiceapp_braedengiasson.helpers;

import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;

import java.util.ArrayList;
import java.util.List;

public class DefaultAddresses {
    public static Address Address1(int customer1Id){
        return new Address(
                customer1Id,
                "123 Rue Ben",
                "Valleyfield",
                "Quebec",
                "Canada"
        );
    }

    public static Address Address2(int customer2Id){
        return new Address(
                customer2Id,
                "478 Leperd Street",
                "Penfield",
                "Ohio",
                "USA"
        );
    }

    public static Address Address3(int customer3Id){
        return new Address(
                customer3Id,
                "478 Svenska",
                "Goteberg",
                "",
                "Sweden"
        );
    }
}
