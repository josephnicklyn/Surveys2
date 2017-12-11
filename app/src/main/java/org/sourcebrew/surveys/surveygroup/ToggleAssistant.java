package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.sourcebrew.surveys.R;

import java.util.ArrayList;

/**
 * Created by John on 12/10/2017.
 */

public class ToggleAssistant {

    public interface ToggleAssistantInterface {
        public void onToggleSelected(Object source, CompoundButton old_button, CompoundButton new_button);
    }

    private ToggleAssistantInterface toggleAssistantInterface;
    private ArrayList<CompoundButton> viewList = new ArrayList<>();

    private final Context context;

    public ToggleAssistant(Context context) {
        this.context = context;
    }

    private CompoundButton.OnCheckedChangeListener checkStateListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (buttonView instanceof RadioButton) {
                    for (CompoundButton rb : viewList) {
                        if (rb == buttonView || !(rb instanceof RadioButton))
                            continue;
                        rb.setChecked(false);
                    }
                } else {
                    if (buttonView.getTag(R.string.CHOICE_ON_SELECT_TOGGLE) != null) {
                        String onToggleA = (String)buttonView.getTag(R.string.CHOICE_ON_SELECT_TOGGLE);
                        Log.e("TOGGLE_SELECT", onToggleA);
                        for (CompoundButton rb : viewList) {
                            if (rb == buttonView)
                                continue;
                            if (rb.getTag(R.string.CHOICE_KEY) != null) {
                                String onToggleB = (String) rb.getTag(R.string.CHOICE_KEY);
                                if (onToggleA.equalsIgnoreCase(onToggleB))
                                    rb.setChecked(false);
                            }

                        }

                    }
                }
            }

            if (buttonView.getTag(R.string.CHOICE_FOR_VIEW) != null) {
                View forOption = (View) buttonView.getTag(R.string.CHOICE_FOR_VIEW);
                forOption.setEnabled(isChecked);
                forOption.requestFocus();
            }

        }
    };


    public void setOnToggle(ToggleAssistantInterface toggleListener) {
        toggleAssistantInterface = toggleListener;
    }

    public void clear() {
        viewList.clear();
    }

    public void addCompundButton(CompoundButton btn) {
        if (viewList.contains(btn))
            return;
        viewList.add(btn);
        btn.setOnCheckedChangeListener(checkStateListener);
    }


}
