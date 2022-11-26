package com.example.invoiceapp_braedengiasson.recyclerAdapter;

import static com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoicesListAdapter.EXTRA_DATA_INVOICE_DETAILS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.activities.CustomerInvoiceActivity;
import com.example.invoiceapp_braedengiasson.databinding.InvoiceDetailsItemRecyclerViewLayoutBinding;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;

import java.util.List;

public class InvoiceDetailsListAdapter extends RecyclerView.Adapter<InvoiceDetailsListAdapter.InvoiceAppViewHolder>{

    private final LayoutInflater mInflater;
    private List<InvoiceDetails> invoiceDetails;
    private static ClickListener clickListener;
    private InvoiceDetailsViewModel invoiceViewModel;

    public InvoiceDetailsListAdapter(Context context, InvoiceDetailsViewModel invoiceViewModel) {
        mInflater = LayoutInflater.from(context);
        this.invoiceViewModel = invoiceViewModel;
    }

    @Override
    public InvoiceAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InvoiceDetailsItemRecyclerViewLayoutBinding binding =
                InvoiceDetailsItemRecyclerViewLayoutBinding.inflate(mInflater, parent, false);
        return new InvoiceAppViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InvoiceAppViewHolder holder, int position) {
        if (invoiceDetails != null) {
            InvoiceDetails current = invoiceDetails.get(position);
            holder.setCustomerDetails(current, position);
        }
    }

    /**
     * Associates a list of words with this adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setInvoices(List<InvoiceDetails> invoices) {
        this.invoiceDetails = invoices;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mWords has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (invoiceDetails != null)
            return invoiceDetails.size();
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
    public InvoiceDetails getInvoiceDetailAtPosition(int position) {
        return invoiceDetails.get(position);
    }

    class InvoiceAppViewHolder extends RecyclerView.ViewHolder {
        private InvoiceDetailsItemRecyclerViewLayoutBinding binding;

        private InvoiceAppViewHolder(InvoiceDetailsItemRecyclerViewLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void setCustomerDetails(InvoiceDetails invoiceDetails, int position){
            binding.invoiceDetailProductName.setText(invoiceDetails.getProductName());
            binding.invoiceDetailPricePerUnit.setText(Double.toString(invoiceDetails.getPricePerUnit()));
            binding.invoiceDetailLineTotal.setText(Double.toString(invoiceDetails.getLineTotal()));
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        InvoiceDetailsListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
