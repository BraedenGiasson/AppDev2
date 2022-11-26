package com.example.invoiceapp_braedengiasson.helpers;

import android.annotation.SuppressLint;

import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.tables.Invoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class DefaultInvoices {

    private static int[] totals = new int[]{
        100,
        200,
        250,
        350,
        500,
        800,
        444
    };

    public static Invoice generate(int customerId){
        Random random1 = new Random();
        int randomIndex = random1.nextInt(3);

        Random random2 = new Random();
        int randomIndex2 = random2.nextInt(totals.length);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendarInstance = Calendar.getInstance();
        String dateOfIssue = sdf.format(calendarInstance.getTime());

        switch (randomIndex){
            case 0:
                return new Invoice(
                        customerId,
                        DefaultAddresses.Address1(customerId).toString(),
                        totals[randomIndex2],
                        dateOfIssue);
            case 1:
                return new Invoice(
                        customerId,
                        DefaultAddresses.Address2(customerId).toString(),
                        totals[randomIndex2],
                        dateOfIssue);
        }

        return new Invoice(
                customerId,
                DefaultAddresses.Address3(customerId).toString(),
                totals[randomIndex2],
                dateOfIssue);
    }
}
