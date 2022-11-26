package com.example.invoiceapp_braedengiasson.activities;

import static com.example.invoiceapp_braedengiasson.recyclerAdapter.CustomerListAdapter.EXTRA_DATA_CUSTOMER_DETAILS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.databinding.ActivityCustomerDetailsLayoutBinding;
import com.example.invoiceapp_braedengiasson.fragments.InvoiceListFragment;
import com.example.invoiceapp_braedengiasson.settings.SettingsActivity;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;

public class CustomerDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_INVOICE_FOR_CREATING= "extra_invoice_for_creating";

    private ActivityCustomerDetailsLayoutBinding binding;
    private CustomerViewModel customerViewModel;
    private AddressViewModel addressViewModel;
    private InvoiceViewModel invoiceViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerDetailsLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        try {
            customerViewModel = ViewModelProviders.of(this)
                    .get(CustomerViewModel.class);
            addressViewModel = ViewModelProviders.of(this)
                    .get(AddressViewModel.class);
            invoiceViewModel = ViewModelProviders.of(this)
                    .get(InvoiceViewModel.class);

            Customer customer = getExtras();

            setDetails(customer);

            // Set the fragment for the frame layout
            InvoiceListFragment invoiceListFragment = new InvoiceListFragment(
                    this,
                    invoiceViewModel,
                    CustomerDetailsActivity.this,
                    customer,
                    binding);

            // Replace the frame layout
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack(CustomerDetailsActivity.this.toString())
                    .replace(R.id.fragment_invoices_in_customer_details, invoiceListFragment)
                    .commit();

            binding.createCustomerInvoiceFab.setOnClickListener(view1 -> {
                Intent intent = new Intent(CustomerDetailsActivity.this, CreateInvoiceActivity.class);
                intent.putExtra(EXTRA_DATA_INVOICE_FOR_CREATING, customer.getId());
                startActivity(intent);
            });
        }
        catch (Exception ex){
            return;
        }
    }

    private Customer getExtras(){
        try {
            final Bundle extras = getIntent().getExtras();

            if (extras == null) return null;

            // If we are passed content, fill it in for the user to edit.
            int customerId = extras.getInt(EXTRA_DATA_CUSTOMER_DETAILS, 0);

            Customer customer = customerViewModel.getCustomerById(customerId);

            setDetails(customer);

            return customer;
        }
        catch (Exception ex){
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    public void setDetails(Customer customer){
        try {
            // Set the welcome
            binding.customerDetailsWelcomeTv.setText(
                    String.format("Welcome back, %s!", customer.getName())
            );

            // Get the address associated with the customer
            Address customerAddress = addressViewModel.getAddressByCustomerId(customer.getId());

            // Set the address text
            binding.customerDetailsAddressTv.setText(customerAddress.toString());

            // Get the total amount for invoices for the customer
            double total = invoiceViewModel.totalOfInvoicesByCustomerId(customer.getId());

            // Set the invoice total for the customer
            binding.customerDetailsTotalInvoicesTv.setText(Double.toString(total));
        }
        catch (Exception ex){
            return;
        }
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
