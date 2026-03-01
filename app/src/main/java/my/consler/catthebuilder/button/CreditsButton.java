package my.consler.catthebuilder.button;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import my.consler.catthebuilder.activity.CreditsActivity;

public class CreditsButton implements View.OnClickListener
{
    private final Context context;

    public CreditsButton(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(View view)
    {
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, CreditsActivity.class);
        activity.startActivity(intent);
    }
}
