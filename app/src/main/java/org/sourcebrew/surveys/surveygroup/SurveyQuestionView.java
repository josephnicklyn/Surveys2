package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sourcebrew.surveys.R;

import java.util.ArrayList;

/**
 * Created by John on 12/8/2017.
 */

public class SurveyQuestionView extends LinearLayout {

    TextView sq_title;

    FrameLayout sq_container;

    private ArrayList<LinearLayout> groupContainers = new ArrayList<>();
    QuestionsLayout questionLayout;

    FrameLayout getContainer() {
        return sq_container;
    }

    public SurveyQuestionView(LinearLayout target, JSONObject question) {
        super(target.getContext());
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.template_survey_question, this, true);

        sq_title = findViewById(R.id.sq_title);
        sq_container = findViewById(R.id.sq_container);
        setTitle(ChoiceLayout.getValue(question, "title"));
        questionLayout = new QuestionsLayout(this, question);
        sq_container.removeAllViews();
        sq_container.addView(questionLayout);

    }

    public void setTitle(String value) {
        sq_title.setText(value);
    }

}
