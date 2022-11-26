package com.example.invoiceapp_braedengiasson.swipe;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public abstract class SwipeControllerActions {
    public void onLeftClicked(int position, Context context, View view) {}
    public void onRightClicked(int position, Context context, View view) {}
}
