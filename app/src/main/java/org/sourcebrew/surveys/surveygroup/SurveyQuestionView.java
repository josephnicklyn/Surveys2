package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.sourcebrew.surveys.R;
import org.sourcebrew.surveys.surveygroup.surveyresponce.SurveyResponseRoot;
import org.sourcebrew.surveys.surveygroup.widgets.ChoiceLayout;

import java.util.ArrayList;

/**
 * Created by John on 12/8/2017.
 */

public class SurveyQuestionView extends LinearLayout  {

    int bkgExpandedTrue, bkgExpandedFalse;

    TextView sq_title;
    TextView sq_index;
    FrameLayout sq_container;
    int padding = 0;
    private ArrayList<LinearLayout> groupContainers = new ArrayList<>();
    QuestionsLayout questionLayout;

    FrameLayout getContainer() {
        return sq_container;
    }

    SurveyViewBuilder surveyViewBuilderParent;

    public SurveyQuestionView(SurveyViewBuilder parent, LinearLayout target, JSONObject question) {
        super(target.getContext());
        this.surveyViewBuilderParent = parent;
        bkgExpandedFalse = R.drawable.round_border;
        bkgExpandedTrue = R.drawable.round_border_select;


        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.template_survey_question, this, true);

        sq_title = findViewById(R.id.sq_title);
        sq_index = findViewById(R.id.sq_index);
        padding = sq_index.getPaddingLeft();
        sq_container = findViewById(R.id.sq_container);
        setTitle(ChoiceLayout.getValue(question, getResources().getString(R.string.SURVEY_FIELD_TITLE)));
        questionLayout = new QuestionsLayout(surveyViewBuilderParent, this, question);
        sq_container.removeAllViews();
        sq_container.addView(questionLayout);
        sq_index.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpanded();
            }
        });
        setExpanded(false);
    }

    boolean isExpanded = true;
    private void toggleExpanded() {
        setExpanded(!isExpanded);
    }

    public QuestionsLayout getQuestionLayout() {
        return questionLayout;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean value) {
        isExpanded = value;
        if (isExpanded) {
            sq_index.setBackgroundResource(bkgExpandedTrue);
            sq_container.setVisibility(View.VISIBLE);
        } else {
            sq_index.setBackgroundResource(bkgExpandedFalse);
            sq_container.setVisibility(View.GONE);
        }
    }

    public void setIndex(int value) {
        sq_index.setText(String.valueOf(value));
        if (value < 10) {
            int p = padding * 3 / 2;
            sq_index.setPadding(p, padding, p, padding);
        } else {
            sq_index.setPadding(padding, padding, padding, padding);
        }
    }

    public void setTitle(String value) {
        sq_title.setText(value);
    }

    public String getQuestion() {
        return sq_title.getText().toString();
    }

    public boolean hasAnUpdateWaiting() {
        for (SurveyResponseRoot q : getQuestionLayout()) {
            if (q.hasUpdate())
                return true;
        }
        return false;
    }
}
