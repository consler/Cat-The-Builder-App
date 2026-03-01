package my.consler.catthebuilder.button;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import my.consler.catthebuilder.activity.LanguagePickerActivity;
import my.consler.catthebuilder.activity.OptionsActivity;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class LanguagePickerButton implements View.OnClickListener
{
    private final Context context;

    public LanguagePickerButton(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(View view)
    {
        Activity activity = (Activity) context;

        startActivityForResult(
                activity,
                new Intent(activity, LanguagePickerActivity.class),
                200,
                null
        );
    }
}
