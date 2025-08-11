package my.consler.catthebuilder.buttons;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import my.consler.catthebuilder.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AdvancedBuildOptionsButton implements View.OnClickListener
{
    private static boolean is_open = false;
    private final Context context;

    public AdvancedBuildOptionsButton(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(View v)
    {
        if(is_open)
        {
            is_open = false;
            Button b = ((Activity) context).findViewById(R.id.advanced_build_button);
            b.setText(R.string.advanced_build_options_closed);
            ((Activity) context).findViewById(R.id.more_build_options).setVisibility(GONE);
        }
        else
        {
            is_open = true;
            Button b = ((Activity) context).findViewById(R.id.advanced_build_button);
            b.setText(R.string.advanced_build_options_open);
            ((Activity) context).findViewById(R.id.more_build_options).setVisibility(VISIBLE);
        }
    }
}
