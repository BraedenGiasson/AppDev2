package com.example.invoiceapp_braedengiasson.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.activities.CreateInvoiceActivity;
import com.example.invoiceapp_braedengiasson.activities.UpdateCustomerActivity;
import com.example.invoiceapp_braedengiasson.databinding.FragmentAddressReyclerViewLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.ModifyAddressesLayoutBinding;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.AddressesListAdapter;
import com.example.invoiceapp_braedengiasson.swipe.SwipeController;
import com.example.invoiceapp_braedengiasson.swipe.SwipeControllerActions;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.viewModel.AddressViewModel;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;

public class ModifyAddressesFragment extends Fragment {
    private Context context;
    private InvoiceDetailsViewModel invoiceDetailsViewModel;
    private AddressViewModel addressViewModel;
    private FragmentAddressReyclerViewLayoutBinding binding;
    private UpdateCustomerActivity activity;
    private CreateInvoiceActivity createInvoiceActivity;
    private Customer customer;
    private ModifyAddressesLayoutBinding layoutBinding;

    /**
     * Instantiates a new instance of the trip list fragment.
     * @param context
     */
    public ModifyAddressesFragment(
            Context context,
            AddressViewModel addressViewModel,
            UpdateCustomerActivity activity,
            Customer customer,
            ModifyAddressesLayoutBinding layoutBinding)
    {
        this.context = context;
        this.addressViewModel = addressViewModel;
        this.activity = activity;
        this.customer = customer;
        this.layoutBinding = layoutBinding;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressReyclerViewLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AddressesListAdapter addressesListAdapter = new AddressesListAdapter(
                activity,
                addressViewModel);

        SwipeController swipeController = addSwipeCapability(addressesListAdapter, binding.addressRecylcerView);

        binding.addressRecylcerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c, getResources());
            }
        });

        // Set adapter for recycler view
        binding.addressRecylcerView.setLayoutManager(new LinearLayoutManager(activity));
        binding.addressRecylcerView.setAdapter(addressesListAdapter);
    }

    /**
     * Adds the swiping capability for the specified recycler view.
//     * @param invoicesListAdapter The invoices list adapter.
     * @param addressesListAdapter The invoices recycler view.
     * @return The created swiping capability.
     */
    private SwipeController addSwipeCapability(
            AddressesListAdapter addressesListAdapter,
            RecyclerView addressRecyclerView)
    {
        // Add swiping capability to list of customer
        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLeftClicked(int position, Context context, View view) {
            }

            @Override
            public void onRightClicked(int position, Context context, View view) {
                // Show delete dialog to make sure delete customer
//                DialogHolder.showInvoiceDetailDeleteDialog(
//                        activity1,
//                        view,
//                        invoicesListAdapter,
//                        position,
//                        invoiceDetailsViewModel
//                );
            }
        }, context, 150);

        // Add swiping capability to recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(addressRecyclerView);

        return swipeController;
    }
}
