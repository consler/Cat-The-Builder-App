package my.consler.catthebuilder;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import my.consler.catthebuilder.build.Build;
import android.view.View;

import java.io.IOException;

public class BuildButton implements View.OnClickListener
{
    private final Context context;

    public BuildButton(Context context)
    {
        this.context = context;

    }

    private static String pickedFileName;

    public static void setPickedFileName(String name) {
        pickedFileName = name;
    }
    public static String getPickedFileName() {
        return pickedFileName;
    }




    @Override
    public void onClick(View view)
    {
        Activity activity = (Activity) context;

        TextInputEditText app_name_input = activity.findViewById(R.id.appname);
        TextInputEditText package_input = activity.findViewById(R.id.packagename);
        TextInputEditText version_input = activity.findViewById(R.id.version);

        TextView action = activity.findViewById(R.id.action);

        if (app_name_input.getText().toString().isEmpty())
        {
            Toast.makeText(context, "App Name is empty", Toast.LENGTH_SHORT).show();

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
        else if (getPickedFileName() == null)
        {
            Toast.makeText(context, "Catrobat file not chosen", Toast.LENGTH_SHORT).show();

        }
        else if (! getPickedFileName().endsWith(".catrobat"))
        {
            Toast.makeText(context, "File chosen doesn't end with .catrobat", Toast.LENGTH_SHORT).show();

        }
        else
        {
            try
            {
                Build.start(context, app_name_input.getText().toString(), package_input.getText().toString(), version_input.getText().toString(), pickedFileName, action);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }

    }

}
