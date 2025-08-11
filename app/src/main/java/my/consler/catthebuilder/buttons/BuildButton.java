package my.consler.catthebuilder.buttons;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import my.consler.catthebuilder.R;
import android.view.View;

import java.io.File;
import java.util.Objects;

public class BuildButton implements View.OnClickListener
{
    private final Context context;

    public BuildButton(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(View view)
    {
        Activity activity = (Activity) context;

        TextInputEditText app_name_input = activity.findViewById(R.id.appname);
        TextInputEditText package_input = activity.findViewById(R.id.packagename);
        TextInputEditText version_input = activity.findViewById(R.id.version);
        TextInputEditText version_code_input = activity.findViewById(R.id.version_code);

        TextView action = activity.findViewById(R.id.action);

        CheckBox debug = ((Activity) context).findViewById(R.id.debug_option);
        CheckBox round_icon_is_resizable = ((Activity) context).findViewById(R.id.auto_resizable_round_icon_option);
        CheckBox use_adaptive_icon = ((Activity) context).findViewById(R.id.use_adaptive_icon_option);

        String package_regex = "(?![0-9])([a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)+)$";
        if (Objects.requireNonNull(app_name_input.getText()).toString().isEmpty())
        {
            Toast.makeText(context, context.getString(R.string.app_name_empty), Toast.LENGTH_SHORT).show();
        }
        else if (Objects.requireNonNull(package_input.getText()).toString().isEmpty())
        {
            Toast.makeText(context, context.getString(R.string.package_empty), Toast.LENGTH_SHORT).show();
        }
        else if (!(package_input.getText().toString().matches(package_regex)))
        {
            Toast.makeText(context, context.getString(R.string.invalid_package), Toast.LENGTH_SHORT).show();
        }
        else if (Objects.requireNonNull(version_input.getText()).toString().isEmpty())
        {
            Toast.makeText(context, context.getString(R.string.version_empty), Toast.LENGTH_SHORT).show();
        }
        else if (Objects.requireNonNull(version_code_input.getText()).toString().isEmpty())
        {
            Toast.makeText(context, context.getString(R.string.version_code_empty), Toast.LENGTH_SHORT).show();
        }
        else if (! new File(context.getCacheDir(), "CATGAME.catrobat").exists())
        {
            Toast.makeText(context, context.getString(R.string.catrobat_not_chosen), Toast.LENGTH_SHORT).show();
        }
        else if (FilePicker.getIcon() == null)
        {
            Toast.makeText(context, context.getString(R.string.icon_not_chosen), Toast.LENGTH_SHORT).show();
        }
        else if (!validate_version_code( version_code_input.getText().toString()))
        {
            Toast.makeText(context, context.getString(R.string.invalid_version_code), Toast.LENGTH_SHORT).show();
        }
        else
        {
            my.consler.catthebuilder.build.Build.start(context, app_name_input.getText().toString(), package_input.getText().toString(), version_input.getText().toString(), version_code_input.getText().toString(), action, debug.isChecked(), round_icon_is_resizable.isChecked(), use_adaptive_icon.isChecked());
        }
    }

    public static boolean validate_version_code(String version_code)
    {
        try
        {
            Integer.parseInt(version_code);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

}
