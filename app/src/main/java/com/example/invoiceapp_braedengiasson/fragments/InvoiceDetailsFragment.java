package com.example.invoiceapp_braedengiasson.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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

import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.activities.CreateInvoiceActivity;
import com.example.invoiceapp_braedengiasson.activities.CustomerInvoiceActivity;
import com.example.invoiceapp_braedengiasson.databinding.FragmentInvoiceDetailsRecyclerViewLayoutBinding;
import com.example.invoiceapp_braedengiasson.databinding.CreateInvoiceDetailLayoutBinding;
import com.example.invoiceapp_braedengiasson.helpers.DialogHolder;
import com.example.invoiceapp_braedengiasson.recyclerAdapter.InvoiceDetailsListAdapter;
import com.example.invoiceapp_braedengiasson.swipe.SwipeController;
import com.example.invoiceapp_braedengiasson.swipe.SwipeControllerActions;
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;
import com.example.invoiceapp_braedengiasson.viewModel.InvoiceDetailsViewModel;
import com.google.android.material.snackbar.Snackbar;

public class InvoiceDetailsFragment extends Fragment {
    private Context context;
    private InvoiceDetailsViewModel invoiceDetailsViewModel;
    private FragmentInvoiceDetailsRecyclerViewLayoutBinding binding;
    private CustomerInvoiceActivity activity;
    private CreateInvoiceActivity createInvoiceActivity;
    private Invoice invoice;

    /**
     * Instantiates a new instance of the trip list fragment.
     * @param context
     */
    public InvoiceDetailsFragment(
            Context context,
            InvoiceDetailsViewModel invoiceViewModel,
            CustomerInvoiceActivity activity,
            Invoice invoice)
    {
        this.context = context;
        this.invoiceDetailsViewModel = invoiceViewModel;
        this.activity = activity;
        this.invoice = invoice;
    }

    /**
     * Instantiates a new instance of the trip list fragment.
     * @param context
     */
    public InvoiceDetailsFragment(
            Context context,
            InvoiceDetailsViewModel invoiceViewModel,
            CreateInvoiceActivity activity,
            Invoice invoice)
    {
        this.context = context;
        this.invoiceDetailsViewModel = invoiceViewModel;
        this.createInvoiceActivity = activity;
        this.invoice = invoice;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInvoiceDetailsRecyclerViewLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final InvoiceDetailsListAdapter invoicesListAdapter = new InvoiceDetailsListAdapter(
                context,
                invoiceDetailsViewModel);

//        SwipeController swipeController = addSwipeCapability(invoicesListAdapter, binding.invoiceDetailsRecylcerView);
//
//        binding.invoiceDetailsRecylcerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c, getResources());
//            }
//        });

        invoiceDetailsViewModel.getInvoicesDetailsForInvoice(invoice.getId()).observe(
                activity == null ? createInvoiceActivity : activity
                ,
                invoices ->
                        invoicesListAdapter.setInvoices(invoices)
                );

        // Set adapter for recycler view
        binding.invoiceDetailsRecylcerView.setLayoutManager(new LinearLayoutManager(context));
        binding.invoiceDetailsRecylcerView.setAdapter(invoicesListAdapter);
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
        Activity activity1 = activity == null ? createInvoiceActivity : activity;

        // Add swiping capability to list of customer
        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLeftClicked(int position, Context context, View view) {
                InvoiceDetails invoiceDetail = invoicesListAdapter.getInvoiceDetailAtPosition(position);

                // Show update customer activity
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity1);

                CreateInvoiceDetailLayoutBinding layoutBinding =
                        CreateInvoiceDetailLayoutBinding.inflate(activity1.getLayoutInflater(), null, false);

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

                    activity.setTotal(invoice.getTotal());
                });

                // Cancel the invoice detail on click
                layoutBinding.cancelCreateInvoiceDetailBtn.setOnClickListener(view12 -> dialog.dismiss());
            }

            @Override
            public void onRightClicked(int position, Context context, View view) {
                // Show delete dialog to make sure delete customer
                DialogHolder.showInvoiceDetailDeleteDialog(
                        activity1,
                        view,
                        invoicesListAdapter,
                        position,
                        invoiceDetailsViewModel
                );
            }
        }, context, 150);

        // Add swiping capability to recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(invoicesRecyclerView);

        return swipeController;
    }
}
