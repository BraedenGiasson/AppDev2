package com.example.invoiceapp_braedengiasson.activities;

import static com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoicesListAdapter.EXTRA_DATA_CUSTOMER_DETAIL;
import static com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoicesListAdapter.EXTRA_DATA_INVOICE_DETAILS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.databinding.ActivityInvoiceDetailsLayoutBinding;
import com.example.invoiceapp_braedengiasson.fragments.InvoiceDetailsFragment;
import com.example.invoiceapp_braedengiasson.fragments.InvoiceListFragment;
import com.example.invoiceapp_braedengiasson.settings.SettingsActivity;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;

import java.util.List;

public class CustomerInvoiceActivity extends AppCompatActivity {

    private ActivityInvoiceDetailsLayoutBinding binding;
    private InvoiceViewModel invoiceViewModel;
    private InvoiceDetailsViewModel invoiceDetailsViewModel;
    private CustomerViewModel customerViewModel;
    private AddressViewModel addressViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInvoiceDetailsLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        try {
            customerViewModel = ViewModelProviders.of(this)
                    .get(CustomerViewModel.class);
            invoiceViewModel = ViewModelProviders.of(this)
                    .get(InvoiceViewModel.class);
            invoiceDetailsViewModel = ViewModelProviders.of(this)
                    .get(InvoiceDetailsViewModel.class);

            final Bundle extras = getIntent().getExtras();

            if (extras != null) {
                // If we are passed content, fill it in for the user to edit.
                int invoiceId = extras.getInt(EXTRA_DATA_INVOICE_DETAILS, 0);
                int customerId = extras.getInt(EXTRA_DATA_CUSTOMER_DETAIL, 0);

                Invoice invoice = invoiceViewModel.get(invoiceId);
                Customer customer = customerViewModel.getCustomerById(customerId);

                // Set the details
               setDetails(customer, invoice, invoiceId);

                setTotal(invoice.getTotal());

                InvoiceDetailsFragment invoiceListFragment = new InvoiceDetailsFragment(
                        this,
                        invoiceDetailsViewModel,
                        CustomerInvoiceActivity.this,
                        invoice);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(CustomerInvoiceActivity.this.toString())
                        .replace(R.id.fragment_invoices_details_in_invoice, invoiceListFragment)
                        .commit();
            }
        }
        catch (Exception ex){
            return;
        }
    }

    @SuppressLint("SetTextI18n")
    public void setTotal(double total){
//        binding.customerDetailsTotalInvoicesTv.setText(
//                Double.toString(
//                        invoiceDetailsViewModel.getInvoiceDetailsTotalForInvoice(invoiceId)
//                ));
        binding.customerDetailsTotalInvoicesTv.setText(Double.toString(total));
    }

    /**
     * Sets the details for the invoice details activity.
     * @param customer
     * @param invoice
     * @param invoiceId
     */
    private void setDetails(Customer customer, Invoice invoice, int invoiceId){
        binding.invoiceDetailName.setText(customer.getName());
        binding.invoiceDetailShippingAddress.setText(invoice.getDeliveryAddress());
        binding.invoiceDetailDate.setText(invoice.getDateOfIssue());
        binding.incoiceDetailInvoiceNumber.setText(Integer.toString(invoiceId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_home_settings){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
