package my.consler.catthebuilder.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Icon
{
    public static void change(Context context) throws IOException
    {
        File old_icon;
        if (new File(context.getCacheDir(), "CATGAME/res/rR.png").exists())
        {
            old_icon = new File(context.getCacheDir(), "CATGAME/res/rR.png");

        }
        else if (new File(context.getCacheDir(), "CATGAME/res/rR.jpg").exists())
        {
            old_icon = new File(context.getCacheDir(), "CATGAME/res/rR.jpg");

        }
        else
        {
            throw new RuntimeException("Icon not found");
        }

        if (new File(context.getCacheDir(), "icon.png").exists())
        {
            File icon = new File(context.getCacheDir(), "icon.png");
            Files.copy(icon.toPath(), old_icon.toPath(), StandardCopyOption.REPLACE_EXISTING);


        }
        else if (new File(context.getCacheDir(), "icon.jpg").exists())
        {
            File icon = new File(context.getCacheDir(), "icon.jpg");
            Files.copy(icon.toPath(), old_icon.toPath(), StandardCopyOption.REPLACE_EXISTING);

        }

    }

}
