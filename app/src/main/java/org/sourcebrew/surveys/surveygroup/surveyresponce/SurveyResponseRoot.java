package org.sourcebrew.surveys.surveygroup.surveyresponce;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sourcebrew.surveys.R;
import org.sourcebrew.surveys.surveygroup.widgets.DatePickerFragment;
import org.sourcebrew.surveys.surveygroup.SimpleMessageInterface;
import org.sourcebrew.surveys.surveygroup.widgets.TimePickerFragment;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * An abstract for a survey option which is part of a survey question,
 * this response can be <code>TextView</code>, <code>EditText</code>, custom drawn
 * radio buttons or checkboxes, or any UI a user may use to provide a response to a question.
 *
 * Created by John on 12/12/2017.
 */

public abstract class SurveyResponseRoot extends LinearLayout implements View.OnClickListener {

    /**
     * Returns the soft keyboard type to be used for a responce <code>EditView</code>
     *
     * @param value string: input type
     * @return an or'd int used to set the soft keyboard type
     */
    public static int inputTypeMask(String value) {
        value = value.toLowerCase();
        switch(value) {
            case "phone": return InputType.TYPE_CLASS_PHONE;
            case "date":case "date_picker": return InputType.TYPE_CLASS_DATETIME
                    | InputType.TYPE_DATETIME_VARIATION_DATE;
            case "time":case "time_picker": return InputType.TYPE_CLASS_DATETIME
                    |InputType.TYPE_DATETIME_VARIATION_TIME;
            case "email": return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
            case "auto_complete": return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
            case "password": return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            case "int":case "integer":case "number": return InputType.TYPE_CLASS_NUMBER;
            default: return InputType.TYPE_CLASS_TEXT;
        }
    }

    /*
     * The following allows access to JSON objects, fields and arrays without having to wrap each
     * individual call in a try/catch block.
     */

