package org.sourcebrew.surveys.surveygroup;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sourcebrew.surveys.R;

/**
 * Created by John on 12/11/2017.
 */

public class QuestionsLayout extends ChoiceLayout {

    public QuestionsLayout(SurveyQuestionView target, JSONObject jsonObject) {
        super(target.getContext());

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
                Log.e("WIDGETS", "obj = " + obj);
                addWidget(this, obj);
            }

        }

        //

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
                Log.e("WIDGETS", "type = " + obj);
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

        Log.e("WIDGETS", "type = " + type);

        switch (type.toLowerCase()) {
            case "radio":
                layoutTarget.addView(getOptionButton(group, true));
                break;
            case "checkbox":
                layoutTarget.addView(getOptionButton(group, false));
                break;
            case "text":

                addTextWidget(layoutTarget, group);

                break;
            case "range_select":
                layoutTarget.addView(new RangeSelect(getContext(), group));
                break;
        }

    }

    private void addTextWidget(ViewGroup layoutTarget, JSONObject group) {

        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.template_labeled_field, linearLayout, true);
        TextView title_template_labeled_field_title = linearLayout.findViewById(R.id.title_template_labeled_field_title);
        FrameLayout title_template_labeled_field_container = linearLayout.findViewById(R.id.title_template_labeled_field_container);

        String title = getValue(group, "value");
        String input_type = getValue(group, "input_type");
        title_template_labeled_field_title.setText(title);
        if (title == null || title.isEmpty())
            title_template_labeled_field_title.setVisibility(View.GONE);

        /*
        Button b = new Button(getContext());
        b.setLayoutParams(
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )

        );
        */

        View view = null;

        switch(input_type) {
            case "date":
                view = getDateButton();
                break;
            case "time":
                view = getTimeButton();
                break;
            case "phone":
            case "email":
            case "auto_complete":
            case "password":
            case "int":
                case "integer":
                case "number":

            default:
                EditText b = new EditText(getContext());
                b.setText("SELECT TIME");
                b.setLayoutParams(
                        new LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT
                        )

                );

                view = b;
            break;
        }
        /*
        b.setText("SELECT TIME");
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                Activity activity = (Activity)getContext();
                newFragment.show(activity.getFragmentManager(),"TimePicker");
            }
        });
        */
        title_template_labeled_field_container.addView(view);


        layoutTarget.addView(linearLayout);



    }


    public View getDateButton() {
        Button b = new Button(getContext());
        b.setLayoutParams(
            new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            )
        );

        b.setText("SELECT DATE");
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Activity activity = (Activity)getContext();
                newFragment.show(activity.getFragmentManager(),"DatePicker");
            }
        });


        return b;
    }

    public View getTimeButton() {
        Button b = new Button(getContext());
        b.setLayoutParams(
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )
        );

        b.setText("SELECT TIME");
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                Activity activity = (Activity)getContext();
                newFragment.show(activity.getFragmentManager(),"TimePicker");
            }
        });


        return b;
    }
}
/*
    "groups": [
        [
          { "key":"C.2.1", "type":"radio", "value":"Very satisfied"},
          { "key":"C.2.2", "type":"radio", "value":"Somewhat satisfied"},
          { "key":"C.2.3", "type":"radio", "value":"Neither satisfied nor disatisfied"}
        ],
        [
          { "key":"C.2.4", "type":"radio", "value":"Somewhat dissatisfied"},
          { "key":"C.2.5", "type":"radio", "value":"very dissatisfied"},
          { "key":"C.2.6", "type":"radio", "value":"other", "input_type":"auto_complete"}
        ]
      ]
 */