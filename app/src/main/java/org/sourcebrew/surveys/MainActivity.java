package org.sourcebrew.surveys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.sourcebrew.surveys.surveygroup.SurveySource;
import org.sourcebrew.surveys.surveygroup.SurveyViewBuilder;

public class MainActivity extends AppCompatActivity {
    LinearLayout ic_target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        LinearLayout ic_target = (LinearLayout)findViewById(R.id.ic_target);



        SurveySource.getInstance().useExampleSurvey(this);

        SurveyViewBuilder.getInstance().test(ic_target);


    }
}
