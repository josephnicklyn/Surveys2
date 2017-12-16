package org.sourcebrew.surveys.surveygroup.surveyresponce;

/**
 * Created by John on 12/16/2017.
 */

public enum SurveyResponceSelected {
    SELECTED_NOT_APPLICABLE,
    SELECTED_TRUE,
    SELECTED_FALSE;

    public static SurveyResponceSelected translateCompare(Object p, Object no, Object yes) {
        if (p == no)
            return SELECTED_FALSE;
        if (p == yes)
            return SELECTED_TRUE;
        return SELECTED_NOT_APPLICABLE;
    }

    public static SurveyResponceSelected translateCompare(boolean value) {
        if (!value)
            return SELECTED_FALSE;
        else
            return SELECTED_TRUE;
    }
}
