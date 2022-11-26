package com.example.invoiceapp_braedengiasson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.invoiceapp_braedengiasson.activities.CreateCustomerActivity;
import com.example.invoiceapp_braedengiasson.activities.CustomerDetailsActivity;
import com.example.invoiceapp_braedengiasson.activities.UpdateCustomerActivity;
import com.example.invoiceapp_braedengiasson.helpers.DialogHolder;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.CustomerListAdapter;
import com.example.invoiceapp_braedengiasson.settings.SettingsActivity;
import com.example.invoiceapp_braedengiasson.swipe.SwipeController;
import com.example.invoiceapp_braedengiasson.swipe.SwipeControllerActions;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * ***********************************************
 * Everything I got for the swiping capability comes from this website:
 * https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
 * https://github.com/FanFataL/swipe-controller-demo/blob/master/app/src/main/java/pl/fanfatal/swipecontrollerdemo/MainActivity.java
 * ***********************************************
 */
public class MainActivity extends AppCompatActivity {

    public static final int CREATE_CUSTOMER_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_CUSTOMER_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_CUSTOMER = "extra_customer_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_INVOICE = "extra_invoice_to_be_updated";

    private FloatingActionButton addCustomerButton;
    private CustomerViewModel customerViewModel;
    private AddressViewModel addressViewModel;
    private InvoiceViewModel invoiceViewModel;
    private InvoiceDetailsViewModel invoiceDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerViewModel = ViewModelProviders.of(this)
                .get(CustomerViewModel.class);
        addressViewModel = ViewModelProviders.of(this)
                .get(AddressViewModel.class);
        invoiceViewModel = ViewModelProviders.of(this)
                .get(InvoiceViewModel.class);
        invoiceDetailsViewModel = ViewModelProviders.of(this)
                .get(InvoiceDetailsViewModel.class);

        // Set up the RecyclerView.
        RecyclerView customerRecyclerView = findViewById(R.id.customers_recylcer_view);
        final CustomerListAdapter customerListAdapter = new CustomerListAdapter(this, invoiceViewModel, MainActivity.this);
        customerRecyclerView.setAdapter(customerListAdapter);
        customerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeController swipeController = addSwipeCapability(customerListAdapter, customerRecyclerView);

        customerRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c, getResources());
            }
        });

        customerViewModel.getAllCustomers().observe(this, customers ->
                customerListAdapter.setCustomers(customers)
        );

        addCustomerButton = findViewById(R.id.main_add_customer_fab);
        addCustomerButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateCustomerActivity.class);
            startActivity(intent);
        });

        customerListAdapter.setOnItemClickListener((v, position) -> {
            Intent intent = new Intent(MainActivity.this, CustomerDetailsActivity.class);
            startActivity(intent);
        });

        PreferenceManager.setDefaultValues(this,
                R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Adds the swiping capability for the specified recycler view.
     * @param customerListAdapter The customer list adapter.
     * @param customerRecyclerView The customer recycler view.
     * @return The created swiping capability.
     */
    private SwipeController addSwipeCapability(
            CustomerListAdapter customerListAdapter,
            RecyclerView customerRecyclerView)
    {
        // Add swiping capability to list of customer
        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position, Context context, View view) {
                int customerId = customerListAdapter.getCustomerAtPosition(position).getId();

                // Show update customer activity
                Intent intent = new Intent(MainActivity.this, UpdateCustomerActivity.class);
                intent.putExtra(EXTRA_DATA_UPDATE_CUSTOMER, customerId);
                startActivity(intent);
            }

            @Override
            public void onRightClicked(int position, Context context, View view) {
                // Show delete dialog to make sure delete customer
                DialogHolder.showCustomerDeleteDialog(
                        MainActivity.this,
                        MainActivity.this,
                        view,
                        customerListAdapter,
                        position,
                        customerViewModel,
                        addressViewModel,
                        invoiceViewModel,
                        invoiceDetailsViewModel
                );
            }
        }, this);

        // Add swiping capability to recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(customerRecyclerView);

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