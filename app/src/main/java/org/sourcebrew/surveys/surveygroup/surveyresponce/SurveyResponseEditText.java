package org.sourcebrew.surveys.surveygroup.surveyresponce;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;
import org.sourcebrew.surveys.R;

/**
 * Created by John on 12/13/2017.
 */

public class SurveyResponseEditText extends SurveyResponseRoot {
    final TextView editText;
    public SurveyResponseEditText(Context context, JSONObject sourceJSON) {
        super(context, sourceJSON);
        String inputType = getValue(sourceJSON, "input_type");

        switch (inputType.toLowerCase()) {
            case "time_picker":
            case "time":
            case "date_picker":
            case "date":
                editText = new TextView(context);
                editText.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                int p = toDP(6);
                editText.setPadding(p, p, p, p);
                editText.setHint(getValue(sourceJSON, getResources().getString(R.string.SURVEY_FIELD_HINT)));
                editText.setBackgroundResource(R.drawable.border_for_view);
                break;
            default:
                editText = new EditText(context);
                editText.setInputType(inputTypeMask(inputType));
                break;
        }
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        editText.setLayoutParams(createParams(false, true, 0, 0, 0, 0));

        addView(editText, 0);

        switch (inputType.toLowerCase()) {
            case "time_picker":
            case "time":
                showTimePicker(editText);
                break;
            case "date_picker":
            case "date":
                showDatePicker(editText);
                break;
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0)
                    setSurveyResponceWait(s.toString(), SurveyResponceSelected.SELECTED_TRUE);
            }
        });

    }

    public String getResult() throws Exception {
        String v = getValue();
        if (v != null && !v.isEmpty())
            return v;
        else
            throw new Exception("NOT SELECTED");
    }

    @Override
    public String getValue() {
        return editText.getText().toString();
    }

    @Override
    public void setValue(String value) {
        editText.setText(value);
    }

    @Override
    public void onClick(View v) {

    }
}
