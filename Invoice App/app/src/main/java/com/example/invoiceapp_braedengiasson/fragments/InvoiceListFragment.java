package com.example.invoiceapp_braedengiasson.fragments;

import static com.example.invoiceapp_braedengiasson.MainActivity.EXTRA_DATA_UPDATE_CUSTOMER;
import static com.example.invoiceapp_braedengiasson.MainActivity.EXTRA_DATA_UPDATE_INVOICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.MainActivity;
import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.activities.CustomerDetailsActivity;
import com.example.invoiceapp_braedengiasson.activities.UpdateCustomerActivity;
import com.example.invoiceapp_braedengiasson.activities.UpdateInvoiceActivity;
import com.example.invoiceapp_braedengiasson.databinding.ActivityCustomerDetailsLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.FragmentInvoicesRecyclerViewLayoutBinding;
import com.example.invoiceapp_braedengiasson.helpers.DialogHolder;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.CustomerListAdapter;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoicesListAdapter;
import com.example.invoiceapp_braedengiasson.swipe.SwipeController;
import com.example.invoiceapp_braedengiasson.swipe.SwipeControllerActions;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceViewModel;
import com.google.android.material.snackbar.Snackbar;

public class InvoiceListFragment extends Fragment {
    private Context context;
    private InvoiceViewModel invoiceViewModel;
    private FragmentInvoicesRecyclerViewLayoutBinding binding;
    private ActivityCustomerDetailsLayoutBinding parentBinding;
    private CustomerDetailsActivity activity;
    private Customer customer;

    /**
     * Instantiates a new instance of the trip list fragment.
     * @param context
     */
    public InvoiceListFragment(
            Context context,
            InvoiceViewModel invoiceViewModel,
            CustomerDetailsActivity activity,
            Customer customer,
            ActivityCustomerDetailsLayoutBinding binding)
    {
        this.context = context;
        this.invoiceViewModel = invoiceViewModel;
        this.activity = activity;
        this.customer = customer;
        this.parentBinding = binding;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInvoicesRecyclerViewLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final InvoicesListAdapter invoicesListAdapter = new InvoicesListAdapter(
                context,
                invoiceViewModel,
                customer);

        SwipeController swipeController = addSwipeCapability(invoicesListAdapter, binding.invoicesRecylcerView);

        binding.invoicesRecylcerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c, getResources());
            }
        });

        invoiceViewModel.getInvoicesForCustomer(customer.getId()).observe(activity,
                invoices -> {
                    // Let user know to add first invoice if none
                    if (invoices.size() == 0)
                        Snackbar.make(
                                context, view,
                                getString(R.string.add_first_invoice),
                                Snackbar.LENGTH_LONG).show();
                    // Set the invoices
                    else
                        invoicesListAdapter.setInvoices(invoices);
                });

        // Set adapter for recycler view
        binding.invoicesRecylcerView.setLayoutManager(new LinearLayoutManager(context));
        binding.invoicesRecylcerView.setAdapter(invoicesListAdapter);
    }

    /**
     * Adds the swiping capability for the specified recycler view.
     * @param invoicesListAdapter The invoices list adapter.
     * @param invoicesRecyclerView The invoices recycler view.
     * @return The created swiping capability.
     */
    private SwipeController addSwipeCapability(
            InvoicesListAdapter invoicesListAdapter,
            RecyclerView invoicesRecyclerView)
    {
        // Add swiping capability to list of customer
        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position, Context context, View view) {
                int invoiceId = invoicesListAdapter.getInvoiceAtPosition(position).getId();

                // Show update customer activity
                Intent intent = new Intent(activity, UpdateInvoiceActivity.class);
                intent.putExtra(EXTRA_DATA_UPDATE_INVOICE, invoiceId);
                intent.putExtra(EXTRA_DATA_UPDATE_CUSTOMER, customer.getId());
                startActivity(intent);

                activity.setDetails(customer);
            }

            @Override
            public void onRightClicked(int position, Context context, View view) {
                // Show delete dialog to make sure delete customer
                DialogHolder.showInvoiceDeleteDialog(
                        activity,
                        view,
                        invoicesListAdapter,
                        position,
                        invoiceViewModel,
                        customer
                );
            }
        }, context);

        // Add swiping capability to recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(invoicesRecyclerView);

        return swipeController;
    }
}
