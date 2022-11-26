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

package com.example.invoiceapp_braedengiasson.recyclerAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.activities.CustomerDetailsActivity;
import com.example.invoiceapp_braedengiasson.activities.CustomerInvoiceActivity;
import com.example.invoiceapp_braedengiasson.databinding.InvoicesItemRecyclerViewLayoutBinding;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;

import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of words.
 */
public class InvoicesListAdapter extends RecyclerView.Adapter<InvoicesListAdapter.InvoiceAppViewHolder> {

    public static final String EXTRA_DATA_INVOICE_DETAILS = "extra_invoice_details";
    public static final String EXTRA_DATA_CUSTOMER_DETAIL = "extra_customer_detail";

    private final LayoutInflater mInflater;
    private List<Invoice> invoices;
    private InvoiceViewModel invoiceViewModel;
    private Customer customer;

    public InvoicesListAdapter(
            Context context,
            InvoiceViewModel invoiceViewModel,
            Customer customer) {
        mInflater = LayoutInflater.from(context);
        this.invoiceViewModel = invoiceViewModel;
        this.customer = customer;
    }

    @Override
    public InvoiceAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InvoicesItemRecyclerViewLayoutBinding binding =
                InvoicesItemRecyclerViewLayoutBinding.inflate(mInflater, parent, false);
        return new InvoiceAppViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InvoiceAppViewHolder holder, int position) {
        if (invoices != null) {
            Invoice current = invoices.get(position);
            holder.setCustomerDetails(current, position);

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mInflater.getContext(), CustomerInvoiceActivity.class);
                intent.putExtra(EXTRA_DATA_INVOICE_DETAILS, current.getId());
                intent.putExtra(EXTRA_DATA_CUSTOMER_DETAIL, customer.getId());
                mInflater.getContext().startActivity(intent);
            });
        }
    }

    /**
     * Associates a list of words with this adapter
    */
    @SuppressLint("NotifyDataSetChanged")
    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mWords has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (invoices != null)
            return invoices.size();
        else return 0;
    }

    /**
     * Gets the word at a given position.
     * This method is useful for identifying which word
     * was clicked or swiped in methods that handle user events.
     *
     * @param position The position of the word in the RecyclerView
     * @return The word at the given position
     */
    public Invoice getInvoiceAtPosition(int position) {
        return invoices.get(position);
    }

    class InvoiceAppViewHolder extends RecyclerView.ViewHolder {
        private InvoicesItemRecyclerViewLayoutBinding binding;

        private InvoiceAppViewHolder(InvoicesItemRecyclerViewLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void setCustomerDetails(Invoice invoice, int position){
            binding.invoiceNameCardTv.setText(String.format("Invoice %d", position + 1));
            binding.invoiceItemTotal.setText(Double.toString(invoice.getTotal()));
        }
    }
}
