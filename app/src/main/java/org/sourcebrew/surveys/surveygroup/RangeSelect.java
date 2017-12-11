package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sourcebrew.surveys.R;

/**
 * Created by John on 12/11/2017.
 */

public class RangeSelect extends LinearLayout {

    TextView range_select_left, range_select_right;
    LinearLayout range_select_container;

    public RangeSelect(Context context, JSONObject jsonObject) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.template_range_select, this, true);
        range_select_left = findViewById(R.id.range_select_left);
        range_select_right = findViewById(R.id.range_select_right);
        range_select_container = findViewById(R.id.range_select_container);

        JSONArray values = ChoiceLayout.getArray(jsonObject,"values");
        if (values != null) {
            for(int i = 0; i < values.length(); i++) {
                JSONObject value = ChoiceLayout.getObject(values, i);
                if (value != null) {
                    addOption(ChoiceLayout.getValue(value, "value"));
                }
            }
        }

        setLeftText(ChoiceLayout.getValue(jsonObject, "title_left"));
        setRightText(ChoiceLayout.getValue(jsonObject, "title_right"));

    }

    private void addOption(String s) {

        if (s == null || s.isEmpty())
            return;

        TextView t = new TextView(getContext());
        t.setText(s);
        t.setGravity(Gravity.CENTER);

        LayoutParams ll = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        ll.weight = 1;
        int p = (int)(getResources().getDisplayMetrics().density * 4);
        t.setLayoutParams(ll);
        t.setBackgroundResource(R.drawable.border_for_view);
        t.setPadding(p, p, p, p);
        range_select_container.addView(t);
        t.setClickable(true);
        t.setOnClickListener(onClicked);

    }

    View previousSelect = null;
    OnClickListener onClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (previousSelect != null)
                previousSelect.setBackgroundResource(R.drawable.border_for_view);
            v.setBackgroundResource(R.drawable.border_for_view_selected);
            previousSelect = v;
        }
    };

    public void setLeftText(String value) {
        range_select_left.setText(value);
    }

    public void setRightText(String value) {
        range_select_right.setText(value);
    }

}
