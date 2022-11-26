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
import android.os.AsyncTask;

import com.example.invoiceapp_braedengiasson.dao.InvoiceDao;
import com.example.invoiceapp_braedengiasson.roomDatabase.InvoiceAppRoomDatabase;
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

public class InvoiceRepository {

    private InvoiceDao invoiceDao;
    private LiveData<List<Invoice>> allInvoices;
    private static Long insertedInvoice;

    public InvoiceRepository(Application application) {
        InvoiceAppRoomDatabase db = InvoiceAppRoomDatabase.getDatabase(application);
        invoiceDao = db.invoiceDao();
        allInvoices = invoiceDao.getAll();
        insertedInvoice = null;
    }

    public LiveData<List<Invoice>> getAllInvoices() {
        return allInvoices;
    }

    public Invoice get(int id){
        return invoiceDao.get(id);
    }

    public LiveData<List<Invoice>> getInvoicesForCustomer(int id){
        return invoiceDao.getInvoicesForCustomer(id);
    }

    public double totalOfInvoicesDetailsByInvoiceId(int id){
        return invoiceDao.totalOfInvoicesDetailsByInvoiceId(id);
    }

    public double totalOfInvoicesByCustomerId(int id){
        return Math.floor(invoiceDao.totalOfInvoicesByCustomerId(id) * 100) / 100;
    }

    public Invoice getNewlyAdded(){
        return invoiceDao.getNewlyAdded();
    }

    public void insert(Invoice invoice) {
       new insertAsyncTask(invoiceDao).execute(invoice);
    }

    public void update(Invoice invoice)  {
        new updateWordAsyncTask(invoiceDao).execute(invoice);
    }

    public void deleteAll()  {
        new deleteAllWordsAsyncTask(invoiceDao).execute();
    }

    // Must run off main thread
    public void deleteWord(Invoice invoice) {
        new deleteWordAsyncTask(invoiceDao).execute(invoice);
    }

    public void deleteAllInvoiceDetailsForCustomerId(int customerId) {
        invoiceDao.deleteAllInvoicesForCustomer(customerId);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Invoice, Void, Void> {

        private InvoiceDao mAsyncTaskDao;

        insertAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Invoice... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all words from the database (does not delete the table).
     */
    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private InvoiceDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(InvoiceDao dao) {
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
    private static class deleteWordAsyncTask extends AsyncTask<Invoice, Void, Void> {
        private InvoiceDao mAsyncTaskDao;

        deleteWordAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Invoice... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a word in the database.
     */
    private static class updateWordAsyncTask extends AsyncTask<Invoice, Void, Void> {
        private InvoiceDao mAsyncTaskDao;

        updateWordAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Invoice... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
