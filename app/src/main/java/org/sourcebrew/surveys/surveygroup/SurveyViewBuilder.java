package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sourcebrew.surveys.surveygroup.surveyresponce.ResponseUpdateInterface;
import org.sourcebrew.surveys.surveygroup.surveyresponce.SurveyResponce;
import org.sourcebrew.surveys.surveygroup.surveyresponce.SurveyResponseRoot;

import java.util.ArrayList;

import static android.R.attr.duration;

/**
 * Created by John on 12/8/2017.
 */

public class SurveyViewBuilder implements QuestionViewInterface, ResponseUpdateInterface {


    String title = "";
    String description = "";

    private static SurveyViewBuilder  instance;
    private ArrayList<SurveyQuestionView> surveyQuestionViewArrayList = new ArrayList<>();

    public static SurveyViewBuilder getInstance() {
        if (instance == null)
            instance = new SurveyViewBuilder();
        return instance;
    }

    LinearLayout forContext;

    public void inflate(LinearLayout target, JSONObject json) {
        forContext = target;
        if (target == null || json == null)
            return;
        target.removeAllViews();
        JSONObject survey;
        surveyQuestionViewArrayList.clear();
        try {
            survey = json.getJSONArray("surveys").getJSONObject(0);
            JSONArray questions = survey.getJSONArray("questions");

            for(int i = 0; i < questions.length(); i++) {
                SurveyQuestionView quest = new SurveyQuestionView(this, target, questions.getJSONObject(i));
                quest.setIndex(i+1);
                target.addView(quest);
                surveyQuestionViewArrayList.add(quest);
            }


        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("TEST", e.getMessage());
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

    @Override
    public void expandedChange(SurveyQuestionView view, boolean value) {
        for(SurveyQuestionView s: surveyQuestionViewArrayList) {
            if (s == view)
                continue;
            s.setExpanded(false);
        }
    }

    @Override
    public void responceItemUpdated(SurveyQuestionView questionOwner, SurveyResponce responce) {


        Context context = forContext.getContext().getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        StringBuilder b = new StringBuilder();

        for(SurveyQuestionView sqv: surveyQuestionViewArrayList) {
            if (sqv.hasAnUpdateWaiting()) {
                b.append("\n").append(sqv.getQuestion()).append("\n");

                for (SurveyResponseRoot q : sqv.getQuestionLayout()) {
                    b.append(q.getSurveyResponce()).append("\n");
                }
            }
        }

            /*
            for (SurveyResponseRoot s : questionOwner.getQuestionLayout()) {
                try {
                    b.append(s.getSurveyResponce()).append("\n");
                } catch (Exception e) {

                }
            }
            */

        Toast toast = Toast.makeText(context, b.toString(), duration);
        toast.show();
    }
}
