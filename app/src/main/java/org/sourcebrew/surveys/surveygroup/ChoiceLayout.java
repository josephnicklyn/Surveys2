package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sourcebrew.surveys.R;

/**
 * Created by John on 12/9/2017.
 */

public class ChoiceLayout extends FlowLayout {

    private ToggleAssistant toggleAssistant;

    public ToggleAssistant getToggleAssistant() {
        if (toggleAssistant == null)
            toggleAssistant = new ToggleAssistant(getContext());
        return toggleAssistant;
    }

    public static int inputTypeMask(String value) {

        value = value.toLowerCase();

        switch(value) {
            case "phone": return InputType.TYPE_CLASS_PHONE;
            case "date": return InputType.TYPE_CLASS_DATETIME
                    | InputType.TYPE_DATETIME_VARIATION_DATE;
            case "time": return InputType.TYPE_CLASS_DATETIME
                    |InputType.TYPE_DATETIME_VARIATION_TIME;
            case "email": return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
            case "auto_complete": return InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
            case "password": return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            case "int":case "integer":case "number": return InputType.TYPE_CLASS_NUMBER;
            default: return InputType.TYPE_CLASS_TEXT;
        }
    }




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

        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(DEFAULT_PARAMS_FOR_CHILD_VIEWS);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.LEFT | Gravity.TOP);
        container.setPadding(p, 0, p, 0);
        container.setBackgroundColor(0xFFf4f4f8);
        addView(container);

        return container;
    }

    public void buildFromJSON(JSONObject object) throws Exception {}

    View getOptionButton(JSONObject group, boolean pill) {
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

            int p = (int)(getResources().getDisplayMetrics().density * 8);

            ll.weight = 1;
            ll.setMargins(0, p/2, 0, p);

            LinearLayout container = new LinearLayout(getContext());
            returnView = container;

            EditText editText = new EditText(getContext());
            editText.setHint(value);
            container.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            editText.setLayoutParams(ll);
            container.addView(button);
            container.addView(editText);
            editText.setEnabled(false);
            editText.setFocusable(false);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | inputTypeMask(input_type));

            button.setTag(R.string.CHOICE_FOR_VIEW, editText);
        } else {
            button.setText(value);
        }

        getToggleAssistant().addCompundButton(button);

        return returnView;
    }

}
