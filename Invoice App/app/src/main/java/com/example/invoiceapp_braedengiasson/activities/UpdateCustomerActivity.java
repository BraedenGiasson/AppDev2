package com.example.invoiceapp_braedengiasson.activities;

import static com.example.invoiceapp_braedengiasson.MainActivity.EXTRA_DATA_UPDATE_CUSTOMER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
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
import com.example.invoiceapp_braedengiasson.databinding.ActivityUpdateCustomerLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.CreateInvoiceDetailLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.ModifyAddressesLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.AddAddressLayoutBinding;
import com.example.invoiceapp_braedengiasson.fragments.InvoiceListFragment;
import com.example.invoiceapp_braedengiasson.fragments.ModifyAddressesFragment;
import com.example.invoiceapp_braedengiasson.helpers.DialogHolder;
import com.example.invoiceapp_braedengiasson.interfaces.IValidateInput;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.AddressesListAdapter;
import com.example.invoiceapp_braedengiasson.settings.SettingsActivity;
import com.example.invoiceapp_braedengiasson.swipe.SwipeController;
import com.example.invoiceapp_braedengiasson.swipe.SwipeControllerActions;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class UpdateCustomerActivity
        extends AppCompatActivity
{
    public static String EXTRA_REPLY = "";
    public static String EXTRA_REPLY_ID = "";

    private ActivityUpdateCustomerLayoutBinding binding;
    private CustomerViewModel customerViewModel;
    private AddressViewModel addressViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateCustomerLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        customerViewModel = ViewModelProviders.of(this)
                .get(CustomerViewModel.class);
        addressViewModel = ViewModelProviders.of(this)
                .get(AddressViewModel.class);

        Customer customer = getExtras();

        LiveData<List<Address>> addressesList = addressViewModel.getAddressesForCustomer(customer.getId());

        Address customerDefaultAddress =
                addressViewModel.getCustomerDefaultAddress(customer.getDefaultAddressId());

        addressesList.observe(UpdateCustomerActivity.this, addresses1 -> {
            if (addresses1.size() == 0)
                Snackbar.make(
                        UpdateCustomerActivity.this, view,
                        getString(R.string.add_first_address_update),
                        Snackbar.LENGTH_LONG).show();

            // Set spinner adapter to customer addresses
            binding.updateCustomerAddressesSp.setAdapter(new ArrayAdapter<>(
                    this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    addresses1
            ));

            if (customer.getDefaultAddressId() != -1 && customer.getDefaultAddressId() != 0){
//                Address customerDefaultAddress =
//                        addressViewModel.getCustomerDefaultAddress(customer.getDefaultAddressId());

                for (int i = 0; i < addresses1.size(); i++)
                    if (addresses1.get(i).getId() == customerDefaultAddress.getId()) {
                        binding.updateCustomerAddressesSp.setSelection(i);
                        binding.updateSetDefaultAddressCb.setChecked(true);
                    }
            }
            else
                Collections.reverse(addresses1);
        });

        if (customerDefaultAddress != null)
            checkCheckboxOnSpinnerItemChange(customerDefaultAddress);

        binding.updateCustomerBtn.setOnClickListener(view1 -> {
//            if (validateInputFields())
                updateCustomerDetails(view1, customer, addressesList);
        });

        binding.updateAddNewAddressBtn.setOnClickListener(view13 -> {
            // Show update customer activity
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateCustomerActivity.this);

            AddAddressLayoutBinding layoutBinding =
                    AddAddressLayoutBinding.inflate(getLayoutInflater(), null, false);

            layoutBinding.updateAddressTitleEd.setText(getString(R.string.update_add_address_title));
            layoutBinding.updateAddressBtn.setText(getString(R.string.add_text));

            dialogBuilder.setView(layoutBinding.getRoot());

            AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            // Update the invoice detail on clock
            layoutBinding.updateAddressBtn.setOnClickListener(view1 -> {
                if (validateInputFieldsNewAddress(layoutBinding)){
                    // Create the new address
                    Address newAddress = new Address(
                            customer.getId(),
                            layoutBinding.updateAddressStreetEd.getText().toString(),
                            layoutBinding.updateAddressCityEd.getText().toString(),
                            layoutBinding.updateAddressProvinceEd.getText().toString(),
                            layoutBinding.updateAddressCountryEd.getText().toString()
                    );

                    addressViewModel.insert(newAddress);

                    dialog.dismiss();

                    addressViewModel.getAddressesForCustomer(customer.getId())
                            .observe(this, addresses1 -> {

                                // Set spinner adapter to customer addresses
                                binding.updateCustomerAddressesSp.setAdapter(new ArrayAdapter<>(
                                        this,
                                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                        addresses1
                                ));
                            });

                    // Show success
                    Snackbar.make(
                            UpdateCustomerActivity.this, view,
                            "Added address",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            // Cancel the invoice detail on click
            layoutBinding.cancelUpdateAddressBtn.setOnClickListener(view12 -> dialog.dismiss());
        });

        binding.cancelUpdateCustomerBtn.setOnClickListener(view1 -> {
            // Creating the alert dialog
            AlertDialog.Builder cancelDialog = new AlertDialog.Builder(UpdateCustomerActivity.this)
                    .setTitle(R.string.cancel_text)
                    .setIcon(R.drawable.ic_baseline_warning_ywellow_24)
                    .setMessage("Are you sure you want to cancel updating the customer?");

            // Positive button
            cancelDialog.setPositiveButton(R.string.delete_dialog_positive, (dialogInterface, i) -> finish());

            // Negative button
            cancelDialog.setNegativeButton(R.string.delete_dialog_negative, (dialogInterface, i) -> dialogInterface.dismiss());

            // Show dialog
            cancelDialog.create().show();
        });

        addressViewModel.getAddressesForCustomer(customer.getId()).observe(this, addresses -> {
            // If no addresses, disable button
            if (addresses.size() == 0) {
                binding.updateCustomerEditAddressesBtn.setEnabled(false);
                binding.updateCustomerEditAddressesBtn.setTooltipText("Update address to enable");
            }
        });

        binding.updateCustomerEditAddressesBtn.setOnClickListener(v -> {
            // Show update customer activity
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateCustomerActivity.this);

            ModifyAddressesLayoutBinding layoutBinding =
                    ModifyAddressesLayoutBinding.inflate(getLayoutInflater(), null, false);

            final AddressesListAdapter addressesListAdapter = new AddressesListAdapter(
                    UpdateCustomerActivity.this,
                    addressViewModel);

            SwipeController swipeController = addSwipeCapability(
                    addressesListAdapter,
                    layoutBinding.fragmentAddressesInModifyAddresses,
                    customer);

            layoutBinding.fragmentAddressesInModifyAddresses.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c, getResources());
                }
            });

            addressViewModel.getAddressesForCustomer(customer.getId()).observe(UpdateCustomerActivity.this,
                    invoices -> {
                        // Let user know to add first invoice if none
                        if (invoices.size() == 0)
                            Snackbar.make(
                                    UpdateCustomerActivity.this, view,
                                    getString(R.string.add_first_address),
                                    Snackbar.LENGTH_LONG).show();
                            // Set the invoices
                        else
                            addressesListAdapter.setAddresses(invoices);
                    });

            // Set adapter for recycler view
            layoutBinding.fragmentAddressesInModifyAddresses.setLayoutManager(new LinearLayoutManager(UpdateCustomerActivity.this));
            layoutBinding.fragmentAddressesInModifyAddresses.setAdapter(addressesListAdapter);

            dialogBuilder.setView(layoutBinding.getRoot());

            AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            // Update the invoice detail on click
            layoutBinding.doneAddressModificationBtn.setOnClickListener(view1 -> {
                dialog.dismiss();
            });

            // Cancel the invoice detail on click
            layoutBinding.cancelAddressModificationBtn.setOnClickListener(view12 -> dialog.dismiss());
        });
    }

    private Customer getExtras(){
        final Bundle extras = getIntent().getExtras();

        if (extras == null) return null;

        // If we are passed content, fill it in for the user to edit.
        int customerId = extras.getInt(EXTRA_DATA_UPDATE_CUSTOMER, 0);

        Customer customer = customerViewModel.getCustomerById(customerId);

        setCustomerDetails(customer);

        return customer;
    }

    private void setCustomerDetails(Customer customer){
        if (customer == null) return;

        String[] words = customer.getName().split(" ");

        binding.updateCustomerFirstNameEd.setText(words[0]);
        binding.updateCustomerLastNameEd.setText(words[1]);
    }

    private void updateCustomerDetails(View view, Customer customer, LiveData<List<Address>> addressesList){
        if (customer == null) return;

        customer.setName(
                String.format("%s %s", binding.updateCustomerFirstNameEd.getText().toString(),
                        binding.updateCustomerLastNameEd.getText().toString())
        );

        if (binding.updateSetDefaultAddressCb.isChecked()){
           Address address = (Address) binding.updateCustomerAddressesSp.getSelectedItem();

            customer.setDefaultAddressId(address.getId());
            customerViewModel.update(customer);
//            addressViewModel.update(address);
        }

//        Address address = addressViewModel.getNewlyAdded();
//        customer.setDefaultAddressId(address.getId());
//        customerViewModel.update(customer);
//
//        addressesList.observe(this, addresses1 -> {
//            if (customer.getDefaultAddressId() != -1){
//                Address customerDefaultAddress =
//                        addresses1.get(customerViewModel.getDefaultAddress(customer.getId()));
//
//                addresses1.remove(customerDefaultAddress);
//                addresses1.add(0, customerDefaultAddress);
//            }
//
//            // Set spinner adapter to customer addresses
//            binding.updateCustomerAddressesSp.setAdapter(new ArrayAdapter<>(
//                    this,
//                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
//                    addresses1
//            ));
//        });

//        Address customerAddress = addressViewModel.getAddressByCustomerId(customer.getId());
//
//        addressViewModel.getAddressesForCustomer(customer.getId()).observe(UpdateCustomerActivity.this, addresses -> {
//            // Set default address if there are no addresses
//            if (addresses.size() == 0)
//                customerViewModel.setDefaultAddress(customer.getId(), customerAddress.getId());
//        });

//        addressViewModel.update(customerAddress);
        customerViewModel.update(customer);

        finish();
    }

    /**
     * Validates the input fields to make sure they're all filled in.
     * @return True if all the input fields are filled in; otherwise false.
     */
    public boolean validateInputFieldsNewAddress(AddAddressLayoutBinding binding){
        boolean allFieldsFilledIn = true;

        try {
            if (binding.updateAddressStreetEd.getText().toString().equals("")){
                binding.updateAddressStreetEd.setError(getString(R.string.product_name_isRequired));
                allFieldsFilledIn = false;
            }
            if (binding.updateAddressCityEd.getText().toString().equals("")){
                binding.updateAddressCityEd.setError(getString(R.string.ppu_isRequired));
                if (allFieldsFilledIn) allFieldsFilledIn = false;
            }
            if (binding.updateAddressProvinceEd.getText().toString().equals("")){
                binding.updateAddressProvinceEd.setError(getString(R.string.quantity_isRequired));
                if (allFieldsFilledIn) allFieldsFilledIn = false;
            }
            if (binding.updateAddressCountryEd.getText().toString().equals("")){
                binding.updateAddressCountryEd.setError(getString(R.string.quantity_isRequired));
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
     //     * @param invoicesListAdapter The invoices list adapter.
     * @param addressesListAdapter The invoices recycler view.
     * @return The created swiping capability.
     */
    private SwipeController addSwipeCapability(
            AddressesListAdapter addressesListAdapter,
            RecyclerView addressRecyclerView,
            Customer customer) {

        // Add swiping capability to list of customer
        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLeftClicked(int position, Context context, View view) {
                Address address = addressesListAdapter.getAddressAtPosition(position);

                // Show update customer activity
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateCustomerActivity.this);

                AddAddressLayoutBinding layoutBinding =
                        AddAddressLayoutBinding.inflate(getLayoutInflater(), null, false);

                // Set the details
                layoutBinding.updateAddressStreetEd.setText(address.getStreet());
                layoutBinding.updateAddressCityEd.setText(address.getCity());
                layoutBinding.updateAddressProvinceEd.setText(address.getProvince());
                layoutBinding.updateAddressCountryEd.setText(address.getCountry());

                dialogBuilder.setView(layoutBinding.getRoot());

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                // Update the invoice detail on clock
                layoutBinding.updateAddressBtn.setOnClickListener(view1 -> {
                    if (validateInputFieldsNewAddress(layoutBinding)){
                        address.setStreet(layoutBinding.updateAddressStreetEd.getText().toString());
                        address.setCity(layoutBinding.updateAddressCityEd.getText().toString());
                        address.setProvince(layoutBinding.updateAddressProvinceEd.getText().toString());
                        address.setCountry(layoutBinding.updateAddressCountryEd.getText().toString());

                        addressViewModel.update(address);
                        customerViewModel.update(customer);

                        dialog.dismiss();
                    }
                });

                // Cancel the invoice detail on click
                layoutBinding.cancelUpdateAddressBtn.setOnClickListener(view12 -> dialog.dismiss());
            }

            @Override
            public void onRightClicked(int position, Context context, View view) {
                // Show delete dialog to make sure delete customer
                DialogHolder.showAddressDeleteDialog(
                        UpdateCustomerActivity.this,
                        view,
                        addressesListAdapter,
                        position,
                        addressViewModel,
                        customer,
                        customerViewModel
                );
            }
        }, this, 150);

        // Add swiping capability to recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(addressRecyclerView);

        return swipeController;
    }

    /**
     * Checks if the selected address in the spinner is the customer's default address, if so,
     * check the default checkbox; otherwise false.
     * @param customerDefaultAddress
     */
    private void checkCheckboxOnSpinnerItemChange(Address customerDefaultAddress) {
        // Set the checkmark when spinner item changes
        binding.updateCustomerAddressesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Address selectedAddress = (Address) adapterView.getSelectedItem();

                binding.updateSetDefaultAddressCb.setChecked(
                        selectedAddress.getId() == customerDefaultAddress.getId()
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
