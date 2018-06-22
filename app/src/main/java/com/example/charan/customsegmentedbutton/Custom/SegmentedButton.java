package com.example.charan.customsegmentedbutton.Custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.charan.customsegmentedbutton.R;

public class SegmentedButton extends LinearLayout {

    private int textColor;
    private int textColorSelected;
    private int buttonBackground;
    private int buttonBackgroundSelected;
    private int numButtons;
    private int buttonPadding;
    private int textSize;
    private int currentSelected;
    private boolean hasChanged;
    private OnOptionChangedListener onOptionChangedListener;
    private Drawable viewBackground;
    private CharSequence[] entries;
    private TextView[] textViews;
    private LinearLayout root;

    public SegmentedButton(final Context context, AttributeSet attr) {
        super(context, attr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attr, R.styleable.SegmentedButton, 0, 0);
        try {
            textColor = typedArray.getColor(R.styleable.SegmentedButton_text_color, getResources().getColor(R.color.button_color));
            textColorSelected = typedArray.getColor(R.styleable.SegmentedButton_selected_text_color, getResources().getColor(R.color.white));
            buttonBackground = typedArray.getColor(R.styleable.SegmentedButton_button_bg, getResources().getColor(R.color.white));
            buttonBackgroundSelected = typedArray.getColor(R.styleable.SegmentedButton_selected_button_bg, getResources().getColor(R.color.button_color));
            numButtons = typedArray.getInteger(R.styleable.SegmentedButton_num_options, 0);
            entries = typedArray.getTextArray(R.styleable.SegmentedButton_android_entries);
            buttonPadding = (int) typedArray.getDimension(R.styleable.SegmentedButton_button_padding, 0);
            textSize = (int) typedArray.getDimension(R.styleable.SegmentedButton_text_font_size, 0);
            viewBackground = typedArray.getDrawable(R.styleable.SegmentedButton_background_drawable);
            currentSelected = typedArray.getInteger(R.styleable.SegmentedButton_selected, 0);
        }
        finally {
            typedArray.recycle();
        }
        root = this;
        textViews = new TextView[numButtons];
        int padding = (int) getResources().getDimension(R.dimen.size_1_dp);
        setPadding(padding, padding, padding, padding);
        setOrientation(HORIZONTAL);
        setBackground(viewBackground);
        for (int i = 0; i < numButtons; i ++) {
            textViews[i] = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textViews[i].setLayoutParams(layoutParams);
            textViews[i].setTextColor(textColor);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setBackgroundColor(buttonBackground);
            textViews[i].setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);
            if (textSize > 0) {
                textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            final int pos = i;
            textViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeSelected(pos);
                    hasChanged = true;
                    root.performClick();
                }
            });
            addView(textViews[i]);
        }
        hasChanged = false;
        makeSelected(currentSelected);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasChanged) {
                    hasChanged = false;
                    onOptionChangedListener.onChange();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < numButtons; i ++) {
            textViews[i].setText(entries[i]);
        }
        final LinearLayout linearLayout = this;
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int maxWidth = 0;
                for (TextView textView : textViews) {
                    if (textView.getWidth() > maxWidth) {
                        maxWidth = textView.getWidth();
                    }
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(maxWidth, LayoutParams.WRAP_CONTENT);
                for (int i = 0; i < numButtons; i ++) {
                    textViews[i].setLayoutParams(layoutParams);
                }
            }
        });
    }

    private void makeSelected(int i) {
        textViews[currentSelected].setBackgroundColor(buttonBackground);
        textViews[currentSelected].setTextColor(textColor);
        textViews[i].setBackgroundColor(buttonBackgroundSelected);
        textViews[i].setTextColor(textColorSelected);
        currentSelected = i;
    }

    public void setOnOptionChangedListener(OnOptionChangedListener onOptionChangedListener) {
        this.onOptionChangedListener = onOptionChangedListener;
    }

    public int getPosition() {
        return currentSelected;
    }
}