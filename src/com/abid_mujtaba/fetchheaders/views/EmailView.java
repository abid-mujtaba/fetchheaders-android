package com.abid_mujtaba.fetchheaders.views;

/**
 * This is a custom view that is used to display information about a specific email.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abid_mujtaba.fetchheaders.R;

public class EmailView extends LinearLayout
{
    private TextView tvDate, tvFrom, tvSubject;

    public EmailView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        View.inflate(context, R.layout.email_view, this);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvSubject = (TextView) findViewById(R.id.tvSubject);
    }

    public void setInfo(String _date, String _from, String _subject)
    {
        tvDate.setText(_date);
        tvFrom.setText(_from);
        tvSubject.setText(_subject);
    }
}
