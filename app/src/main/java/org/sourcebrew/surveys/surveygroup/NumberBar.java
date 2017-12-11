package org.sourcebrew.surveys.surveygroup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sourcebrew.surveys.R;

/**
 * Created by John on 12/6/2017.
 */

public class NumberBar extends LinearLayout {



    public NumberBar(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        for(int i = 0; i <= 5; i++) {
            char c = (char)('A'+i);
            addOption(Character.toString(c));
        }
        setBackgroundResource(R.drawable.border_for_view);

    }

    private void addOption(String s) {
        TextView t = new TextView(getContext());
        t.setText(s);
        t.setGravity(Gravity.CENTER);

        LayoutParams ll = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        ll.weight = 1;
        int p = (int)(getResources().getDisplayMetrics().density * 8);
        t.setLayoutParams(ll);
        t.setBackgroundResource(R.drawable.border_for_view);
        t.setPadding(p, p, p, p);
        addView(t);
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
}
