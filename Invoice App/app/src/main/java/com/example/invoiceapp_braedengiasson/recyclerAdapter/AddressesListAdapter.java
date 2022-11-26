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

import com.example.invoiceapp_braedengiasson.activities.CustomerInvoiceActivity;
import com.example.invoiceapp_braedengiasson.databinding.AddressesItemRecyclerViewLayoutBinding;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;

import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of words.
 */
public class AddressesListAdapter extends RecyclerView.Adapter<AddressesListAdapter.InvoiceAppViewHolder> {

    public static final String EXTRA_DATA_INVOICE_DETAILS = "extra_invoice_details";

    private final LayoutInflater mInflater;
    private List<Address> addresses;
	private static ClickListener clickListener;
    private AddressViewModel addressViewModel;

    public AddressesListAdapter(Context context, AddressViewModel addressViewModel) {
        mInflater = LayoutInflater.from(context);
        this.addressViewModel = addressViewModel;
    }

    @Override
    public InvoiceAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressesItemRecyclerViewLayoutBinding binding =
                AddressesItemRecyclerViewLayoutBinding.inflate(mInflater, parent, false);
        return new InvoiceAppViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InvoiceAppViewHolder holder, int position) {
        if (addresses != null) {
            Address current = addresses.get(position);
            holder.setCustomerDetails(current);
        }
    }

    /**
     * Associates a list of words with this adapter
    */
    @SuppressLint("NotifyDataSetChanged")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mWords has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (addresses != null)
            return addresses.size();
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
    public Address getAddressAtPosition(int position) {
        return addresses.get(position);
    }

    class InvoiceAppViewHolder extends RecyclerView.ViewHolder {
        private AddressesItemRecyclerViewLayoutBinding binding;

        private InvoiceAppViewHolder(AddressesItemRecyclerViewLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void setCustomerDetails(Address address){
            binding.addressNameCardTv.setText(address.toString());
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AddressesListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
