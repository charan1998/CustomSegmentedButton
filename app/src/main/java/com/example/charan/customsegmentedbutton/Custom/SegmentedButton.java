package com.example.charan.customsegmentedbutton.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
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
    private OnOptionChangedListener onOptionChangedListener;
    private int backgroundColor;
    private CharSequence[] entries;
    private TextView[] textViews;
    private int borderWidth;
    private int buttonSpacing;
    private float borderRadius;
    private GradientDrawable viewBackgroundDrawable;
    private GradientDrawable buttonBackgroundDrawable;
    private float[] leftRounded;
    private float[] rightRounded;

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
            backgroundColor = typedArray.getColor(R.styleable.SegmentedButton_border_color, getResources().getColor(R.color.button_color));
            currentSelected = typedArray.getInteger(R.styleable.SegmentedButton_selected, 0);
            borderWidth = (int) typedArray.getDimension(R.styleable.SegmentedButton_border_width, 0);
            buttonSpacing = (int) typedArray.getDimension(R.styleable.SegmentedButton_button_spacing, 0);
            borderRadius = typedArray.getDimension(R.styleable.SegmentedButton_border_radius, 0);
        }
        finally {
            typedArray.recycle();
        }
        viewBackgroundDrawable = new GradientDrawable();
        viewBackgroundDrawable.setCornerRadius(borderRadius);
        viewBackgroundDrawable.setColor(backgroundColor);
        textViews = new TextView[numButtons];
        leftRounded = new float[]{borderRadius, borderRadius, 0, 0, 0, 0, borderRadius, borderRadius};
        rightRounded = new float[]{0, 0, borderRadius, borderRadius, borderRadius, borderRadius, 0, 0};
        setOrientation(HORIZONTAL);
        setBackground(viewBackgroundDrawable);
        setPadding(borderWidth, borderWidth, borderWidth, borderWidth);
        for (int i = 0; i < numButtons; i ++) {
            textViews[i] = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textViews[i].setTextColor(textColor);
            textViews[i].setGravity(Gravity.CENTER);
            setBackground(i, buttonBackground);
            textViews[i].setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);
            if (textSize > 0) {
                textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            final int pos = i;
            textViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentSelected != pos) {
                        makeSelected(pos);
                        onOptionChangedListener.onChange(pos);
                    }
                }
            });
            addView(textViews[i], layoutParams);
            if ((i != numButtons - 1) && (buttonSpacing > 0)) {
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(buttonSpacing, LayoutParams.MATCH_PARENT);
                View divider = new View(context);
                divider.setBackgroundColor(backgroundColor);
                addView(divider, dividerParams);
            }
        }
        makeSelected(currentSelected);
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
        setBackground(currentSelected, buttonBackground);
        textViews[currentSelected].setTextColor(textColor);
        setBackground(i, buttonBackgroundSelected);
        textViews[i].setTextColor(textColorSelected);
        currentSelected = i;
    }

    private void setBackground(int i, int color) {
        buttonBackgroundDrawable = new GradientDrawable();
        buttonBackgroundDrawable.setColor(color);
        if (i == 0) {
            buttonBackgroundDrawable.setCornerRadii(leftRounded);
        }
        else if (i == numButtons - 1) {
            buttonBackgroundDrawable.setCornerRadii(rightRounded);
        }
        textViews[i].setBackground(buttonBackgroundDrawable);
    }

    public void setOnOptionChangedListener(OnOptionChangedListener onOptionChangedListener) {
        this.onOptionChangedListener = onOptionChangedListener;
    }

    public int getPosition() {
        return currentSelected;
    }
}