package my.consler.catthebuilder.helper;

import android.content.Context;
import android.util.TypedValue;
import androidx.core.content.ContextCompat;

public class ThemeHelper
{
    public static int getSecondaryColor(Context context)
    {
        TypedValue typed_value = new TypedValue();
        context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typed_value, true);
        int color;
        if (typed_value.resourceId != 0)
        {
            color = ContextCompat.getColor(context, typed_value.resourceId);
        }
        else
        {
            color = typed_value.data;
        }
        return color;
    }
    public static int getPrimaryColor(Context context)
    {
        TypedValue typed_value = new TypedValue();
        context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typed_value, true);
        int color;
        if (typed_value.resourceId != 0)
        {
            color = ContextCompat.getColor(context, typed_value.resourceId);
        }
        else
        {
            color = typed_value.data;
        }
        return color;
    }
}
