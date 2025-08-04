package my.consler.catthebuilder.buttons;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import my.consler.catthebuilder.R;
import android.view.View;

import java.io.File;

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

        if (app_name_input.getText().toString().isEmpty())
        {
            Toast.makeText(context, "App name is empty", Toast.LENGTH_SHORT).show();

        }
        else if (package_input.getText().toString().isEmpty())
        {
            Toast.makeText(context, "Package is empty", Toast.LENGTH_SHORT).show();

        }
//        TODO: ADD A REGEX
//        else if (!(package_input.getText().toString().matches("")))
//        {
//            Toast.makeText(context, "Invalid package", Toast.LENGTH_SHORT).show();

//        }
        else if (version_input.getText().toString().isEmpty())
        {
            Toast.makeText(context, "Version is empty", Toast.LENGTH_SHORT).show();

        }
        else if (! new File(context.getCacheDir(), "CATGAME.catrobat").exists())
        {
            Toast.makeText(context, "Catrobat file was never chosen", Toast.LENGTH_SHORT).show();

        }
        else if (version_code_input.getText().toString().isEmpty())
        {
            Toast.makeText(context, "Version code is empty", Toast.LENGTH_SHORT).show();
        }
        else if (FilePicker.getIcon() == null)
        {
            Toast.makeText(context, "Icon not chosen", Toast.LENGTH_SHORT).show();
        }
        else if (!validate_version_code( version_code_input.getText().toString()))
        {
            Toast.makeText(context, "Version code is not a valid number", Toast.LENGTH_SHORT).show();
        }
        else if (! new File(context.getCacheDir(), "CATGAME.catrobat").getName().endsWith(".catrobat"))
        {
            Toast.makeText(context, "File chosen is not a Catrobat file", Toast.LENGTH_SHORT).show();
        }
        else
        {
            my.consler.catthebuilder.build.Build.start(context, app_name_input.getText().toString(), package_input.getText().toString(), version_input.getText().toString(), version_code_input.getText().toString(), action);
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
