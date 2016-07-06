package com.tsengvn.prefixinput;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015, Posiba. All rights reserved.
 *
 * @author Hien Ngo
 * @since 6/12/15
 */
public class PrefixMetricInput extends AppCompatEditText {
    private static final BigDecimal MAX = BigDecimal.valueOf(999999999999.999d);

    private BigDecimal mValue = BigDecimal.valueOf(0d);
    private DecimalFormat mDecimalFormat = new DecimalFormat("#.##");
    private GIVNNumberFilter mNumberFilter = new GIVNNumberFilter();

    private NumberListener mNumberListener;

    public PrefixMetricInput(Context context) {
        super(context);
        initialize();
    }

    public PrefixMetricInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public PrefixMetricInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setNumberListener(NumberListener numberListener) {
        mNumberListener = numberListener;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            unabbreviating();
        } else {
            parseValue();
            fireListener();
            abbreviating();
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    private void initialize() {
        if (isInEditMode()){
            return;
        }

        this.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        InputFilter[] filters = getFilters();
        List<InputFilter> filterList = new ArrayList<>();
            for (InputFilter inputFilter : filters) {
                filterList.add(inputFilter);
            }
        filterList.add(mNumberFilter);
        this.setFilters(filterList.toArray(new InputFilter[filterList.size()]));

    }

    public void setValue(double value) {
        mValue = BigDecimal.valueOf(value);
        fireListener();
        if (hasFocus()) {
            unabbreviating();
        } else {
            abbreviating();
        }
    }

    public String getTextValue() {
        if (hasFocus()) {
            return getText().toString();
        } else {
            return mValue.doubleValue() != 0d ? mDecimalFormat.format(mValue.doubleValue()) : "";
        }
    }

    public void setTextValue(CharSequence s) {
        setValue(Double.parseDouble(s.toString()));
    }

    private void abbreviating() {
        if (mValue.doubleValue() != 0d) {
            String number = NumberUtil.formatNumber(NumberUtil.createDouble(mValue.doubleValue()), true, 4);
            setText(number);
        }
    }

    private void unabbreviating() {
        if (mValue.doubleValue() == 0f) {
            setText("");
        } else {
            setText(mDecimalFormat.format(mValue.doubleValue()));
        }
    }

    private void parseValue() {
        try {
            mValue = new BigDecimal(getText().toString());
        } catch (Exception e) {
            mValue = BigDecimal.valueOf(0D);
//            throw new IllegalArgumentException("Text of this view must be number!");
        }
    }

    private void fireListener() {
        if (mNumberListener != null) {
            mNumberListener.onNumberSet(mValue.doubleValue());
        }
    }

    class GIVNNumberFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (hasFocus()) {
                try {
                    String newValue = new StringBuilder(dest).insert(dstart, source).toString();
                    BigDecimal input = BigDecimal.valueOf(Double.parseDouble(newValue));
                    if (input.compareTo(MAX) < 0)
                        return null;
                } catch (NumberFormatException nfe) { }
                return "";
            } else {
                return null;
            }
        }
    }

    public interface NumberListener {
        void onNumberSet(double number);
    }
}
