package com.example.invoiceapp_braedengiasson.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.databinding.ActivityCreateNewCustomerLayoutBinding;
import com.example.invoiceapp_braedengiasson.interfaces.IValidateInput;
import com.example.invoiceapp_braedengiasson.settings.SettingsActivity;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;

import java.util.Objects;

public class CreateCustomerActivity
        extends AppCompatActivity
        implements IValidateInput
{
    public static String EXTRA_REPLY = "";
    public static String EXTRA_REPLY_ID = "";

    private ActivityCreateNewCustomerLayoutBinding binding;
    private CustomerViewModel customerViewModel;
    private AddressViewModel addressViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateNewCustomerLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        customerViewModel = ViewModelProviders.of(this)
                        .get(CustomerViewModel.class);
        addressViewModel = ViewModelProviders.of(this)
                        .get(AddressViewModel.class);

        binding.createNewCustomerBtn.setOnClickListener(view1 -> {
            if (validateInputFields())
                evaluateNewCustomer(view1);
        });

        binding.cancelNewCustomerBtn.setOnClickListener(view1 -> {
            if (someFieldsFilled()){
                // Creating the alert dialog
                AlertDialog.Builder cancelDialog = new AlertDialog.Builder(CreateCustomerActivity.this)
                        .setTitle(R.string.cancel_text)
                        .setIcon(R.drawable.ic_baseline_warning_ywellow_24)
                        .setMessage("There's some form fields filled in.\n" +
                                        "Are you sure you want to cancel creating a customer?");

                // Positive button
                cancelDialog.setPositiveButton(R.string.delete_dialog_positive, (dialogInterface, i) -> finish());

                // Negative button
                cancelDialog.setNegativeButton(R.string.delete_dialog_negative, (dialogInterface, i) -> dialogInterface.dismiss());

                // Show dialog
                cancelDialog.create().show();
            }
            else finish();
        });
    }

    private void evaluateNewCustomer(View view){
        String name = String.format("%s %s",
                binding.createCustomerFirstNameEd.getText().toString(),
                binding.createCustomerLastNameEd.getText().toString()
        );

        Customer newCustomer = new Customer(name);

        Address newAddress = new Address(
                newCustomer.getId(),
                binding.createCustomerAddressStreetEd.getText().toString(),
                binding.createCustomerAddressCityEd.getText().toString(),
                binding.createCustomerAddressProvinceEd.getText().toString(),
                binding.createCustomerAddressCountryEd.getText().toString()
        );

        customerViewModel.insert(newCustomer);
        addressViewModel.insert(newAddress);

        // Set the default address if checked
        if (binding.createCustomerSetDefaultAddress.isChecked())
            customerViewModel.setDefaultAddress(customerViewModel.getNewlyAdded().getId(),
                    addressViewModel.getNewlyAdded().getId());

        finish();
    }

    /**
     * Validates the input fields to make sure they're all filled in.
     * @return True if all the input fields are filled in; otherwise false.
     */
    public boolean validateInputFields(){
        boolean allFieldsFilledIn = true;

       try {
           // Validate the first name field is filled in
           if (binding.createCustomerFirstNameEd.getText().toString().equals("")){
               binding.createCustomerFirstNameEd.setError(getString(R.string.first_name_isRequired));
               allFieldsFilledIn = false;
           }
           // Validate the last name field is filled in
           if (binding.createCustomerLastNameEd.getText().toString().equals("")){
               binding.createCustomerLastNameEd.setError(getString(R.string.last_name_isRequired));
               if (allFieldsFilledIn) allFieldsFilledIn = false;
           }
           // Validate the address street field is filled in
           if (binding.createCustomerAddressStreetEd.getText().toString().equals("")){
               binding.createCustomerAddressStreetEd.setError(getString(R.string.address_isRequired));
               if (allFieldsFilledIn) allFieldsFilledIn = false;
           }
           // Validate the city field is filled in
           if (binding.createCustomerAddressCityEd.getText().toString().equals("")){
               binding.createCustomerAddressCityEd.setError(getString(R.string.city_isRequired));
               if (allFieldsFilledIn) allFieldsFilledIn = false;
           }
           // Validate the country field is filled in
           if (binding.createCustomerAddressCountryEd.getText().toString().equals("")){
               binding.createCustomerAddressCountryEd.setError(getString(R.string.country_isRequired));
               if (allFieldsFilledIn) allFieldsFilledIn = false;
           }
       }
       catch (Exception ex){
           return false;
       }

        return allFieldsFilledIn;
    }

    /**
     * Checks if all some form fields are filled.
     * @return True if all some form form fields are filled; otherwise false.
     */
    private boolean someFieldsFilled(){
        return !binding.createCustomerFirstNameEd.getText().toString().equals("")
            || !binding.createCustomerLastNameEd.getText().toString().equals("")
            || !binding.createCustomerAddressStreetEd.getText().toString().equals("")
            || !binding.createCustomerAddressCityEd.getText().toString().equals("")
            || !binding.createCustomerAddressProvinceEd.getText().toString().equals("")
            || !binding.createCustomerAddressCountryEd.getText().toString().equals("");
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