    /**
     * Grabs an integer value from a json object frrom the field defined by the key
     *
     * @param json the source JSON Object
     * @param key the field of interest
     * @param defaultValue a default view if the field is not found or it is not a valid integer
     * @return integer
     */
    public static int getInteger(JSONObject json, String key, int defaultValue) {
        try {
            return Integer.parseInt(json.getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * Grabs an array from a JSON Object defined by 'key'
     *
     * @param json the source object
     * @param key the field
     * @return JSON Array
     */
    public static JSONArray getArray(JSONObject json, String key) {
        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * [2d array] Grabs an array from within JSON Array defined by the index of 'i'
     *
     * @param json the source object
     * @param i the index of the array
     * @return JSON Array
     */
    public static JSONArray getArray(JSONArray json, int i) {
        try {
            return json.getJSONArray(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Grabs the i'th element of a JSON Array
     *
     * @param json JSON array
     * @param i index of the item
     * @return a JSON Object, or null if iinvalid
     */
    public static JSONObject getObject(JSONArray json, int i) {
        try {
            return json.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Grabs the value of a field with the name 'key'
     *
     * @param json JSONObject
     * @param key the field name
     * @return value of 'key' or an empty string if not exists
     */
    public static String getValue(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * Convince method which returns a concrete implementation of <code>SurveyResponseRoot</code>
     *
     * @param context the context from which this call was triggered
     * @param obj source json object
     * @return a concrete SurveyResponseRoot
     * @throws Exception currently none
     */
    public static SurveyResponseRoot surveyResponseFromJSON(
            Context context,
            JSONObject obj) throws Exception {


        SurveyResponseRoot surveyResponseRoot = null;

        String type = getValue(obj, "type");


        switch (type.toLowerCase()) {
            case "checkbox":
            case "radio":
                surveyResponseRoot = new SurveyResponseChoiceBox(context, obj);
                break;
            case "range_select":
                surveyResponseRoot = new SurveyResponseRangeSelect(context, obj);
                break;
            case "yes_no":
                surveyResponseRoot = new SurveyResponceYesNo(context, obj);
                break;
            default:

                surveyResponseRoot = new SurveyResponseEditText(context, obj);
                break;
        }

        return surveyResponseRoot;
    }

    /**
     * creates a graphical image to be clicked to show a date picker
     *
     * @return View
     */
    protected View getDateButton() {
        ImageView b = new ImageView(getContext());
        b.setLayoutParams(createParams2(true, false, 4, 4, 8, 4));
        int p = toDP(8);
        b.setPadding(p, p, p, p);
        b.setScaleType(ImageView.ScaleType.FIT_CENTER);
        b.setImageResource(R.drawable.ic_date_range);
        showDatePicker(b);
        return b;
    }

    /**
     * creates a graphical image to be clicked to show a time picker
     *
     * @return View
     */
    protected View getTimeButton() {
        ImageView b = new ImageView(getContext());
        b.setLayoutParams(createParams2(true, false, 4, 4, 8, 4));
        int p = toDP(8);
        b.setPadding(p, p, p, p);
        b.setScaleType(ImageView.ScaleType.FIT_CENTER);
        b.setImageResource(R.drawable.ic_access_time);
        showTimePicker(b);
        return b;
    }


    /**
     * Shows a date picker
     *
     * @param view the view whose click will be listened for
     */
    protected void showDatePicker(View view) {

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                ((DatePickerFragment)newFragment).setOnCallback(
                        new SimpleMessageInterface() {
                            @Override
                            public void OnSimpleMessage(String value) {
                                setValue(value);
                            }
                        }
                );
                Activity activity = (Activity)getContext();
                newFragment.show(activity.getFragmentManager(),"DatePicker");
            }
        });

    }

    /**
     * Shows a time picker
     *
     * @param view the view whose click will be listened for
     */
    protected void showTimePicker(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();

                ((TimePickerFragment)newFragment).setOnCallback(
                        new SimpleMessageInterface() {
                            @Override
                            public void OnSimpleMessage(String value) {
                                setValue(value);
                            }
                        }
                );
                Activity activity = (Activity)getContext();
                newFragment.show(activity.getFragmentManager(),"TimePicker");
            }
        });
    }

    /**
     * convince method returnind LayoutParams
     *
     * @param wrap boolean, wrap/match parent
     * @param fill boolean, stretch/fill remaining space
     * @param l left
     * @param t top
     * @param r right
     * @param b bottom
     * @return LayoutParams
     */
    protected LinearLayout.LayoutParams createParams(boolean wrap, boolean fill, int l, int t, int r, int b) {

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
            wrap?LayoutParams.WRAP_CONTENT:LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );

        ll.setMargins(
            (int)(l * getResources().getDisplayMetrics().density),
            (int)(t * getResources().getDisplayMetrics().density),
            (int)(r * getResources().getDisplayMetrics().density),
            (int)(b * getResources().getDisplayMetrics().density)
        );

        ll.weight = (fill)?1:0;
        ll.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        return ll;
    }

    /**
     * convince method returnind LayoutParams
     *
     * @param wrap boolean, wrap/match parent
     * @param fill boolean, stretch/fill remaining space
     * @param l left
     * @param t top
     * @param r right
     * @param b bottom
     * @return LayoutParams
     */
    protected LinearLayout.LayoutParams createParams2(boolean wrap, boolean fill, int l, int t, int r, int b) {

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
                wrap?LayoutParams.WRAP_CONTENT:LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        ll.setMargins(
            (int)(l * getResources().getDisplayMetrics().density),
            (int)(t * getResources().getDisplayMetrics().density),
            (int)(r * getResources().getDisplayMetrics().density),
            (int)(b * getResources().getDisplayMetrics().density)
        );

        ll.weight = (fill)?1:0;
        ll.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        return ll;
    }

    /**
     * convince method to scale for screen densities
     *
     * @param l the pixel size
     * @return scaled to pixel size
     */
    public int toDP(int l) {
        return (int)(l * getResources().getDisplayMetrics().density);
    }

    private final JSONObject json;

    public JSONObject getSourceJSON() {
        return json;
    }

    public SurveyResponseRoot(Context context, JSONObject sourceJSON) {
        super(context);
        json = sourceJSON;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    }

    /**
     * returns boolean if applicable
     * @return
     */
    public boolean getBooleanValue() {
        return Boolean.getBoolean(getValue());
    }

    /**
     * returns integer value if possible
     * @return integer
     */
    public int getIntegerValue() {

        int v = 0;

        String b = getValue();
        boolean hasNeg = false;
        for(char c: b.toCharArray()) {
            if (c == '-') {
                hasNeg = true;
            }
            if (c >= '0' && c <= '9') {
                v*=10;
                v+=(int)(c-'0');
            }
        }
        if (hasNeg) v*=-1;

        return v ;
    }

    public abstract String getValue();
    public abstract void setValue(String value);

    public boolean toBoolean() { return false; };
    public void setValue(boolean value) {  };

    protected SurveyResponceChangeInterface surveyResponceChangeInterface;

    public void setQuestionItemValueChangeInterface(SurveyResponceChangeInterface listener) {
        surveyResponceChangeInterface = listener;
    }

    public boolean cordiateState(
            SurveyResponseRoot source
    ) {
        return false;
    }

    private SurveyResponce surveyResponce;

    void sendWasUpdateMessage(SurveyResponce responce) {
        if (surveyResponceChangeInterface != null) {
            surveyResponceChangeInterface.valueUpdated(this, responce);
        }
    }

    public String getResult() throws Exception { return ""; }

    @Override
    public String toString() {
        String title = getValue(json, "value");
        if (title == null || title.isEmpty()) title = "no value";
        return "{ID:" + getValue(json, getResources().getString(R.string.SURVEY_FIELD_ID)) + "}, {VALUE:" + title + "}";
    }

    /**
     * update the survey response and send the message
     * @param value the new value
     * @param selected boolean, selected (true/false if applicable), otherwise false
     */
    public void setSurveyResponce(
            String value,
            SurveyResponceSelected selected) {
        SurveyResponce r = getSurveyResponcePrivate();
        hasUpdate = true;
        r.selected = selected;
        r.value = value;
        sendWasUpdateMessage(r);
    }

    /*
     * for delaying outputs, while a user may be selecting multiple items
     */
    private static Timer timer;


    /**
     * update the survey response and send the message after a given amount of time.
     * all paths will lead to the same listener.
     *
     * @param value the new value
     * @param selected boolean, selected (true/false if applicable), otherwise false
     */
    public void setSurveyResponceWait(
        String value,
        SurveyResponceSelected selected) {
        //setSurveyResponce(value, selected);
        hasUpdate = true;
        SurveyResponce r = getSurveyResponcePrivate();
        r.selected = selected;
        r.value = value;
        waitTimer();
    }

    void waitTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    sendWasUpdateMessage(getSurveyResponcePrivate());

                    }
                });

            }
        }, 2500);

    }
    private void runOnUiThread(Runnable task) {
        new android.os.Handler(Looper.getMainLooper()).post(task);
    }

    private boolean hasUpdate = false;

    public boolean hasUpdate() {
        return hasUpdate;
    }
    /**
     * public access to the survey response
     * @return <code>SurveyResponse</code>
     */
    public SurveyResponce getSurveyResponce() {
        if (surveyResponce == null) {
            surveyResponce = new SurveyResponce(json);
            surveyResponce.selected = SurveyResponceSelected.SELECTED_NOT_APPLICABLE;
            surveyResponce.value = "";
            surveyResponce.id = getValue(json,  getResources().getString(R.string.SURVEY_FIELD_ID));
        }
        hasUpdate = false;
        return surveyResponce;
    }

    /**
     * public access to the survey response
     * @return <code>SurveyResponse</code>
     */
    private SurveyResponce getSurveyResponcePrivate() {
        if (surveyResponce == null) {
            surveyResponce = new SurveyResponce(json);
            surveyResponce.selected = SurveyResponceSelected.SELECTED_NOT_APPLICABLE;
            surveyResponce.value = "";
            surveyResponce.id = getValue(json,  getResources().getString(R.string.SURVEY_FIELD_ID));
        }
        return surveyResponce;
    }

}
