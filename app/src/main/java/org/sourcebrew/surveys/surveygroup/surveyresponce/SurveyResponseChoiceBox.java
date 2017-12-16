package org.sourcebrew.surveys.surveygroup.surveyresponce;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONObject;
import org.sourcebrew.surveys.R;

/**
 * Created by John on 12/13/2017.
 */

public class SurveyResponseChoiceBox extends SurveyResponseRoot {

    final ImageView imgToggle;
    final TextView textView;

    boolean toggleState = false;
    final boolean isCheckType, isEditText;
    final int imgChecked, imgUnchecked;

    public SurveyResponseChoiceBox(Context context, JSONObject sourceJSON) {

        super(context, sourceJSON);

        String type = getValue(sourceJSON, "type");


        switch (type.toLowerCase()) {
            case "checkbox":
                imgChecked = R.drawable.ic_check_box_black;
                imgUnchecked = R.drawable.ic_check_box_outline_blank;
                isCheckType = true;
                break;
            default:
                imgChecked = R.drawable.ic_radio_button_checked;
                imgUnchecked = R.drawable.ic_radio_button_unchecked;
                isCheckType = false;
                break;
        }


        imgToggle = new ImageView(context);

        imgToggle.setLayoutParams(createParams(true, false, 4, 4, 4, 4));
        imgToggle.setColorFilter(Color.argb(255, 0, 80, 128));
        String inputType = getValue(sourceJSON, "input_type");
        if (inputType.isEmpty()) {
            textView = new TextView(context);
            isEditText = false;
            setValue(getValue(sourceJSON, "value"));
        } else {
            EditText et = new EditText(context);
            setValue(getValue(sourceJSON, "value"));
            isEditText = true;
            et.setInputType(inputTypeMask(inputType));
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0)
                        setSurveyResponceWait(s.toString(), SurveyResponceSelected.translateCompare(toggleState));
                }
            });
            textView = et;
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        textView.setLayoutParams(createParams(false, true, 8, 4, 4, 4));

        addView(imgToggle);
        addView(textView);
        setValue(false);

        setOnClickListener(this);
    }


    @Override
    public String getValue() {
        return textView.getText().toString();
    }

    @Override
    public void setValue(String value) {
        textView.setText(value);
    }


    @Override
    public void onClick(View v) {
        if (v == this) {
            toggleState();
        }
    }

    private void toggleState() {
        setValue(!toggleState);
    }
    @Override
    public boolean toBoolean() {
        return toggleState;
    }

    public String getResult() throws Exception {
        if (toggleState)
            return getValue();
        else
            throw new Exception("NOT SELECTED");
    }


    public void setValue(boolean value) {

        if (value) {
            if (surveyResponceChangeInterface != null && !lock_call_backs) {
                surveyResponceChangeInterface.OnChoiceBoxSelected(this);
            }
        }

        if (value == true && isCheckType && !lock_call_backs) {
            if (surveyResponceChangeInterface != null && surveyResponceChangeInterface.selectedMaxChoices()) {
                return;
            }
        }


        if (isEditText) {
            textView.setEnabled(value);
        }
        if (value) {
            imgToggle.setImageResource(imgChecked);
        } else {
            imgToggle.setImageResource(imgUnchecked);
        }
        boolean oldValue = toggleState;

        toggleState = value;

        if (oldValue != toggleState && !lock_call_backs) {
            setSurveyResponceWait(getValue(),  SurveyResponceSelected.translateCompare(toggleState));
        }

    }

    static boolean lock_call_backs = false;
    @Override
    public boolean cordiateState(
            SurveyResponseRoot source
    ) {
        if (this == source)
            return false;
        boolean result = false;
        if (source instanceof SurveyResponseChoiceBox) {
            SurveyResponseChoiceBox s = (SurveyResponseChoiceBox)source;
            lock_call_backs = true;
            if (s.isCheckType) {
               String v1 = getValue(s.getSourceJSON(), "on_select_toggle");
               String id =  getValue(getSourceJSON(), getResources().getString(R.string.SURVEY_FIELD_ID));
                if (v1.equalsIgnoreCase(id)) {
                    setValue(false);
                    result = true;
                }
            } else {
                setValue(false);
                result = true;
            }
            lock_call_backs = false;

        }
        return result;
    }

}
