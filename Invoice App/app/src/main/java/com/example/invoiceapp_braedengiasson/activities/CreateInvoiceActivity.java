package com.example.invoiceapp_braedengiasson.activities;

import static com.example.invoiceapp_braedengiasson.activities.CustomerDetailsActivity.EXTRA_DATA_INVOICE_FOR_CREATING;
import static com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoicesListAdapter.EXTRA_DATA_INVOICE_DETAILS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.databinding.ActivityCreateNewInvoiceLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.CreateInvoiceDetailLayoutBinding;
import com.example.invoiceapp_braedengiasson.fragments.InvoiceDetailsFragment;
import com.example.invoiceapp_braedengiasson.helpers.DialogHolder;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.AddressesListAdapter;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoiceDetailsListAdapter;
import com.example.invoiceapp_braedengiasson.settings.SettingsActivity;
import com.example.invoiceapp_braedengiasson.swipe.SwipeController;
import com.example.invoiceapp_braedengiasson.swipe.SwipeControllerActions;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreateInvoiceActivity extends AppCompatActivity {

    private ActivityCreateNewInvoiceLayoutBinding binding;
    private InvoiceDetailsViewModel invoiceDetailsViewModel;
    private InvoiceViewModel invoiceViewModel;
    private AddressViewModel addressViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateNewInvoiceLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        invoiceDetailsViewModel = ViewModelProviders.of(this)
                .get(InvoiceDetailsViewModel.class);
        invoiceViewModel = ViewModelProviders.of(this)
                .get(InvoiceViewModel.class);
        addressViewModel = ViewModelProviders.of(this)
                .get(AddressViewModel.class);

        try {
            int customerId = getExtras();

            // Get all addresses associated with the customer
            LiveData<List<Address>> liveDataAddresses =
                    addressViewModel.getAddressesForCustomer(customerId);

            // Populate the spinner
            liveDataAddresses.observe(this, addresses1 ->
                    binding.createInvoiceAddressSp.setAdapter(new ArrayAdapter<>(
                            this,
                             androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                            addresses1
                    )));

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendarInstance = Calendar.getInstance();
            String dateOfIssue = sdf.format(calendarInstance.getTime());

            // Create the temporary invoice
            Invoice createInvoice = new Invoice(
                    customerId,
                    "",
                    0,
                    dateOfIssue);

            invoiceViewModel.insert(createInvoice);
            Invoice invoice = invoiceViewModel.getNewlyAdded();

            binding.createInvoiceDetailFab.setOnClickListener(view13 -> {
                // Show update customer activity
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getLayoutInflater().getContext());

                CreateInvoiceDetailLayoutBinding layoutBinding =
                        CreateInvoiceDetailLayoutBinding.inflate(getLayoutInflater(), null, false);

                // Set the details of the title and button
                layoutBinding.createInvoiceDetailTitleTv.setText(getString(R.string.customer_invoice_detail_title_text));
                layoutBinding.createInvoiceDetailBtn.setText(getString(R.string.add_text));

                dialogBuilder.setView(layoutBinding.getRoot());

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                // Update the invoice detail on clock
                layoutBinding.createInvoiceDetailBtn.setOnClickListener(view1 -> {
                    if (validateInputFields(layoutBinding)){
                        InvoiceDetails invoiceDetail = new InvoiceDetails(
                                invoice.getId(),
                                layoutBinding.createInvoiceDetailProductNameEd.getText().toString(),
                                Double.parseDouble(layoutBinding.createInvoiceDetailPriceperunitEd.getText().toString()),
                                Integer.parseInt(layoutBinding.createInvoiceDetailQuantityEd.getText().toString())
                        );

                        invoiceDetailsViewModel.insert(invoiceDetail);
                        invoiceDetailsViewModel.update(invoiceDetail);

                        dialog.dismiss();
                    }
                });

                // Cancel the invoice detail on click
                layoutBinding.cancelCreateInvoiceDetailBtn.setOnClickListener(view12 -> dialog.dismiss());

                binding.createNewInvoiceBtn.setOnClickListener(view14 -> {
                    createInvoice.setDeliveryAddress(binding.createInvoiceAddressSp.getSelectedItem().toString());
                    createInvoice.setTotal(invoiceDetailsViewModel.getInvoiceDetailsTotalForInvoice(createInvoice.getId()));
                    invoiceViewModel.update(createInvoice);

                    finish();
                });
            });

            // Remove the created invoice and finish the activity
            binding.cancelNewInvoiceBtn.setOnClickListener(view1 -> {
                invoiceViewModel.deleteInvoice(invoice);
                finish();
            });

            // Create recycler view for invoice details
            final InvoiceDetailsListAdapter invoiceDetailsListAdapter = new InvoiceDetailsListAdapter(
                    CreateInvoiceActivity.this,
                    invoiceDetailsViewModel);

            // Add swiping capability for the invoice details
            SwipeController swipeController = addSwipeCapability(
                    invoiceDetailsListAdapter,
                    binding.fragmentInvoiceDetailsInUpdateInvoice);

            binding.fragmentInvoiceDetailsInUpdateInvoice.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c, getResources());
                }
            });

            // Set the invoices
            invoiceDetailsViewModel.getInvoicesDetailsForInvoice(invoice.getId())
                    .observe(CreateInvoiceActivity.this,
                    invoices -> {
                        // Let user know to add first invoice if none
                        if (invoices.size() == 0)
                            Snackbar.make(
                                    CreateInvoiceActivity.this, view,
                                    getString(R.string.add_first_address),
                                    Snackbar.LENGTH_LONG).show();
                            // Set the invoices
                        else
                            invoiceDetailsListAdapter.setInvoices(invoices);
                    });

            // Set adapter for recycler view
            binding.fragmentInvoiceDetailsInUpdateInvoice.setLayoutManager(new LinearLayoutManager(CreateInvoiceActivity.this));
            binding.fragmentInvoiceDetailsInUpdateInvoice.setAdapter(invoiceDetailsListAdapter);
        }
        catch (Exception ex){
            return;
        }
    }

    private int getExtras(){
        try {
            final Bundle extras = getIntent().getExtras();

            if (extras == null) return -1;

            // If we are passed content, fill it in for the user to edit.
            return extras.getInt(EXTRA_DATA_INVOICE_FOR_CREATING, 0);
        }
        catch (Exception ex){
            return -1;
        }
    }

    /**
     * Validates the input fields to make sure they're all filled in.
     * @return True if all the input fields are filled in; otherwise false.
     */
    public boolean validateInputFields(CreateInvoiceDetailLayoutBinding binding){
        boolean allFieldsFilledIn = true;

        try {
            // Validate the product name field is filled in
            if (binding.createInvoiceDetailProductNameEd.getText().toString().equals("")){
                binding.createInvoiceDetailProductNameEd.setError(getString(R.string.product_name_isRequired));
                allFieldsFilledIn = false;
            }
            // Validate the price per unit field is filled in
            if (binding.createInvoiceDetailPriceperunitEd.getText().toString().equals("")){
                binding.createInvoiceDetailPriceperunitEd.setError(getString(R.string.ppu_isRequired));
                if (allFieldsFilledIn) allFieldsFilledIn = false;
            }
            // Validate the quantity field is filled in
            if (binding.createInvoiceDetailQuantityEd.getText().toString().equals("")){
                binding.createInvoiceDetailQuantityEd.setError(getString(R.string.quantity_isRequired));
                if (allFieldsFilledIn) allFieldsFilledIn = false;
            }
        }
        catch (Exception ex){
            return false;
        }

        return allFieldsFilledIn;
    }

    /**
     * Adds the swiping capability for the specified recycler view.
     * @param invoicesListAdapter The invoices list adapter.
     * @param invoicesRecyclerView The invoices recycler view.
     * @return The created swiping capability.
     */
    private SwipeController addSwipeCapability(
            InvoiceDetailsListAdapter invoicesListAdapter,
            RecyclerView invoicesRecyclerView)
    {
        // Add swiping capability to list of customer
        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLeftClicked(int position, Context context, View view) {
                InvoiceDetails invoiceDetail = invoicesListAdapter.getInvoiceDetailAtPosition(position);

                // Show update customer activity
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateInvoiceActivity.this);

                CreateInvoiceDetailLayoutBinding layoutBinding =
                        CreateInvoiceDetailLayoutBinding.inflate(getLayoutInflater(), null, false);

                // Set the details of the title and button
                layoutBinding.createInvoiceDetailTitleTv.setText(getString(R.string.customer_invoice_detail_title__update_text));
                layoutBinding.createInvoiceDetailBtn.setText(getString(R.string.update_text));

                // Set the text field inputs to the invoice details
                layoutBinding.createInvoiceDetailProductNameEd.setText(invoiceDetail.getProductName());
                layoutBinding.createInvoiceDetailPriceperunitEd.setText(Double.toString(invoiceDetail.getPricePerUnit()));
                layoutBinding.createInvoiceDetailQuantityEd.setText(Integer.toString(invoiceDetail.getQuantity()));

                dialogBuilder.setView(layoutBinding.getRoot());

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                // Update the invoice detail on clock
                layoutBinding.createInvoiceDetailBtn.setOnClickListener(view1 -> {
                    invoiceDetail.setProductName(layoutBinding.createInvoiceDetailProductNameEd.getText().toString());

                    // Get the price per unit and quantity of the invoice
                    double newPricePerUnit = Double.parseDouble(layoutBinding.createInvoiceDetailPriceperunitEd.getText().toString());
                    int newQuantity = Integer.parseInt(layoutBinding.createInvoiceDetailQuantityEd.getText().toString());

                    // Set the new details of the invoice
                    invoiceDetail.setPricePerUnit(newPricePerUnit);
                    invoiceDetail.setQuantity(newQuantity);
                    invoiceDetail.setLineTotal(newPricePerUnit * newQuantity);

                    invoiceDetailsViewModel.update(invoiceDetail);

                    dialog.dismiss();
                });

                // Cancel the invoice detail on click
                layoutBinding.cancelCreateInvoiceDetailBtn.setOnClickListener(view12 -> dialog.dismiss());
            }

            @Override
            public void onRightClicked(int position, Context context, View view) {
                // Show delete dialog to make sure delete customer
                DialogHolder.showInvoiceDetailDeleteDialog(
                        CreateInvoiceActivity.this,
                        view,
                        invoicesListAdapter,
                        position,
                        invoiceDetailsViewModel
                );
            }
        }, CreateInvoiceActivity.this, 150);

        // Add swiping capability to recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(invoicesRecyclerView);

        return swipeController;
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
