package org.sourcebrew.surveys.surveygroup.surveyresponce;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.sourcebrew.surveys.R;
import org.sourcebrew.surveys.surveygroup.widgets.DatePickerFragment;
import org.sourcebrew.surveys.surveygroup.SimpleMessageInterface;
import org.sourcebrew.surveys.surveygroup.widgets.TimePickerFragment;

/**
 * Created by John on 12/15/2017.
 */

public class SurveyResponceYesNo  extends SurveyResponseRoot {

    final TextView imgYes;
    final TextView imgNo;

    View selectedItem = null;
    final int imgChecked, imgUnchecked;

    final TextView textView;
    TextView extra;
    boolean isForDate;
    private final TextView targetView;

    public SurveyResponceYesNo(Context context, JSONObject sourceJSON) {

        super(context, sourceJSON);
        ViewGroup target = this;

        String input_type = getValue(sourceJSON, "input_type");
        textView = new TextView(context);
        if (!input_type.isEmpty()) {
            setOrientation(LinearLayout.VERTICAL);
            LinearLayout container = new LinearLayout(getContext());
            container.setOrientation(LinearLayout.HORIZONTAL);
            isForDate = input_type.toLowerCase().contains("date");
            boolean isForTextView = input_type.toLowerCase().contains("time") || isForDate;
            if (isForTextView) {
                extra = new TextView(getContext());
                extra.setBackgroundResource(R.drawable.border_for_view);
                int i = toDP(8);
                extra.setPadding(i, i, i, i);
                extra.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                extra.setClickable(true);
            } else {
                extra = new EditText(getContext());
            }
            extra.setLayoutParams(createParams(false, true, 0, 4, 0, 4));
            extra.setHint(getValue(sourceJSON, "hint"));
            extra.setVisibility(View.GONE);
            extra.setInputType(inputTypeMask(input_type));
            extra.setOnClickListener(this);

            addView(container);
            addView(extra);
            target = container;
            targetView = extra;
            extra.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            extra.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setSurveyResponceWait(getValue(), SurveyResponceSelected.translateCompare(selectedItem, imgNo, imgYes));
                }
            });
        } else {
            targetView = textView;
        }

        setPadding(toDP(4), 0, toDP(4), toDP(4));

        String type = getValue(sourceJSON, "type");

        imgChecked = R.drawable.ic_check_box_black;
        imgUnchecked = R.drawable.ic_check_box_outline_blank;

        imgYes = createCheckBox(target, "yes");
        imgNo = createCheckBox(target, "no");

        imgYes.setTag(R.string.DEFAULT_BACKGROUND, R.drawable.border_yes);
        imgNo.setTag(R.string.DEFAULT_BACKGROUND, R.drawable.border_no);


        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setLayoutParams(createParams(false, true, 8, 4, 4, 4));

        textView.setText(getValue(sourceJSON, "value"));
        target.addView(textView, 0);

        imgYes.setOnClickListener(this);
        imgNo.setOnClickListener(this);
        textView.setOnClickListener(this);

        setBackgroundResource(R.drawable.bottom_line);
    }

    private TextView createCheckBox(ViewGroup p, String title) {
        TextView img = new TextView(getContext());
        img.setLayoutParams(createParams(true, false, 4, 4, 4, 4));
        img.setTextColor(Color.argb(255, 0, 80, 128));
        img.setText(title);
        img.setBackgroundResource(R.drawable.border_for_switch);
        p.addView(img);
        return img;
    }

    @Override
    public String getValue() {
        if (selectedItem == imgNo)
            return "no";
        else if (selectedItem == imgYes) {
            if (extra != null) {
                return "yes:" + extra.getText();
            } else
                return "yes";
        } else
            return "null";
    }

    @Override
    public void setValue(String value) {

        targetView.setText(value);

    }

    private void setToggleState(View v) {
        if (v == imgYes) {
            imgYes.setBackgroundResource((int)v.getTag(R.string.DEFAULT_BACKGROUND));
            imgNo.setBackgroundResource(R.drawable.border_for_switch);
            selectedItem = imgYes;
            textView.setTypeface(null, Typeface.BOLD);
            if (getChildCount() == 2) {
                View extra = getChildAt(1);
                extra.setVisibility(View.VISIBLE);
            }
        } else if (v == imgNo) {
            imgNo.setBackgroundResource((int)v.getTag(R.string.DEFAULT_BACKGROUND));
            imgYes.setBackgroundResource(R.drawable.border_for_switch);
            selectedItem = imgNo;
            textView.setTypeface(null, Typeface.BOLD);
            if (getChildCount() == 2) {
                View extra = getChildAt(1);
                extra.setVisibility(View.GONE);
            }
        } else {
            imgYes.setBackgroundResource(R.drawable.border_for_switch);
            imgNo.setBackgroundResource(R.drawable.border_for_switch);
            selectedItem = null;
            textView.setTypeface(null, Typeface.NORMAL);
            if (getChildCount() == 2) {
                View extra = getChildAt(1);
                extra.setVisibility(View.GONE);
            }
        }
        setSurveyResponceWait(getValue(), SurveyResponceSelected.translateCompare(selectedItem, imgNo, imgYes));
    }

    @Override
    public void onClick(View v) {
        if (extra != null && v == extra) {
            showPopup();
            return;
        }
        if (v == imgYes || v == imgNo) {
            setToggleState(v);
        } else if (v == textView) {
            if (selectedItem == imgNo) {
                selectedItem = null;
            } else if (selectedItem == imgYes) {
                selectedItem = imgNo;
            } else if (selectedItem == null) {
                selectedItem = imgYes;
            }
            setToggleState(selectedItem);
        }
    }

    private void showPopup() {
        if (extra != null) {

            if (!isForDate) {

                DialogFragment newFragment = new TimePickerFragment();

                ((TimePickerFragment) newFragment).setOnCallback(
                        new SimpleMessageInterface() {
                            @Override
                            public void OnSimpleMessage(String value) {
                                setValue(value);
                            }
                        }
                );
                Activity activity = (Activity) getContext();
                newFragment.show(activity.getFragmentManager(), "TimePicker");

            } else {
                DialogFragment newFragment = new DatePickerFragment();
                ((DatePickerFragment)newFragment).setOnCallback(
                        new SimpleMessageInterface() {
                            @Override
                            public void OnSimpleMessage(String value) {
                                setValue(value);
                            }
                        }
                );
                Activity activity = (Activity)getContext();
                newFragment.show(activity.getFragmentManager(),"DatePicker");
            }
        }
    }

    @Override
    public boolean toBoolean() {
        return selectedItem == imgYes;
    }

    public String getResult() throws Exception {
        if (selectedItem != null) {
           return getValue();
        }
        else
            throw new Exception("NOT SELECTED");
    }


    public void setValue(boolean value) {

    }


}