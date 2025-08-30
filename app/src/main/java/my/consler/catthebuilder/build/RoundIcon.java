package my.consler.catthebuilder.build;

import android.content.Context;
import android.graphics.*;
import com.reandroid.archive.io.FileChannelOutputStream;
import my.consler.catthebuilder.buttons.FilePicker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class RoundIcon
{
    public static boolean change(Context context, boolean auto_resize)
    {
        File old_icon = new File(context.getCacheDir(), "CATGAME/res/kS.png");
        if (FilePicker.getIcon() == null) throw new RuntimeException();

        File icon = FilePicker.getIcon();

        if (auto_resize)
        {
            File round_icon = new File(context.getCacheDir(), "round_icon.png");
            try
            {
                Bitmap round_icon_bitmap = createCircularImage(icon);
                FileOutputStream fos = new FileOutputStream(round_icon);
                FileChannelOutputStream fcos = new FileChannelOutputStream(fos.getChannel());
                round_icon_bitmap.compress(Bitmap.CompressFormat.PNG, 100, fcos);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            try
            {
                Files.copy(round_icon.toPath(), old_icon.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            try
            {
                Files.copy(icon.toPath(), old_icon.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
    private static Bitmap createCircularImage(File round_icon)
    {
        Bitmap originalImage = BitmapFactory.decodeFile(round_icon.getPath());

        if (originalImage == null) throw new RuntimeException("Something went wrong");

        int diameter = 512;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalImage, diameter, diameter, false);

        Bitmap circularBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circularBitmap);
        Paint paint = new Paint();
        Path path = new Path();

        path.addCircle((float) diameter / 2, (float) diameter / 2, (float) diameter / 2, Path.Direction.CCW);
        canvas.clipPath(path);

        canvas.drawBitmap(scaledBitmap, 0, 0, paint);

        return circularBitmap;
    }

}
