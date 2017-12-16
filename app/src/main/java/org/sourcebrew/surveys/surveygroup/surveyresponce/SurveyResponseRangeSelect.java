package org.sourcebrew.surveys.surveygroup.surveyresponce;

import android.content.Context;
import android.view.View;

import org.json.JSONObject;
import org.sourcebrew.surveys.surveygroup.widgets.RangeSelect;

/**
 * Created by John on 12/13/2017.
 */

public class SurveyResponseRangeSelect extends SurveyResponseRoot {
    final RangeSelect rangeSelect;

    public SurveyResponseRangeSelect(Context context, JSONObject sourceJSON) {
        super(context, sourceJSON);
        rangeSelect = new RangeSelect(getContext(), sourceJSON);
        rangeSelect.setLayoutParams(createParams(false, true, 0, 0, 0, 0));
        addView(rangeSelect);
        rangeSelect.setOnRangeSelectChange(new RangeSelect.RangeSelectInterface() {
            @Override
            public void itemSelected(String oldValue, String newValue) {
                setSurveyResponce(getValue(), SurveyResponceSelected.SELECTED_NOT_APPLICABLE);
            }
        });
    }

    @Override
    public String getValue() {
        return rangeSelect.getSelectedValue();
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public void onClick(View v) {

    }

    public String getResult() throws Exception {
        String v = getValue();
        if (v != null && !v.isEmpty())
            return v;
        else
            throw new Exception("NOT SELECTED");
    }

}
