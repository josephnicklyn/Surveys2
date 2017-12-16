package org.sourcebrew.surveys.surveygroup;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sourcebrew.surveys.surveygroup.surveyresponce.ResponseUpdateInterface;
import org.sourcebrew.surveys.surveygroup.surveyresponce.SurveyResponce;
import org.sourcebrew.surveys.surveygroup.surveyresponce.SurveyResponceChangeInterface;
import org.sourcebrew.surveys.surveygroup.surveyresponce.SurveyResponseRoot;
import org.sourcebrew.surveys.surveygroup.widgets.ChoiceLayout;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by John on 12/11/2017.
 */

public class QuestionsLayout extends ChoiceLayout implements SurveyResponceChangeInterface, Iterable<SurveyResponseRoot> {

    private ResponseUpdateInterface responseUpdateInterface;
    final JSONObject sourceObject;
    private final SurveyQuestionView surveyQuestionView;
    final ArrayList<SurveyResponseRoot> members = new ArrayList<>();



    public QuestionsLayout(
            ResponseUpdateInterface ref,
            SurveyQuestionView target,
            JSONObject jsonObject) {

        super(target.getContext());
        surveyQuestionView = target;
        //if (ResponseUpdateInterface.class.isAssignableFrom(target.getClass())) {
            responseUpdateInterface = ref;
        //}
        sourceObject = jsonObject;
        JSONArray choices = getArray(jsonObject, "choices");
        setMaxColumns(getInteger(jsonObject, "columns", -1));

        for(int i = 0; i < choices.length(); i++) {

            JSONObject choice = getObject(choices, i);

            if (choice == null)
                continue;

            JSONArray groups = getArray(choice, "groups");
            if (groups != null)
                pushGroups(groups);
            else {
                JSONObject obj = getObject(choices, i);
                addWidget(this, obj);
            }
        }
    }

    private boolean pushGroups(JSONArray groups) {

        if (groups == null)
            return false;

        for(int i = 0; i < groups.length(); i++) {
            JSONArray group = getArray(groups, i);

            if (group == null)
                continue;

            LinearLayout layoutTarget = null;

            for(int g = 0; g < group.length(); g++) {
                JSONObject obj = getObject(group, g);
                if (obj == null)
                    continue;
                if (layoutTarget == null)
                    layoutTarget = getNewChoiceGroup();

                addWidget(layoutTarget, obj);
            }

        }

        return true;
    }

    private void addWidget(ViewGroup layoutTarget, JSONObject group) {
        if (layoutTarget == null || group == null)
            return;

        String type = getValue(group, "type");

        try {
            SurveyResponseRoot responceObject =
                    SurveyResponseRoot.surveyResponseFromJSON(
                            getContext(),
                            group);
            responceObject.setQuestionItemValueChangeInterface(this);
            layoutTarget.addView(responceObject);
            members.add(responceObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean OnChoiceBoxSelected(SurveyResponseRoot source) {
        boolean result = false;
        for(SurveyResponseRoot srb: members) {
            if (srb == source)
                continue;
            else
                if (srb.cordiateState(source))
                    result = true;
        }
        return result;
    }

    @Override
    public JSONObject getParentObject() {
        return sourceObject;
    }

    @Override
    public boolean selectedMaxChoices() {

        int r = getInteger(sourceObject, "max_choices", -1);
        if (r > 0) {
            for(SurveyResponseRoot srb: members) {
                if (srb.toBoolean()) {
                    r--;
                }
            }
            return r <= 0;
        }
        return false;
    }

    @Override
    public void valueUpdated(SurveyResponseRoot source,  SurveyResponce responce) {
        if (responseUpdateInterface != null) {
            responseUpdateInterface.responceItemUpdated(surveyQuestionView, responce);
        }
    }

    @Override
    public Iterator<SurveyResponseRoot> iterator() {
        return members.iterator();
    }
}
