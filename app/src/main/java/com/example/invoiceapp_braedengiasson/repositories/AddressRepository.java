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

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.AsyncTask;

import com.example.invoiceapp_braedengiasson.dao.AddressDao;
import com.example.invoiceapp_braedengiasson.roomDatabase.InvoiceAppRoomDatabase;
import com.example.invoiceapp_braedengiasson.tables.Address;
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

public class AddressRepository {

    private AddressDao addressDao;
    private LiveData<List<Address>> allAddresses;

    public AddressRepository(Application application) {
        InvoiceAppRoomDatabase db = InvoiceAppRoomDatabase.getDatabase(application);
        addressDao = db.addressDao();
        allAddresses = addressDao.getAll();
    }

    public LiveData<List<Address>> getAllAddresses() {
        return allAddresses;
    }

    public Address getAddressByCustomerId(int id){
        return addressDao.getFromCustomerId(id);
    }

    public LiveData<List<Address>> getAddressesForCustomer(int id){
        return addressDao.getAddressesForCustomer(id);
    }

    public LiveData<List<Address>> getAddressesForCustomerNewest(int id){
        return addressDao.getAddressesForCustomerNewest(id);
    }

    public Address getCustomerDefaultAddress(int id){
        return addressDao.getCustomerDefaultAddress(id);
    }

    public Address getNewlyAdded(){
        return addressDao.getNewlyAdded();
    }

    public void insert(Address address) {
        new insertAsyncTask(addressDao).execute(address);
    }

    public void update(Address address)  {
        new updateWordAsyncTask(addressDao).execute(address);
    }

    public void deleteAll()  {
        new deleteAllWordsAsyncTask(addressDao).execute();
    }

    // Must run off main thread
    public void deleteAddress(Address address) {
        new deleteWordAsyncTask(addressDao).execute(address);
    }

    public void deleteAllAddressForCustomer(int customerId) {
        addressDao.deleteAllAddressForCustomer(customerId);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Address, Void, Void> {

        private AddressDao mAsyncTaskDao;

        insertAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Address... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all words from the database (does not delete the table).
     */
    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AddressDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(AddressDao dao) {
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
    private static class deleteWordAsyncTask extends AsyncTask<Address, Void, Void> {
        private AddressDao mAsyncTaskDao;

        deleteWordAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Address... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a word in the database.
     */
    private static class updateWordAsyncTask extends AsyncTask<Address, Void, Void> {
        private AddressDao mAsyncTaskDao;

        updateWordAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Address... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
