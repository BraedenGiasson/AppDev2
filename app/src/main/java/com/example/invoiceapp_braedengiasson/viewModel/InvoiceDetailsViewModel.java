package com.example.invoiceapp_braedengiasson.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_braedengiasson.repositories.InvoiceDetailsRepository;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;

import java.util.List;

public class InvoiceDetailsViewModel extends AndroidViewModel {
    private InvoiceDetailsRepository invoiceRepository;

    private LiveData<List<InvoiceDetails>> allInvoicesDetails;

    public InvoiceDetailsViewModel(Application application) {
        super(application);
        invoiceRepository = new InvoiceDetailsRepository(application);
        allInvoicesDetails = invoiceRepository.getAllInvoiceDetails();
    }

    public LiveData<List<InvoiceDetails>> getAllInvoiceDetails() {
        return allInvoicesDetails;
    }

    public LiveData<List<InvoiceDetails>> getInvoicesDetailsForInvoice(int id){
        return invoiceRepository.getInvoicesDetailsForInvoice(id);
    }

    public double getInvoiceDetailsTotalForInvoice(int id){
        return invoiceRepository.getInvoiceDetailsTotalForInvoice(id);
    }

    public InvoiceDetails get(int id){
        return invoiceRepository.get(id);
    }

    public void insert(InvoiceDetails invoiceDetails) {
        invoiceRepository.insert(invoiceDetails);
    }

    public void deleteAll() {
        invoiceRepository.deleteAll();
    }

    public void deleteInvoiceDetail(InvoiceDetails invoiceDetails) {
        invoiceRepository.deleteInvoiceDetail(invoiceDetails);
    }

    public void deleteAllInvoiceDetailsForInvoiceId(int invoiceId) {
        invoiceRepository.deleteAllInvoiceDetailsForInvoiceId(invoiceId);
    }

    public void update(InvoiceDetails invoiceDetails) {
        invoiceRepository.update(invoiceDetails);
    }
}
