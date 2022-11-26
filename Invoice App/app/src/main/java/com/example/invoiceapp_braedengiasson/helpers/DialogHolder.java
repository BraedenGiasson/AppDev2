package com.example.invoiceapp_braedengiasson.helpers;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.activities.CustomerDetailsActivity;
import com.example.invoiceapp_braedengiasson.activities.CustomerInvoiceActivity;
import com.example.invoiceapp_braedengiasson.databinding.ActivityCustomerDetailsLayoutBinding;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.AddressesListAdapter;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.CustomerListAdapter;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoiceDetailsListAdapter;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoicesListAdapter;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;
import com.google.android.material.snackbar.Snackbar;

public class DialogHolder {

    /**
     * Shows a dialog for deleting.
     * @param context The views context.
     * @param view The current view.
     * @param customerListAdapter The customer list adapter (customer recycler view)
     * @param position The position of the item in the list adapter.
     * @param customerViewModel The customer view model.
     */
    public static void showCustomerDeleteDialog(
        MainActivity activity,
        Context context,
        View view,
        CustomerListAdapter customerListAdapter,
        int position,
        CustomerViewModel customerViewModel,
        AddressViewModel addressViewModel,
        InvoiceViewModel invoiceViewModel,
        InvoiceDetailsViewModel invoiceDetailsViewModel
    )
    {
        // Creating the alert dialog
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.customer_delete_dialog_title)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage(String.format("Are you sure you want to delete customer '%s'?\nEverything about '%s' will be deleted.",
                        customerListAdapter.getCustomerAtPosition(position).getName(), customerListAdapter.getCustomerAtPosition(position).getName()));

        // Positive button
        deleteDialog.setPositiveButton(R.string.delete_dialog_positive, (dialogInterface, i) -> {
            Customer customer = customerListAdapter.getCustomerAtPosition(position);
            String customerName = customer.getName();

            invoiceViewModel.getInvoicesForCustomer(customer.getId()).observe(activity, invoices -> {
                for (Invoice invoice : invoices)
                    // Delete all invoice details associated with the invoice
                    invoiceDetailsViewModel.deleteAllInvoiceDetailsForInvoiceId(invoice.getId());
            });

            // Delete all invoice associated with the customer
            invoiceViewModel.deleteAllInvoiceDetailsForCustomerId(customer.getId());

            // Delete all addresses associated with the customer
            addressViewModel.deleteAllAddressForCustomer(customer.getId());

            customerViewModel.deleteCustomer(customer);

            customerListAdapter.notifyItemRemoved(position);
            customerListAdapter.notifyItemRangeChanged(position, customerListAdapter.getItemCount());

            // Delete success message
            Snackbar.make(context, view, String.format("Deleted customer '%s'.", customerName), Snackbar.LENGTH_LONG)
                    .show();

            dialogInterface.dismiss();
        });

        // Negative button
        deleteDialog.setNegativeButton(R.string.delete_dialog_negative, (dialogInterface, i) -> dialogInterface.dismiss());

