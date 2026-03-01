package my.consler.catthebuilder.build;

import android.content.Context;
import android.util.Log;
import my.consler.catthebuilder.button.FilePickerButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Icon
{

    public static boolean change(Context context)
    {
        File old_icon = deleteAndReturnIcon(context);
        File old_icon_foreground = deleteAndReturnIconForeground(context);

        if(FilePickerButton.getIcon() == null) return false;
        File icon = FilePickerButton.getIcon();

        try
        {
            Files.copy(icon.toPath(), old_icon.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(icon.toPath(), old_icon_foreground.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return true;
    }

    private static File deleteAndReturnIconForeground(Context context)
    {
        File old_icon_foreground;

        if (new File(context.getCacheDir(), "CATGAME/res/Qu.png").exists())
        {
            old_icon_foreground = new File(context.getCacheDir(), "CATGAME/res/Qu.png");
            new File(context.getCacheDir(), "CATGAME/res/Qu.jpg").delete();
            new File(context.getCacheDir(), "CATGAME/res/Qu.webp").delete();
        }
        else if (new File(context.getCacheDir(), "CATGAME/res/Qu.jpg").exists())
        {
            old_icon_foreground = new File(context.getCacheDir(), "CATGAME/res/Qu.jpg");
            new File(context.getCacheDir(), "CATGAME/res/Qu.png").delete();
            new File(context.getCacheDir(), "CATGAME/res/Qu.webp").delete();
        }
        else if (new File(context.getCacheDir(), "CATGAME/res/Qu.webp").exists())
        {
            old_icon_foreground = new File(context.getCacheDir(), "CATGAME/res/Qu.webp");
            new File(context.getCacheDir(), "CATGAME/res/Qu.png").delete();
            new File(context.getCacheDir(), "CATGAME/res/Qu.jpg").delete();
        }
        else
        {
            Log.wtf("Icon", "Has someone tampered with CATGAME??");
            throw new RuntimeException();
        }

        return old_icon_foreground;
    }

    private static File deleteAndReturnIcon(Context context)
    {
        File old_icon;
        if (new File(context.getCacheDir(), "CATGAME/res/rR.png").exists())
        {
            old_icon = new File(context.getCacheDir(), "CATGAME/res/rR.png");
            new File(context.getCacheDir(), "CATGAME/res/rR.jpg").delete();
            new File(context.getCacheDir(), "CATGAME/res/rR.webp").delete();
        }
        else if (new File(context.getCacheDir(), "CATGAME/res/rR.jpg").exists())
        {
            old_icon = new File(context.getCacheDir(), "CATGAME/res/rR.jpg");
            new File(context.getCacheDir(), "CATGAME/res/rR.png").delete();
            new File(context.getCacheDir(), "CATGAME/res/rR.webp").delete();
        }
        else if (new File(context.getCacheDir(), "CATGAME/res/rR.webp").exists())
        {
            old_icon = new File(context.getCacheDir(), "CATGAME/res/rR.webp");
            new File(context.getCacheDir(), "CATGAME/res/rR.png").delete();
            new File(context.getCacheDir(), "CATGAME/res/rR.jpg").delete();
        }
        else
        {
            Log.wtf("Icon", "Has someone tampered with CATGAME??");
            throw new RuntimeException();
        }
        return old_icon;
    }

}
