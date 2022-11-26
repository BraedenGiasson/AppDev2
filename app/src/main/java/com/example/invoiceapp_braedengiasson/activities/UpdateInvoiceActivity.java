package com.example.invoiceapp_braedengiasson.activities;

import static com.example.invoiceapp_braedengiasson.MainActivity.EXTRA_DATA_UPDATE_CUSTOMER;
import static com.example.invoiceapp_braedengiasson.MainActivity.EXTRA_DATA_UPDATE_INVOICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.databinding.ActivityUpdateInvoiceLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.CreateInvoiceDetailLayoutBinding;
import com.example.invoiceapp_braedengiasson.fragments.InvoiceDetailsFragment;
import com.example.invoiceapp_braedengiasson.fragments.InvoiceListFragment;
import com.example.invoiceapp_braedengiasson.helpers.DialogHolder;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoiceDetailsListAdapter;
import com.example.invoiceapp_braedengiasson.settings.SettingsActivity;
import com.example.invoiceapp_braedengiasson.swipe.SwipeController;
import com.example.invoiceapp_braedengiasson.swipe.SwipeControllerActions;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class UpdateInvoiceActivity extends AppCompatActivity {

    private ActivityUpdateInvoiceLayoutBinding binding;
    private InvoiceDetailsViewModel invoiceDetailsViewModel;
    private InvoiceViewModel invoiceViewModel;
    private AddressViewModel addressViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateInvoiceLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        invoiceDetailsViewModel = ViewModelProviders.of(this)
                .get(InvoiceDetailsViewModel.class);
        invoiceViewModel = ViewModelProviders.of(this)
                .get(InvoiceViewModel.class);
        addressViewModel = ViewModelProviders.of(this)
                .get(AddressViewModel.class);

        try {
            final Bundle extras = getIntent().getExtras();

            if (extras != null) {
                int invoiceId = extras.getInt(EXTRA_DATA_UPDATE_INVOICE, 0);
                int customerId = extras.getInt(EXTRA_DATA_UPDATE_CUSTOMER, 0);

                // Get all addresses associated with the customer
                LiveData<List<Address>> liveDataAddresses =
                        addressViewModel.getAddressesForCustomer(customerId);

                // Populate the spinner
                liveDataAddresses.observe(this, addresses1 ->
                        binding.updateInvoiceAddressSp.setAdapter(new ArrayAdapter<>(
                                this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                addresses1
                        )));

                binding.createInvoiceDetailFab.setOnClickListener(view13 -> {
                    // Show update customer activity
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getLayoutInflater().getContext());

                    CreateInvoiceDetailLayoutBinding layoutBinding =
                            CreateInvoiceDetailLayoutBinding.inflate(getLayoutInflater(), null, false);

                    dialogBuilder.setView(layoutBinding.getRoot());

                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                    // Update the invoice detail on clock
                    layoutBinding.createInvoiceDetailBtn.setOnClickListener(view1 -> {
                        InvoiceDetails invoiceDetail = new InvoiceDetails(
                                invoiceId,
                                layoutBinding.createInvoiceDetailProductNameEd.getText().toString(),
                                Double.parseDouble(layoutBinding.createInvoiceDetailPriceperunitEd.getText().toString()),
                                Integer.parseInt(layoutBinding.createInvoiceDetailQuantityEd.getText().toString())
                        );

                        invoiceDetailsViewModel.insert(invoiceDetail);
                        invoiceDetailsViewModel.update(invoiceDetail);

                        dialog.dismiss();
                    });

                    // Cancel the invoice detail on click
                    layoutBinding.cancelCreateInvoiceDetailBtn.setOnClickListener(view12 -> dialog.dismiss());
                });

                binding.updateInvoiceBtn.setOnClickListener(view14 -> {
                    Invoice invoice = invoiceViewModel.get(invoiceId);

                    invoice.setDeliveryAddress(binding.updateInvoiceAddressSp.getSelectedItem().toString());
                    invoice.setTotal(invoiceDetailsViewModel.getInvoiceDetailsTotalForInvoice(invoiceId));
                    invoiceViewModel.update(invoice);

                    finish();
                });

                binding.cancelUpdateInvoiceBtn.setOnClickListener(view15 -> finish());

                final InvoiceDetailsListAdapter invoiceDetailsListAdapter = new InvoiceDetailsListAdapter(
                        UpdateInvoiceActivity.this,
                        invoiceDetailsViewModel);

                // Add swiping capability
                SwipeController swipeController = addSwipeCapability(
                        invoiceDetailsListAdapter,
                        binding.fragmentInvoiceDetailsInUpdateInvoice);

                binding.fragmentInvoiceDetailsInUpdateInvoice.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                        swipeController.onDraw(c, getResources());
                    }
                });

                // Set the invoice details
                invoiceDetailsViewModel.getInvoicesDetailsForInvoice(invoiceId)
                        .observe(UpdateInvoiceActivity.this,
                                invoices -> {
                                    // Let user know to add first invoice if none
                                    if (invoices.size() == 0)
                                        Snackbar.make(
                                                UpdateInvoiceActivity.this, view,
                                                getString(R.string.add_first_address),
                                                Snackbar.LENGTH_LONG).show();
                                        // Set the invoices
                                    else
                                        invoiceDetailsListAdapter.setInvoices(invoices);
                                });

                // Set adapter for recycler view
                binding.fragmentInvoiceDetailsInUpdateInvoice.setLayoutManager(new LinearLayoutManager(UpdateInvoiceActivity.this));
                binding.fragmentInvoiceDetailsInUpdateInvoice.setAdapter(invoiceDetailsListAdapter);
            }
        }
        catch (Exception ex){
            return;
        }
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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateInvoiceActivity.this);

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
                        UpdateInvoiceActivity.this,
                        view,
                        invoicesListAdapter,
                        position,
                        invoiceDetailsViewModel
                );
            }
        }, UpdateInvoiceActivity.this, 150);

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
