/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.invoiceapp_braedengiasson.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_braedengiasson.dao.CustomerDao;
import com.example.invoiceapp_braedengiasson.roomDatabase.InvoiceAppRoomDatabase;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.tables.Invoice;

import java.util.List;

/**
 * This class holds the implementation code for the methods that interact with the database.
 * Using a repository allows us to group the implementation methods together,
 * and allows the WordViewModel to be a clean interface between the rest of the app
 * and the database.
 *
 * For insert, update and delete, and longer-running queries,
 * you must run the database interaction methods in the background.
 *
 * Typically, all you need to do to implement a database method
 * is to call it on the data access object (DAO), in the background if applicable.
 */

public class CustomerRepository {

    private CustomerDao customerDao;
    private LiveData<List<Customer>> allCustomers;

    public CustomerRepository(Application application) {
        InvoiceAppRoomDatabase db = InvoiceAppRoomDatabase.getDatabase(application);
        customerDao = db.customerDao();
        allCustomers = customerDao.getAll();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public Customer getNewlyAdded(){
        return customerDao.getNewlyAdded();
    }

    public int getDefaultAddress(int customerId){
        return customerDao.get(customerId).getId();
    }

    public void setDefaultAddress(int customerId, @Nullable int addressId){
        customerDao.get(customerId).setDefaultAddressId(addressId);
    }

    public Customer getCustomerById(int id){
        return customerDao.get(id);
    }

    public void insert(Customer customer) {
        new insertAsyncTask(customerDao).execute(customer);
    }

    public void update(Customer customer)  {
        new updateWordAsyncTask(customerDao).execute(customer);
    }

    public void deleteAll()  {
        new deleteAllWordsAsyncTask(customerDao).execute();
    }

    // Must run off main thread
    public void deleteWord(Customer customer) {
        new deleteWordAsyncTask(customerDao).execute(customer);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Customer, Void, Customer> {

        private CustomerDao mAsyncTaskDao;

        insertAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Customer doInBackground(final Customer... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all words from the database (does not delete the table).
     */
    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private CustomerDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single word from the database.
     */
    private static class deleteWordAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao mAsyncTaskDao;

        deleteWordAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Customer... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a word in the database.
     */
    private static class updateWordAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao mAsyncTaskDao;

        updateWordAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Customer... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
