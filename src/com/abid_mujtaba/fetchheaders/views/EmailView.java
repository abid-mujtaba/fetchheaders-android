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

import com.caverock.androidsvg.SVGImageView;


public class EmailView extends LinearLayout
{
    private TextView tvDate, tvFrom, tvSubject;
    private SVGImageView uIcon;

    private boolean fSeen = false;

    private final String SVG_ICON_SEEN = "icons/email_seen.svg";
    private final String SVG_ICON_UNSEEN = "icons/email_unseen.svg";
    private final String SVG_ICON_TRASH = "icons/trash_can.svg";


    private EmailView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        View.inflate(context, R.layout.email_view, this);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvSubject = (TextView) findViewById(R.id.tvSubject);
        uIcon = (SVGImageView) findViewById(R.id.email_icon);
    }

    public EmailView(Context context, AttributeSet attrs, boolean flag_seen)         // constructor for email view for seen and unseen emails
    {
        this(context, attrs);

        if (flag_seen)
        {
            android.content.res.Resources resources = getResources();

            tvDate.setTextColor( resources.getColor(R.color.seen) );
            tvFrom.setTextColor( resources.getColor(R.color.seen) );
            tvSubject.setTextColor( resources.getColor(R.color.seen) );

            uIcon.setImageAsset(SVG_ICON_SEEN);

            fSeen = true;       // A flag that indicates that the email has been seen.
        }
        else
        {
            uIcon.setImageAsset(SVG_ICON_UNSEEN);
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
//        tvSubject.setPaintFlags(tvSubject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        tvDate.setPaintFlags(tvSubject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        tvFrom.setPaintFlags(tvSubject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        uIcon.setImageAsset(SVG_ICON_TRASH);
    }

    public void removeStrikethrough()          // Removes strike through of the subject since the email has been undeleted
    {
//        tvSubject.setPaintFlags(tvSubject.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//        tvDate.setPaintFlags(tvSubject.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//        tvFrom.setPaintFlags(tvSubject.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        if (fSeen) { uIcon.setImageAsset(SVG_ICON_SEEN); }
        else { uIcon.setImageAsset(SVG_ICON_UNSEEN); }
    }
}
