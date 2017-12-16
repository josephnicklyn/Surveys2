package org.sourcebrew.surveys.surveygroup.widgets;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sourcebrew.surveys.surveygroup.widgets.FlowLayout;

/**
 * Created by John on 12/9/2017.
 */

public class ChoiceLayout extends FlowLayout {

    public static int getInteger(JSONObject json, String key, int defaultValue) {

        try {
            return Integer.parseInt(json.getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }

    }

    public static JSONArray getArray(JSONObject json, String key) {

        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static JSONArray getArray(JSONArray json, int i) {
        try {
            return json.getJSONArray(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getObject(JSONArray json, int i) {
        try {
            return json.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getValue(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    private static LinearLayout.LayoutParams DEFAULT_PARAMS_FOR_CHILD_VIEWS = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public ChoiceLayout(Context context) {
        super(context);
        setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ));
    }

    protected final LinearLayout getNewChoiceGroup() {
        int p = (int)(getResources().getDisplayMetrics().density * 8);
        int hp = p/2;
        LinearLayout container = new LinearLayout(getContext());

        container.setLayoutParams(DEFAULT_PARAMS_FOR_CHILD_VIEWS);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.LEFT | Gravity.TOP);
        container.setPadding(p, hp, p, 0);
        container.setBackgroundColor(0xFFfafafc);
        addView(container);

        return container;
    }

    /*
    public void buildFromJSON(JSONObject object) throws Exception {}

    View[] getOptionButton(JSONObject group, boolean pill) {
        String key = getValue(group, "key");
        String value = getValue(group, "value");
        String input_type = getValue(group, "input_type");

        CompoundButton button = (pill?new RadioButton(getContext()):new CheckBox(getContext()));
        View returnView = button;
        button.setTag(R.string.CHOICE_KEY, key);

        if (!input_type.isEmpty()) {

            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            //int p = (int)(getResources().getDisplayMetrics().density * 8);

            ll.weight = 1;
            ll.setMargins(0, 0, 0, 0);

            final LinearLayout container = new LinearLayout(getContext());
            returnView = container;

            final EditText editText = new EditText(getContext());
            editText.setHint(value);
            container.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            editText.setLayoutParams(ll);
            container.addView(button);
            container.addView(editText);
            editText.setEnabled(false);
            editText.setTag(R.string.CHOICE_FOR_VIEW, button);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | inputTypeMask(input_type));
            button.setTag(R.string.CHOICE_FOR_VIEW, editText);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    CompoundButton b = (CompoundButton)editText.getTag(R.string.CHOICE_FOR_VIEW);
                    if (b!=null && b.getTag(R.string.CHOICE_VALUE_CHANGED_LISTENER) != null) {
                        QuestionItemValueChangeInterface qi = (QuestionItemValueChangeInterface) b.getTag(R.string.CHOICE_VALUE_CHANGED_LISTENER);
                        //qi.OnQuestionItemValueChanged(b, s.toString(), b.isChecked() );

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        } else {
            button.setText(value);
        }
        button.setTag(R.string.CHOICE_ON_SELECT_TOGGLE, getValue(group, "on_select_toggle"));

        getToggleAssistant().addCompundButton(button);

        return new View[]{button, returnView };
    }
*/
}
