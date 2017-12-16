package org.sourcebrew.surveys.surveygroup.surveyresponce;

import org.json.JSONObject;

/**
 * Contents of a response
 *
 * Created by John on 12/16/2017.
 */

public class SurveyResponce {


    final JSONObject json;
    String id;
    String value;
    SurveyResponceSelected selected = SurveyResponceSelected.SELECTED_NOT_APPLICABLE;

    SurveyResponce(JSONObject json) {
        this.json = json;
    }

    @Override public String toString() {
        return "{ id:" + id + ", value:" + value + ", selected:" + selected.name() + "}";
    }

}
