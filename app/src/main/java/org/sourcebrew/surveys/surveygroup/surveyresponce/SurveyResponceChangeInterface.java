package org.sourcebrew.surveys.surveygroup.surveyresponce;

import org.json.JSONObject;
import org.sourcebrew.surveys.surveygroup.surveyresponce.SurveyResponseRoot;

/**
 * Allows intermediate response updates, this is used to coordinate changes with other
 * <code>SurveyResponseRoot</code> objects
 *
 * Created by John on 12/11/2017.
 */

public interface SurveyResponceChangeInterface {
    public boolean OnChoiceBoxSelected(SurveyResponseRoot source);
    public JSONObject getParentObject();
    public boolean selectedMaxChoices();
    public void valueUpdated(SurveyResponseRoot source, SurveyResponce responce);

}