        // Show dialog
        deleteDialog.create().show();
    }

    /**
     * Shows a dialog for deleting.
     * @param context The views context.
     * @param view The current view.
     * @param invoicesListAdapter The invoice list adapter (invoice recycler view)
     * @param position The position of the item in the list adapter.
     * @param invoiceViewModel The invoice view model.
     */
    public static void showInvoiceDeleteDialog(
            CustomerDetailsActivity context,
            View view,
            InvoicesListAdapter invoicesListAdapter,
            int position,
            InvoiceViewModel invoiceViewModel,
            Customer customer
    )
    {
        // Creating the alert dialog
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.invoice_delete_dialog_title)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage("Are you sure you want to delete the invoice?");

        // Positive button
        deleteDialog.setPositiveButton(R.string.delete_dialog_positive, (dialogInterface, i) -> {
            Invoice invoice = invoicesListAdapter.getInvoiceAtPosition(position);

            // Delete the trip from the repository
            invoiceViewModel.deleteInvoice(invoice);

            invoicesListAdapter.notifyItemRemoved(position);
            invoicesListAdapter.notifyItemRangeChanged(position, invoicesListAdapter.getItemCount());

            context.setDetails(customer);

            // Delete success message
            Snackbar.make(context, view, "Deleted invoice.", Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete_dialog_undo, currentView -> {
                        // Re-add the invoice
                        invoiceViewModel.insert(invoice);

                        invoicesListAdapter.notifyItemInserted(invoicesListAdapter.getItemCount());
                        invoicesListAdapter.notifyItemRangeChanged(invoicesListAdapter.getItemCount(), invoicesListAdapter.getItemCount());

                        context.setDetails(customer);

                        // Add re-add message
                        Snackbar.make(context, currentView,"Successfully re-added invoice.", Snackbar.LENGTH_LONG).show();
                    })
                    .show();

            dialogInterface.dismiss();
        });

        // Negative button
        deleteDialog.setNegativeButton(R.string.delete_dialog_negative, (dialogInterface, i) -> dialogInterface.dismiss());

        // Show dialog
        deleteDialog.create().show();
    }

    /**
     * Shows a dialog for deleting.
     * @param context The views context.
     * @param view The current view.
     * @param invoiceDetailsListAdapter The invoice list adapter (invoice recycler view)
     * @param position The position of the item in the list adapter.
     * @param invoiceDetailsViewModel The invoice view model.
     */
    public static void showInvoiceDetailDeleteDialog(
            Context context,
            View view,
            InvoiceDetailsListAdapter invoiceDetailsListAdapter,
            int position,
            InvoiceDetailsViewModel invoiceDetailsViewModel
    )
    {
        // Creating the alert dialog
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.invoice_delete_dialog_title)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage("Are you sure you want to delete the invoice detail?");

        // Positive button
        deleteDialog.setPositiveButton(R.string.delete_dialog_positive, (dialogInterface, i) -> {
            InvoiceDetails invoiceDetail = invoiceDetailsListAdapter.getInvoiceDetailAtPosition(position);

            // Delete the trip from the repository
            invoiceDetailsViewModel.deleteInvoiceDetail(invoiceDetail);

            invoiceDetailsListAdapter.notifyItemRemoved(position);
            invoiceDetailsListAdapter.notifyItemRangeChanged(position, invoiceDetailsListAdapter.getItemCount());

            // Delete success message
            Snackbar.make(context, view, "Deleted invoice detail.", Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete_dialog_undo, currentView -> {
                        // Re-add the invoice
                        invoiceDetailsViewModel.insert(invoiceDetail);

                        invoiceDetailsListAdapter.notifyItemInserted(invoiceDetailsListAdapter.getItemCount());
                        invoiceDetailsListAdapter.notifyItemRangeChanged(invoiceDetailsListAdapter.getItemCount(), invoiceDetailsListAdapter.getItemCount());

                        // Add re-add message
                        Snackbar.make(context, currentView,"Successfully re-added invoice detail.", Snackbar.LENGTH_LONG).show();
                    })
                    .show();

            dialogInterface.dismiss();
        });

        // Negative button
        deleteDialog.setNegativeButton(R.string.delete_dialog_negative, (dialogInterface, i) -> dialogInterface.dismiss());

        // Show dialog
        deleteDialog.create().show();
    }

    /**
     * Shows a dialog for deleting.
     * @param context The views context.
     * @param view The current view.
     * @param addressesListAdapter The invoice list adapter (invoice recycler view)
     * @param position The position of the item in the list adapter.
     * @param addressViewModel The invoice view model.
     */
    public static void showAddressDeleteDialog(
            Context context,
            View view,
            AddressesListAdapter addressesListAdapter,
            int position,
            AddressViewModel addressViewModel,
            Customer customer,
            CustomerViewModel customerViewModel
    )
    {
        // Creating the alert dialog
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.address_delete_dialog_title)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage("Are you sure you want to delete the address?\nIt will be permanently deleted.");

        // Positive button
        deleteDialog.setPositiveButton(R.string.delete_dialog_positive, (dialogInterface, i) -> {
            Address address = addressesListAdapter.getAddressAtPosition(position);

            // Reset the customer's default address id if deleting default address id
            if (customer.getDefaultAddressId() == address.getId())
                customerViewModel.setDefaultAddress(customer.getId(), -1);

            customerViewModel.update(customer);

            // Delete the trip from the repository
            addressViewModel.deleteAddress(address);

            addressesListAdapter.notifyItemRemoved(position);
            addressesListAdapter.notifyItemRangeChanged(position, addressesListAdapter.getItemCount());

            // Delete success message
            Snackbar.make(context, view, "Deleted address.", Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete_dialog_undo, currentView -> {
                        // Re-add the invoice
                        addressViewModel.insert(address);

                        // Reset the customer's default address id
                        if (customer.getDefaultAddressId() == address.getId())
                            customerViewModel.setDefaultAddress(customer.getId(), address.getId());

                        customerViewModel.update(customer);

                        addressesListAdapter.notifyItemInserted(addressesListAdapter.getItemCount());
                        addressesListAdapter.notifyItemRangeChanged(addressesListAdapter.getItemCount(), addressesListAdapter.getItemCount());

                        // Add re-add message
                        Snackbar.make(context, currentView,"Successfully re-added address.", Snackbar.LENGTH_LONG).show();
                    })
                    .show();

            dialogInterface.dismiss();
        });

        // Negative button
        deleteDialog.setNegativeButton(R.string.delete_dialog_negative, (dialogInterface, i) -> dialogInterface.dismiss());

        // Show dialog
        deleteDialog.create().show();
    }
}
