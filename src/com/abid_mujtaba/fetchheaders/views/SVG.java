package com.abid_mujtaba.fetchheaders.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.abid_mujtaba.fetchheaders.R;
import com.caverock.androidsvg.SVGImageView;

/**
 * Implements a cache of the drawables for commonly used SVG images (icons) for speeding up loading.
 *
 * Since the email icons all have a fixed size we create SVGImageView for each icon (with the same dimensions) and use
 * it to create a Drawable which we then cache and use instead of loading an svg every time which is a slow process.
 */


public class SVG
{
    public static Drawable SVG_ICON_SEEN;               // Stores the Drawables extracted from the SVG images. These are used to populate ImageViews
    public static Drawable SVG_ICON_UNSEEN;
    public static Drawable SVG_ICON_TRASH;


    public static void cacheSVGDrawables(Context context)          // Method used to cache drawables extracted from SVG images
    {
        SVG_ICON_SEEN = getSVGDrawable(context, "icons/email_seen.svg");
        SVG_ICON_UNSEEN = getSVGDrawable(context, "icons/email_unseen.svg");
        SVG_ICON_TRASH = getSVGDrawable(context, "icons/trash_can.svg");
    }


    private static Drawable getSVGDrawable(Context context, String asset)
    {
        SVGImageView view = (SVGImageView) View.inflate(context, R.layout.email_icon, null);

        view.setImageAsset(asset);

        return view.getDrawable();
    }
}
