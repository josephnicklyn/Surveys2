package org.sourcebrew.surveys.surveygroup.surveyresponce;

import org.sourcebrew.surveys.surveygroup.SurveyQuestionView;

/**
 * Communicates a response update to a listener.
 *
 * A response update occures when a user selects a choice, or changes the value of a text view.
 *
 * Created by John on 12/16/2017.
 */

public interface ResponseUpdateInterface {
    public void responceItemUpdated(SurveyQuestionView questionOwner, SurveyResponce responce);
}
