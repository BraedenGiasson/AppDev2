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
//import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.activities.CustomerDetailsActivity;
import com.example.invoiceapp_braedengiasson.databinding.CustomerItemRecyclerViewLayoutBinding;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;

import java.io.Serializable;
import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of words.
 */
public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.InvoiceAppViewHolder> {

    public static final String EXTRA_DATA_CUSTOMER_DETAILS = "extra_customer_details";

    private final LayoutInflater mInflater;
    private List<Customer> customers;
	private static ClickListener clickListener;
    private InvoiceViewModel invoiceViewModel;
    private MainActivity mainActivity;

    public CustomerListAdapter(Context context, InvoiceViewModel invoiceViewModel, MainActivity mainActivity) {
        mInflater = LayoutInflater.from(context);
        this.invoiceViewModel = invoiceViewModel;
        this.mainActivity = mainActivity;
    }

    @Override
    public InvoiceAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CustomerItemRecyclerViewLayoutBinding binding =
                CustomerItemRecyclerViewLayoutBinding.inflate(mInflater, parent, false);
        return new InvoiceAppViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InvoiceAppViewHolder holder, int position) {
        if (customers != null) {
            Customer current = customers.get(position);
            holder.setCustomerDetails(current);

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mInflater.getContext(), CustomerDetailsActivity.class);
                intent.putExtra(EXTRA_DATA_CUSTOMER_DETAILS, current.getId());
                mInflater.getContext().startActivity(intent);
            });
        }
    }

    /**
     * Associates a list of words with this adapter
    */
    @SuppressLint("NotifyDataSetChanged")
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mWords has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (customers != null)
            return customers.size();
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
    public Customer getCustomerAtPosition(int position) {
        return customers.get(position);
    }

    class InvoiceAppViewHolder extends RecyclerView.ViewHolder {
        private CustomerItemRecyclerViewLayoutBinding binding;

        private InvoiceAppViewHolder(CustomerItemRecyclerViewLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void setCustomerDetails(Customer customer){
            binding.customerNameCardTv.setText(customer.getName());
            invoiceViewModel.getInvoicesForCustomer(customer.getId()).observe(mainActivity,
                    invoices -> binding.invoiceNumberCardTv.setText(Integer.toString(invoices.size())));
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CustomerListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
