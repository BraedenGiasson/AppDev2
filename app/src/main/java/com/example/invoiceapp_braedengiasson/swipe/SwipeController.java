package com.example.invoiceapp_braedengiasson.swipe;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoiceapp_braedengiasson.R;
import com.example.invoiceapp_braedengiasson.enums.ButtonsState;

/**
 * This was inspired from this website:
 * https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28#ed30
 */
public class SwipeController extends ItemTouchHelper.Callback {
    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private SwipeControllerActions buttonsActions = null;
    private float buttonWidth = 200;
    public Context context;

    public SwipeController(SwipeControllerActions buttonsActions, Context context) {
        this.buttonsActions = buttonsActions;
        this.context = context;
    }

    public SwipeController(SwipeControllerActions buttonsActions, Context context, float buttonWidth) {
        this.buttonsActions = buttonsActions;
        this.context = context;
        this.buttonWidth = buttonWidth;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(
            Canvas c,
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive)
    {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE)
                    dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE)
                    dX = Math.min(dX, -buttonWidth);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        if (buttonShowedState == ButtonsState.GONE)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        currentItemViewHolder = viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(
            final Canvas c,
            final RecyclerView recyclerView,
            final RecyclerView.ViewHolder viewHolder,
            final float dX,
            final float dY,
            final int actionState,
            final boolean isCurrentlyActive)
    {
        recyclerView.setOnTouchListener((v, event) -> {
            swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;

            if (swipeBack) {
                if (dX < -buttonWidth)
                    buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                else if (dX > buttonWidth)
                    buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                }
            }

            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(
            final Canvas c,
            final RecyclerView recyclerView,
            final RecyclerView.ViewHolder viewHolder,
            final float dX,
            final float dY,
            final int actionState,
            final boolean isCurrentlyActive)
    {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(
            final Canvas c,
            final RecyclerView recyclerView,
            final RecyclerView.ViewHolder viewHolder,
            final float dX,
            final float dY,
            final int actionState,
            final boolean isCurrentlyActive)
    {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);

                recyclerView.setOnTouchListener((v1, event1) -> false);
                setItemsClickable(recyclerView, true);
                swipeBack = false;

                if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                    if (buttonShowedState == ButtonsState.LEFT_VISIBLE)
                        buttonsActions.onLeftClicked(viewHolder.getAdapterPosition(), context, v);
                    else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE)
                        buttonsActions.onRightClicked(viewHolder.getAdapterPosition(), context, v);
                }

                buttonShowedState = ButtonsState.GONE;
                currentItemViewHolder = null;
            }

            return false;
        });
    }

    private void setItemsClickable(
            RecyclerView recyclerView,
            boolean isClickable)
    {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder, Resources context) {
        float buttonWidthWithoutPadding = buttonWidth - 10; // 20
        float corners = 42;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, corners, corners, p);
        drawIcon(R.drawable.ic_baseline_edit_24, leftButton, c, context);


        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(rightButton, corners, corners, p);
        drawIcon(R.drawable.ic_baseline_delete_24, rightButton, c, context);

        buttonInstance = null;

        if (buttonShowedState == ButtonsState.LEFT_VISIBLE)
            buttonInstance = leftButton;
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE)
            buttonInstance = rightButton;
    }

    private void drawIcon(int resource, RectF button, Canvas c, Resources context){
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable d = context.getDrawable(resource);

        d.setBounds(
                Math.round(button.centerX() - 24),
                Math.round(button.centerY() - 24),
                Math.round(button.centerX() + 24),
                Math.round(button.centerY() + 24)
        );

        d.draw(c);
    }

    public void onDraw(Canvas c, Resources context) {
        if (currentItemViewHolder != null)
            drawButtons(c, currentItemViewHolder, context);
    }
}
