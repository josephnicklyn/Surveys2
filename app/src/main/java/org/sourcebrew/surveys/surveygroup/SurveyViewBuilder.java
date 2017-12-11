package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by John on 12/8/2017.
 */

public class SurveyViewBuilder {


    String title = "";
    String description = "";

    private static SurveyViewBuilder  instance;

    public static SurveyViewBuilder getInstance() {
        if (instance == null)
            instance = new SurveyViewBuilder();
        return instance;
    }

    public void inflate(LinearLayout target, JSONObject json) {

        if (target == null || json == null)
            return;
        target.removeAllViews();
        JSONObject survey;

        try {
            survey = json.getJSONArray("surveys").getJSONObject(0);
            //inflateQuestions(target, survey);

            JSONArray questions = survey.getJSONArray("questions");

            for(int i = 0; i < questions.length(); i++) {
                target.addView(new SurveyQuestionView(target, questions.getJSONObject(i)));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }

    private String getValue(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }


    public void test(LinearLayout target) {
        inflate(target, SurveySource.getInstance().getJSON());
    }
}
