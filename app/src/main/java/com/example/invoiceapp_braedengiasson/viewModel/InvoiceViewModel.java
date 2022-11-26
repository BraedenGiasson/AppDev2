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

package com.example.invoiceapp_braedengiasson.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_braedengiasson.repositories.InvoiceRepository;
import com.example.invoiceapp_braedengiasson.tables.Invoice;

import java.util.List;

/**
 * The WordViewModel provides the interface between the UI and the data layer of the app,
 * represented by the Repository
 */

public class InvoiceViewModel extends AndroidViewModel {

    private InvoiceRepository invoiceRepository;

    private LiveData<List<Invoice>> allInvoices;

    public InvoiceViewModel(Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        allInvoices = invoiceRepository.getAllInvoices();
    }

    public LiveData<List<Invoice>> getAllInvoices() {
        return allInvoices;
    }

    public LiveData<List<Invoice>> getInvoicesForCustomer(int id){
        return invoiceRepository.getInvoicesForCustomer(id);
    }

    public Invoice getNewlyAdded(){
        return invoiceRepository.getNewlyAdded();
    }

    public Invoice get(int id){
        return invoiceRepository.get(id);
    }

    public double totalOfInvoicesDetailsByInvoiceId(int id){
        return invoiceRepository.totalOfInvoicesDetailsByInvoiceId(id);
    }

    public double totalOfInvoicesByCustomerId(int id){
        return invoiceRepository.totalOfInvoicesByCustomerId(id);
    }

    public void insert(Invoice invoice) {
        invoiceRepository.insert(invoice);
    }

    public void deleteAll() {
        invoiceRepository.deleteAll();
    }

    public void deleteInvoice(Invoice invoice) {
        invoiceRepository.deleteWord(invoice);
    }

    public void deleteAllInvoiceDetailsForCustomerId(int customerId) {
        invoiceRepository.deleteAllInvoiceDetailsForCustomerId(customerId);
    }

    public void update(Invoice invoice) {
        invoiceRepository.update(invoice);
    }
}
