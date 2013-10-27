package com.abid_mujtaba.fetchheaders.views;

/**
 * This is a custom view that is used to display information about a specific email.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
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

    public EmailView(Context context, AttributeSet attrs, boolean flag_seen)         // constructor for email view for seen and unseen emails
    {
        this(context, attrs);

        if (flag_seen)
        {
            Resources resources = getResources();

            tvDate.setTextColor( resources.getColor(R.color.seen) );
            tvFrom.setTextColor( resources.getColor(R.color.seen) );
            tvSubject.setTextColor( resources.getColor(R.color.seen) );
        }

    }

    public void setInfo(String _date, String _from, String _subject)
    {
        tvDate.setText(_date);
        tvFrom.setText(_from);
        tvSubject.setText(_subject);
    }

    public void strikethrough()     // Strikes through the subject textview to indicate that the email is marked for deletion
    {
        tvSubject.setPaintFlags(tvSubject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvDate.setPaintFlags(tvSubject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvFrom.setPaintFlags(tvSubject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void removeStrikethrough()          // Removes strike through of the subject since the email has been undeleted
    {
        tvSubject.setPaintFlags(tvSubject.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        tvDate.setPaintFlags(tvSubject.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        tvFrom.setPaintFlags(tvSubject.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
    }
}
