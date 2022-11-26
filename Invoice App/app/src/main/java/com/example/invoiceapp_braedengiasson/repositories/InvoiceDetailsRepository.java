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

import com.example.invoiceapp_braedengiasson.dao.InvoiceDetailsDao;
import com.example.invoiceapp_braedengiasson.roomDatabase.InvoiceAppRoomDatabase;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;

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

public class InvoiceDetailsRepository {

    private InvoiceDetailsDao invoiceDetailsDao;
    private LiveData<List<InvoiceDetails>> allInvoiceDetails;

    public InvoiceDetailsRepository(Application application) {
        InvoiceAppRoomDatabase db = InvoiceAppRoomDatabase.getDatabase(application);
        invoiceDetailsDao = db.invoiceDetailsDao();
        allInvoiceDetails = invoiceDetailsDao.getAll();
    }

    public LiveData<List<InvoiceDetails>> getAllInvoiceDetails() {
        return allInvoiceDetails;
    }

    public LiveData<List<InvoiceDetails>> getInvoicesDetailsForInvoice(int id){
        return invoiceDetailsDao.getInvoicesDetailsForInvoice(id);
    }

    public double getInvoiceDetailsTotalForInvoice(int id){
        return invoiceDetailsDao.getInvoiceDetailsTotalForInvoice(id);
    }

    public InvoiceDetails get(int id){
        return invoiceDetailsDao.get(id);
    }

    public void insert(InvoiceDetails word) {
        new insertAsyncTask(invoiceDetailsDao).execute(word);
    }

    public void update(InvoiceDetails word)  {
        new updateWordAsyncTask(invoiceDetailsDao).execute(word);
    }

    public void deleteAll()  {
        new deleteAllWordsAsyncTask(invoiceDetailsDao).execute();
    }

    // Must run off main thread
    public void deleteInvoiceDetail(InvoiceDetails word) {
        new deleteWordAsyncTask(invoiceDetailsDao).execute(word);
    }

    public void deleteAllInvoiceDetailsForInvoiceId(int invoiceId) {
        invoiceDetailsDao.deleteAllInvoicesDetailsForInvoice(invoiceId);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<InvoiceDetails, Void, Void> {

        private InvoiceDetailsDao mAsyncTaskDao;

        insertAsyncTask(InvoiceDetailsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InvoiceDetails... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all words from the database (does not delete the table).
     */
    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private InvoiceDetailsDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(InvoiceDetailsDao dao) {
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
    private static class deleteWordAsyncTask extends AsyncTask<InvoiceDetails, Void, Void> {
        private InvoiceDetailsDao mAsyncTaskDao;

        deleteWordAsyncTask(InvoiceDetailsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InvoiceDetails... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a word in the database.
     */
    private static class updateWordAsyncTask extends AsyncTask<InvoiceDetails, Void, Void> {
        private InvoiceDetailsDao mAsyncTaskDao;

        updateWordAsyncTask(InvoiceDetailsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InvoiceDetails... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
