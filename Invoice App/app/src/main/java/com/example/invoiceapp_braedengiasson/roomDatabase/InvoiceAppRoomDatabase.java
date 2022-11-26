package com.example.invoiceapp_braedengiasson.roomDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.invoiceapp_braedengiasson.dao.AddressDao;
import com.example.invoiceapp_braedengiasson.dao.CustomerDao;
import com.example.invoiceapp_braedengiasson.dao.InvoiceDao;
import com.example.invoiceapp_braedengiasson.dao.InvoiceDetailsDao;
import com.example.invoiceapp_braedengiasson.helpers.DefaultAddresses;
import com.example.invoiceapp_braedengiasson.helpers.DefaultCustomers;
import com.example.invoiceapp_braedengiasson.helpers.DefaultInvoices;
import com.example.invoiceapp_braedengiasson.helpers.RandomNumber;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;

import java.util.List;


/**
 * Uses the Singleton model for the database.
 */
@Database(
        entities = {
                Customer.class,
                Address.class,
                Invoice.class,
                InvoiceDetails.class
        },
        version = 4,
        exportSchema = false
)
public abstract class InvoiceAppRoomDatabase extends RoomDatabase {

    // Add the dao's
    public abstract CustomerDao customerDao();
    public abstract AddressDao addressDao();
    public abstract InvoiceDao invoiceDao();
    public abstract InvoiceDetailsDao invoiceDetailsDao();

    private static InvoiceAppRoomDatabase INSTANCE;

    public static InvoiceAppRoomDatabase getDatabase(final Context context) {
        // If no instance of the database, create the instance
        if (INSTANCE == null) {
            synchronized (InvoiceAppRoomDatabase.class) {
                // Create the database now
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    InvoiceAppRoomDatabase.class, "invoice_app_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            //.fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries() // need this in order to access database on main thread
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    // This callback is called when the database has opened.
    // In this case, use PopulateDbAsync to populate the database
    // with the initial data set if the database has no entries.
    private static androidx.room.RoomDatabase.Callback sRoomDatabaseCallback =
            new androidx.room.RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Populate the database with the initial data set
    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        public final CustomerDao customerDao;
        public final AddressDao addressDao;
        public final InvoiceDao invoiceDao;
        public final InvoiceDetailsDao invoiceDetailsDao;

        /**
         * Instantiates a new instance of the {{@link PopulateDbAsync}} class.
         * @param db The room database.
         */
        PopulateDbAsync(InvoiceAppRoomDatabase db) {
            customerDao = db.customerDao();
            addressDao = db.addressDao();
            invoiceDao = db.invoiceDao();
            invoiceDetailsDao = db.invoiceDetailsDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            int numberOfCustomer = customerDao.getAny().length;

            if (numberOfCustomer < 3)
                addInitialCustomers(numberOfCustomer);

            return null;
        }

        /**
         * Adds the initial customers, addresses, invoices, invoicedetails to the database.
         */
        private void addInitialCustomers(int numberOfCustomers){
            List<Customer> customers = DefaultCustomers.generate();

            for (int i = 0; i < 3 - numberOfCustomers; i++) {
                Customer currentCustomer = customers.get(i);

                // Get the long id of the customer
                long customer2 = customerDao.insert(currentCustomer);
                // Get the int version of the id
                int customer = (int) customer2;

                long addressId = -1;

                switch (i){
                    case 0:
                        addressId = addressDao.insert(DefaultAddresses.Address1(customer));
                        break;
                    case 1:
                        addressId = addressDao.insert(DefaultAddresses.Address2(customer));
                        break;
                    case 2:
                        addressId = addressDao.insert(DefaultAddresses.Address3(customer));
                        break;
                }

                currentCustomer.setDefaultAddressId((int) addressId);
                customerDao.update(currentCustomer);

                int randomIndex = RandomNumber.getRandomNumber(3, 8);

                for (int j = 0; j < randomIndex; j++) {
                    Invoice invoice = DefaultInvoices.generate(customer);

                    // Get the long id of the customer
                    long invoice1 = invoiceDao.insert(invoice);
                    // Get the int version of the id
                    int invoiceId = (int) invoice1;

                    // Add all the invoice details
                    for (int k = 0; k < 10; k++) {
                        InvoiceDetails invoiceDetail = new InvoiceDetails(
                                invoiceId,
                                "Product name",
                                19.99,
                                3
                        );

                        invoiceDetailsDao.insert(invoiceDetail);
                    }

                    // Get the total for the invoice
                    double totalForInvoices = invoiceDao.totalOfInvoicesDetailsByInvoiceId(invoiceId);

                    // Set the total
                    invoice.setTotal(totalForInvoices);

                    invoiceDao.update(invoice);
                }
            }
        }
    }
}
