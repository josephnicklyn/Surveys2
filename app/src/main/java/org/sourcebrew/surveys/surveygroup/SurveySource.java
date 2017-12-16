package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.sourcebrew.surveys.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by John on 12/8/2017.
 */

public class SurveySource {

    private static SurveySource instance;

    private JSONObject json;

    public static SurveySource getInstance() {
        if (instance == null)
            instance = new SurveySource();
        return instance;
    }

    private SurveySource() { }

    public void useExampleSurvey(Context context) {
        JSONObject jsonObject = null;
        InputStream in = context.getResources().openRawResource(R.raw.survey);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder b = new StringBuilder();
            String str = "";
            while ((str=reader.readLine()) != null) {
                b.append(str);
            }
            jsonObject = new JSONObject(b.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject;
    }

    @Override
    public String toString() {
        if (json != null) {
            try {
                return json.toString(4);
            } catch (JSONException e) {
                return e.getMessage();
            }
        } else {
            return "NULL";
        }
    }

    public JSONObject getJSON() {
        return json;
    }
}
